package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

public class DetSolicitudArticuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSolicitud;
    private int cantidad;
    private Short aprobada;
    private Date fechaCap;
    private Date fechaMod;
    private CatArticuloDTO articulo;
    private DetEmpleadoDTO empleadoSol;
    private DetEmpleadoDTO empleadoRev;
    private String descripcionRechazo;

    public DetSolicitudArticuloDTO() {
        articulo = new CatArticuloDTO();
    }
    
    public DetSolicitudArticuloDTO(Integer idSolicitud, Integer articulo, String descripcionA, String descripcionT, String unidad, Integer cantidadMax, int cantidad, Short aprobada, Date fechaCap, Date fechaMod,
             Integer empleadoSol) {
        super();
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.articulo = new CatArticuloDTO(articulo, descripcionA, cantidadMax, unidad, null, (short)1);
        this.empleadoSol = new DetEmpleadoDTO(empleadoSol);
    }
    
    /**
     * Constructor para Det Incidencias
     * @param idSolicitud
     * @param articulo
     * @param descripcionA
     * @param unidad
     * @param cantidadMax
     * @param cantidad
     * @param aprobada
     * @param fechaCap
     * @param fechaMod
     * @param empleadoSol 
     */
    public DetSolicitudArticuloDTO(Integer idSolicitud, Integer articulo, String descripcionA, String unidad, Integer cantidadMax, int cantidad, Short aprobada, Date fechaCap, Date fechaMod,
             Integer empleadoSol) {
        super();
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.articulo = new CatArticuloDTO(articulo, descripcionA, cantidadMax, unidad, (short)1);
        this.empleadoSol = new DetEmpleadoDTO(empleadoSol);
    }
    
    public DetSolicitudArticuloDTO(Integer idSolicitud, Integer articulo, String descripcionA, String unidad, Integer cantidadMax, int cantidad, Short aprobada, Date fechaCap, Date fechaMod,
             Integer empleadoSol, String descripcionRechazo) {
        super();
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.articulo = new CatArticuloDTO(articulo, descripcionA, cantidadMax, unidad, (short)1);
        this.empleadoSol = new DetEmpleadoDTO(empleadoSol);
        this.descripcionRechazo = descripcionRechazo;
    }

    public DetSolicitudArticuloDTO(Integer idSolicitud, Integer articulo, String descripcionA, int cantidad, Short aprobada, Date fechaCap, Date fechaMod,
             Integer empleadoSol) {
        super();
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.articulo = new CatArticuloDTO(articulo, descripcionA, (short)1);
        this.empleadoSol = new DetEmpleadoDTO(empleadoSol);
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Short getAprobada() {
        return aprobada;
    }

    public void setAprobada(Short aprobada) {
        this.aprobada = aprobada;
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

    public CatArticuloDTO getArticulo() {
        return articulo;
    }

    public void setArticulo(CatArticuloDTO articulo) {
        this.articulo = articulo;
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

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }

}
