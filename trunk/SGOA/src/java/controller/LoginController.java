package controller;

import facede.FuncionarioFacade;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Funcionario;
import org.hibernate.Session;
import util.CriptografiaUtil;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private String email;
    private String emailEnvio;
    private String password;
    private String newpassword;
    private Funcionario usuarioSession = null;

    public LoginController() {
        setEmail(null);
        setPassword(null);
        setNewpassword(null);

    }

    public String login() {
        FuncionarioFacade ebjUsario = new FuncionarioFacade();
        Funcionario usuario = null;
        String senha = null;
        try {
            senha = CriptografiaUtil.encrypt(password);
        } catch (NoSuchAlgorithmException ex) {
            JsfUtil.addErrorMessageExterna("Erro ao criptografar dados.");
            return "/login?faces-redirect=true";
        }
        try {
            Session sessao = HibernateFactory.currentSession();
            usuario = ebjUsario.login(sessao, getEmail(), senha);
        } catch (Exception ex) {
            JsfUtil.addErrorMessageExterna("Erro ao buscar dados.");
            return "/login?faces-redirect=true";
        } finally {
            HibernateFactory.closeSession();
        }
        if (usuario == null) {
            JsfUtil.addErrorMessageExterna("E-mail ou Senha inválidos.");
            return "/login?faces-redirect=true";
        } else if (!usuario.getAtivo()) {
            JsfUtil.addErrorMessageExterna("Funionário inativo.");
            return "/login?faces-redirect=true";
        } else {
            setEmail(null);
            setPassword(null);
            usuarioSession = usuario;
            return "/index?faces-redirect=true";
        }
    }

    public String logout() {
        usuarioSession = null;
        return "/login?faces-redirect=true";
    }

    public void alterarSenha() {
        if (usuarioSession != null) {
            FuncionarioFacade ebjUsario = new FuncionarioFacade();
            try {
                Session sessao = HibernateFactory.currentSession();
                usuarioSession = ebjUsario.obterPorId(sessao, usuarioSession.getId());
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, "Erro ao buscar dados.");
            } finally {
                HibernateFactory.closeSession();
            }
            String senhaAntiga;
            String senhaNova;
            try {
                senhaAntiga = CriptografiaUtil.encrypt(password);
                senhaNova = CriptografiaUtil.encrypt(newpassword);
            } catch (NoSuchAlgorithmException ex) {
                JsfUtil.addErrorMessage(ex, "Erro ao criptografar dados.");
                return;
            }
            if (!senhaAntiga.equals(usuarioSession.getSenha())) {
                JsfUtil.addErrorMessageExterna("Senha atual não confere.");
                return;
            }
            if (senhaAntiga.equals(senhaNova)) {
                JsfUtil.addErrorMessageExterna("A nova senha deve ser diferente da senha anterior.");
                return;
            }
            try {
                usuarioSession.setSenha(senhaNova);
                try {
                    Session sessao = HibernateFactory.currentSession();
                    ebjUsario.alterar(sessao, usuarioSession);
                } finally {
                    HibernateFactory.closeSession();
                }
                setPassword(null);
                setNewpassword(newpassword);
                JsfUtil.addSuccessMessage("Senha alterada com sucesso!");
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, "Não foi possivel alterar a senha. Tente novamente.");
            }
        } else {
            JsfUtil.addErrorMessage("Sessão encerrada.");
        }
    }

    public void enviarSenha() {
        try {
            if (emailEnvio == null || emailEnvio.isEmpty()) {
                JsfUtil.addErrorMessageExterna("Informe o e-mail de cadastro.");
            } else {
                Session sessao = HibernateFactory.currentSession();
                FuncionarioFacade ebjUsario = new FuncionarioFacade();
                ebjUsario.recuperarSenha(sessao, emailEnvio);
                setEmailEnvio(null);
                JsfUtil.addSuccessMessage("A nova senha foi enviada para o e-mail informado.");
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Erro ao recuperar senha. " + ex.getMessage());
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void cancelarAlterarSenha() {
        setPassword(null);
        setNewpassword(newpassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Funcionario getUsuarioSession() {
        return usuarioSession;
    }

    public boolean usuarioLogadoIsGerente() {
        return "Gerente".equals(getUsuarioSession().getPerfilAcesso().getDescricao());
    }

    public boolean verificarPermissao(String pagina) {
        boolean acesso = false;
        if (getUsuarioSession() != null) {
            if (usuarioLogadoIsGerente()) {
                acesso = true;
            } /*else if ("Administrativo".equals(getUsuarioSession().getPerfilAcesso().getDescricao())) {
             if (pagina.contains("usuario.xhtml")
             || pagina.contains("alterarsenha.xhtml")) {
             acesso = false;
             } else {
             acesso = true;
             }
             } else if ("Operacional".equals(getUsuarioSession().getPerfilAcesso().getDescricao())) {
            
             }*/
        }
        return acesso;
    }

    public boolean verificarPermissao(String perfil, String pagina) {
        boolean acesso = false;
        if (getUsuarioSession() != null) {
            if (usuarioLogadoIsGerente()) {
                acesso = true;
            } /*else if ("Administrativo".equals(getUsuarioSession().getPerfilAcesso().getDescricao())) {
             if (pagina.contains("usuario.xhtml")
             || pagina.contains("alterarsenha.xhtml")) {
             acesso = false;
             } else {
             acesso = true;
             }
             } else if ("Operacional".equals(getUsuarioSession().getPerfilAcesso().getDescricao())) {
            
             }*/
        }
        return acesso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getEmailEnvio() {
        return emailEnvio;
    }

    public void setEmailEnvio(String emailEnvio) {
        this.emailEnvio = emailEnvio;
    }
}
