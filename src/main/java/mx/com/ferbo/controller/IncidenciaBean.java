package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.business.empleado.RegistroAsistenciaBL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.SolicitudArticuloDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.dao.n.SolicitudPrendaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabriel
 */
@Named("incidenciaBean")
@ViewScoped
public class IncidenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciaBean.class);
    
    private RegistroAsistenciaBL empleadoAsistencia;
    private DiasDeDescansoObligatorioBL diasDeDescansoObligatorio;
    private final IncidenciaDAO incidenciaDAO;
    private final SolicitudPermisoDAO solicitudPermisoDAO;
    private final TipoSolicitudDAO tipoSolicitudDAO;
    private final SolicitudPrendaDAO solicitudPrendaDAO;
    private final SolicitudArticuloDAO solicitudArticulosDAO;    
    private DetIncidencia incidenciaSelected;
    private Date fechaSeleccionada;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> listaPermisos;
    private List<DetIncidencia> listaPermisosFiltrada;
    private List<DetIncidencia> listaPrendas;
    private List<DetIncidencia> listaArticulos;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetSolicitudArticulo> listArticulos;
    private List<DetSolicitudPermiso> listPermisos;
    private List<DetIncidencia> listAuxPermisos;
    private List<DetSolicitudPrenda> listPrendas;
    private List<Date> lstRangoRegistro;
    private List<Integer> invalidDays;
    private List<Date> diasDeAsueto;
    private List<Date> diasDeVacaciones;
    private Date minDate;
    private Date periodoInicio;
    private Date periodoFin;
    private boolean incidenciaVacaciones;
    private boolean incidenciaIncapacidadCorta;
    private boolean incidenciaIncapacidadLarga;
    private boolean incidenciaPermiso;
    private boolean estatusAprovado;
    private boolean estatusRechazado;
    private boolean estatusCancelado;
    private boolean estatusEnviado;
    private Integer diasVacacionesSolicitados;

    private DetEmpleado empleadoSelected;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;
    private final EmpleadoDAO empleadoDAO;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IncidenciaBean() {
        incidenciaDAO = new IncidenciaDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        minDate = new Date();

        empleadoDAO = new EmpleadoDAO();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
        
        solicitudArticulosDAO = new SolicitudArticuloDAO();
        solicitudPermisoDAO = new SolicitudPermisoDAO();
        solicitudPrendaDAO = new SolicitudPrendaDAO();
        
        empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado());
        inicializarIncidencia();
        this.empleadoAsistencia = new RegistroAsistenciaBL();
        this.diasDeDescansoObligatorio = new DiasDeDescansoObligatorioBL();
    }

    @PostConstruct
    public void init() {
        consultaIncidencias();
        lstTipoSol = tipoSolicitudDAO.buscarActivos();
        listArticulos = solicitudArticulosDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        listPermisos = solicitudPermisoDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        listPrendas = solicitudPrendaDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        this.periodoFin = new Date();
        this.periodoInicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(periodoFin));
        validarFechaInicioFin();
        this.incidenciaVacaciones = false;
        this.incidenciaIncapacidadCorta = false;
        this.incidenciaIncapacidadLarga = false;
        this.incidenciaPermiso = false;
        this.estatusAprovado = false;
        this.estatusRechazado = false;
        this.estatusCancelado = false;
        this.estatusEnviado = false;
        diasDeAsueto = diasDeDescansoObligatorio.getDiasAsueto();
        status = new ManageStatus();
    }
    
    
    public void validarFechaInicioFin()
    {
        if(this.periodoFin.equals(this.periodoInicio))
        {
            this.periodoInicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(periodoFin) - 1);
        }
    }

    private void consultaIncidencias() {
        lstIncidencias = incidenciaDAO.buscarTodos();

        listaPermisos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(1) ||  objeto.getIdTipo().getIdTipo().equals(2))
                .collect(Collectors.toList());
        
        listaPrendas = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(3))
                .collect(Collectors.toList());
        
        listaArticulos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(4))
                .collect(Collectors.toList());
    }
    
    public List<DetIncidencia> consultarTipoPermisos()
    {
        List<DetIncidencia> listaPeriodo = new ArrayList();
        
        if(this.periodoInicio != null && this.periodoFin != null)
        {
            listaPeriodo = listaPermisos.stream()
                .filter(objeto -> objeto.getIdSolPermiso().getFechaInicio().after(this.periodoInicio))
                .filter(objeto -> objeto.getIdSolPermiso().getFechaFin().before(this.periodoFin))
                .collect(Collectors.toList());
        }
        
        List<DetIncidencia> listaTipoPermiso = new ArrayList<>();
        
        if(this.incidenciaPermiso)
        {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                .filter(objeto -> objeto.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud().equals(1))
                .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.incidenciaVacaciones)
        {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                .filter(objeto -> objeto.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud().equals(2))
                .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.incidenciaIncapacidadCorta)
        {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                .filter(objeto -> objeto.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud().equals(3))
                .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.incidenciaIncapacidadLarga)
        {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                .filter(objeto -> objeto.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud().equals(4))
                .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        List<DetIncidencia> listaTipoEstatus = new ArrayList<>();
        
        if(!this.estatusEnviado && !this.estatusAprovado && !this.estatusRechazado && !this.estatusCancelado)
        {
            listaTipoEstatus = listaTipoPermiso;
        }
        
        if(this.estatusEnviado)
        {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                .filter(objeto -> objeto.getIdEstatus().getIdEstatus().equals(1))
                .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.estatusAprovado)
        {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                .filter(objeto -> objeto.getIdEstatus().getIdEstatus().equals(2))
                .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.estatusRechazado)
        {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                .filter(objeto -> objeto.getIdEstatus().getIdEstatus().equals(3))
                .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        if(this.estatusCancelado)
        {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                .filter(objeto -> objeto.getIdEstatus().getIdEstatus().equals(4))
                .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }
        
        return listaTipoEstatus;
    }

    public void visualizaDialog() {
        Date fechaInicio = incidenciaSelected.getIdSolPermiso().getFechaInicio();
        Date fechaFin = incidenciaSelected.getIdSolPermiso().getFechaFin();
        List<Date> fechas = DateUtil.generarArreglosFechas(fechaInicio, fechaFin);
        switch (incidenciaSelected.getIdTipo().getIdTipo()) {
            // Tipo Permisos
            case 1:
                switch (incidenciaSelected.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud()) {
                    case 1://PERMISO
                    case 3://INCAPACDAD CORTA
                        fechaSeleccionada = incidenciaSelected.getIdSolPermiso().getFechaInicio();
                        break;
                    default:
                        lstRangoRegistro = Arrays.asList(incidenciaSelected.getIdSolPermiso().getFechaInicio(), incidenciaSelected.getIdSolPermiso().getFechaFin());
                        break;
                }
                this.invalidDays = obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
                this.diasDeVacaciones = empleadoAsistencia.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa());
                log.trace("Dias Solicitados: {}", this.diasDeVacaciones.toString());
                this.diasVacacionesSolicitados = this.diasDeVacaciones.size();
                log.trace("Total Dias de Vacaciones Solicitados: {}", this.diasVacacionesSolicitados);
                PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                break;
            // Tipo Vacaciones
            case 2:
                this.invalidDays = obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
                this.diasDeVacaciones = empleadoAsistencia.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa());
                log.trace("Dias Solicitados: {}", this.diasDeVacaciones.toString());
                this.diasVacacionesSolicitados = this.diasDeVacaciones.size();
                lstRangoRegistro = Arrays.asList(diasDeVacaciones.get(0), diasDeVacaciones.get(this.diasVacacionesSolicitados-1));
                log.trace("Total Dias de Vacaciones Solicitados: {}", this.diasVacacionesSolicitados);
                PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                break;
            // Tipo Prendas
            case 3:
                PrimeFaces.current().executeScript("PF('dialogPrendas').show();");
                break;
            // Tipo Articulos
            case 4:
                PrimeFaces.current().executeScript("PF('dialogArticulos').show();");
                break;
            default:
                log.warn("EX-0023: Error al seleccionar opción");
        }
    }
    
    public void inicializarIncidencia()
    {
        incidenciaSelected = new DetIncidencia();
        incidenciaSelected.setIdEstatus(new CatEstatusIncidencia());
        incidenciaSelected.setIdSolArticulo(new DetSolicitudArticulo());
        incidenciaSelected.setIdSolPermiso(new DetSolicitudPermiso());
        incidenciaSelected.setIdSolPrenda(new DetSolicitudPrenda());
        incidenciaSelected.setIdTipo(new CatTipoIncidencia());
    }

    public void guardarEstatusIncidencia(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudPermiso auxPermiso: listPermisos)
            {
                if(auxPermiso.getIdSolicitud().equals(incidenciaSelected.getIdSolPermiso().getIdSolicitud()))
                {
                    auxPermiso.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxPermiso.setFechaMod(new Date());
                    auxPermiso.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudPermisoDAO.actualizar(auxPermiso);
                    incidenciaSelected.setIdSolPermiso(auxPermiso);
                }
            }
            
            incidenciaSelected.getIdSolPermiso().setFechaMod(new Date());
            incidenciaSelected.getIdSolPermiso().setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
            
            incidenciaDAO.actualizar(incidenciaSelected);
            
            if(incidenciaSelected.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud() == 2 && incidenciaSelected.getIdEstatus().getIdEstatus() == 2)
            {
                this.empleadoAsistencia.guardarRegistroVacaciones(empleadoSelected, incidenciaSelected, diasDeDescansoObligatorio.getDiasAsueto());
            }
            
            log.info("Dias solicitados del empleado {} son: {}", empleadoSelected.getIdEmpleado(), this.diasVacacionesSolicitados);
            
            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";    
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0028: " + ex.getMessage() + ". Error al guardar el status del registro de la incidencia del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
        if(incidenciaSelected.getIdSolPermiso().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPermisos').hide()");
        }
    }
    
    public void guardarEstatusArticulo(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudArticulo auxArticulo: listArticulos)
            {
                if(auxArticulo.getIdSolicitud().equals(incidenciaSelected.getIdSolArticulo().getIdSolicitud()))
                {
                    auxArticulo.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxArticulo.setFechaMod(new Date());
                    auxArticulo.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudArticulosDAO.actualizar(auxArticulo);
                    incidenciaSelected.setIdSolArticulo(auxArticulo);
                }
            }
            
            incidenciaDAO.actualizar(incidenciaSelected);
            
            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0029: " + ex.getMessage() + ". Error al guardar el status del registro del artículo del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtArticulosSolicitados");
        if(incidenciaSelected.getIdSolArticulo().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }
    
    public void guardarEstatusPrenda(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudPrenda auxPrenda : listPrendas)
            {
                if(auxPrenda.getIdSolicitud().equals(incidenciaSelected.getIdSolPrenda().getIdSolicitud()))
                {
                    auxPrenda.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxPrenda.setFechaMod(new Date());
                    auxPrenda.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudPrendaDAO.actualizar(auxPrenda);
                    incidenciaSelected.setIdSolPrenda(auxPrenda);
                }
            }
            
            incidenciaDAO.actualizar(incidenciaSelected);
            
            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0030: " + ex.getMessage() + ". Error al guardar el status del registro de la prenda del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtPrendasSolicitados");
        if(incidenciaSelected.getIdSolPrenda().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
        }
    }
    
    public List<Integer> obtenerDiasSeleccionados(InfDatoEmpresa empleadoEmpresa) 
    {  
        List<Integer> diasSeleccionados = new ArrayList<>();
        
        if (empleadoEmpresa.getDiaLunes() != true) 
            diasSeleccionados.add(1);
        if (empleadoEmpresa.getDiaMartes() != true) 
            diasSeleccionados.add(2);
        if (empleadoEmpresa.getDiaMiercoles() != true) 
            diasSeleccionados.add(3);
        if (empleadoEmpresa.getDiaJueves() != true) 
            diasSeleccionados.add(4);
        if (empleadoEmpresa.getDiaViernes() != true) 
            diasSeleccionados.add(5);
        if (empleadoEmpresa.getDiaSabado() != true) 
            diasSeleccionados.add(6);
        if (empleadoEmpresa.getDiaDomingo() != true) 
            diasSeleccionados.add(0);
        
        log.trace("Dias de bloqueo: {}", diasSeleccionados.toString());
        return diasSeleccionados;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public DetIncidencia getIncidenciaSelected() {
        return incidenciaSelected;
    }

    public void setIncidenciaSelected(DetIncidencia incidenciaSelected) {
        this.incidenciaSelected = incidenciaSelected;
    }

    public List<DetIncidencia> getLstIncidencias() {
        return lstIncidencias;
    }

    public void setLstIncidencias(List<DetIncidencia> lstIncidencias) {
        this.lstIncidencias = lstIncidencias;
    }

    public List<DetIncidencia> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<DetIncidencia> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<DetIncidencia> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<DetIncidencia> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public List<Date> getLstRangoRegistro() {
        return lstRangoRegistro;
    }

    public void setLstRangoRegistro(List<Date> lstRangoRegistro) {
        this.lstRangoRegistro = lstRangoRegistro;
    }

    public List<Integer> getInvalidDays() {
        return invalidDays;
    }

    public void setInvalidDays(List<Integer> invalidDays) {
        this.invalidDays = invalidDays;
    }

    public List<CatTipoSolicitud> getLstTipoSol() {
        return lstTipoSol;
    }

    public void setLstTipoSol(List<CatTipoSolicitud> lstTipoSol) {
        this.lstTipoSol = lstTipoSol;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<DetIncidencia> getListaPrendas() {
        return listaPrendas;
    }

    public void setListaPrendas(List<DetIncidencia> listaPrendas) {
        this.listaPrendas = listaPrendas;
    }
    
    public ManageStatus getStatus() {
        return status;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }

    public boolean isIncidenciaVacaciones() {
        return incidenciaVacaciones;
    }

    public void setIncidenciaVacaciones(boolean incidenciaVacaciones) {
        this.incidenciaVacaciones = incidenciaVacaciones;
    }

    public boolean isIncidenciaIncapacidadCorta() {
        return incidenciaIncapacidadCorta;
    }

    public void setIncidenciaIncapacidadCorta(boolean incidenciaIncapacidadCorta) {
        this.incidenciaIncapacidadCorta = incidenciaIncapacidadCorta;
    }

    public boolean isIncidenciaIncapacidadLarga() {
        return incidenciaIncapacidadLarga;
    }

    public void setIncidenciaIncapacidadLarga(boolean incidenciaIncapacidadLarga) {
        this.incidenciaIncapacidadLarga = incidenciaIncapacidadLarga;
    }

    public boolean isIncidenciaPermiso() {
        return incidenciaPermiso;
    }

    public void setIncidenciaPermiso(boolean incidenciaPermiso) {
        this.incidenciaPermiso = incidenciaPermiso;
    }

    public boolean isEstatusAceptado() {
        return estatusAprovado;
    }

    public void setEstatusAceptado(boolean estatusAprovado) {
        this.estatusAprovado = estatusAprovado;
    }

    public boolean isEstatusRechazado() {
        return estatusRechazado;
    }

    public void setEstatusRechazado(boolean estatusRechazado) {
        this.estatusRechazado = estatusRechazado;
    }

    public boolean isEstatusCancelado() {
        return estatusCancelado;
    }

    public void setEstatusCancelado(boolean estatusCancelado) {
        this.estatusCancelado = estatusCancelado;
    }

    public boolean isEstatusEnviado() {
        return estatusEnviado;
    }

    public void setEstatusEnviado(boolean estatusEnviado) {
        this.estatusEnviado = estatusEnviado;
    }
    
    public List<DetIncidencia> getListAuxPermisos() {
        return listAuxPermisos;
    }

    public void setListAuxPermisos(List<DetIncidencia> listAuxPermisos) {
        this.listAuxPermisos = listAuxPermisos;
    }
    
    public List<DetIncidencia> getListaPermisosFiltrada() {
        return listaPermisosFiltrada;
    }

    public void setListaPermisosFiltrada(List<DetIncidencia> listaPermisosFiltrada) {
        this.listaPermisosFiltrada = listaPermisosFiltrada;
    }
    
    public Integer getDiasVacacionesSolicitados() {
        return diasVacacionesSolicitados;
    }

    public void setDiasVacacionesSolicitados(Integer diasVacacionesSolicitados) {
        this.diasVacacionesSolicitados = diasVacacionesSolicitados;
    }
    
    public DiasDeDescansoObligatorioBL getDiasDeDescansoObligatorio() {
        return diasDeDescansoObligatorio;
    }

    public void setDiasDeDescansoObligatorio(DiasDeDescansoObligatorioBL diasDeDescansoObligatorio) {
        this.diasDeDescansoObligatorio = diasDeDescansoObligatorio;
    }
    
    public RegistroAsistenciaBL getEmpleadoAsistencia() {
        return empleadoAsistencia;
    }

    public void setEmpleadoAsistencia(RegistroAsistenciaBL empleadoAsistencia) {
        this.empleadoAsistencia = empleadoAsistencia;
    }

    public List<Date> getDiasDeAsueto() {
        return diasDeAsueto;
    }

    public void setDiasDeAsueto(List<Date> diasDeAsueto) {
        this.diasDeAsueto = diasDeAsueto;
    }

    public List<Date> getDiasDeVacaciones() {
        return diasDeVacaciones;
    }

    public void setDiasDeVacaciones(List<Date> diasDeVacaciones) {
        this.diasDeVacaciones = diasDeVacaciones;
    }
    //</editor-fold>
}
