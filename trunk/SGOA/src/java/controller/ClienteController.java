package controller;

import facede.ClienteFacade;
import facede.CorFacade;
import facede.MarcaFacade;
import facede.ModeloFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.ConstraintViolationException;
import model.Cliente;
import model.Cor;
import model.Marca;
import model.Modelo;
import model.Veiculo;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class ClienteController implements Serializable {

    private int pageSize = 10;
    private Cliente current;
    private LazyDataModel<Cliente> lazyModel;
    @EJB
    private facede.ClienteFacade ejbFacade;
    //propriedades para filtro da pesquisa
    private String nomeFiltro;
    private String placaFiltro;

    public String getNomeFiltro() {
        return nomeFiltro;
    }

    public void setNomeFiltro(String nomeFiltro) {
        this.nomeFiltro = nomeFiltro;
    }

    public String getPlacaFiltro() {
        return placaFiltro;
    }

    public void setPlacaFiltro(String placaFiltro) {
        this.placaFiltro = placaFiltro;
    }
    //propriedades cadastro
    private String tipoPessoa = "F";
    private String documento;
    private String sexo;
    private Date dataNasc;
    private String telefoneSecundario;
    private String cepEnd;
    private String ruaEnd;
    private Integer numeroEnd;
    private String complementoEnd;
    private String bairroEnd;
    private String cidadeEnd;
    private String ufEnd;
    private Marca marca;
    private Modelo modelo;
    private String placa;
    private Integer anoFab;
    private Integer anoModelo;
    private Cor cor;
    private List<Veiculo> veiculos;
    private List<Marca> marcasAtivos;
    private List<Modelo> modelosAtivos;
    private List<Cor> coresAtivas;

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

    public String getCepEnd() {
        return cepEnd;
    }

    public void setCepEnd(String cepEnd) {
        this.cepEnd = cepEnd;
    }

    public String getRuaEnd() {
        return ruaEnd;
    }

    public void setRuaEnd(String ruaEnd) {
        this.ruaEnd = ruaEnd;
    }

    public Integer getNumeroEnd() {
        return numeroEnd;
    }

    public void setNumeroEnd(Integer numeroEnd) {
        this.numeroEnd = numeroEnd;
    }

    public String getComplementoEnd() {
        return complementoEnd;
    }

    public void setComplementoEnd(String complementoEnd) {
        this.complementoEnd = complementoEnd;
    }

    public String getBairroEnd() {
        return bairroEnd;
    }

    public void setBairroEnd(String bairroEnd) {
        this.bairroEnd = bairroEnd;
    }

    public String getCidadeEnd() {
        return cidadeEnd;
    }

    public void setCidadeEnd(String cidadeEnd) {
        this.cidadeEnd = cidadeEnd;
    }

    public String getUfEnd() {
        return ufEnd;
    }

    public void setUfEnd(String ufEnd) {
        this.ufEnd = ufEnd;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getAnoFab() {
        return anoFab;
    }

    public void setAnoFab(Integer anoFab) {
        this.anoFab = anoFab;
    }

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public List<Marca> getMarcasAtivos() {
        return marcasAtivos;
    }

    public List<Modelo> getModelosAtivos() {
        return modelosAtivos;
    }

    public List<Cor> getCoresAtivas() {
        return coresAtivas;
    }

    public ClienteController() {
        this.veiculos = new ArrayList<Veiculo>();
        montaListaCor();
        montaListaMarca();
        lazyModel = new LazyDataModel<Cliente>() {

            @Override
            public List<Cliente> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Cliente> resultado = new ArrayList<Cliente>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize, getNomeFiltro(), getPlacaFiltro());
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

    public LazyDataModel<Cliente> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ClienteFacade getFacade() {
        return ejbFacade;
    }

    public Cliente getCurrent() {
        return current;
    }

    public void prepararEdicao(ActionEvent event) {
        current = (Cliente) lazyModel.getRowData();
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Cliente) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Cliente();
        current.setAtivo(true);
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            // current.setMarca(marcaCadastro);
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Cliente alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Cliente incluído com sucesso!");
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
            JsfUtil.addSuccessMessage("Cliente excluído com sucesso!");
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

    public void changeMarca() {
        setModelo(null);
        montaListaModelo();
    }

    public void limparCampos() {
        current = null;
        setNomeFiltro(null);
        setPlacaFiltro(null);
        setModelo(null);
        montaListaCor();
        montaListaMarca();
    }

    private void montaListaMarca() {
        try {
            Session sessao = HibernateFactory.currentSession();
            MarcaFacade ebjMarca = new MarcaFacade();
            marcasAtivos = ebjMarca.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de marcas. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    private void montaListaModelo() {
        try {
            Session sessao = HibernateFactory.currentSession();
            ModeloFacade ebjModelo = new ModeloFacade();
            modelosAtivos = ebjModelo.selecionarAtivosPorMarca(sessao, marca);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de modelos. ");
        } finally {
            HibernateFactory.closeSession();
        }

    }
    
    private void montaListaCor() {
        try {
            Session sessao = HibernateFactory.currentSession();
            CorFacade ebjCor = new CorFacade();
            coresAtivas = ebjCor.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de cores. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }
}