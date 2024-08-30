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
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;
import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dto.DetTokenDTO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetRegistro;
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
		EmpleadoFotoDAO empleadoFotoDAO = null;
		EstatusRegistroDAO estatusDAO = null;
		DetTokenDAO detTokenDAO = null;
		RegistroDAO registroDAO = null;
		
		CatEstatusRegistro statusEnTiempo = null;
		CatEstatusRegistro statusRetardo = null;
		DetEmpleado empleado = null;
		DetEmpleadoFoto foto = null;
		DetTokenDTO tokenEmpleado = null;
		DetRegistro registro = null;
		String tipoRegistro = null;
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
		try {
			session = request.getSession(true);
			
			horaSistema = new Date();
			fechaEntradaInicio = new Date();
			fechaEntradaFin = new Date();
			DateUtil.setTime(fechaEntradaInicio, 0, 0, 0, 0);
			DateUtil.setTime(fechaEntradaFin, 23, 59, 59, 999);
			
			registro = new DetRegistro();
			registroDAO = new RegistroDAO(DetRegistro.class);
			numeroEmpleado = request.getParameter("numero");
			token = request.getParameter("token");
			accion = request.getParameter("accion");// registro, Entrada/Salida
			fechaActual = new Date();
			log.info("Buscando biometricos para el empleado numero: " + numeroEmpleado);
			
			if (numeroEmpleado == null) {
				throw new Exception("Información incorrecta.");
			}
			
			if ("".equalsIgnoreCase(numeroEmpleado.trim()))
				throw new Exception("Información incorrecta.");

			empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
			
			empleado = empleadoDAO.buscarPorNumeroEmpleado(numeroEmpleado, true);
			
			if(empleado == null || empleado.getIdEmpleado() == null)
				throw new SGPException("El empleado indicado es incorrecto.");
			
			estatusDAO = new EstatusRegistroDAO(CatEstatusRegistro.class);
			statusEnTiempo = estatusDAO.buscarPorId(1);
			statusRetardo = estatusDAO.buscarPorId(2);

			detTokenDAO = new DetTokenDAO();
			log.info("Buscando token........: " + token);
			tokenEmpleado = detTokenDAO.buscarPorToken(token);
			log.info("Token: {}, Caducidad: {}", tokenEmpleado.getNbToken(), tokenEmpleado.getCaducidad());
			log.info("Fecha hora actual: {}", fechaActual);
			
			if (tokenEmpleado.getCaducidad().before(fechaActual)) {
				log.info("La fecha recuperada es valida: {}", tokenEmpleado.getCaducidad());
				throw new SGPException("El token ha expirado.");
			}
			
			if(tokenEmpleado.isValido() == false) {
				log.info("El token no es válido: {}", tokenEmpleado.isValido());
				throw new SGPException("El token no es válido.");
			}
			
			prettyGson = new GsonBuilder().setPrettyPrinting().create();
			
			tokenEmpleado.setValido(false);
			empleadoFotoDAO = new EmpleadoFotoDAO(DetEmpleadoFoto.class);
			foto = empleadoFotoDAO.buscar(empleado.getNumEmpleado());
			detTokenDAO.actualizar(tokenEmpleado);
			session.setAttribute("empleado", empleado);
			session.setAttribute("fotografia", foto);
			
			respuesta = new RegistryResponse();
			respuesta.setCodigo(0);
			respuesta.setMensaje(null);
			if ("registro".equalsIgnoreCase(accion)) {
				// opcion 1: replicar metodo login debajo, opcion 2: hacer instancia de
				// loginBean y llamar a su metodo login si es posible
				respuesta.setUrl("/protected/registroAsistencia.xhtml");
				
				log.info("Buscando entrada del empleado {} entre las {} y las {}", 
						empleado.getNumEmpleado(),
						DateUtil.getString(fechaEntradaInicio, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS),
						DateUtil.getString(fechaEntradaFin, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS));
				registro = registroDAO.buscarPorEmpleadoFechaEntrada(empleado.getIdEmpleado(), fechaEntradaInicio, fechaEntradaFin);
				if(registro == null || registro.getIdRegistro() == null)
					tipoRegistro = "Entrada";
				else
					tipoRegistro = "Salida";
				
				horaLimiteEntrada = new Date();
				DateUtil.setTime(horaLimiteEntrada, 7, 10, 0, 0);
				
				switch (tipoRegistro) {
				
				case "Entrada":
				case "ENTRADA":
				case "entrada":
					log.info("Registrando entrada...");
					registro = new DetRegistro();
					registro.setFechaEntrada(fechaActual);
					registro.setFechaSalida(null);
					registro.setIdEmpleado(empleado);
					if (horaSistema.after(horaLimiteEntrada)) {
						registro.setIdEstatus(statusRetardo);
					} else {
						registro.setIdEstatus(statusEnTiempo);
					}
					registroDAO.guardar(registro);
					log.info("Entrada registrada correctamente");
					break;
				case "Salida":
				case "SALIDA":
				case "salida":
					log.info("Registrando salida...");
					registro.setFechaSalida(fechaActual);
					registroDAO.actualizar(registro);
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
