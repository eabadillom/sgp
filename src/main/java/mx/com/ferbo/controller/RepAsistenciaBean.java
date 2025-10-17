package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.SGPSecurityException;

@Named(value = "repAsistenciaBean")
@ViewScoped
public class RepAsistenciaBean implements Serializable {

    private static final long serialVersionUID = -1127550691400298355L;
    private static Logger log = LogManager.getLogger(RepAsistenciaBean.class);
    
    private HttpServletRequest request = null;
    private FacesContext context = null;
//    private String contextPath = null;
    private HttpSession session = null;

    private PlantaDAO plantaDAO;
    private RegistroDAO registroDAO;
    private EstatusRegistroDAO statusRegistroDAO;

    private List<CatPlanta> plantas;
    private List<DetRegistro> registros;

    private CatPlanta planta = null;
    private Date fechaInicio;
    private Date fechaFin;

    private StreamedContent pdfFile;
    private StreamedContent xlsFile;

    private String badgeColor;
    private DetRegistro registroSelected;
    private List<CatEstatusRegistro> lstEstatus;
    private List<Date> diasDesabilitados;

    public RepAsistenciaBean() throws SGPSecurityException {
    	DetEmpleado empleadoSesion = null;
    	
    	this.context = FacesContext.getCurrentInstance();
    	this.request = (HttpServletRequest) context.getExternalContext().getRequest();
    	this.session = request.getSession(false);
    	
    	CatEstatusRegistro status = null;
    	
    	try {
    		log.info("Ejecutando constructor...");
    		empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
    		
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() != 1)
    			throw new SGPSecurityException("No tiene acceso a este recurso.");
    		
    		
    		plantaDAO = new PlantaDAO(CatPlanta.class);
    		registroDAO = new RegistroDAO(DetRegistro.class);
    		statusRegistroDAO = new EstatusRegistroDAO(CatEstatusRegistro.class);
    		
    		plantas = plantaDAO.buscarTodos();
    		lstEstatus = new ArrayList<CatEstatusRegistro>();
    		status = statusRegistroDAO.buscarPorCodigo("T");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("R");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("F");
    		statusRegistroDAO.buscarPorCodigo("J");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("X");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("J");
    		lstEstatus.add(status);
    		
    		this.fechaInicio = new Date();
    		this.fechaFin = new Date();
    		this.badgeColor = "";
    		
    		byte[] bytes = {};
    		
    		pdfFile = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
    				.name("ReporteAsistencia.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
    		
    		xlsFile = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").contentLength(bytes.length)
    				.name("ReporteAsistencia.xlsx").stream(() -> new ByteArrayInputStream(bytes)).build();
    		
    		log.info("Termina constructor.");
    	} catch(SGPSecurityException ex) {
    		throw ex;
    	} catch(Exception ex) {
    		log.error("Problema para inicializar el reporte de asistencia...", ex);
    	}
    }

    @PostConstruct
    public void init() {
    	DetEmpleado empleadoSesion = null;
//    	String path = null;
    	empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
//    	contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
//    	FacesContext context = null;
    	
    	try {
    		log.info("Ejecutando proceso init...");
    		
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() == 1) {
    			return;
    		}
    		
//	    	path = this.contextPath + "/unauthorized.xhtml?faces-redirect=true";
//	    	log.info("Redirigiendo a {}", path);
//	    	context = FacesContext.getCurrentInstance();
//	    	FacesContext.getCurrentInstance()
//	    		.getApplication()
//	    		.getNavigationHandler()
//	    		.handleNavigation(context, null, path);
//    		
//    		
//    		log.info("Proceso init terminado.");
    	} catch(Exception ex) {
    		log.error("Problema para entrar al reporte de inventario...", ex);
    	}
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
            if (fechaInicio.after(fechaFin)) {
                throw new SGPException("La fecha inicio del periodo de búsqueda no puede ser posterior a la de fin de periodo.");
            }

            idPlanta = this.planta == null ? null : this.planta.getIdPlanta();

            registros = registroDAO.buscarPorPlantaPeriodo(idPlanta, this.fechaInicio, this.fechaFin);

