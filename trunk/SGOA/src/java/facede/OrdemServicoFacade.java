package facede;

import facede.base.BaseFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Cliente;
import model.ConfigOrdemServico;
import model.Etapa;
import model.Funcionario;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.OrdemServicoFoto;
import model.Setor;
import model.TipoEvento;
import model.Veiculo;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.primefaces.model.SortOrder;
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

    public void incluirEvento(Session sessao, OrdemServicoEtapa etapa, Funcionario funcionario,
            TipoEvento tipo, String descricao, Date dataInicioParada, List<OrdemServicoFoto> fotos,
            boolean notificaViaEmail) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            OrdemServicoEvento evento = adicionarEvento(etapa, funcionario, tipo);
            evento.setNotificaViaEmail(notificaViaEmail);
            etapa.getEventos().add(0, evento);
            evento.setDescricao(descricao);
            evento.setDataInicioParada(dataInicioParada);
            if (fotos != null) {
                for (int i = 0; i < fotos.size(); i++) {
                    fotos.get(i).setEvento(evento);
                }
            }
            evento.setFotos(fotos);
            if (tipo == TipoEvento.InterrupcaoAtividade) {
                etapa.setSituacao('P');
            }
            if (tipo == TipoEvento.ReinicioAtividade) {
                etapa.setSituacao('E');
            }
            sessao.saveOrUpdate(etapa);
            HibernateFactory.commitTransaction();
            if (notificaViaEmail) {
                util.JsfUtil.enviarEmail(sessao, etapa.getOrdemServico().getOrcamento().getCliente().getPessoa(),
                        "Notificação do andamento do serviço",
                        "A oficina acaba de incluir novas informações do serviço que esta sendo realizado em seu veículo. <br /> Acompanhe o andamento do serviço pelo site da oficina. <br /> Obrigado.");
            }
            if (tipo == TipoEvento.ContatoCliente) {
                util.JsfUtil.enviarEmailOficina(sessao, etapa.getOrdemServico().getOrcamento().getVeiculo().getPlaca(), descricao);
            }
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
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
            Etapa tarefa, boolean inicioImediato, Funcionario funcProximaEtapa, Date dataEntrada) {
        Calendar cal = Calendar.getInstance();
        Date dataAtual = new Date();
        cal.setTime(dataAtual);
        OrdemServicoEtapa etapa = new OrdemServicoEtapa();
        etapa.setDataCadastro(cal.getTime());
        etapa.setEtapa(tarefa);
        etapa.setOrdemServico(os);
        os.setEtapaAtual(etapa);
        if (inicioImediato) {
            etapa.setDataEntrada(dataEntrada);
            etapa.setFuncionario(funcProximaEtapa);
            etapa.getEventos().add(0, adicionarEvento(etapa, funcExecutor, TipoEvento.InicioAtividade));
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
            if (ObterOrdemServicoPorPlaca(sessao, item.getOrcamento().getVeiculo().getPlaca()) != null) {
                throw new Exception("Erro na aprovação do orçamento. O veículo já possui uma ordem de serviço em execução.");
            }
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
            Calendar dataProximaAtiv = Calendar.getInstance();
            dataProximaAtiv.setTime(cal.getTime());
            dataProximaAtiv.add(Calendar.MINUTE, 1);
            if (item.getOrcamento().getSeguradora() != null) {
                etapa = adicionarAtividade(item.getFuncionarioAprovacao(), item, config.getEtapaInicialSeguradora(), true, null, dataProximaAtiv.getTime());
            } else {
                etapa = adicionarAtividade(item.getFuncionarioAprovacao(), item, config.getEtapaInicial(), false, null, null);
            }
            item.setEtapaAtual(etapa);
            item.getEtapas().add(0, etapa);
            sessao.save(item);
            HibernateFactory.commitTransaction();
            if (etapa.getEtapa().getEnviaEmailInicio()) {
                util.JsfUtil.enviarEmail(sessao, item.getOrcamento().getCliente().getPessoa(), "Notificação do andamento do serviço",
                        "O seu veículo acaba de iniciar um nova atividade na oficina. <br /> Acompanhe o andamento do serviço pelo site da oficina. <br /> Obrigado.");
            }
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
                item.getEventos().add(0, adicionarEvento(item, funcExecutor, TipoEvento.InicioAtividade));
                item.setSituacao('E'); //Em execução
            }
            if (item.getDataSaida() != null) {
                item.getEventos().add(0, adicionarEvento(item, funcExecutor, TipoEvento.FimAtividade));
                item.setSituacao('C'); //Concluido
                //proxima atividade
                if (proximaEtapa == null) {
                    throw new Exception("Próxima atividade é obrigatória.");
                }
                Calendar dataProximaAtiv = Calendar.getInstance();
                dataProximaAtiv.setTime(item.getDataSaida());
                dataProximaAtiv.add(Calendar.MINUTE, 1);
                item.getOrdemServico().getEtapas().add(0, adicionarAtividade(funcExecutor, item.getOrdemServico(),
                        proximaEtapa, inicioImediato, funcProximaEtapa, dataProximaAtiv.getTime()));
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
            if (item.getEtapa().getEnviaEmailFim()) {
                util.JsfUtil.enviarEmail(sessao, item.getOrdemServico().getOrcamento().getCliente().getPessoa(), "Notificação do andamento do serviço",
                        "O seu veículo acaba de encerar uam atividade na oficina. <br /> Acompanhe o andamento do serviço pelo site da oficina. <br /> Obrigado.");
            }
            if (proximaEtapa != null && proximaEtapa.getEnviaEmailInicio()) {
                util.JsfUtil.enviarEmail(sessao, item.getOrdemServico().getOrcamento().getCliente().getPessoa(), "Notificação do andamento do serviço",
                        "O seu veículo acaba de iniciar um nova atividade na oficina. <br /> Acompanhe o andamento do serviço pelo site da oficina. <br /> Obrigado.");
            }
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
        OrdemServico resultado = (OrdemServico) c.uniqueResult();
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

    public OrdemServicoEvento obterEvento(Session sessao, int id) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        OrdemServicoEvento entidade = (OrdemServicoEvento) sessao.get(OrdemServicoEvento.class, id);
        return entidade;
    }

    public OrdemServicoEtapa obterEtapa(Session sessao, int id) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        OrdemServicoEtapa entidade = (OrdemServicoEtapa) sessao.get(OrdemServicoEtapa.class, id);
        return entidade;
    }

    public List<OrdemServicoFoto> obterFotosOS(Session sessao, int id) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(OrdemServicoFoto.class, "osf");
        c.createCriteria("evento", "ev").createCriteria("etapa", "et").createCriteria("ordemservico", "os");
        c.add(Restrictions.eq("os.id", id));
        List<OrdemServicoFoto> resultado = c.list();
        return resultado;
    }

    public List<OrdemServicoEtapa> monitorarPatio(Session sessao, String sort, SortOrder order,
            Integer page, Integer maxPage, String situacaoAtiv, Cliente cliente, String placa,
            String situacaoOS, Etapa atividade, Setor setor) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }

        // <editor-fold defaultstate="collapsed" desc="busca o total de registro que atendam o filtro da pesquisa">
        Criteria c = sessao.createCriteria(OrdemServicoEtapa.class, "ose");
        c.add(Restrictions.isNull("ose.dataSaida"));
        c.createCriteria("ordemservico", "os").createCriteria("orcamento", "o");
        List<Character> situacoes = new ArrayList<Character>();
        situacoes.add('R');
        situacoes.add('E');
        c.add(Restrictions.in("os.situacao", situacoes));
        if (situacaoOS != null && !situacaoOS.isEmpty()) {
            c.add(Restrictions.eq("os.situacao", situacaoOS.charAt(0)));

        }
        if (situacaoAtiv != null && !situacaoAtiv.isEmpty()) {
            c.add(Restrictions.eq("ose.situacao", situacaoAtiv.charAt(0)));
        }
        if (cliente != null) {
            c.add(Restrictions.eq("o.cliente", cliente));
        }
        if (placa != null && !placa.isEmpty()) {
            c.createCriteria("o.cliente", "cli").createCriteria("pessoa", "pes");
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placa, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "pes.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        if (atividade != null) {
            c.add(Restrictions.eq("ose.etapa", atividade));
        }
        if (setor != null) {
            c.createCriteria("ose.etapa", "et");
            c.add(Restrictions.eq("et.setor", setor));
        }
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="paginacao por demanda">
        c = sessao.createCriteria(OrdemServicoEtapa.class, "ose");
        c.setFirstResult(page).setMaxResults(maxPage);
        c.add(Restrictions.isNull("ose.dataSaida"));
        c.createCriteria("ordemservico", "os").createCriteria("orcamento", "o");
        c.add(Restrictions.in("os.situacao", situacoes));
        if (situacaoOS != null && !situacaoOS.isEmpty()) {
            c.add(Restrictions.eq("os.situacao", situacaoOS.charAt(0)));

        }
        if (situacaoAtiv != null && !situacaoAtiv.isEmpty()) {
            c.add(Restrictions.eq("ose.situacao", situacaoAtiv.charAt(0)));
        }
        if (cliente != null) {
            c.add(Restrictions.eq("o.cliente", cliente));
        }
        if (placa != null && !placa.isEmpty()) {
            c.createCriteria("o.cliente", "cli").createCriteria("pessoa", "pes");
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placa, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "pes.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        if (atividade != null) {
            c.add(Restrictions.eq("ose.etapa", atividade));
        }
        if (setor != null) {
            c.createCriteria("ose.etapa", "et");
            c.add(Restrictions.eq("et.setor", setor));
        }
        // </editor-fold>
        List<OrdemServicoEtapa> resultado = c.list();
        return resultado;

    }
    
     public void cancelar(Session sessao, OrdemServico item, Funcionario funcExecutor, String motivo) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            ConfigOrdemServicoFacade ebjConfig = new ConfigOrdemServicoFacade();
            ConfigOrdemServico config = ebjConfig.obterPorId(sessao, 1);
            item.setSituacao('C');
            item.getOrcamento().setSituacao('C');
            item.getEtapaAtual().setSituacao('P');
             Calendar cal = Calendar.getInstance();
            Date dataAtual = new Date();
            cal.setTime(dataAtual);
            item.setDataCancelamento(cal.getTime());
            item.setFuncionarioCancelamento(funcExecutor);
            item.setMotivoCancelamento(motivo);
            OrdemServicoEtapa ativCanc = adicionarAtividade(funcExecutor, item, config.getEtapaCancelamentoConcerto(), false, null, null);
            ativCanc.setDataEntrada(ativCanc.getDataCadastro());
            ativCanc.setDataSaida(ativCanc.getDataCadastro());
            ativCanc.setFuncionario(funcExecutor);
            ativCanc.setSituacao('C');
            sessao.save(ativCanc);
            sessao.saveOrUpdate(item);
            HibernateFactory.commitTransaction();
            if (config.getEtapaCancelamentoConcerto().getEnviaEmailFim()) {
                util.JsfUtil.enviarEmail(sessao, item.getOrcamento().getCliente().getPessoa(), "Cancelamento da Ordem de Serviço",
                        "Ordem de serviço " + item.getOrcamento().toString() + " acaba de ser cancelada. \n + Motivo: " +  motivo);
            }
           
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }

    }
    
}
