package controller;

import facede.ClienteFacade;
import facede.CorFacade;
import facede.MarcaFacade;
import facede.ModeloFacade;
import facede.PessoaFacade;
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
import model.Cor;
import model.Marca;
import model.Modelo;
import model.Pessoa;
import model.PessoaEndereco;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Veiculo;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
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
    private PessoaEndereco enderecoAux;
    private String tipoPessoa = "F";
    private String documento;
    private String sexo;
    private Date dataNasc;
    private Marca marca;
    private Veiculo currentVeiculo;
    private List<Veiculo> veiculos;
    private List<Marca> marcasAtivos;
    private List<Modelo> modelosAtivos;
    private List<Cor> coresAtivas;
    private boolean editandoVeiculo = false;

    public PessoaEndereco getEnderecoAux() {
        return enderecoAux;
    }

    public void setTipoPessoa(PessoaEndereco enderecoAux) {
        this.enderecoAux = enderecoAux;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public boolean isEditandoVeiculo() {
        return editandoVeiculo;
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

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public Veiculo getCurrentVeiculo() {
        return currentVeiculo;
    }

    public void setCurrentVeiculo(Veiculo currentVeiculo) {
        this.currentVeiculo = currentVeiculo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
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
        limparCampos();
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
        currentVeiculo = new Veiculo();
        current = (Cliente) lazyModel.getRowData();
        //dados basicos
        tipoPessoa = String.valueOf(current.getPessoa().getTipo());
        if (tipoPessoa.equals("F")) {
            documento = ((PessoaFisica) current.getPessoa()).getCpf();
            sexo = String.valueOf(((PessoaFisica) current.getPessoa()).getSexo());
            dataNasc = ((PessoaFisica) current.getPessoa()).getDataNascimento();
        } else {
            documento = ((PessoaJuridica) current.getPessoa()).getCnpj();
        }
        //endereco
        enderecoAux = new PessoaEndereco();
        if (current.getPessoa().getEndereco() != null) {
            enderecoAux.setCep(current.getPessoa().getEndereco().getCep().replaceAll("\\.", "").replaceAll("-", ""));
            enderecoAux.setBairro(current.getPessoa().getEndereco().getBairro());
            enderecoAux.setComplemento(current.getPessoa().getEndereco().getComplemento());
            enderecoAux.setLogradouro(current.getPessoa().getEndereco().getLogradouro());
            enderecoAux.setMunicipio(current.getPessoa().getEndereco().getMunicipio());
            enderecoAux.setPredical(current.getPessoa().getEndereco().getPredical());
            enderecoAux.setUf(current.getPessoa().getEndereco().getUf());
        }
        //veiculos
        veiculos = new ArrayList<Veiculo>();
        for (Veiculo veiculo : current.getVeiculos()) {
            veiculos.add(veiculo);
        }
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Cliente) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Cliente();
        current.setAtivo(true);
        limparCamposCadastro();
        currentVeiculo = new Veiculo();
        veiculos = new ArrayList<Veiculo>();
        enderecoAux = new PessoaEndereco();
    }

    public void excluirVeiculo(Veiculo veiculo) {
        veiculos.remove(veiculo);
    }

    public void editarVeiculo(Veiculo veiculo) {
        currentVeiculo = veiculo;
        marca = currentVeiculo.getModelo().getMarca();
        editandoVeiculo = true;
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            Pessoa pesAux = null;
            PessoaFacade pessoaFacade = new PessoaFacade();
            documento = documento.replaceAll("\\.", "").replaceAll("-", "").replace("/", "");
            if (current.getId() != null) {
                pesAux = pessoaFacade.obterPorId(sessao, current.getPessoa().getId());
            } else {
                Calendar cal = Calendar.getInstance();
                current.setDataCadastro(cal.getTime());
                Pessoa pessoa = pessoaFacade.selecionarPorNumeroDocumento(sessao, tipoPessoa.equals("F"), documento);
                if (pessoa != null) {
                    if (tipoPessoa.equals("F")) {
                        pesAux = (PessoaFisica) pessoa;
                    } else {
                        pesAux = (PessoaJuridica) pessoa;
                    }
                } else {
                    if (tipoPessoa.equals("F")) {
                        pesAux = new PessoaFisica();
                    } else {
                        pesAux = new PessoaJuridica();
                    }
                }
            }
            //dados basicos
            pesAux.setNome(current.getPessoa().getNome());
            pesAux.setTipo(tipoPessoa.charAt(0));
            if (tipoPessoa.equals("F")) {
                ((PessoaFisica) pesAux).setDataNascimento(dataNasc);
                ((PessoaFisica) pesAux).setCpf(documento);
                ((PessoaFisica) pesAux).setSexo(sexo.charAt(0));
            } else {
                ((PessoaJuridica) pesAux).setCnpj(documento);
            }
            //veiculo
            for (Veiculo veiculo : veiculos) {
                if (!current.getVeiculos().contains(veiculo)) {
                    veiculo.setPessoa(pesAux);
                    current.getVeiculos().add(veiculo);
                } else {
                    int index = current.getVeiculos().indexOf(veiculo);
                    current.getVeiculos().get(index).setAtivo(veiculo.getAtivo());
                    current.getVeiculos().get(index).setPessoa(pesAux);
                    current.getVeiculos().get(index).setAnoFabricacao(veiculo.getAnoFabricacao());
                    current.getVeiculos().get(index).setAnoModelo(veiculo.getAnoModelo());
                    current.getVeiculos().get(index).setPlaca(veiculo.getPlaca());
                    current.getVeiculos().get(index).setCor(veiculo.getCor());
                    current.getVeiculos().get(index).setModelo(veiculo.getModelo());
                }
            }
            for (int i = 0; i < current.getVeiculos().size(); i++) {
                if (!veiculos.contains(current.getVeiculos().get(i))) {
                    current.getVeiculos().remove(i);
                }
            }
            //contato
            pesAux.setTelefonePrimario(current.getPessoa().getTelefonePrimario());
            pesAux.setTelefoneSecundario(current.getPessoa().getTelefoneSecundario());
            pesAux.setEmail(current.getPessoa().getEmail());
            //endereco

            PessoaEndereco endereco = null;
            if (pesAux.getEndereco() != null && pesAux.getEndereco().getId() > 0) {
                endereco = pessoaFacade.obterPorIdEndereco(sessao, pesAux.getEndereco().getId());
            } else {
                endereco = new PessoaEndereco();
                endereco.setPessoa(pesAux);
            }
            endereco.setCep(enderecoAux.getCep().replaceAll("\\.", "").replaceAll("-", ""));
            endereco.setBairro(enderecoAux.getBairro());
            endereco.setComplemento(enderecoAux.getComplemento());
            endereco.setLogradouro(enderecoAux.getLogradouro());
            endereco.setMunicipio(enderecoAux.getMunicipio());
            endereco.setPredical(enderecoAux.getPredical());
            endereco.setUf(enderecoAux.getUf());
            pesAux.setEndereco(endereco);

            //inclusão
            current.setPessoa(pesAux);

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
        currentVeiculo.setModelo(null);
        montaListaModelo();
    }

    public void changeDocumento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            PessoaFacade pessoaFacade = new PessoaFacade();
            String numDoc = documento;
            String tipoAux = tipoPessoa;
            Pessoa pessoa = pessoaFacade.selecionarPorNumeroDocumento(sessao, tipoPessoa.equals("F"), numDoc.replaceAll("\\.", "").replaceAll("-", "").replace("/", ""));
            if (pessoa != null) {
                prepararInclusao(null);
                tipoPessoa = tipoAux;
                if (tipoPessoa.equals("F")) {
                    sexo = String.valueOf(((PessoaFisica) pessoa).getSexo());
                    dataNasc = ((PessoaFisica) pessoa).getDataNascimento();
                }
                documento = numDoc;
                current.getPessoa().setNome(pessoa.getNome());
                current.getPessoa().setTelefonePrimario(pessoa.getTelefonePrimario());
                current.getPessoa().setTelefoneSecundario(pessoa.getTelefoneSecundario());
                current.getPessoa().setEmail(pessoa.getEmail());
                this.enderecoAux = new PessoaEndereco();
                if (pessoa.getEndereco() != null) {
                    enderecoAux.setCep(pessoa.getEndereco().getCep());
                    enderecoAux.setBairro(pessoa.getEndereco().getBairro());
                    enderecoAux.setComplemento(pessoa.getEndereco().getComplemento());
                    enderecoAux.setLogradouro(pessoa.getEndereco().getLogradouro());
                    enderecoAux.setMunicipio(pessoa.getEndereco().getMunicipio());
                    enderecoAux.setPredical(pessoa.getEndereco().getPredical());
                    enderecoAux.setUf(pessoa.getEndereco().getUf());
                }
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Já existe uma pessoa cadastrada no sistema com o número do documento informado. <br />Os dados foram recuperados com sucesso!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar dados da pessoa.");
        } finally {
            HibernateFactory.closeSession();
        }

    }

    public void limparCampos() {
        current = null;
        setNomeFiltro(null);
        setPlacaFiltro(null);
        limparCamposCadastro();
    }

    private void limparCamposCadastro() {
        tipoPessoa = "F";
        documento = "";
        sexo = "";
        dataNasc = null;
        this.enderecoAux = new PessoaEndereco();
        //campos do cadastro de veiculo
        marca = null;
        currentVeiculo = null;
        veiculos = null;
        editandoVeiculo = false;
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

    public void adicionarVeiculo(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        String mensagem = "O campo '%s' é obrigatório.";
        if (currentVeiculo.getAnoFabricacao() == null || currentVeiculo.getAnoFabricacao() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format(mensagem, "Ano Fabricação"), ""));
        }
        if (currentVeiculo.getAnoModelo() == null || currentVeiculo.getAnoModelo() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format(mensagem, "Ano Modelo"), ""));
        }
        if (currentVeiculo.getPlaca() == null || currentVeiculo.getPlaca().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format(mensagem, "Placa"), ""));
        }
        if (currentVeiculo.getCor() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format(mensagem, "Cor"), ""));
        }
        if (currentVeiculo.getModelo() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format(mensagem, "Modelo"), ""));
        }
        if (context.getMessageList().isEmpty()) {
            if (veiculos.contains(currentVeiculo)) {
                if (editandoVeiculo) {
                    veiculos.remove(currentVeiculo);
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Veículo já cadastrado.", ""));
                    return;
                }
            }
            editandoVeiculo = false;
            veiculos.add(currentVeiculo);
            //limpa campos
            currentVeiculo = new Veiculo();
            marca = new Marca();
        }

    }
}