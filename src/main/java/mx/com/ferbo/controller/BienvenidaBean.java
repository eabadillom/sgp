package mx.com.ferbo.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;

@Named(value = "bienvenidaBean")
@ViewScoped
public class BienvenidaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(BienvenidaBean.class);
    
	private DetEmpleado empleadoSelected;
    private String numeroEmpl;
    private String strCumpleanios;
    private DateFormat dateFormat;
    private Date fechaActual;
    private LocalDate currentDate;
    private LocalDate fechaCumpleanios;
    private String strFechaActual;
    private String strFechaCumpleanios;

    private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session = null;
    
    private DetRegistroDTO registro;
    private final RegistroDAO registroDAO;
    private DetEmpleadoFoto empleadoFoto;

    public BienvenidaBean() {
        registroDAO = new RegistroDAO();
        empleadoSelected = new DetEmpleado();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = LocalDate.parse(getDateFormat().format(new Date()));
        strCumpleanios = "";
        fechaActual = Date.from(Instant.now());
    }

    @PostConstruct
    public void init() {
    	
    	try {
    		this.context = FacesContext.getCurrentInstance();
    		this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
    		this.session = this.request.getSession(false);
    		this.empleadoSelected = (DetEmpleado) this.request.getSession(true).getAttribute("empleado");
    		this.empleadoFoto = (DetEmpleadoFoto) session.getAttribute("fotografia");
    		
    		empleadoLogeado();
    		consultaRegistro();
    		
    		PrimeFaces.current().executeScript("PF('bar').show()");
    	} catch(Exception ex) {
    		log.error("Problema en el registro de asistencia del empleado...");
    	}
    }

    public String pasoDeEmpleado(DetEmpleado detEmpleadoDTO) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null));
        empleadoSelected = detEmpleadoDTO;
        return "protected/registroAsistencia.xhtml";
    }

    public void empleadoLogeado() {
        int diaActual = currentDate.getDayOfMonth();
        int mesActual = currentDate.getMonthValue();
        strFechaCumpleanios = getDateFormat().format(empleadoSelected.getFechaNacimiento());
        fechaCumpleanios = LocalDate.parse(strFechaCumpleanios);
        int diaCumpleanios = fechaCumpleanios.getDayOfMonth();
        int mesCumpleanios = fechaCumpleanios.getMonthValue();
        if (diaActual == diaCumpleanios && mesActual == mesCumpleanios) {
            strCumpleanios = "¡Feliz Cumpleaños " + empleadoSelected.getNombre() + " " + empleadoSelected.getPrimerAp() + " " + empleadoSelected.getSegundoAp() + "!";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null));
    }
    
    public void consultaRegistro(){
       Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(Date.from(Instant.now()));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        registro = registroDAO.buscarEntradaHoy(empleadoSelected.getIdEmpleado(), cal.getTime());         
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public String getNumeroEmpl() {
        return numeroEmpl;
    }

    public void setNumeroEmpl(String numeroEmpl) {
        this.numeroEmpl = numeroEmpl;
    }

    public String getStrCumpleanios() {
        return strCumpleanios;
    }

    public void setStrCumpleanios(String strCumpleanios) {
        this.strCumpleanios = strCumpleanios;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public String getStrFechaActual() {
        return strFechaActual;
    }

    public void setStrFechaActual(String strFechaActual) {
        this.strFechaActual = strFechaActual;
    }

    public DetRegistroDTO getRegistro() {
        return registro;
    }

    public void setRegistro(DetRegistroDTO registro) {
        this.registro = registro;
    }
    //</editor-fold>

	public DetEmpleadoFoto getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}
}