            log.info("{}  registro(s) encontrado(s)", registros.size());

        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente. Si el problema persiste, por favor comuniquese con su administrador del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            if (mensaje != null) {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update(":form:messages :form:cmd-exportar-pdf");
            }
        }
    }

    public void ajustaHoras() {
    	try {
    		log.info("Ajustando inicio y fin del periodo...");
    		DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
    		DateUtil.setTime(fechaFin, 23, 59, 59, 0);
    	} catch(Exception ex) {
    		log.error("Problema para establecer la hora inicio y fin del periodo...", ex);
    	}
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            EntityManagerUtil.close(conn);
        }
    }

    public void editarRegistro(DetRegistro registro) {
    	try {
    		log.info("Cargando información de registro...");
    		this.registroSelected = registro;
    		
    		int anio = DateUtil.getAnio(registro.getFechaEntrada());
    		int mes = DateUtil.getMes(registro.getFechaEntrada());
    		int dia = DateUtil.getDia(registro.getFechaEntrada());
    		
    		Date diaPermitido = DateUtil.getDateTime(anio, mes, dia, 0, 0, 0, 0);
    		
    		this.diasDesabilitados = new ArrayList<Date>();
    		
    		int maximoDia = 30;
    		
    		if (mes == 1) {
    			maximoDia = 28;
    			if (DateUtil.esBisiesto(diaPermitido)) {
    				maximoDia = 29;
    			}
    		}
    		
    		if (mes == 0 || mes == 2 || mes == 4 || mes == 6 || mes == 7 || mes == 9 || mes == 11) {
    			maximoDia = 31;
    		}
    		
    		maximoDia++;
    		
    		for (int i = 1; i < maximoDia; i++) {
    			Date diaNoPermitido = DateUtil.getDateTime(anio, mes, i, 0, 0, 0, 0);
    			if (diaNoPermitido.compareTo(diaPermitido) != 0) {
    				this.diasDesabilitados.add(diaNoPermitido);
    			}
    		}
    		
    		if (registro.getStatus().getDescripcion().equals("Falta")) {
    			int horaSalida = DateUtil.getHora(registro.getIdEmpleado().getDatoEmpresa().getHorasalida());
    			Date diaSalida = DateUtil.getDateTime(anio, mes, dia, horaSalida, 0, 0, 0);
    			registro.setFechaSalida(diaSalida);
    		}
    	} catch(Exception ex) {
    		log.error("Problema para editar el registro de asistencia...", ex);
    	}
    }

    public void restablecerValores() {
    	try {
    		log.info("Reiniciando valores...");
    		this.registroSelected = null;
//    		Integer idPlanta = this.planta == null ? null : this.planta.getIdPlanta();
//    		registros = registroDAO.buscarPorPlantaPeriodo(idPlanta, this.fechaInicio, this.fechaFin);
    	} catch(Exception ex) {
    		log.error("Problema para reestablecer los valores...", ex);
    	}
    }

    public void actualizarRegistro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar asistencia";
        try {
        	log.info("Actualizando registro...");
            if ("F".equalsIgnoreCase(this.registroSelected.getStatus().getCodigo())) {
                int anio = DateUtil.getAnio(this.registroSelected.getFechaEntrada());
                int mes = DateUtil.getMes(this.registroSelected.getFechaEntrada());
                int dia = DateUtil.getDia(this.registroSelected.getFechaEntrada());
                Date entrada = DateUtil.getDateTime(anio, mes, dia, 0, 0, 0, 0);
                this.registroSelected.setFechaEntrada(entrada);
                this.registroSelected.setFechaSalida(entrada);
            }

            registroDAO.actualizar(this.registroSelected);
            mensaje = "El registro de actualizo correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException sgpEx) {
            log.error("Ocurrió un problema al actualizar el registro de asistencia...", sgpEx);
            mensaje = "No se actualizo el registro.";
            severity = FacesMessage.SEVERITY_WARN;

        } catch (Exception ex) {
            log.error("Ocurrió un error inesperado al actualizar el registro de asistencia...", ex);
            mensaje = "No se actualizo el registro. Favor de contactar con el admistrador del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public String obtenerEstatusAsistencia(DetRegistro registro) {
    	try {
    		switch (registro.getStatus().getCodigo()) {
    		
    		case "T":
    		case "J":
    		case "V":
    		case "P":
    		case "X":
    		case "I":
    		case "D":
    			this.badgeColor = new String();
    			this.badgeColor = "success";
    			break;
    			
    		case "R":
    			this.badgeColor = new String();
    			this.badgeColor = "warning";
    			break;
    			
    		case "F":
    			this.badgeColor = new String();
    			this.badgeColor = "danger";
    			break;
    		}
    	} catch(Exception ex) {
    		log.error("Problema para determinar el status del registro de asistencia...", ex);
        }

        return registro.getStatus().getDescripcion();
    }

    public List<CatPlanta> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<CatPlanta> plantas) {
        this.plantas = plantas;
    }

    public CatPlanta getPlanta() {
        return planta;
    }

    public void setPlanta(CatPlanta planta) {
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

    public List<DetRegistro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<DetRegistro> registros) {
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

    public String getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }

    public DetRegistro getRegistroSelected() {
        return registroSelected;
    }

    public void setRegistroSelected(DetRegistro registroSelected) {
        this.registroSelected = registroSelected;
    }

    public EstatusRegistroDAO getEstatusRegistroDAO() {
        return statusRegistroDAO;
    }

    public void setEstatusRegistroDAO(EstatusRegistroDAO estatusRegistroDAO) {
        this.statusRegistroDAO = estatusRegistroDAO;
    }

    public List<CatEstatusRegistro> getLstEstatus() {
        return lstEstatus;
    }

    public void setLstEstatus(List<CatEstatusRegistro> lstEstatus) {
        this.lstEstatus = lstEstatus;
    }

    public List<Date> getDiasDesabilitados() {
        return diasDesabilitados;
    }

    public void setDiasDesabilitados(List<Date> diasDesabilitados) {
        this.diasDesabilitados = diasDesabilitados;
    }

}
