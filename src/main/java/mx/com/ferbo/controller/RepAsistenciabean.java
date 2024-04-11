package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.CatPlantaDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.CatPlantaDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.SGPException;


@Named(value = "repAsistenciaBean")
@ViewScoped
public class RepAsistenciabean implements Serializable {

	private static final long serialVersionUID = -1127550691400298355L;
	private static Logger log = LogManager.getLogger(RepAsistenciabean.class);
	private List<CatPlantaDTO> plantas;
	private CatPlantaDTO planta = null;
	private CatPlantaDAO plantaDAO;
	private Date fechaInicio;
	private Date fechaFin;
	private List<DetRegistroDTO> registros;
	private RegistroDAO registroDAO;
	
	private StreamedContent pdfFile;
	private StreamedContent xlsFile;
	
	public RepAsistenciabean() {
		plantaDAO = new CatPlantaDAO();
		registroDAO = new RegistroDAO();
	}
	
	@PostConstruct
	public void init() {
		plantas = plantaDAO.buscarTodos();
		this.fechaInicio = new Date();
		this.fechaFin = new Date();
		pdfFile = null;
	}
	
	public void cargaInfo() {
		FacesMessage message = null;
		Severity severity = null;
		String titulo = "Reporte";
		String mensaje = null;
		
		Integer idPlanta = null;
		
		try {
			DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
			DateUtil.setTime(fechaFin, 23, 59, 59, 0);
			
			log.info("Cargando información de la planta: {}", this.planta);
			log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
			log.info("Fin del periodo de búsqueda: {}", this.fechaFin);
			if(fechaInicio.after(fechaFin))
				throw new SGPException("La fecha inicio del periodo de búsqueda no puede ser posterior a la de fin de periodo.");
			
			idPlanta = this.planta == null ? null : this.planta.getIdPlanta();
			
			registros = registroDAO.buscarPorPlantaPeriodo(idPlanta, this.fechaInicio, this.fechaFin);
			
			this.exportarPDF();
			this.exportarXLS();
			
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente. Si el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if(mensaje != null) {
				message = new FacesMessage(severity, titulo, mensaje);
				FacesContext.getCurrentInstance().addMessage(null, message);
				PrimeFaces.current().ajax().update(":form:messages :form:cmd-exportar-pdf");
			}
		}
	}
	
	public void ajustaHoras() {
		DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
		DateUtil.setTime(fechaFin, 23, 59, 59, 0);
	}
	
	public void exportarPDF() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgFile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;
		
		Date fechaFinT = null;
		try {
			jasperPath = "/jasper/ReporteAsistencia.jrxml";
			filename = String.format("ReporteAsistencia_%s_%s.pdf", DateUtil.getString(this.fechaInicio, DateUtil.FORMATO_YYYY_MM_DD), DateUtil.getString(this.fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			images = "/images/logo.png";
			reportFile = new File(jasperPath);
			jasperReportUtil = new JasperReportUtil();
			parameters = new HashMap<String, Object>();
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			conn = EntityManagerUtil.getConnection();
			log.info("Conexion: {}", conn);
			DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
			DateUtil.setTime(fechaFin, 23, 59, 59, 0);
			log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
			log.info("Fin del periodo de búsqueda: {}", this.fechaFin);
			
			fechaFinT = new Date(this.fechaFin.getTime());
			fechaFinT = DateUtil.addDay(fechaFinT, 1);
			
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("idPlanta", this.planta == null ? null : this.planta.getIdPlanta());
			parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-06:00"));
			parameters.put("REPORT_LOCALE", new Locale("es", "MX"));
			parameters.put("fechaInicio", this.fechaInicio);
			parameters.put("fechaFin", fechaFinT);
			parameters.put("imagen", imgFile.getPath());
			pdfFile = jasperReportUtil.getPdf(filename, parameters, reportFile.getPath());
			log.info("Exportación completa.");
		} catch (Exception e) {
			log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
			message = String.format("No es posible exportar el reporte.");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
		}finally {
			EntityManagerUtil.close(conn);
		}
	}
	
	public void exportarXLS() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgFile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;
		
		Date fechaFinT = null;
		try {
			jasperPath = "/jasper/ReporteAsistencia.jrxml";
			filename = String.format("ReporteAsistencia_%s_%s.xlsx", DateUtil.getString(this.fechaInicio, DateUtil.FORMATO_YYYY_MM_DD), DateUtil.getString(this.fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			images = "/images/logo.png";
			reportFile = new File(jasperPath);
			jasperReportUtil = new JasperReportUtil();
			parameters = new HashMap<String, Object>();
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			conn = EntityManagerUtil.getConnection();
			log.info("Conexion: {}", conn);
			DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
			DateUtil.setTime(fechaFin, 23, 59, 59, 0);
			log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
			log.info("Fin del periodo de búsqueda: {}", this.fechaFin);
			
			fechaFinT = new Date(this.fechaFin.getTime());
			fechaFinT = DateUtil.addDay(fechaFinT, 1);
			
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("idPlanta", this.planta == null ? null : this.planta.getIdPlanta());
			parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-06:00"));
			parameters.put("REPORT_LOCALE", new Locale("es", "MX"));
			parameters.put("fechaInicio", this.fechaInicio);
			parameters.put("fechaFin", fechaFinT);
			parameters.put("imagen", imgFile.getPath());
			xlsFile = jasperReportUtil.getXls(filename, parameters, reportFile.getPath());
			log.info("Exportación completa.");
		} catch (Exception e) {
			log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
			message = String.format("No es posible exportar el reporte.");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
		}finally {
			EntityManagerUtil.close(conn);
		}
	}

	public List<CatPlantaDTO> getPlantas() {
		return plantas;
	}

	public void setPlantas(List<CatPlantaDTO> plantas) {
		this.plantas = plantas;
	}

	public CatPlantaDTO getPlanta() {
		return planta;
	}

	public void setPlanta(CatPlantaDTO planta) {
		this.planta = planta;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<DetRegistroDTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<DetRegistroDTO> registros) {
		this.registros = registros;
	}

	public StreamedContent getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(StreamedContent pdfFile) {
		this.pdfFile = pdfFile;
	}

	public StreamedContent getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(StreamedContent xlsFile) {
		this.xlsFile = xlsFile;
	}
	
	
	
}
