package controller;

import facede.MarcaFacade;
import facede.ModeloFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Marca;
import model.Modelo;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class ModeloController implements Serializable {

    private int pageSize = 10;
    private Modelo current;
    private LazyDataModel<Modelo> lazyModel;
    @EJB
    private facede.ModeloFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String descFiltro;
    private Marca marcaFiltro;
    private Marca marcaCadastro;
    private List<Marca> marcas;
    private List<Marca> marcasAtivos;

    public String getDescFiltro() {
        return descFiltro;
    }

    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }
    public Marca getMarcaFiltro() {
        return marcaFiltro;
    }

    public void setMarcaFiltro(Marca marca) {
        this.marcaFiltro = marca;
    }
    
    public Marca getMarcaCadastro() {
        return marcaCadastro;
    }

    public void setMarcaCadastro(Marca marca) {
        this.marcaCadastro = marca;
    }
    
    public ModeloController() {
        lazyModel = new LazyDataModel<Modelo>() {
           @Override
            public List<Modelo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Modelo> resultado = new ArrayList<Modelo>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize, getDescFiltro(), getMarcaFiltro());
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
        montaListaMarca();
    }

    public LazyDataModel<Modelo> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ModeloFacade getFacade() {
        return ejbFacade;
    }

    public Modelo getCurrent() {
        return current;
    }

    
    public void prepararEdicao(ActionEvent event) {
        current = (Modelo) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Modelo) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Modelo();
        current.setAtivo(true);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
             current.setMarca(marcaCadastro);
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Modelo alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Modelo incluído com sucesso!");
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
        marcasAtivos = null;
        marcas = null;
        setDescFiltro(null);
        setMarcaFiltro(null);
        montaListaMarca();
    }
    
    public List<Marca> getMarcas() {
        return marcas;
    }

    public List<Marca> getMarcasAtivos() {
        return marcasAtivos;
    }

    private void montaListaMarca() {
        try {
            Session sessao = HibernateFactory.currentSession();
            MarcaFacade ebjFabricante = new MarcaFacade();
            marcasAtivos = ebjFabricante.selecionarTodosAtivos(sessao);
            //listagem de fabricante filtro
            marcas = ebjFabricante.selecionarTodos(sessao, "descricao", SortOrder.ASCENDING);
        } catch (Exception ex) {
            Logger.getLogger(ModeloController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HibernateFactory.closeSession();
        }
    }
}