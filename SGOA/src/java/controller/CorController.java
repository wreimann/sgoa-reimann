package controller;

import facede.CorFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.ConstraintViolationException;
import model.Cor;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class CorController implements Serializable {

    private int pageSize = 10;
    private Cor current;
    private LazyDataModel<Cor> lazyModel;
    @EJB
    private facede.CorFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String descFiltro;

    public String getDescFiltro() {
        return descFiltro;
    }

    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }

    public CorController() {
        lazyModel = new LazyDataModel<Cor>() {

            @Override
            public List<Cor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Cor> resultado = new ArrayList<Cor>();
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

    public LazyDataModel<Cor> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public CorFacade getFacade() {
        return ejbFacade;
    }

    public Cor getCurrent() {
        return current;
    }

    
    public void prepararEdicao(ActionEvent event) {
        current = (Cor) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Cor) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Cor();
        current.setAtivo(true);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Cor alterada com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Cor incluída com sucesso!");
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
            JsfUtil.addSuccessMessage("Cor excluída com sucesso!");
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

    public void limparCampos() {
        current = null;
        setDescFiltro(null);
    }
}