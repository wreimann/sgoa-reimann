package controller;

import facede.EtapaFacade;
import facede.FuncionarioFacade;
import facede.OrdemServicoFacade;
import filter.LoginFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Etapa;
import model.Funcionario;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public final class OrdemServicoController implements Serializable {

    private OrdemServicoEtapa current;
    @EJB
    private OrdemServicoFacade ejbFacade;
    private List<OrdemServicoEtapa> atividades;

    public List<OrdemServicoEtapa> getAtividades() {
        return atividades;
    }
    private List<Funcionario> funcionariosAtivos;

    public List<Funcionario> getFuncionariosAtivos() {
        return funcionariosAtivos;
    }
    private List<Etapa> etapasAtivas;

    public List<Etapa> getEtapasAtivas() {
        return etapasAtivas;
    }
    private String placa;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    private String veiculo;

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    private String cliente;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    private Etapa proximaEtapa;

    public Etapa getProximaEtapa() {
        return proximaEtapa;
    }

    public void setProximaEtapa(Etapa proximaEtapa) {
        this.proximaEtapa = proximaEtapa;
    }
    private boolean inicioImediato;

    public boolean isInicioImediato() {
        return inicioImediato;
    }

    public void setInicioImediato(boolean inicioImediato) {
        this.inicioImediato = inicioImediato;
    }
    private Funcionario funcProximaEtapa;

    public Funcionario getFuncProximaEtapa() {
        return funcProximaEtapa;
    }

    public void setFuncProximaEtapa(Funcionario funcProximaEtapa) {
        this.funcProximaEtapa = funcProximaEtapa;
    }

    public OrdemServicoFacade getFacade() {
        return ejbFacade;
    }

    public OrdemServicoEtapa getCurrent() {
        return current;
    }

    public OrdemServicoController() {
        limparCampos();

    }

    public void salvar(ActionEvent actionEvent) {
        if (current.getDataEntrada() != null && current.getDataSaida() != null) {
            if (current.getDataEntrada().after(current.getDataSaida())) {
                JsfUtil.addErrorMessage(null, "A data de saída deve ser maior que a data de entrada na atividade.");
                return;
            }
        } else if (current.getDataSaida() != null && proximaEtapa == null) {
            JsfUtil.addErrorMessage(null, "Informe a próxima atividade.");
            return;
        } else if (current.getDataSaida() != null && current.getFuncionario() == null) {
            JsfUtil.addErrorMessage(null, "Informe o funcionário executor da atividade.");
            return;
        } else if (current.getDataSaida() != null && current.getHorasTrabalhadas() == 0) {
            JsfUtil.addErrorMessage(null, "Informe um valor de horas de trabalho.");
            return;
        }

        try {
            Session sessao = HibernateFactory.currentSession();
            ejbFacade.atualizarServico(sessao, LoginFilter.usuarioLogado(sessao), current, proximaEtapa, inicioImediato, funcProximaEtapa);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void limparCampos() {
        current = new OrdemServicoEtapa();
        setPlaca(null);
        setCliente(null);
        setVeiculo(null);
        funcionariosAtivos = montaListaFuncionarios();
        etapasAtivas = montaListaEtapas();
    }

    public void handleFileUpload(FileUploadEvent event) {
        //file = event.getFile();
    }

    public List<Etapa> montaListaEtapas() {
        List<Etapa> resultado = new ArrayList<Etapa>();
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade ebj = new EtapaFacade();
            resultado = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de etapas. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public void changePlaca() {
        if (placa == null || placa.isEmpty()) {
            return;
        }
        try {
            OrdemServico resultado = null;
            Session sessao = HibernateFactory.currentSession();
            resultado = ejbFacade.ObterOrdemServicoPorPlaca(sessao, placa);
            if (resultado != null) {
                setCliente(resultado.getOrcamento().getCliente().toString());
                setVeiculo(resultado.getOrcamento().getVeiculo().toString());
                current = resultado.getEtapaAtual();
                atividades = resultado.getEtapas();
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Nenhuma ordem de serviço localiza para a placa informada.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public List<Funcionario> montaListaFuncionarios() {
        List<Funcionario> resultado = new ArrayList<Funcionario>();
        try {
            Session sessao = HibernateFactory.currentSession();
            FuncionarioFacade ebj = new FuncionarioFacade();
            resultado = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de funcionarios. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }
}