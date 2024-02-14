package mx.com.ferbo.servlet;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.loader.plan.exec.process.spi.ReaderCollector;

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









//import com.digitalpersona.uareu.Reader;
//import com.digitalpersona.uareu.ReaderCollection;
//import com.digitalpersona.uareu.UareUException;
//import com.digitalpersona.uareu.UareUGlobal;

/**
 * Servlet implementation class RegistryServlet
 */
public class RegistryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(RegistryServlet.class);
	
    ReaderCollector m_Collection;
    Reader reader;
    boolean fingerM;
    private Calendar calendarHoy;
    private Calendar calendarDiaSistema;
    private final Date diaSistema;
    private Date diaActual;
    private final SimpleDateFormat sdFormat;
    private final SimpleDateFormat sdFormatHMS;
    private final String strFechaActual ;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistryServlet() {
    	diaActual = new Date();
        calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        calendarDiaSistema = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        sdFormatHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strFechaActual = sdFormat.format(diaActual) + "%";
        diaSistema = new Date();
        
    }

    public boolean isFinger_M() {
        return fingerM;
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		EmpleadoDAO empleadoDAO = null;
		DetEmpleadoDTO detEmpleadoDTO = null;
		DetTokenDAO detTokenDAO = null;
		DetTokenDTO detTokenDTO = null;
		DetRegistroDTO registroDTO = null;
		List<DetRegistroDTO> listadetRegistro = null;
		RegistroDAO registroDAO = null;
		Date fecha = null;
		String numeroEmpleado = null;
		String jsonResponse = null;
		int httpStatus = -1;
		String captura = null;
		String token = null;
		String accion = null;
		RegistryResponse respuesta = null;
		Gson prettyGson = null;
	   CatEstatusRegistroDTO status = null;
		try {
			status = new CatEstatusRegistroDTO();
			registroDTO = new DetRegistroDTO();
			listadetRegistro = new ArrayList<>();
			registroDAO = new RegistroDAO();
			numeroEmpleado = request.getParameter("numero");
			token = request.getParameter("token");
			accion = request.getParameter("accion");//registro, Entrada/Salida
			//accion2 = request.getParameter("perfil");// perfil Mi Perfil
			fecha = new Date();
			log.info("Buscando biometricos para el empleado numero: " + numeroEmpleado);
			log.info("Buscando biometricos........: " + captura);
			log.info("Buscando token........: " + token);
			
			
			if(numeroEmpleado == null) {
				throw new Exception("El Numero de Empleado esta vacio");
			}
			
			empleadoDAO = new EmpleadoDAO();
			detEmpleadoDTO = empleadoDAO.buscarPorNumEmpl(numeroEmpleado);
			
			detTokenDAO = new DetTokenDAO();
			detTokenDTO = detTokenDAO.buscarPorIdEmpleadoAndFecha(detEmpleadoDTO.getIdEmpleado());
			
			
               
			if(detTokenDTO.getCaducidad().before(fecha)) {
				log.info("La fecha recuperada es valida"); 
				
				
			}else {
				//Invalidar el token y redirigir al jsp
				log.info("La fecha recuperada no es valida");
				//Actualizamos campo valido ?? a 0 ??
				
				prettyGson = new GsonBuilder().setPrettyPrinting().create();
				
				detTokenDTO.setValido(false);
				detTokenDAO.actualizar(detTokenDTO);
				request.getSession(true).setAttribute("empleado", detEmpleadoDTO);
				
				respuesta = new RegistryResponse();
				respuesta.setCodigo(0);
				respuesta.setMensaje(null);
				if(accion.equals("registro")) {
					
					respuesta.setUrl("protected/registroAsistencia.xhtml");//opcion 1: replicar metodo login debajo, opcion 2: hacer instancia de loginBeany llamar a su metodo login si es posible
					listadetRegistro = registroDAO.buscarPorIdFechaEntrada(detEmpleadoDTO.getIdEmpleado(), strFechaActual);
		               String registro = (listadetRegistro.isEmpty()) ? "Entrada" : "Salida";
		               calendarHoy.setTime(diaActual);
		               diaActual = calendarHoy.getTime();
					
		               calendarDiaSistema.setTime(diaSistema);
		               String sDiaSistema = sdFormatHMS.format(diaActual);
		               calendarHoy.setTime(diaSistema);
		               
		               calendarHoy.set(Calendar.HOUR_OF_DAY, 7);
		               calendarHoy.set(Calendar.MINUTE,10);
		               Date hoy = calendarHoy.getTime();
		               String strActual = sdFormatHMS.format(hoy);
		               int result = sDiaSistema.compareTo(strActual);
		               
		               switch(registro) {
		               
		               case "Entrada":
		            	   
		               registroDTO.setFechaEntrada(fecha);
		               registroDTO.setFechaSalida(null);
		               registroDTO.setDetEmpleadoDTO(detEmpleadoDTO);
		               if(result > 0) {
		            	   status.setIdEstatus(2);
		               }else {
		            	   status.setIdEstatus(1);
		               }
		               registroDTO.setCatEstatusRegistroDTO(status);
		               registroDAO.guardar(registroDTO);
		               break;
		               
		               case "Salida":
		            	   registroDTO = listadetRegistro.get(listadetRegistro.size()-1);
		            	   listadetRegistro.get(listadetRegistro.size()-1);
		            	   registroDTO.setFechaSalida(fecha);
		            	   registroDAO.actualizar(registroDTO);		            	   
		               break;

		               }
					/*LoginBean login = new LoginBean();
					login.login(numeroEmpleado);*/
				}				
				else if(accion.equals("perfil")) {
					respuesta.setUrl("protected/kardexEmpleado.xhtml");
				}
				
			}
			jsonResponse = prettyGson.toJson(respuesta);
			httpStatus = HttpServletResponse.SC_OK;
		} catch(Exception ex) {
		
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
