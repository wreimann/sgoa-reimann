package controller;

import facede.ClienteFacade;
import facede.EtapaFacade;
import facede.OrdemServicoFacade;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import model.Cliente;
import model.Etapa;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.OrdemServicoFoto;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public final class OSController implements Serializable {

    @EJB
    private OrdemServicoFacade ejbFacade;
    private OrdemServico current;
    private int pageSize = 10;
    private LazyDataModel<OrdemServico> lazyModel;
    private List<Etapa> etapasAtivas;
    private String orcamentoFiltro;
    private String situacaoFiltro;
    private Etapa etapaFiltro;
    private Cliente clienteFiltro;
    private String placaFiltro;
    private List<OrdemServicoFoto> fotosOS;
    private List<OrdemServicoEtapa> atividades;
    private List<OrdemServicoEvento> eventos;
    private OrdemServicoEtapa atividade;

    public List<Etapa> getEtapasAtivas() {
        return etapasAtivas;
    }

    public String getOrcamentoFiltro() {
        return orcamentoFiltro;
    }

    public void setOrcamentoFiltro(String orcamentoFiltro) {
        this.orcamentoFiltro = orcamentoFiltro;
    }

    public String getSituacaoFiltro() {
        return situacaoFiltro;
    }

    public void setSituacaoFiltro(String situacaoFiltro) {
        this.situacaoFiltro = situacaoFiltro;
    }

    public String getPlacaFiltro() {
        return placaFiltro;
    }

    public void setPlacaFiltro(String placaFiltro) {
        this.placaFiltro = placaFiltro;
    }

    public Etapa getEtapaFiltro() {
        return etapaFiltro;
    }

    public void setEtapaFiltro(Etapa etapaFiltro) {
        this.etapaFiltro = etapaFiltro;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente clienteFiltro) {
        this.clienteFiltro = clienteFiltro;
    }

    public OrdemServicoFacade getFacade() {
        return ejbFacade;
    }

    public OrdemServico getCurrent() {
        return current;
    }

    public OSController() {
        limparCampos();
        lazyModel = new LazyDataModel<OrdemServico>() {
            @Override
            public List<OrdemServico> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<OrdemServico> resultado = new ArrayList<OrdemServico>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize,
                            getOrcamentoFiltro(), getSituacaoFiltro(), getEtapaFiltro(), getClienteFiltro(), getPlacaFiltro());
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex, "Erro ao consultar dados. ");
                } finally {
                    HibernateFactory.closeSession();
                }
                getLazyModel().setRowCount(ejbFacade.getRowCount().intValue());
                return resultado;
            }
        };
        lazyModel.setPageSize(pageSize);
    }

    public void prepararEdicao(ActionEvent event) {
        current = (OrdemServico) getLazyModel().getRowData();
        try {
            Session sessao = HibernateFactory.currentSession();
            current = ejbFacade.ObterOrdemServicoPorId(sessao, current.getId());
            Hibernate.initialize(current.getEtapas());
            atividades = current.getEtapas();
            fotosOS = ejbFacade.obterFotosOS(sessao, current.getId());
            if (fotosOS == null) {
                fotosOS = new ArrayList<OrdemServicoFoto>();
            }
            changeFoto();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro carregar as fotos.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void limparCampos() {
        current = new OrdemServico();
        setPlacaFiltro(null);
        setClienteFiltro(null);
        setEtapaFiltro(null);
        setOrcamentoFiltro(null);
        setSituacaoFiltro(null);
        etapasAtivas = montaListaEtapas();
        fotosOS = new ArrayList<OrdemServicoFoto>();
        atividades = new ArrayList<OrdemServicoEtapa>();
        setAtividade(null);
        eventos = new ArrayList<OrdemServicoEvento>();
    }

    public List<Etapa> montaListaEtapas() {
        List<Etapa> resultado = new ArrayList<Etapa>();
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade etapaFacade = new EtapaFacade();
            resultado = etapaFacade.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de etapas. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public LazyDataModel<OrdemServico> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<OrdemServico> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<OrdemServicoFoto> getFotosOS() {
        return fotosOS;
    }

    private void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeFoto() {
        try {
            for (OrdemServicoFoto f : fotosOS) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                String nomeArquivo = f.getId().toString() + ".jpg";
                String arquivo = scontext.getRealPath("/fotos/" + nomeArquivo);
                criaArquivo(f.getImagem(), arquivo);
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar as fotos. Tente novamente");
        }
    }

    public void salvar(ActionEvent actionEvent) {
        char situacaoAnterior = current.getSituacao();
        try {
            Session sessao = HibernateFactory.currentSession();
            ejbFacade.alterar(sessao, current);
            JsfUtil.addSuccessMessage("Ordem de Serviço atualizada com sucesso!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao atualizar a ordem de serviço.");
            current.setSituacao(situacaoAnterior);
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public List<OrdemServicoEtapa> getAtividades() {
        return atividades;
    }

    public List<OrdemServicoEvento> getEventos() {
        return eventos;
    }

    public void prepararEvento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            OrdemServicoEtapa etapaAux = ejbFacade.obterEtapa(sessao, getAtividade().getId());
            Hibernate.initialize(etapaAux.getEventos());
            eventos = etapaAux.getEventos();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao carregar os eventos da atividade.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public OrdemServicoEtapa getAtividade() {
        return atividade;
    }

    public void setAtividade(OrdemServicoEtapa atividade) {
        this.atividade = atividade;
    }
}
