package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.BiometricoDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetBiometrico;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.SGPException;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(LoginBean.class);

    private DetEmpleado empleadoSelected;
    private DetEmpleado detEmpleadoDTO;
    private List<DetEmpleado> lstEmpleados;

    private List<DetRegistro> lstregistroEmpleados;
    private DetRegistro registroEmpleado;

    private CatEstatusRegistro catEstatusRegistro;

    private String numEmpleado;
    private String idEmpleado;
    private Date strDiaHoy;
    private Integer contador;
    private Date diaHoy;
    private boolean navegacion;
    private List<String> listadoBiometricos;
    private final Date diaSistema;
    private Calendar cDiaHoy;
    private final Calendar cDiaSistema;
    private final SimpleDateFormat sDFormat;
    private final SimpleDateFormat sDFormatHMS;

    private HttpServletRequest httpServletRequest;
    private HttpSession session;

    private final EmpleadoDAO empleadoDAO;
    private final RegistroDAO registroDAO;
    private final BiometricoDAO biometricoDAO;

    private LocalDate hoy;
    private DayOfWeek diaSemana;

    public LoginBean() {
        empleadoDAO = new EmpleadoDAO();
        registroDAO = new RegistroDAO();
        biometricoDAO = new BiometricoDAO();
        empleadoSelected = new DetEmpleado();
        lstEmpleados = new ArrayList<>();
        detEmpleadoDTO = new DetEmpleado();
        lstregistroEmpleados = new ArrayList<>();
        registroEmpleado = new DetRegistro();
        catEstatusRegistro = new CatEstatusRegistro();
        diaHoy = new Date();
        cDiaHoy = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cDiaSistema = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        sDFormat = new SimpleDateFormat("yyyy-MM-dd");
        sDFormatHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            strDiaHoy = sDFormat.parse(sDFormat.format(diaHoy));
        }catch(ParseException ex)
        {
            log.info("Error al convertir fechas: {}", ex.getMessage());
        }
        diaSistema = new Date();
        listadoBiometricos = new ArrayList<>();

        hoy = LocalDate.now();
        diaSemana = hoy.getDayOfWeek();
        contador = 0;
        setNumEmpleado(null);
    }

    @PostConstruct
    public void init() {
        contador = 0;
    }

    /**
     * Realizar consulta en DAO por numero Empleado
     *
     * @param navegacion
     * @throws IOException
     */
    public void login(String numEmpleado) throws IOException {
    	log.info("Entrando a login");
        empleadoSelected = empleadoDAO.buscarPorId(Integer.valueOf(numEmpleado));

        if (contador <= 3) {
            if (empleadoSelected != null) {
                lstregistroEmpleados = registroDAO.buscarPorIdEmpleadoActivo(empleadoSelected.getIdEmpleado(), strDiaHoy);
                String registro = (lstregistroEmpleados.isEmpty()) ? "Entrada" : "Salida";
                cDiaHoy.setTime(diaHoy);
                diaHoy = cDiaHoy.getTime();

                /*
                 * Código para saber si llega a tiempo
                 */
                cDiaSistema.setTime(diaSistema);
                String strDiaSistema = sDFormatHMS.format(diaHoy);
                cDiaHoy.setTime(diaSistema);
                /*
                 * Declarando la Hora y Minutos para retardos
                 */
                cDiaHoy.set(Calendar.HOUR_OF_DAY, 7);
                cDiaHoy.set(Calendar.MINUTE, 10);
                Date diaHoyActual = cDiaHoy.getTime();
                String strDiaHoyActual = sDFormatHMS.format(diaHoyActual);
                int result = strDiaSistema.compareTo(strDiaHoyActual);

                switch (registro) {
                    case "Entrada":
                        registroEmpleado.setIdEmpleado(empleadoSelected);
                        registroEmpleado.setFechaEntrada(new Date());
                        registroEmpleado.setFechaSalida(null);
                        if (result > 0) {
                            catEstatusRegistro.setIdEstatus(2);
                        } else {
                            catEstatusRegistro.setIdEstatus(1);
                        }
                        registroEmpleado.setIdEstatus(catEstatusRegistro);
                        try {
                            registroDAO.guardar(registroEmpleado);

                            //en caso de que todas las validaciones se encuentren correctas, se procederá a registrar
                            //el usuario en sesión y redirigir a la página de bienvenida.
                            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                            httpServletRequest.getSession(true).setAttribute("empleado", empleadoSelected);
                            if (navegacion) {
                                FacesContext.getCurrentInstance().getExternalContext().redirect("protected/kardexEmpleado.xhtml");
                            } else {
                                FacesContext.getCurrentInstance().getExternalContext().redirect("protected/registroAsistencia.xhtml");
                            }
                        } catch (SGPException e) {
                            log.warn("EX-0004: " + e.getMessage() + ". Error al guardar el registro de entrada del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                        }
                        break;
                    case "Salida":
                        try {
                        registroEmpleado = lstregistroEmpleados.get(lstregistroEmpleados.size() - 1);
                        registroEmpleado.setIdEmpleado(lstregistroEmpleados.get(lstregistroEmpleados.size() - 1).getIdEmpleado());
                        lstregistroEmpleados.get(lstregistroEmpleados.size() - 1);
                        registroEmpleado.setFechaSalida(new Date());
                        registroDAO.actualizar(registroEmpleado);
                        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                        httpServletRequest.getSession(true).setAttribute("empleado", empleadoSelected);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null));
                        if (navegacion) {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("protected/kardexEmpleado.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("protected/registroAsistencia.xhtml");
                        }
                    } catch (SGPException ex) {
                        log.warn("EX-0005: " + ex.getMessage() + ". Error al guardar el registro de salida del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                    }
                    break;
                    default:
                        break;
                }
            } else {
                empleadoSelected = new DetEmpleado();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Verifique su usuario."));
                contador++;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte al Administrador."));
            log.warn("EX-0006: La cantidad de intentos que realizo el empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null + " supero el limite.");
        }
    }

    public void consultaBiometrico(String numEmpleado, boolean byNavegacion) {
    	log.info("Solicitando biometricos...");
        listadoBiometricos.clear();
        DetBiometrico biometrico = biometricoDAO.consultaBiometricoByNumEmpleado(numEmpleado);
        listadoBiometricos.add(biometrico != null ? biometrico.getHuella() : "");
        if (biometrico != null && biometrico.getHuella2() != null) {
            listadoBiometricos.add(biometrico.getHuella2());
        }
        PrimeFaces.current().ajax().update("login_form:listBiometrico");
        navegacion = byNavegacion;
    }
    
    public void logProvisional(boolean isActive){
        if(isActive){
            log.info("Modo activo");
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsp?faces-redirect=true");
            } catch (IOException ex) {
                log.info("Error al redireccionar");
            }
        } else {
            log.info("Modo suspensión");
        }
        setNumEmpleado(null);
    }

    public void killSesion() throws IOException {
    	log.info("Entrada al killSesion");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        if(session.getAttribute("empleado") != null){
            session.removeAttribute("empleado");
        }
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("JSESSIONID", null, null);
        
        String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(context);
        
        setNumEmpleado(null);

        log.info("==================== Finalizando sesion ====================");
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public DetEmpleado getDetEmpleadoDTO() {
        return detEmpleadoDTO;
    }

    public void setDetEmpleadoDTO(DetEmpleado detEmpleadoDTO) {
        this.detEmpleadoDTO = detEmpleadoDTO;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public List<DetEmpleado> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(List<DetEmpleado> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public EmpleadoDAO getEmpleadoDAO() {
        return empleadoDAO;
    }

    public CatEstatusRegistro getCatEstatusRegistro() {
        return catEstatusRegistro;
    }

    public void setCatEstatusRegistro(CatEstatusRegistro catEstatusRegistro) {
        this.catEstatusRegistro = catEstatusRegistro;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getDiaHoy() {
        return diaHoy;
    }

    public void setDiaHoy(Date diaHoy) {
        this.diaHoy = diaHoy;
    }

    public List<DetRegistro> getLstregistroEmpleados() {
        return lstregistroEmpleados;
    }

    public void setLstregistroEmpleados(List<DetRegistro> lstregistroEmpleados) {
        this.lstregistroEmpleados = lstregistroEmpleados;
    }

    public DetRegistro getRegistroEmpleado() {
        return registroEmpleado;
    }

    public void setRegistroEmpleado(DetRegistro registroEmpleado) {
        this.registroEmpleado = registroEmpleado;
    }

    public List<String> getListadoBiometricos() {
        return listadoBiometricos;
    }

    public void setListadoBiometricos(List<String> listadoBiometricos) {
        this.listadoBiometricos = listadoBiometricos;
    }

    public boolean isNavegacion() {
        return navegacion;
    }

    public void setNavegacion(boolean navegacion) {
        this.navegacion = navegacion;
    }

    public Calendar getcDiaHoy() {
        return cDiaHoy;
    }

    public void setcDiaHoy(Calendar cDiaHoy) {
        this.cDiaHoy = cDiaHoy;
    }

    public LocalDate getHoy() {
        return hoy;
    }

    public void setHoy(LocalDate hoy) {
        this.hoy = hoy;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }
    //</editor-fold>
}
