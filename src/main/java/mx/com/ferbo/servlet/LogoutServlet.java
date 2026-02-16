package mx.com.ferbo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3299448375722311361L;
	
	private static Logger log = LogManager.getLogger(LogoutServlet.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		log.info("Cerrando la sesión del usuario...");
		HttpSession session = request.getSession(false);
		log.info(request.getContextPath());
		
		try {
			if(session != null)
				session.invalidate();
			
		} catch(IllegalStateException ex) {
			log.error("Problema con el cierre de la sesión...", ex);
		}
		
		response.sendRedirect(request.getContextPath());
		log.info("Sesión terminada.");
	}

}
