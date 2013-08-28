package controller;

import facede.ClienteFacade;
import facede.OrcamentoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Cliente;
import model.Orcamento;
import model.Veiculo;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class OrcamentoController implements Serializable {

    private int pageSize = 10;
    private Orcamento current;
    private LazyDataModel<Orcamento> lazyModel;
    @EJB
    private facede.OrcamentoFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String numero;
    private Cliente clienteFiltro;
    private String placaFiltro;
    private Date dataInicial;
    private Date dataFinal;
    private String situacaoFiltro;

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

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getSituacaoFiltro() {
        return situacaoFiltro;
    }

    public void setSituacaoFiltro(String situacaoFiltro) {
        this.situacaoFiltro = situacaoFiltro;
    }
    //propriedades cadastro
    private List<Veiculo> veiculos;

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public OrcamentoController() {
        limparCampos();
        lazyModel = new LazyDataModel<Orcamento>() {

            @Override
            public List<Orcamento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Orcamento> resultado = new ArrayList<Orcamento>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize, "", "");
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

    public LazyDataModel<Orcamento> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public OrcamentoFacade getFacade() {
        return ejbFacade;
    }

    public Orcamento getCurrent() {
        return current;
    }

    public void prepararEdicao(ActionEvent event) {
        current = (Orcamento) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Orcamento) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Orcamento();
        limparCamposCadastro();

    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Orçamento alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Orçamento incluído com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            ejbFacade.excluir(sessao, current);
            JsfUtil.addSuccessMessage("Orçamento excluído com sucesso!");
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

    public void changeCliente() {
        current.setVeiculo(null);
        veiculos = new ArrayList<Veiculo>();
        for (Veiculo veiculo : current.getCliente().getVeiculos()) {
            getVeiculos().add(veiculo);
        }
    }

    public void limparCampos() {
        current = null;
        setNumero("");
        setClienteFiltro(null);
        setPlacaFiltro("");
        setDataFinal(null);
        setDataInicial(null);
        setSituacaoFiltro("");
        limparCamposCadastro();
    }

    private void limparCamposCadastro() {
        veiculos = new ArrayList<Veiculo>();

    }

    public List<Cliente> selecionarClienteAutoComplete(String query) {
        List<Cliente> resultado = new ArrayList<Cliente>();
        try {
            Session sessao = HibernateFactory.currentSession();
            ClienteFacade ebjCliente = new ClienteFacade();
            resultado = ebjCliente.selecionarPorNomeAutoComplete(sessao, query);        
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de cores. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }
}