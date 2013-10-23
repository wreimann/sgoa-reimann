package facede;

import facede.base.BaseFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ConfigOrdemServico;
import model.Etapa;
import model.Funcionario;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.TipoEvento;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import util.HibernateFactory;

@Stateless
public class OrdemServicoFacade extends BaseFacade<OrdemServico> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdemServicoFacade() {
        super(OrdemServico.class);
    }

    private OrdemServicoEvento adicionarEvento(OrdemServicoEtapa etapa, Funcionario funcionario, TipoEvento tipo) {
        Calendar cal = Calendar.getInstance();
        Date dataAtual = new Date();
        cal.setTime(dataAtual);
        OrdemServicoEvento evento = new OrdemServicoEvento();
        evento.setAtivo(true);
        evento.setDataOcorrencia(cal.getTime());
        evento.setEtapa(etapa);
        evento.setFuncionario(funcionario);
        evento.setTipoEvento(tipo);
        String descricao = "";
        switch (tipo) {
            case InicioAtividade:
                descricao = "Início da atividade.";
                break;
            case FimAtividade:
                descricao = "Conclusão da atividade.";
                break;
        }
        evento.setDescricao(descricao);
        return evento;
    }

    private OrdemServicoEtapa adicionarAtividade(Funcionario funcExecutor, OrdemServico os,
            Etapa tarefa, boolean inicioImediato, Funcionario funcProximaEtapa) {
        Calendar cal = Calendar.getInstance();
        Date dataAtual = new Date();
        cal.setTime(dataAtual);
        OrdemServicoEtapa etapa = new OrdemServicoEtapa();
        etapa.setDataCadastro(cal.getTime());
        etapa.setEtapa(tarefa);
        etapa.setOrdemServico(os);
        if (inicioImediato) {
            etapa.setDataEntrada(cal.getTime());
            etapa.setFuncionario(funcProximaEtapa);
            etapa.getEventos().add(adicionarEvento(etapa, funcExecutor, TipoEvento.InicioAtividade));
            etapa.setSituacao('E'); // em execuçao
        } else {
            etapa.setSituacao('F'); //fila de espera
        }
        return etapa;
    }

    @Override
    public void incluir(Session sessao, OrdemServico item) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            item.getOrcamento().setSituacao('P');
            Calendar cal = Calendar.getInstance();
            Date dataAtual = new Date();
            cal.setTime(dataAtual);
            item.setDataAprovacao(cal.getTime());
            item.setSituacao('E'); //OS Em Execução
            //etapa inicial
            ConfigOrdemServicoFacade ebjConfig = new ConfigOrdemServicoFacade();
            ConfigOrdemServico config = ebjConfig.obterPorId(sessao, 1);
            OrdemServicoEtapa etapa = null;
            if (item.getOrcamento().getSeguradora() != null) {
                etapa = adicionarAtividade(item.getFuncionarioAprovacao(), item, config.getEtapaInicialSeguradora(), true, null);
            } else {
                etapa = adicionarAtividade(item.getFuncionarioAprovacao(), item, config.getEtapaInicial(), false, null);
            }
            item.setEtapaAtual(etapa);
            item.getEtapas().add(etapa);
            sessao.save(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
    }

    public void atualizarServico(Session sessao, Funcionario funcExecutor, OrdemServicoEtapa item,
            Etapa proximaEtapa, boolean inicioImediato,
            Funcionario funcProximaEtapa) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            if (item.getSituacao() == 'F') { // Fila de Espera
                item.getEventos().add(adicionarEvento(item, funcExecutor, TipoEvento.InicioAtividade));
                item.setSituacao('E'); //Em execução
            }
            if (item.getDataSaida() != null) {
                item.getEventos().add(adicionarEvento(item, funcExecutor, TipoEvento.FimAtividade));
                item.setSituacao('C'); //Concluido
                //proxima atividade
                if (proximaEtapa == null) {
                    throw new Exception("Próxima atividade é obrigatória.");
                }
                item.getOrdemServico().getEtapas().add(adicionarAtividade(funcExecutor, item.getOrdemServico(),
                        proximaEtapa, inicioImediato, funcProximaEtapa));
                //muda situação da OS
                ConfigOrdemServicoFacade ebjConfig = new ConfigOrdemServicoFacade();
                ConfigOrdemServico config = ebjConfig.obterPorId(sessao, 1);
                if (config.getEtapaFimConcerto().equals(proximaEtapa)) {
                    item.getOrdemServico().setSituacao('R'); //Reparos finalizados
                }
                if (config.getEtapaConclusaoOrdemServico().equals(proximaEtapa)) {
                    item.getOrdemServico().setSituacao('F'); // OS finalizada
                }
            }
            sessao.saveOrUpdate(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
    }

    public OrdemServico ObterOrdemServicoPorPlaca(Session sessao, String placa) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(OrdemServico.class, "os");
        c.add(Restrictions.eq("os.situacao", 'E'));//em execução
        c.createCriteria("orcamento", "orc").createCriteria("veiculo", "v");
        c.add(Restrictions.like("v.placa", placa, MatchMode.EXACT).ignoreCase());
        OrdemServico resultado = ((OrdemServico) c.uniqueResult());
        if (resultado != null) {
            Hibernate.initialize(resultado.getEtapaAtual());
            for (OrdemServicoEtapa item : resultado.getEtapas()) {
                Hibernate.initialize(item);
                Hibernate.initialize(item.getEtapa());
                for (OrdemServicoEvento evento : item.getEventos()) {
                    Hibernate.initialize(evento);
                }
            }
        }
        return resultado;

    }
}
