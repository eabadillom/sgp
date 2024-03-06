package mx.com.ferbo.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.com.ferbo.dao.DetBiometricoDAO;
import mx.com.ferbo.dao.DetFpClientDAO;
import mx.com.ferbo.dao.DetTokenDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.DetBiometricoDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetFpClientDTO;
import mx.com.ferbo.dto.DetTokenDTO;
import mx.com.ferbo.response.ChallengeResponse;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.SecurityUtil;

public class ChallengeServlet extends HttpServlet{
	private static Logger log = LogManager.getLogger(ChallengeServlet.class);

	private static final long serialVersionUID = 1L;
	Date fechaCaducidad;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		response.getOutputStream().print("Hola Mundo");
		log.info("entrado al challenge servlet");
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DetBiometricoDAO biometricoDAO = null;
		DetBiometricoDTO biometricos = null;
		DetTokenDAO tokenDAO = null;
		DetTokenDTO tokenDTO = null;
		DetEmpleadoDTO empleadoSelect = null;
		EmpleadoDAO empleadoDAO = null;
		DetFpClientDAO detFpClientDAO = null;
		DetFpClientDTO fpClient = null;
		
		String numeroEmpleado = "";
		final String password;
		final String idFpClient;
		String token = "";
		SecurityUtil su = null;
		Date fechaTemp = new Date();
		
		String mensaje = null;
		int httpStatus = -1;
		Gson prettyGson = null;
		String jsonResponse =null;
		ChallengeResponse challengeObj = new ChallengeResponse();
		
		try {
			empleadoDAO = new EmpleadoDAO();
			su = new SecurityUtil();
			biometricoDAO = new DetBiometricoDAO();
			detFpClientDAO = new DetFpClientDAO();
			
			numeroEmpleado = request.getParameter("numeroEmpleado");
			if("".equals(numeroEmpleado))
				throw new SGPException("No se proporcionó el número de empleado.");
			
			password = request.getParameter("password");
			idFpClient = request.getParameter("idFpClient");
			
			fpClient = detFpClientDAO.buscarPorId(Integer.parseInt(idFpClient));
			
			if(fpClient == null) {
				log.warn("El id del lector es incorrecto.");
				throw new SGPException("El lector de huella no es válido. Por favor contacte a su administrador de sistemas.");
			}
			
			if(fpClient.getNbPassword().equals(password) == false) {
				log.warn("La contraseña del lector es incorrecta.");
				throw new SGPException("El lector de huella no es válido. Por favor, contacte a su administrador de sistemas.");
			}

			//Buscamos al empleado y recuperamos sus biometricos del enrolamiento.
			biometricos = biometricoDAO.consultaBiometricoByNumEmpleado(numeroEmpleado);
			
			if(biometricos == null) {
				throw new SGPException("No hay información del empleado solicitado.");
			}
			log.info(biometricos.getHuella() + biometricos.getHuella2());
			//Generamos su token, falta validar ?
			token = su.getRandomString();			
			fechaTemp = DateUtil.addMinute(fechaTemp, 5);
			log.info(fechaTemp);				
			
			tokenDAO = new DetTokenDAO();
			tokenDTO = tokenDAO.buscarPorNumEmpleado(numeroEmpleado);
			log.info(tokenDTO);
			
			empleadoSelect = empleadoDAO.buscarPorNumEmpl(numeroEmpleado);
			tokenDTO.setDetEmpleadoDTO(empleadoSelect);
			tokenDTO.setNbToken(token);
			tokenDTO.setCaducidad(fechaTemp);
			tokenDTO.setValido(true);
			
			tokenDAO.guardar(tokenDTO);
			
			httpStatus= HttpServletResponse.SC_CREATED;
			prettyGson = new GsonBuilder().setPrettyPrinting().create();
			challengeObj.setToken(token);
			challengeObj.setNumEmpleado(Integer.parseInt(numeroEmpleado));
			challengeObj.setHuella(biometricos.getHuella());
			challengeObj.setHuella2(biometricos.getHuella2());
	            
			jsonResponse = prettyGson.toJson(challengeObj);
		} catch(SGPException ex) {
            log.error("Problema con el challenge...", ex);
            /*Prepara respuesta de error personalizado.*/
            mensaje = ex.getMessage();
            challengeObj = new ChallengeResponse();
            challengeObj.setCodigoError(1);
            challengeObj.setMensajeError(mensaje);

            /*Prepara entrega respuesta de error personalizado.*/
            httpStatus = HttpServletResponse.SC_FORBIDDEN;
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
            jsonResponse = prettyGson.toJson(challengeObj);
        } catch(Exception ex) {
            log.error("Problema al intentar ingresar al challenge...", ex);
            /*Prepara respuesta de error del sistema.*/
            mensaje = "Se ha presentado un problema con el challenge.\n"
                    + "Por favor contacte a su administrador de sistemas.";
            challengeObj = new ChallengeResponse();
            challengeObj.setCodigoError(2);
            challengeObj.setMensajeError(mensaje);
            
            /*Prepara entrega respuesta de error del sistema.*/
            httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
            jsonResponse = prettyGson.toJson(challengeObj);
            
        } finally {
        	log.info("Respuesta al FP-CLIENT: {}", jsonResponse);
			response.setStatus(httpStatus);
			response.setContentType("aplication/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(jsonResponse);
			response.getWriter().flush();
		}
		
	}
	

	public ChallengeServlet() {
		super();
		fechaCaducidad = new Date();		
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ChallengeServlet.log = log;
	}

}
