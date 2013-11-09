package controller;

import facede.ClienteFacade;
import facede.FluxoFacade;
import facede.OrcamentoFacade;
import facede.OrdemServicoFacade;
import facede.SeguradoraFacade;
import facede.TipoServicoFacade;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Cliente;
import model.Fluxo;
import model.Orcamento;
import model.OrcamentoAnexo;
import model.OrcamentoTipoServico;
import model.OrdemServico;
import model.Seguradora;
import model.TipoServico;
import model.Veiculo;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import util.HibernateFactory;
import util.JsfUtil;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class OrcamentoController implements Serializable {

    private int pageSize = 10;
    private Orcamento current;
    private LazyDataModel<Orcamento> lazyModel;
    @EJB
    private facede.OrcamentoFacade ejbFacade;
    @ManagedProperty(value="#{loginController}")
    private LoginController loginController;
    // <editor-fold defaultstate="collapsed" desc="propriedades para filtro da pesquisa">
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
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="propriedades cadastro">
    private List<Veiculo> veiculos;
    private List<Seguradora> seguradoras;
    private List<OrcamentoTipoServico> servicos;
    private List<TipoServico> tipoServicos;
    private List<Fluxo> fluxos;
    private Fluxo fluxo;
    private double totalHoras;
    private double totalDescoto;
    private double totalServico;
    private UploadedFile file;
    private StreamedContent fileDownload;
    private String motivoCancelamento;
    private Boolean bloqueado;
    private Boolean gerarOS;

    public Fluxo getFluxo() {
        return fluxo;
    }

    public void setFluxo(Fluxo fluxo) {
        this.fluxo = fluxo;
    }

    public Boolean getBloqueado() {
        bloqueado = false;
        if (current != null) {
            bloqueado = current.getSituacao() == 'C' || current.getSituacao() == 'P';
        }
        return bloqueado;
    }

    public Boolean getGerarOS() {
        gerarOS = false;
        if (current != null) {
            gerarOS = current.getSituacao() == 'A';
        }
        return gerarOS;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(StreamedContent fileDownload) {
        this.fileDownload = fileDownload;
    }

    public List<Fluxo> getFluxos() {
        return fluxos;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public List<Seguradora> getSeguradoras() {
        return seguradoras;
    }

    public List<OrcamentoTipoServico> getServicos() {
        return servicos;
    }

    public List<TipoServico> getTipoServicos() {
        return tipoServicos;
    }

    public double getTotalHoras() {
        return totalHoras;
    }

    public double getTotalDescoto() {
        return totalDescoto;
    }

    public double getTotalServico() {
        return totalServico;
    }

    public String getTotalHorasString() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(1);
        formatter.setMinimumFractionDigits(1);
        return formatter.format(totalHoras);
    }

    public String getTotalDescotoString() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(totalDescoto);
    }

    public String getTotalServicoString() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(totalServico);
    }
    // </editor-fold>

    public OrcamentoController() {
        limparCampos();
        lazyModel = new LazyDataModel<Orcamento>() {
            @Override
            public List<Orcamento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Orcamento> resultado = new ArrayList<Orcamento>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize,
                            getNumero(), getSituacaoFiltro(), getClienteFiltro(), getPlacaFiltro(), getDataInicial(), getDataFinal());
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
        veiculos = current.getCliente().getVeiculos();
        servicos = current.getServicos();
        changeTipoServico();
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
            if (file != null) {
                if (current.getAnexos() != null && current.getAnexos().size() > 0) {
                    current.getAnexos().get(0).setNomeArquivo(file.getFileName());
                    current.getAnexos().get(0).setImagem(file.getContents());
                } else {
                    OrcamentoAnexo anexo = new OrcamentoAnexo();
                    anexo.setOrcamento(current);
                    anexo.setNomeArquivo(file.getFileName());
                    anexo.setImagem(file.getContents());
                    if (current.getAnexos() == null) {
                        current.setAnexos(new ArrayList<OrcamentoAnexo>());
                    }
                    current.getAnexos().add(anexo);
                }
            }
            current.setServicos(servicos);
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Orçamento alterado com sucesso!");
            } else {
                current.setFuncionarioCancelamento(getLoginController().getUsuarioSession());
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Orçamento incluído com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
        } finally {
            HibernateFactory.closeSession();
        }

    }

    public void cancelar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            Calendar cal = Calendar.getInstance();
            current.setFuncionarioCancelamento(loginController.getUsuarioSession());
            current.setDataCancelamento(cal.getTime());
            current.setSituacao('C');
            current.setMotivoCancelamento(motivoCancelamento);
            ejbFacade.alterar(sessao, current);
            JsfUtil.addSuccessMessage("Orçamento cancelado com sucesso!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao cancelar o orçamento. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void ordemServico(ActionEvent actionEvent) {
        char situacaoAnterior = current.getSituacao();
        try {
            Session sessao = HibernateFactory.currentSession();
            OrdemServico os = new OrdemServico();
            os.setFuncionarioAprovacao(loginController.getUsuarioSession());
            os.setOrcamento(current);
            OrdemServicoFacade osFacade = new OrdemServicoFacade();
            osFacade.incluir(sessao, os);
            JsfUtil.addSuccessMessage("Ordem de Serviço gerada com sucesso!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao aprovar o orçamento tente novamente.");
            current.setSituacao(situacaoAnterior);
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void changeCliente(SelectEvent event) {
        veiculos = ((Cliente) event.getObject()).getVeiculos();
        current.setVeiculo(null);
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
        seguradoras = montaListaSeguradoras();
        tipoServicos = montaListaTipoServicos();
        fluxos = montaListaFluxos();
        file = null;
        totalDescoto = 0;
        totalHoras = 0;
        totalServico = 0;
        servicos = new ArrayList<OrcamentoTipoServico>();
        for (TipoServico tipo : tipoServicos) {
            OrcamentoTipoServico novo = new OrcamentoTipoServico();
            novo.setTipoServico(tipo);
            novo.setValorHora(tipo.getValorHoraPadrao());
            novo.setOrcamento(current);
            servicos.add(novo);
        }
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

    public List<Seguradora> montaListaSeguradoras() {
        List<Seguradora> resultado = new ArrayList<Seguradora>();
        try {
            Session sessao = HibernateFactory.currentSession();
            SeguradoraFacade ebjSeguradora = new SeguradoraFacade();
            resultado = ebjSeguradora.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de seguradoras. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public List<TipoServico> montaListaTipoServicos() {
        List<TipoServico> resultado = new ArrayList<TipoServico>();
        try {
            Session sessao = HibernateFactory.currentSession();
            TipoServicoFacade ebj = new TipoServicoFacade();
            resultado = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de seguradoras. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public List<Fluxo> montaListaFluxos() {
        List<Fluxo> resultado = new ArrayList<Fluxo>();
        try {
            Session sessao = HibernateFactory.currentSession();
            FluxoFacade ebj = new FluxoFacade();
            resultado = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de seguradoras. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public void changeTipoServico() {
        totalDescoto = 0;
        totalHoras = 0;
        totalServico = 0;
        for (OrcamentoTipoServico servico : servicos) {
            totalDescoto += servico.getDesconto();
            totalHoras += servico.getHoras();
            servico.setTotal((servico.getValorHora() * servico.getHoras()) - servico.getDesconto());
            totalServico += servico.getTotal();
        }
        changeValorOrcamento();

    }

    public void changeValorOrcamento() {
        if (current != null) {
            current.setValorTotal((totalServico + current.getValorPecas() + current.getValorAdicional()) - current.getValorDesconto());
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        InputStream is = new ByteArrayInputStream(file.getContents());
        StreamedContent image = new DefaultStreamedContent(is, "application/pdf", "orçamento_anexo.pdf");
        fileDownload = image;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
