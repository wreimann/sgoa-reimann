package controller;

import facede.MarcaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Marca;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class MarcaController implements Serializable {

    private int pageSize = 10;
    private Marca current;
    private LazyDataModel<Marca> lazyModel;
    @EJB
    private facede.MarcaFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String descFiltro;

    public String getDescFiltro() {
        return descFiltro;
    }

    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }

    public MarcaController() {
        lazyModel = new LazyDataModel<Marca>() {

            @Override
            public List<Marca> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Marca> resultado = new ArrayList<Marca>();
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

    public LazyDataModel<Marca> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public MarcaFacade getFacade() {
        return ejbFacade;
    }

    public Marca getCurrent() {
        return current;
    }

    
    public void prepararEdicao(ActionEvent event) {
        current = (Marca) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Marca) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Marca();
        current.setAtivo(true);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Marca alterada com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Marca incluída com sucesso!");
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
            JsfUtil.addSuccessMessage("Marca excluída com sucesso!");
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