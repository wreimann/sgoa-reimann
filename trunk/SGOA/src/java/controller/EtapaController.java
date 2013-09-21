package controller;

import facede.EtapaFacade;
import facede.ImagemEtapaFacade;
import facede.SetorFacade;
import facede.TipoServicoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Etapa;
import model.ImagemEtapa;
import model.Setor;
import model.TipoServico;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class EtapaController implements Serializable {

    private int pageSize = 10;
    private Etapa current;
    private LazyDataModel<Etapa> lazyModel;
    @EJB
    private facede.EtapaFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String descFiltro;
    private List<Setor> setoresAtivos;
    private List<TipoServico> tiposServicosAtivos;
    private List<ImagemEtapa> imagens;

    public String getDescFiltro() {
        return descFiltro;
    }
    
    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }
    public List<Setor> getSetoresAtivos() {
        return setoresAtivos;
    }
    public List<TipoServico> getTiposServicosAtivos() {
        return tiposServicosAtivos;
    }
    public List<ImagemEtapa> getImagens() {
        return imagens;
    }
    

    public EtapaController() {
        limparCampos();
        lazyModel = new LazyDataModel<Etapa>() {
            @Override
            public List<Etapa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Etapa> resultado = new ArrayList<Etapa>();
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

    public LazyDataModel<Etapa> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public EtapaFacade getFacade() {
        return ejbFacade;
    }

    public Etapa getCurrent() {
        return current;
    }

    
    public void prepararEdicao(ActionEvent event) {
        current = (Etapa) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Etapa) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Etapa();
        current.setAtivo(true);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Etapa alterada com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Etapa incluída com sucesso!");
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
            JsfUtil.addSuccessMessage("Etapa excluída com sucesso!");
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
        montaListaSetor();
        montaListaTipoServico();
        montaListaImagens();
    }
    
    private void montaListaSetor() {
        try {
            Session sessao = HibernateFactory.currentSession();
            SetorFacade ebjSetor = new SetorFacade();
            setoresAtivos = ebjSetor.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de setores. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
    
    private void montaListaTipoServico() {
        try {
            Session sessao = HibernateFactory.currentSession();
            TipoServicoFacade ebjTipoServico = new TipoServicoFacade();
            tiposServicosAtivos = ebjTipoServico.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de tipos de serviço. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
    
    private void montaListaImagens() {
        try {
            Session sessao = HibernateFactory.currentSession();
            ImagemEtapaFacade ebjImagem = new ImagemEtapaFacade();
            imagens = ebjImagem.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de imagens. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
}