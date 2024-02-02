package mx.com.ferbo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.digitalpersona.uareu.Reader;
//import com.digitalpersona.uareu.ReaderCollection;
//import com.digitalpersona.uareu.UareUException;
//import com.digitalpersona.uareu.UareUGlobal;

import mx.com.ferbo.dao.DetBiometricoDAO;
import mx.com.ferbo.dto.DetBiometricoDTO;

/**
 * Servlet implementation class RegistryServlet
 */
public class RegistryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(RegistryServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistryServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String numeroEmpleado = null;
		DetBiometricoDAO biometricoDAO = null;
		DetBiometricoDTO biometrico = null;
		String jsonResponse = null;
		int httpStatus = -1;
		String msgError = null;
		try {
			numeroEmpleado = request.getParameter("numero");
			log.info("Buscando biometricos para el empleado numero: " + numeroEmpleado);
			biometricoDAO = new DetBiometricoDAO();
			biometrico = biometricoDAO.consultaBiometricoByNumEmpleado(numeroEmpleado);
			
			if(biometrico == null)
				throw new Exception("Problema para obtener el numero de empleado: " + numeroEmpleado);
			
			if(biometrico.getHuella() == null && biometrico.getHuella2() == null)
				throw new Exception("Problema para obtener el biometrico del numero de empleado: " + numeroEmpleado);
			
			jsonResponse = String.format("{\"f1\" : \"%s\", \"f2\" : \"%s\"}", biometrico.getHuella(), biometrico.getHuella2());
			log.info(biometrico);
			log.info("Respuesta: " + jsonResponse);
			httpStatus = HttpServletResponse.SC_OK;
		} catch(Exception ex) {
			log.error("Problema para obtener el número de empleado...", ex);
			httpStatus = HttpServletResponse.SC_BAD_REQUEST;
			msgError = "Problema para obtener la información solicitada.";
			jsonResponse = String.format("{\"codigo\" : %d, \"mensaje\" : \"%s\"}", 1, msgError);
		} finally {
			response.setStatus(httpStatus);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResponse);
            response.getWriter().flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String sVerified = null;
//		ReaderCollection m_Collection = null;
//		try {
//			m_Collection = UareUGlobal.GetReaderCollection();
//
//
//		} catch(UareUException ex){
//
//
//		} catch(Exception ex) {
//			
//		} finally {
//
//			try {
//				UareUGlobal.DestroyReaderCollection();
//			} catch(Exception inEx) {
//				log.error("Problema para liberar el lector de huella...", inEx);
//			}
//			
//		}
	}

}
