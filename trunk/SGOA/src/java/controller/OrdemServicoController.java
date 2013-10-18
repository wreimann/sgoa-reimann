package controller;

import facede.EtapaFacade;
import facede.FuncionarioFacade;
import facede.OrdemServicoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Etapa;
import model.Funcionario;
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
    private boolean exibirDetalhes;

    public boolean getExibirDetalhes() {
        return exibirDetalhes;
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
//        try {
//            Session sessao = HibernateFactory.currentSession();
//            if (current.getId() != null) {
//                ejbFacade.alterar(sessao, current);
//                JsfUtil.addSuccessMessage("Serviço alterado com sucesso!");
//            } else {
//                ejbFacade.incluir(sessao, current);
//                JsfUtil.addSuccessMessage("Serviço incluído com sucesso!");
//            }
//
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
//        } finally {
//            HibernateFactory.closeSession();
//        }
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
        //current.setVeiculo(null);
        setCliente("");
        exibirDetalhes = true;
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