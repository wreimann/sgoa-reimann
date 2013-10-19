package facede;

import facede.base.BaseFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ConfigOrdemServico;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.TipoEvento;
import org.hibernate.Criteria;
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
            item.setSituacao('A'); //Em Aberto
            //etapa inicial
            ConfigOrdemServicoFacade ebjConfig = new ConfigOrdemServicoFacade();
            ConfigOrdemServico config = ebjConfig.obterPorId(sessao, 1);
            OrdemServicoEtapa etapa = new OrdemServicoEtapa();
            etapa.setDataCadastro(cal.getTime());
            etapa.setDataEntrada(cal.getTime());
            etapa.setEtapa(config.getEtapaInicial());
            if (item.getOrcamento().getSeguradora() != null) {
                etapa.setEtapa(config.getEtapaInicialSeguradora());
            }
            etapa.setOrdemServico(item);
            etapa.setSituacao('E');
            //evento
            OrdemServicoEvento evento = new OrdemServicoEvento();
            evento.setAtivo(true);
            evento.setDataOcorrencia(cal.getTime());
            evento.setEtapa(etapa);
            evento.setFuncionario(item.getFuncionarioAprovacao());
            evento.setTipoEvento(TipoEvento.InicioAtividade);
            evento.setDescricao("Início da atividade.");
            //salvar registro
            if (etapa.getEventos() == null) {
                etapa.setEventos(new ArrayList<OrdemServicoEvento>());
            }
            etapa.getEventos().add(evento);
            item.setEtapaAtual(etapa);
            if (item.getEtapas() == null) {
                item.setEtapas(new ArrayList<OrdemServicoEtapa>());
            }
            item.getEtapas().add(etapa);
            sessao.save(item);
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
        c.add(Restrictions.eq("os.situacao", 'A'));
        c.createCriteria("orcamento", "orc").createCriteria("veiculo", "v");
        c.add(Restrictions.like("v.placa", placa, MatchMode.EXACT).ignoreCase());
        return ((OrdemServico) c.uniqueResult());
    }
}
