package controller;

import facede.FuncionarioFacade;
import facede.PerfilFacade;
import facede.ProfissaoFacade;
import facede.SetorFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.Funcionario;
import model.Perfil;
import model.PessoaEndereco;
import model.PessoaFisica;
import model.Profissao;
import model.Setor;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
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
    //propriedades para filtro da pesquisa
    private String nomeFiltro;

    public String getNomeFiltro() {
        return nomeFiltro;
    }

    public void setNomeFiltro(String nomeFiltro) {
        this.nomeFiltro = nomeFiltro;
    }
    //propriedades cadastro
    private PessoaEndereco enderecoAux;
    private String documento;
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
        //endereco
        enderecoAux = current.getPessoa().getEndereco();
        if (enderecoAux == null) {
            enderecoAux = new PessoaEndereco();
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
            PessoaFisica pesAux = null;
            if (current.getId() != null) {
                pesAux = current.getPessoa();
            } else {
                Calendar cal = Calendar.getInstance();
                current.setDataCadastro(cal.getTime());
                pesAux = new PessoaFisica();
            }
            //dados basicos
            pesAux.setNome(current.getPessoa().getNome());
            pesAux.setTipo('F');
            documento = documento.replaceAll("\\.", "").replaceAll("-", "").replace("/", "");
            pesAux.setDataNascimento(current.getPessoa().getDataNascimento());
            pesAux.setCpf(documento);
            pesAux.setSexo(current.getPessoa().getSexo());
            //contato
            pesAux.setTelefonePrimario(current.getPessoa().getTelefonePrimario());
            pesAux.setTelefoneSecundario(current.getPessoa().getTelefoneSecundario());
            pesAux.setEmail(current.getPessoa().getEmail());
            //endereco
            if (!enderecoAux.getLogradouro().isEmpty()) {
                enderecoAux.setCep(enderecoAux.getCep().replaceAll("\\.", "").replaceAll("-", ""));
                enderecoAux.setPessoa(pesAux);
                pesAux.setEndereco(enderecoAux);
            } else {
                pesAux.setEndereco(null);
            }
            //inclusão
            current.setPessoa(pesAux);
            Session sessao = HibernateFactory.currentSession();
            if (current.getId() != null) {
                ejbFacade.alterar(sessao, current);
                JsfUtil.addSuccessMessage("Funcionário alterado com sucesso!");
            } else {
                ejbFacade.incluir(sessao, current);
                JsfUtil.addSuccessMessage("Funcionário incluído com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro. ");
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
        documento = "";
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

}