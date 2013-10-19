package controller;

import facede.ConfigOrdemServicoFacade;
import facede.EtapaFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.ConfigOrdemServico;
import model.Etapa;
import org.hibernate.Session;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class ConfigOrdemServicoController implements Serializable {

    private ConfigOrdemServico current;
    private List<Etapa> etapasAtiva;

    public List<Etapa> getEtapasAtiva() {
        return etapasAtiva;
    }

    public ConfigOrdemServicoController() {
        montaListaEtapas();
        try {
            Session sessao = HibernateFactory.currentSession();
            ConfigOrdemServicoFacade ebjFacade = new ConfigOrdemServicoFacade();
            current = ebjFacade.obterPorId(sessao, 1);     
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao consultar dados. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public ConfigOrdemServico getCurrent() {
        return current;
    }
    
  
    public void salvar() {
        try {
            Session sessao = HibernateFactory.currentSession();
            ConfigOrdemServicoFacade ebjFacade = new ConfigOrdemServicoFacade();
            ebjFacade.alterar(sessao, current);
            JsfUtil.addSuccessMessage("Configurações alteradas com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    private void montaListaEtapas() {
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade ebjEtapa = new EtapaFacade();
            etapasAtiva = ebjEtapa.selecionarTodos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao consultar dados. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
}