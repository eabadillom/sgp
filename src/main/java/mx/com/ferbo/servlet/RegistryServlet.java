package mx.com.ferbo.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.com.ferbo.dao.DetTokenDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.CatEstatusRegistroDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.dto.DetTokenDTO;
import mx.com.ferbo.response.RegistryResponse;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

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

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = null;
		Date horaSistema;
		Date horaLimiteEntrada = null;

		EmpleadoDAO empleadoDAO = null;
		DetEmpleadoDTO detEmpleadoDTO = null;
		DetEmpleadoDTO empleadoConFoto = null;
		DetTokenDAO detTokenDAO = null;
		DetTokenDTO detTokenDTO = null;
		DetRegistroDTO registroDTO = null;
		String registro = null;
		RegistroDAO registroDAO = null;
		Date fechaActual = null;
		Date fechaEntradaInicio = null;
		Date fechaEntradaFin = null;
		String numeroEmpleado = null;
		String jsonResponse = null;
		int httpStatus = -1;
		
		String token = null;
		String accion = null;
		RegistryResponse respuesta = null;
		Gson prettyGson = null;
		CatEstatusRegistroDTO status = null;
		try {
			session = request.getSession(true);
			
			horaSistema = new Date();
			fechaEntradaInicio = new Date();
			fechaEntradaFin = new Date();
			DateUtil.setTime(fechaEntradaInicio, 0, 0, 0, 0);
			DateUtil.setTime(fechaEntradaFin, 23, 59, 59, 999);
			
			status = new CatEstatusRegistroDTO();
			registroDTO = new DetRegistroDTO();
			registroDAO = new RegistroDAO();
			numeroEmpleado = request.getParameter("numero");
			token = request.getParameter("token");
			accion = request.getParameter("accion");// registro, Entrada/Salida
			fechaActual = new Date();
			log.info("Buscando biometricos para el empleado numero: " + numeroEmpleado);
			log.info("Buscando token........: " + token);
			
			if (numeroEmpleado == null) {
				throw new Exception("El Numero de Empleado esta vacio");
			}

			empleadoDAO = new EmpleadoDAO();
			detEmpleadoDTO = empleadoDAO.buscarPorNumeroEmpleado(numeroEmpleado, true);
			empleadoConFoto = empleadoDAO.buscarConFoto(detEmpleadoDTO.getIdEmpleado());
			
			if(detEmpleadoDTO == null || detEmpleadoDTO.getIdEmpleado() == null)
				throw new SGPException("El empleado indicado es incorrecto.");

			detTokenDAO = new DetTokenDAO();
			detTokenDTO = detTokenDAO.buscarPorIdEmpleadoAndFecha(detEmpleadoDTO.getIdEmpleado());
			log.info("Token: {}, Caducidad: {}", detTokenDTO.getNbToken(), detTokenDTO.getCaducidad());
			log.info("Fecha hora actual: {}", fechaActual);
			
			if (detTokenDTO.getCaducidad().before(fechaActual)) {
				log.info("La fecha recuperada es valida: {}", detTokenDTO.getCaducidad());
				throw new SGPException("El token ha expirado.");
			}
			
			if(detTokenDTO.isValido() == false) {
				log.info("El token no es válido: {}", detTokenDTO.isValido());
				throw new SGPException("El token no es válido.");
			}
			
			prettyGson = new GsonBuilder().setPrettyPrinting().create();
			
			detTokenDTO.setValido(false);
			detTokenDAO.actualizar(detTokenDTO);
			session.setAttribute("empleado", detEmpleadoDTO);
			session.setAttribute("fotografia", empleadoConFoto.getEmpleadoFoto());
			
			respuesta = new RegistryResponse();
			respuesta.setCodigo(0);
			respuesta.setMensaje(null);
			if ("registro".equalsIgnoreCase(accion)) {
				// opcion 1: replicar metodo login debajo, opcion 2: hacer instancia de
				// loginBean y llamar a su metodo login si es posible
				respuesta.setUrl("/protected/registroAsistencia.xhtml");
				
				log.info("Buscando entrada del empleado {} entre las {} y las {}", 
						detEmpleadoDTO.getNumEmpleado(),
						DateUtil.getString(fechaEntradaInicio, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS),
						DateUtil.getString(fechaEntradaFin, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS));
				registroDTO = registroDAO.buscarPorEmpleadoFechaEntrada(detEmpleadoDTO.getIdEmpleado(), fechaEntradaInicio, fechaEntradaFin);
				if(registroDTO == null || registroDTO.getIdRegistro() == null)
					registro = "Entrada";
				else
					registro = "Salida";
				
				horaLimiteEntrada = new Date();
				DateUtil.setTime(horaLimiteEntrada, 7, 10, 0, 0);
				
				switch (registro) {
				
				case "Entrada":
				case "ENTRADA":
				case "entrada":
					log.info("Registrando entrada...");
					registroDTO = new DetRegistroDTO();
					registroDTO.setFechaEntrada(fechaActual);
					registroDTO.setFechaSalida(null);
					registroDTO.setDetEmpleadoDTO(detEmpleadoDTO);
					if (horaSistema.after(horaLimiteEntrada)) {
						status.setIdEstatus(2);
					} else {
						status.setIdEstatus(1);
					}
					registroDTO.setCatEstatusRegistroDTO(status);
					registroDAO.guardar(registroDTO);
					log.info("Entrada registrada correctamente");
					break;
				case "Salida":
				case "SALIDA":
				case "salida":
					log.info("Registrando salida...");
					registroDTO.setFechaSalida(fechaActual);
					registroDAO.actualizar(registroDTO);
					log.info("Salida registrada correctamente");
					break;
				}
			} else if ("perfil".equalsIgnoreCase(accion)) {
				log.info("Entrando a mi perfil...");
				respuesta.setUrl("/protected/kardexEmpleado.xhtml");
			}
			log.info("Registro completo.");
			jsonResponse = prettyGson.toJson(respuesta);
			httpStatus = HttpServletResponse.SC_OK;
		} catch (Exception ex) {
			log.warn("Problema para el registro del empleado: {}", ex.getMessage());
			respuesta = new RegistryResponse();
			respuesta.setCodigo(1);
			respuesta.setMensaje("Hubo un error en el proceso de registro.");
			respuesta.setUrl(null);
			log.error("Problema para obtener el número de empleado...", ex);
			httpStatus = HttpServletResponse.SC_BAD_REQUEST;
			prettyGson = new GsonBuilder().setPrettyPrinting().create();
			jsonResponse = prettyGson.toJson(respuesta);
		} finally {
			response.setStatus(httpStatus);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(jsonResponse);
			response.getWriter().flush();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
