package controller;

import facede.EtapaFacade;
import facede.FluxoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Etapa;
import model.Fluxo;
import model.FluxoEtapa;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class FluxoController implements Serializable {

    private int pageSize = 10;
    private Fluxo current;
    private LazyDataModel<Fluxo> lazyModel;
    @EJB
    private facede.FluxoFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String descFiltro;
    private List<Etapa> etapasAtivas;
    private List<FluxoEtapa> etapas;

    public String getDescFiltro() {
        return descFiltro;
    }

    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }

    public List<Etapa> getEtapasAtivas() {
        return etapasAtivas;
    }
    
    public List<FluxoEtapa> getEtapas() {
        return etapas;
    }

    public FluxoController() {
        limparCampos();
        lazyModel = new LazyDataModel<Fluxo>() {

            @Override
            public List<Fluxo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Fluxo> resultado = new ArrayList<Fluxo>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorDescricao(sessao, sortField, sortOrder, first, pageSize, getDescFiltro());
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex, "Erro ao consultar dados. ");
                } finally {
                    HibernateFactory.closeSession();
                }
                lazyModel.setRowCount(ejbFacade.getRowCount().intValue());
                return resultado;
            }
        };
        lazyModel.setPageSize(pageSize);
    }

    public LazyDataModel<Fluxo> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public FluxoFacade getFacade() {
        return ejbFacade;
    }

    public Fluxo getCurrent() {
        return current;
    }

    public void prepararEdicao(ActionEvent event) {
        current = (Fluxo) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Fluxo) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Fluxo();
        current.setAtivo(true);
        desenho = new DefaultMindmapNode("Inicio", "Inicio", "FFCC00", false);
        MindmapNode fim = new DefaultMindmapNode("Fim", "Fim", "FFCC00", true);
        desenho.addNode(fim);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Fluxo alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Fluxo incluído com sucesso!");
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
        } finally {
            HibernateFactory.closeSession();
        }

    }

    public void excluir(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            ejbFacade.excluir(sessao, current);
            JsfUtil.addSuccessMessage("Fluxo excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                JsfUtil.addErrorMessage("Este registro esta associado a outros cadastros. Exclusão não permitida.");
            } else {
                JsfUtil.addErrorMessage(ex, "Erro ao excluir o registro. ");
            }
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public final void limparCampos() {
        current = null;
        setDescFiltro(null);
        montaListaEtapasAtivas();
    }

    private void montaListaEtapasAtivas() {
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade ebjEtapa = new EtapaFacade();
            etapasAtivas = ebjEtapa.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de etapas ativas. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
    private MindmapNode desenho;
    private MindmapNode selectedNode;

    public MindmapNode getDesenho() {
        return desenho;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(SelectEvent event) {
        MindmapNode node = (MindmapNode) event.getObject();

        //populate if not already loaded  
        if (node.getChildren().isEmpty()) {
            Object label = node.getLabel();

            if (label.equals("NS(s)")) {
                for (int i = 0; i < 25; i++) {
                    node.addNode(new DefaultMindmapNode("ns" + i + ".google.com", "Namespace " + i + " of Google", "82c542"));
                }
            } else if (label.equals("IPs")) {
                for (int i = 0; i < 18; i++) {
                    node.addNode(new DefaultMindmapNode("1.1.1." + i, "IP Number: 1.1.1." + i, "fce24f"));
                }

            } else if (label.equals("Malware")) {
                for (int i = 0; i < 18; i++) {
                    String random = UUID.randomUUID().toString();
                    node.addNode(new DefaultMindmapNode("Malware-" + random, "Malicious Software: " + random, "3399ff", false));
                }
            }
        }

    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
    }
}