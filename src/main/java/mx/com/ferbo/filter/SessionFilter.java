package mx.com.ferbo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dto.DetEmpleadoDTO;

/**
 *
 * @author erale
 */
public class SessionFilter implements Filter {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(SessionFilter.class);

    private FilterConfig filterConfig = null;
    private List<String> exceptionPages = null;
    private String loginPage = null;
    private boolean enabled;

    private static final String INIT_PARAM_LOGIN_PAGE = "login-page";
    private static final String INIT_PARAM_EXCEPTION_PAGES = "exception-pages";
    private static final String INIT_PARAM_ENABLED = "enabled";
    private static final String DEFAULT_LOGIN_PAGE = "/login.jsp";
    private static final String NO_SESSION_PAGE = "/login.jsp";

    /**
     * Default constructor.
     */
    public SessionFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doAction((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doAction(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        String context = null;
        String pagina = null;

        DetEmpleadoDTO usuario = null;

        context = request.getContextPath();
        pagina = request.getServletPath();

        log.debug("Verificando excepcion para la pagina: " + context + pagina);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        if (exceptionPage(pagina)) {

            log.debug("La pagina no requiere autenticacion: " + context + pagina);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setHeader("Expires", "0"); // Proxies.
            chain.doFilter(request, response);
            return;
        }

        log.debug("La pagina si requiere autenticacion: " + context + pagina);

        if (session == null) {
            log.warn("La sesion no existe. Se requiere autenticar.");
            log.warn("IP: " + request.getRemoteAddr());
            log.warn("URL solicitada: " + context + pagina);
            response.sendRedirect(context + NO_SESSION_PAGE);
            return;
        }

        log.debug("Id de sesion: " + session.getId());
        log.debug("Verificando existencia datos de inicio de sesion...");
        usuario = (DetEmpleadoDTO) session.getAttribute("empleado");

        log.debug("Verificando existencia de usuario en sesion...");

        if (usuario == null) {

            log.warn("Los datos de usuario en sesion no existen. Se requiere autenticar.");
            log.warn("IP: " + request.getRemoteAddr());
            log.warn("URL solicitada: " + context + pagina);
            response.sendRedirect(context + NO_SESSION_PAGE);
            return;
        }

        log.debug("El usuario esta correctamente autenticado.");
        chain.doFilter(request, response);

    }

    private void initFilter() {

        String strEnabled = filterConfig.getInitParameter(INIT_PARAM_ENABLED);
        Boolean isEnabled = Boolean.valueOf(strEnabled);
        enabled = isEnabled.booleanValue();

        if (enabled) {
            log.info("La verificacion de excepcion de paginas se encuentra habilitada.");
        } else {
            log.info("La verificacion de excepcion de paginas se encuentra deshabilitada.");
        }
        loginPage = filterConfig.getInitParameter(INIT_PARAM_LOGIN_PAGE);

        if (loginPage == null) {
            loginPage = DEFAULT_LOGIN_PAGE;
        }

        log.info("Parametro de inicializacion: + " + INIT_PARAM_LOGIN_PAGE + " = " + loginPage);
        log.info("Comienza la inicializacion de excepcion de paginas");
        exceptionPages = new ArrayList<String>();

        String excepInitParam = filterConfig.getInitParameter(INIT_PARAM_EXCEPTION_PAGES);

        if (excepInitParam != null) {
            for (StringTokenizer strToken = new StringTokenizer(excepInitParam); strToken.hasMoreTokens(); exceptionPages.add(strToken.nextToken().trim()));
            if (!exceptionPages.isEmpty()) {
                log.info("Parametro de inicializacion " + INIT_PARAM_EXCEPTION_PAGES + " inicializado");
            }
        }

        if (log.isDebugEnabled()) {
            for (Iterator<String> iter = exceptionPages.iterator(); iter.hasNext(); log.debug("Parametro - " + INIT_PARAM_EXCEPTION_PAGES + "=" + iter.next()));
        }

        log.info("Termina la inicializacion de excepcion de paginas");
    }

    private boolean exceptionPage(String path) {
        String page, str = path.toLowerCase();
        Iterator<String> iter = exceptionPages.iterator();
        while (iter.hasNext()) {
            page = (String) iter.next();
            if (log.isTraceEnabled()) {
                log.trace("Comparando [" + path + "] vs [" + page.toLowerCase() + "] == " + str.endsWith(page.toLowerCase()));
            }
            if (str.endsWith(page.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        this.filterConfig = fConfig;
        this.initFilter();
    }

}
