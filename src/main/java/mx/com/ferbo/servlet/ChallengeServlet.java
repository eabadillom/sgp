package mx.com.ferbo.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import com.digitalpersona.uareu.Fmd;
// import com.digitalpersona.uareu.UareUException;
// import com.digitalpersona.uareu.UareUGlobal;
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
import mx.com.ferbo.request.RespuestaSistema;
import mx.com.ferbo.response.JsonRespuesta;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.SecurityUtil;

public class ChallengeServlet extends HttpServlet{
	private static Logger log = LogManager.getLogger(ChallengeServlet.class);

	private static final long serialVersionUID = 1L;
	Date fechaCaducidad;
	
// public Fmd decodificar(String huella){
		
// 		Fmd fmd = null;
// 		byte[] byteHuella = null;
// 		log.info("entre al metodo decodificador");
		
// 		//Creamos el fmd en base a los bytes del string
		
// 		try {

// 			log.info("descodificare {}" + huella);
// 			byteHuella = Base64.getDecoder().decode(new String(huella).getBytes("UTF-8"));
// 			log.info("descodificada la huellla");			
// 			fmd = UareUGlobal.GetImporter().ImportFmd(byteHuella, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);

// 		} catch (UareUException e) {			
// 			e.printStackTrace();
// 			log.info("error al convertir en fmd");
// 		} catch (UnsupportedEncodingException e) {
// 			log.info("Error al convertir en base 64");			
// 			e.printStackTrace();
// 		}
		

// 		log.info("converti los bytes de huella a fmd ");
// 		log.info("Fmd convertido {}"+ fmd);				
		
// 		return fmd;
// 	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		response.getOutputStream().print("Hola Mundo");
		log.info("entrado al challenge servlet");
	/*	DetBiometricoDAO biometricoDao = null;
		DetBiometricoDTO biometricos = null;
		String numEmpleado = null;
		String json = null;
		String token = "sdf234SD###%&##$2342asFFWqQ";
		Fmd biometricoFmd = null;
		Fmd[] arreglo = null;
		int httpStatus = -1;
		String msgError = null;
		String jsonResponse = null;
		
		try {
			numEmpleado = request.getParameter(numEmpleado);
			biometricoDao = new DetBiometricoDAO();
			biometricos = biometricoDao.consultaBiometricoByNumEmpleado(numEmpleado);
			json = String.format("{\"f1\" : \"%s\", \"f2\" : \"%s\" \"token\" : \"%s\"}", biometricos.getHuella(), biometricos.getHuella2(),token);
			biometricoFmd = decodificar(biometricos.getHuella());
			arreglo[0] = biometricoFmd;
			
			ChallengeServlet challenge = new ChallengeServlet();
			httpStatus = HttpServletResponse.SC_OK;
			
		} catch (Exception e) {
			log.error("Problema para obtener el número de empleado...", e);
			httpStatus = HttpServletResponse.SC_BAD_REQUEST;
			msgError = "Problema para obtener la información solicitada.";
			jsonResponse = String.format("{\"codigo\" : %d, \"mensaje\" : \"%s\"}", 1, msgError);
		}*/
		
		
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
		RespuestaSistema respuesta = null;
		int httpStatus = -1;
		Gson prettyGson = null;
		String jsonResponse =null;
		JsonRespuesta jsonR = new JsonRespuesta();
		try {
			
			empleadoDAO = new EmpleadoDAO();
			su = new SecurityUtil();
			biometricoDAO = new DetBiometricoDAO();
			detFpClientDAO = new DetFpClientDAO();
			
			numeroEmpleado = request.getParameter("numeroEmpleado");
			password = request.getParameter("password");
			idFpClient = request.getParameter("idFpClient");
			
			fpClient = detFpClientDAO.buscarPorId(Integer.parseInt(idFpClient));
			
			//Recuperamos huellas del empleado
			if(!("".equals(numeroEmpleado))) {
				biometricos = biometricoDAO.consultaBiometricoByNumEmpleado(numeroEmpleado);
				log.info(biometricos.getHuella() + biometricos.getHuella2());
			}
			
			if(fpClient != null && biometricos != null) {
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
				 jsonR.setToken(token);
		            jsonR.setNumEmpleado(Integer.parseInt(numeroEmpleado));
		            jsonR.setHuella(biometricos.getHuella());
		            jsonR.setHuella2(biometricos.getHuella2());
		            
				jsonResponse = prettyGson.toJson(jsonR);
				log.info("Json.........", jsonResponse);
			}else {
				response.getOutputStream().print("El servlet no permitio la entrada al idFpClient");
			}
				
		} catch(SGPException ex) {
            log.error("Problema con el challenge...", ex);
            /*Prepara respuesta de error personalizado.*/
            mensaje = ex.getMessage();
            respuesta = new RespuestaSistema();
            respuesta.setCodigo(1);
            respuesta.setMensaje(mensaje);

            /*Prepara entrega respuesta de error personalizado.*/
            httpStatus = HttpServletResponse.SC_BAD_REQUEST;
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
            jsonResponse = prettyGson.toJson(respuesta);
        } catch(Exception ex) {
            log.error("Problema al intentar ingresar al challenge...", ex);
            /*Prepara respuesta de error del sistema.*/
            mensaje = "Se ha presentado un problema con el challenge.\n"
                    + "Por favor contacte a su administrador de sistemas.";
            respuesta = new RespuestaSistema();
            respuesta.setCodigo(2);
            respuesta.setMensaje(mensaje);
            
            /*Prepara entrega respuesta de error del sistema.*/
            httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
            jsonResponse = prettyGson.toJson(respuesta);
            
        } finally {
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
