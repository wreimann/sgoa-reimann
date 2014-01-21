package controller;

import facede.FuncionarioFacade;
import facede.PerfilFacade;
import facede.PessoaFacade;
import facede.ProfissaoFacade;
import facede.SetorFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Funcionario;
import model.Perfil;
import model.Pessoa;
import model.PessoaEndereco;
import model.PessoaFisica;
import model.Profissao;
import model.Setor;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class FuncionarioController implements Serializable {

    private int pageSize = 10;
    private Funcionario current;
    private LazyDataModel<Funcionario> lazyModel;
    @EJB
    private facede.FuncionarioFacade ejbFacade;
    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;
    //propriedades para filtro da pesquisa
    private String nomeFiltro;
    private boolean editarAcesso;

    public String getNomeFiltro() {
        return nomeFiltro;
    }

    public void setNomeFiltro(String nomeFiltro) {
        this.nomeFiltro = nomeFiltro;
    }
    //propriedades cadastro
    private PessoaEndereco enderecoAux;
    private String documento;
    private String matricula;
    private List<Setor> setoresAtivos;
    private List<Profissao> profissoesAtivos;
    private List<Perfil> perfisAtivos;

    public PessoaEndereco getEnderecoAux() {
        return enderecoAux;
    }

    public void setTipoPessoa(PessoaEndereco enderecoAux) {
        this.enderecoAux = enderecoAux;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<Setor> getSetoresAtivos() {
        return setoresAtivos;
    }

    public List<Profissao> getProfissoesAtivos() {
        return profissoesAtivos;
    }

    public List<Perfil> getPerfisAtivos() {
        return perfisAtivos;
    }

    public FuncionarioController() {
        limparCampos();
        lazyModel = new LazyDataModel<Funcionario>() {
            @Override
            public List<Funcionario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Funcionario> resultado = new ArrayList<Funcionario>();
                try {
                    Session sessao = HibernateFactory.currentSession();
                    resultado = ejbFacade.selecionarPorParametros(sessao, sortField, sortOrder, first, pageSize, getNomeFiltro());
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

    public LazyDataModel<Funcionario> getLazyModel() {
        return lazyModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public FuncionarioFacade getFacade() {
        return ejbFacade;
    }

    public Funcionario getCurrent() {
        return current;
    }

    public void prepararEdicao(ActionEvent event) {
        current = (Funcionario) lazyModel.getRowData();
        documento = current.getPessoa().getCpf();
        matricula = current.getMatricula().toString();
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
    }

    public void prepararExclusao(ActionEvent event) {
        current = (Funcionario) lazyModel.getRowData();
    }

    public void prepararInclusao(ActionEvent event) {
        current = new Funcionario();
        current.setAtivo(true);
        limparCamposCadastro();
        enderecoAux = new PessoaEndereco();
    }

    public void salvar(ActionEvent actionEvent) {
        try {
            Session sessao = HibernateFactory.currentSession();
            PessoaFisica pesAux = null;
            PessoaFacade pessoaFacade = new PessoaFacade();
            documento = documento.replaceAll("\\.", "").replaceAll("-", "").replace("/", "");
            if (current.getId() != null) {
                pesAux = (PessoaFisica) pessoaFacade.obterPorId(sessao, current.getPessoa().getId());
            } else {
                Calendar cal = Calendar.getInstance();
                current.setDataCadastro(cal.getTime());
                Pessoa pessoa = pessoaFacade.selecionarPorNumeroDocumento(sessao, true, documento);
                if (pessoa != null) {
                    pesAux = (PessoaFisica) pessoa;
                } else {
                    pesAux = new PessoaFisica();
                }
            }
            //dados basicos
            pesAux.setNome(current.getPessoa().getNome());
            pesAux.setTipo('F');
            pesAux.setDataNascimento(current.getPessoa().getDataNascimento());
            pesAux.setCpf(documento);
            pesAux.setSexo(current.getPessoa().getSexo());
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
                JsfUtil.addSuccessMessage("Funcionário alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Funcionário incluído com sucesso!");
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
            JsfUtil.addSuccessMessage("Funcionário excluído com sucesso!");
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
        setNomeFiltro(null);
        limparCamposCadastro();
    }

    private void limparCamposCadastro() {
        documento = matricula = "";
        this.enderecoAux = new PessoaEndereco();
        //campos do cadastro de veiculo
        montaListaProfissao();
        montaListaSetor();
        montaListaPerfil();
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

    private void montaListaProfissao() {
        try {
            Session sessao = HibernateFactory.currentSession();
            ProfissaoFacade ebjProfissao = new ProfissaoFacade();
            profissoesAtivos = ebjProfissao.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de profissões. ");
        } finally {
            HibernateFactory.closeSession();
        }

    }

    private void montaListaPerfil() {
        try {
            Session sessao = HibernateFactory.currentSession();
            PerfilFacade ebj = new PerfilFacade();
            perfisAtivos = ebj.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de perfil. ");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public boolean isEditarAcesso() {
        return getLoginController().usuarioLogadoIsGerente();
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    
    public void changeDocumento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            PessoaFacade pessoaFacade = new PessoaFacade();
            String numDoc = documento;
            Pessoa pessoa = pessoaFacade.selecionarPorNumeroDocumento(sessao, true, numDoc.replaceAll("\\.", "").replaceAll("-", "").replace("/", ""));
            if (pessoa != null) {
                prepararInclusao(null);
                current.getPessoa().setSexo(((PessoaFisica)pessoa).getSexo());
                current.getPessoa().setDataNascimento(((PessoaFisica)pessoa).getDataNascimento());
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
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Já existe uma pessoa cadastrada no sistema com o número do documento informado. Os dados foram recuperados com sucesso!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar dados da pessoa.");
        } finally {
            HibernateFactory.closeSession();
        }

    }
}