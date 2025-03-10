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
import mx.com.ferbo.business.empleado.RegistroAsistenciaBL;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetToken;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.response.RegistryResponse;
import mx.com.ferbo.util.SGPException;

/**
 * Servlet implementation class RegistryServlet
 */
public class RegistryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(RegistryServlet.class);
        private RegistroAsistenciaBL empleadoAsistencia = null;
       
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistryServlet() {
            empleadoAsistencia = new RegistroAsistenciaBL();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
                int httpStatus = -1;
                String jsonResponse = null;
                RegistryResponse respuesta = null;
		Gson prettyGson = null;
		Date fechaActual = null;
                
                String numeroEmpleado = null;
                String token = null;
                String accion = null;
                
                HttpSession session = null;
                DetToken tokenEmpleado = null;
                DetEmpleado empleado = null;
                DetEmpleadoFoto foto = null;
                
                try {
                        session = request.getSession(true);
                        
                        numeroEmpleado = request.getParameter("numero");
                        token = request.getParameter("token");
                        accion = request.getParameter("accion");// registro, Entrada/Salida;
                        
                        fechaActual = new Date();
                        log.info("Fecha hora actual: {}", fechaActual);
                        
                        tokenEmpleado = this.empleadoAsistencia.obtenerToken(numeroEmpleado, token, fechaActual);
                        empleado = tokenEmpleado.getEmpleado();
                        
                        prettyGson = new GsonBuilder().setPrettyPrinting().create();
                        
                        tokenEmpleado.setValido(false);
                        foto = this.empleadoAsistencia.buscarFotoEmpleado(numeroEmpleado);
                        this.empleadoAsistencia.actualizarToken(tokenEmpleado);
                        
                        session.setAttribute("empleado", empleado);
                        session.setAttribute("fotografia", foto);
			
                        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
                        
                        if(empleadoEmpresa.getFechaBaja() != null && empleado.getActivo() == 0)
                        {
                            log.warn("Empleado {} dado de baja del sistema", empleado.getIdEmpleado());
                            throw new Exception("Acceso Denegado");
                        }
                        
			respuesta = new RegistryResponse();
			respuesta.setCodigo(0);
			respuesta.setMensaje(null);
			if ("registro".equalsIgnoreCase(accion)) {
				// opcion 1: replicar metodo login debajo, opcion 2: hacer instancia de
				// loginBean y llamar a su metodo login si es posible
				respuesta.setUrl("/protected/registroAsistencia.xhtml");
				this.empleadoAsistencia.registroAsistenciaEmpleado(empleado, fechaActual);
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
                        if(ex.getMessage().contains("Acceso Denegado"))
                        {
                            respuesta.setCodigo(2);
                            respuesta.setMensaje("Acceso Denegado");
                            respuesta.setUrl(null);
                        }else{
                            respuesta.setCodigo(1);
                            respuesta.setMensaje("Hubo un error en el proceso de registro.");
                            respuesta.setUrl(null);
                        }
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
