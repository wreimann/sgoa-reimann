package controller;

import facede.FuncionarioFacade;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Funcionario;
import org.hibernate.Session;
import util.CriptografiaUtil;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private String email;
    private String password;
    private HttpSession session = null;
    private Funcionario usuarioSession = null;

    public LoginController() {
        setEmail(null);
        setPassword(null);

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
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            getSession().setAttribute("usuario", (Integer) usuario.getId());
            getSession().setAttribute("perfil", String.valueOf(usuario.getPerfilAcesso()));
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginController", this);
            return "/index?faces-redirect=true";
        }
    }

    public String logout() {
        if (getSession() != null) {
            getSession().removeAttribute("usuario");
            getSession().removeAttribute("perfil");
            getSession().invalidate();
        }
        return "/login?faces-redirect=true";
    }

    public void alterarSenha() {
        if (getSession() != null) {
            Integer idUser = (Integer) getSession().getAttribute("usuario");
            if (idUser != null && idUser > 0) {
                FuncionarioFacade ebjUsario = new FuncionarioFacade();
                Funcionario usuario = null;
                try {
                    Session sessao = HibernateFactory.currentSession();
                    usuario = ebjUsario.obterPorId(sessao, idUser);
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex, "Erro ao buscar dados.");
                } finally {
                    HibernateFactory.closeSession();
                }
                String senha = null;
                try {
                    senha = CriptografiaUtil.encrypt(password);
                } catch (NoSuchAlgorithmException ex) {
                    JsfUtil.addErrorMessage(ex, "Erro ao criptografar dados.");
                    return;
                }
                if (!senha.equals(usuario.getSenha())) {
                    JsfUtil.addErrorMessageExterna("Senha atual não confere.");
                    return;
                }
                try {
                    senha = CriptografiaUtil.encrypt(getPassword());
                    usuario.setSenha(senha);
                    try {
                        Session sessao = HibernateFactory.currentSession();
                        ebjUsario.alterar(sessao, usuario);
                    } finally {
                        HibernateFactory.closeSession();
                    }
                    JsfUtil.addSuccessMessage("Senha alterada com sucesso!");
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex, "Não foi possivel alterar a senha. Tente novamente.");
                }

            } else {
                JsfUtil.addErrorMessage("Usuário não localizado.");
            }
        } else {
            JsfUtil.addErrorMessage("Sessão encerrada.");
        }
    }

    public void cancelarAlterarSenha() {
        setPassword(null);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Funcionario getUsuarioSession() {
        if (getSession() != null) {
            Integer idUser = (Integer) getSession().getAttribute("usuario");
            if (idUser != null && idUser > 0) {
                if (usuarioSession == null) {
                    try {
                        Session sessao = HibernateFactory.currentSession();
                        FuncionarioFacade ebjUsario = new FuncionarioFacade();
                        usuarioSession = ebjUsario.obterPorId(sessao, idUser);
                    } catch (Exception ex) {
                        JsfUtil.addErrorMessage(ex, "Erro ao buscar dados do usuário.");
                    } finally {
                        HibernateFactory.closeSession();
                    }
                }
            } else {
                usuarioSession = null;
            }
        }
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

    public HttpSession getSession() {
        return session;
    }
}
