package filter;

import controller.LoginController;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*.xhtml"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestPath = req.getRequestURI();
        if (!requestPath.contains("login.xhtml") && !requestPath.contains("erroPadrao.xhtml")
                && !requestPath.contains("acompanharservico.xhtml")) {
            LoginController loginController = (LoginController) req.getSession().getAttribute("loginController");
            if (loginController != null && loginController.getUsuarioSession() != null) {
                boolean validate = loginController.verificarPermissao(requestPath);
                if (!validate) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.sendRedirect(req.getContextPath() + "/faces/erroPadrao.xhtml");
                }
            } else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
