package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;

@Named(value = "repEmpleados")
@ViewScoped
public class RepEmpleadosBean implements Serializable {

	private static final long serialVersionUID = -2370437684594791650L;
	private static Logger log = LogManager.getLogger(RepEmpleadosBean.class);
	
	private CatEmpresa empresa = null;
	private CatPlanta planta = null;
	private List<DetEmpleado> empleados = null;
	private List<CatEmpresa> empresas = null;
	private List<CatPlanta> plantas = null;
	
	private EmpleadoDAO empleadoDAO = null;
	private EmpresaDAO empresaDAO = null;
	private PlantaDAO plantaDAO = null;
	
	private StreamedContent pdfFile;
    private StreamedContent xlsFile;
	
	public RepEmpleadosBean() {
		empleadoDAO = new EmpleadoDAO();
		empresaDAO = new EmpresaDAO();
		plantaDAO = new PlantaDAO();
		
		empresas = empresaDAO.buscarActivo();
		plantas = plantaDAO.buscarTodos();
	}
	
	@PostConstruct
	public void init() {
		byte[] bytes = {};
		
		pdfFile = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
				.name("ReporteAsistencia.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
		
		xlsFile = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").contentLength(bytes.length)
				.name("ReporteAsistencia.xlsx").stream(() -> new ByteArrayInputStream(bytes)).build();
	}
	
	public void buscarEmpleados() {
		log.info("Buscando empleados...");
		this.empleados = empleadoDAO.buscarActivos(this.empresa == null ? null : this.empresa.getIdEmpresa(), this.planta == null ? null : this.planta.getIdPlanta(), new Date());
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

        try {
            jasperPath = "/jasper/ReporteEmpleados.jrxml";
            filename = String.format("ReporteEmpleados_%s.pdf", DateUtil.getString(new Date(), DateUtil.FORMATO_YYYY_MM_DD));
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

            parameters.put("REPORT_CONNECTION", conn);
            parameters.put("idEmpresa", this.empresa == null ? null : this.empresa.getIdEmpresa());
            parameters.put("idPlanta", this.planta == null ? null : this.planta.getIdPlanta());
            parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-06:00"));
            parameters.put("REPORT_LOCALE", new Locale("es", "MX"));
            parameters.put("fecha", new Date());
            parameters.put("imagen", imgFile.getPath());
            pdfFile = jasperReportUtil.getPdf(filename, parameters, reportFile.getPath());
            log.info("Exportación completa.");
        } catch (Exception e) {
            log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
            message = String.format("No es posible exportar el reporte.");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
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

        try {
            jasperPath = "/jasper/ReporteEmpleados.jrxml";
            filename = String.format("ReporteEmpleados_%s.xlsx", DateUtil.getString(new Date(), DateUtil.FORMATO_YYYY_MM_DD));
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

            parameters.put("REPORT_CONNECTION", conn);
            parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-06:00"));
            parameters.put("REPORT_LOCALE", new Locale("es", "MX"));
            parameters.put("idEmpresa", this.empresa == null ? null : this.empresa.getIdEmpresa());
            parameters.put("idPlanta", this.planta == null ? null : this.planta.getIdPlanta());
            parameters.put("fecha", new Date());
            parameters.put("imagen", imgFile.getPath());
            xlsFile = jasperReportUtil.getXls(filename, parameters, reportFile.getPath());
            log.info("Exportación completa.");
        } catch (Exception e) {
            log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
            message = String.format("No es posible exportar el reporte.");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            EntityManagerUtil.close(conn);
        }
    }
	
	public CatEmpresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}
	public CatPlanta getPlanta() {
		return planta;
	}
	public void setPlanta(CatPlanta planta) {
		this.planta = planta;
	}
	public List<DetEmpleado> getEmpleados() {
		return empleados;
	}
	public void setEmpleados(List<DetEmpleado> empleados) {
		this.empleados = empleados;
	}

	public List<CatEmpresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<CatEmpresa> empresas) {
		this.empresas = empresas;
	}

	public List<CatPlanta> getPlantas() {
		return plantas;
	}

	public void setPlantas(List<CatPlanta> plantas) {
		this.plantas = plantas;
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
