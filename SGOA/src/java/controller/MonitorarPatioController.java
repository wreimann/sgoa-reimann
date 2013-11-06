package controller;

import facede.ClienteFacade;
import facede.EtapaFacade;
import facede.OrdemServicoFacade;
import facede.SetorFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Cliente;
import model.Etapa;
import model.OrdemServicoEtapa;
import model.Setor;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class MonitorarPatioController implements Serializable {

    private int pageSize = 10;
    private OrdemServicoEtapa current;
    private LazyDataModel<OrdemServicoEtapa> lazyModel;
    @EJB
    private OrdemServicoFacade ejbFacade;
    // <editor-fold defaultstate="collapsed" desc="propriedades para filtro da pesquisa">
    private String numero;
    private Cliente clienteFiltro;
    private String placaFiltro;
    private String situacaoFiltro;
    private Etapa atividadeFiltro;
    private Setor setorFiltro;
    private List<Etapa> etapas;
    private List<Setor> setores;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente cliente) {
        this.clienteFiltro = cliente;
    }

    public String getPlacaFiltro() {
        return placaFiltro;
    }

    public void setPlacaFiltro(String placaFiltro) {
        this.placaFiltro = placaFiltro;
    }

    public String getSituacaoFiltro() {
        return situacaoFiltro;
    }

    public void setSituacaoFiltro(String situacaoFiltro) {
        this.situacaoFiltro = situacaoFiltro;
    }

    public Etapa getAtividadeFiltro() {
        return atividadeFiltro;
    }

    public void setAtividadeFiltro(Etapa atividadeFiltro) {
        this.atividadeFiltro = atividadeFiltro;
    }

    public Setor getSetorFiltro() {
        return setorFiltro;
    }

    public void setSetorFiltro(Setor setorFiltro) {
        this.setorFiltro = setorFiltro;
    }

    public List<Etapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<Etapa> etapas) {
        this.etapas = etapas;
    }

    public List<Setor> getSetores() {
        return setores;
    }

    public void setSetores(List<Setor> setores) {
        this.setores = setores;
    }
    // </editor-fold>

    public MonitorarPatioController() {
        montaListaEtapas();
        montaListaSetores();
        lazyModel = new LazyDataModel<OrdemServicoEtapa>() {
            @Override
            public List<OrdemServicoEtapa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<OrdemServicoEtapa> resultado = new ArrayList<OrdemServicoEtapa>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.monitorarPatio(sessao, sortField, sortOrder, first, pageSize,
                            getNumero(), getClienteFiltro(), getPlacaFiltro(),
                            getSituacaoFiltro(), getAtividadeFiltro(), getSetorFiltro());
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

    public LazyDataModel<OrdemServicoEtapa> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public OrdemServicoFacade getFacade() {
        return ejbFacade;
    }

    public OrdemServicoEtapa getCurrent() {
        return current;
    }

    public void setCurrent(OrdemServicoEtapa current) {
        this.current = current;
    }

    public void limparCampos() {
        current = null;
        setNumero(null);
        setClienteFiltro(null);
        setPlacaFiltro(null);
        setAtividadeFiltro(null);
        setSetorFiltro(null);
        setSituacaoFiltro(null);

    }

    public List<Cliente> selecionarClienteAutoComplete(String query) {
        List<Cliente> resultado = new ArrayList<Cliente>();
        try {
            Session sessao = HibernateFactory.currentSession();
            ClienteFacade ebjCliente = new ClienteFacade();
            resultado = ebjCliente.selecionarPorNomeAutoComplete(sessao, query);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de clientes. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public final void montaListaEtapas() {
        List<Etapa> resultado = new ArrayList<Etapa>();
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade ebjEtapa = new EtapaFacade();
            resultado = ebjEtapa.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de atividades. ");
        } finally {
            HibernateFactory.closeSession();
        }
        setEtapas(resultado);
    }

    public final void montaListaSetores() {
        List<Setor> resultado = new ArrayList<Setor>();
        try {
            Session sessao = HibernateFactory.currentSession();
            SetorFacade ebj = new SetorFacade();
            resultado = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de setores. ");
        } finally {
            HibernateFactory.closeSession();
        }
        setSetores(resultado);
    }

    public String editar() {
      return "servico";
    }
}