package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gabo
 */
public class DetSolicitudPermisoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSolicitud;
    private Date fechaCap;
    private Date fechaMod;
    private Date fechaInicio;
    private Date fechaFin;
    private Short aprobada;
    private String descripcionRechazo;
    private DetEmpleadoDTO empleadoSol;
    private DetEmpleadoDTO empleadoRev;
    private CatTipoSolicitudDTO catTipoSolicitud;

    public DetSolicitudPermisoDTO() {
        catTipoSolicitud = new CatTipoSolicitudDTO();
    }

    public DetSolicitudPermisoDTO(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
    
    /*
     * Constructor utilizado para NamedQuery
     * DetSolicitudPermiso.findByIdEmp
     */
    public DetSolicitudPermisoDTO(Integer idSolicitud, Date fechaCap, Date fechaMod, Date fechaInicio, Date fechaFin, Short aprobada,
                                    String descripcionRechazo, Integer idTipoSolicitud, String descripcionTipo) {
        this.idSolicitud = idSolicitud;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.aprobada = aprobada;
        this.descripcionRechazo = descripcionRechazo;
        this.catTipoSolicitud = new CatTipoSolicitudDTO(idTipoSolicitud, descripcionTipo);
    }
    
    public DetSolicitudPermisoDTO(Integer idSolicitud, Date fechaCap, Date fechaMod, Date fechaInicio, Date fechaFin, Short aprobada,
                                  Integer idTipoSolicitud, String descripcionTipo, Integer idEmpleado) {
        this.idSolicitud = idSolicitud;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.aprobada = aprobada;
        this.catTipoSolicitud = new CatTipoSolicitudDTO(idTipoSolicitud, descripcionTipo);
        this.empleadoSol = new DetEmpleadoDTO(idEmpleado);
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Date getFechaCap() {
        return fechaCap;
    }

    public void setFechaCap(Date fechaCap) {
        this.fechaCap = fechaCap;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
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

    public Short getAprobada() {
        return aprobada;
    }

    public void setAprobada(Short aprobada) {
        this.aprobada = aprobada;
    }

    public DetEmpleadoDTO getEmpleadoSol() {
        return empleadoSol;
    }

    public void setEmpleadoSol(DetEmpleadoDTO empleadoSol) {
        this.empleadoSol = empleadoSol;
    }

    public DetEmpleadoDTO getEmpleadoRev() {
        return empleadoRev;
    }

    public void setEmpleadoRev(DetEmpleadoDTO empleadoRev) {
        this.empleadoRev = empleadoRev;
    }

    public CatTipoSolicitudDTO getCatTipoSolicitud() {
        return catTipoSolicitud;
    }

    public void setCatTipoSolicitud(CatTipoSolicitudDTO catTipoSolicitud) {
        this.catTipoSolicitud = catTipoSolicitud;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }
}
