package filter;

import facede.FuncionarioFacade;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Funcionario;
import org.hibernate.Session;
import util.HibernateFactory;
import util.JsfUtil;

@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*.xhtml"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = (HttpSession) req.getSession();
        String requestPath = req.getRequestURI();
        /*if (!requestPath.contains("login.xhtml") && !requestPath.contains("erroPadrao.xhtml")) {
        boolean validate = false;
        Integer user = (Integer) session.getAttribute("usuario");
        if (user != null) {
        String perfil = (String)session.getAttribute("perfil");
        LoginController controller = new LoginController();
        validate = controller.verificarPermissao(perfil, requestPath);
        }
        if (!validate) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (user != null) {
        httpServletResponse.sendRedirect(req.getContextPath() + "/faces/erroPadrao.xhtml");
        } else {
        httpServletResponse.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
        }
        }
        }*/
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    public static Funcionario usuarioLogado(Session sessao) throws Exception {
        Funcionario funcLogado = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Integer idUser = 1;//(Integer) session.getAttribute("usuario");
        if (idUser != null && idUser > 0) {
            FuncionarioFacade ebjFuncionario = new FuncionarioFacade();
            funcLogado = ebjFuncionario.obterPorId(sessao, idUser);
        } else {
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            try {
                httpServletResponse.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
            } catch (IOException ex) {
                JsfUtil.addErrorMessage(ex, "Erro ao buscar o usuário logado.");
            }
        }
        return funcLogado;
    }
}
