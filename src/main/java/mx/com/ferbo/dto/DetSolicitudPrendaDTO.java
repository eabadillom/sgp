package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DetSolicitudPrendaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSolicitud;
    private CatPrendaDTO prenda;
    private int cantidad;
    private Short aprobada;
    private Date fechaCap;
    private Date fechaMod;
    private Integer idEmpleadoSol;
    private Integer idEmpleadoRev;
    private CatTallaDTO talla;
    private DetEmpleadoDTO empleadoSol;
    private DetEmpleadoDTO empleadoRev;
    private String descripcionRechazo;
    

    public DetSolicitudPrendaDTO() {
        talla = new CatTallaDTO();
        prenda = new CatPrendaDTO();
    }
    /**
     * Constructor para Det Incidencias
     * @param idSolicitud
     * @param prenda
     * @param descripcionP
     * @param cantidad
     * @param aprobada
     * @param fechaCap
     * @param fechaMod
     * @param idEmpleadoSol
     * @param talla
     * @param descripcionT 
     */
    public DetSolicitudPrendaDTO(Integer idSolicitud, Integer prenda, String descripcionP, int cantidad, Short aprobada, Date fechaCap,
            Date fechaMod, Integer idEmpleadoSol, Integer talla, String descripcionT) {
        super();
        this.idSolicitud = idSolicitud;
        this.prenda = new CatPrendaDTO(prenda, descripcionP, (short)1);
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.empleadoSol = new DetEmpleadoDTO(idEmpleadoSol);
        this.talla = new CatTallaDTO(talla, descripcionT, (short)1);
    }
    
    public DetSolicitudPrendaDTO(Integer idSolicitud, Integer prenda, String descripcionP, int cantidad, Short aprobada, Date fechaCap,
            Date fechaMod, Integer idEmpleadoSol, Integer talla, String descripcionT, String descripcionRechazo) {
        super();
        this.idSolicitud = idSolicitud;
        this.prenda = new CatPrendaDTO(prenda, descripcionP, (short)1);
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.empleadoSol = new DetEmpleadoDTO(idEmpleadoSol);
        this.talla = new CatTallaDTO(talla, descripcionT, (short)1);
        this.descripcionRechazo = descripcionRechazo;
    }
    
    public DetSolicitudPrendaDTO(Integer idSolicitud, Integer prenda, String descripcionP, BigDecimal precio, int cantidad, Short aprobada, Date fechaCap,
            Date fechaMod, Integer idEmpleadoSol, Integer talla, String descripcionT) {
        super();
        this.idSolicitud = idSolicitud;
        this.prenda = new CatPrendaDTO(prenda, descripcionP, precio, (short)1);
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.empleadoSol = new DetEmpleadoDTO(idEmpleadoSol);
        this.talla = new CatTallaDTO(talla, descripcionT, (short)1);
    }

    public DetSolicitudPrendaDTO(Integer idSolicitud, Integer prenda, String descripcionP, BigDecimal precio, Integer cantidadMax, int cantidad, Short aprobada, Date fechaCap,
            Date fechaMod, Integer idEmpleadoSol, Integer talla, String descripcionT) {
        super();
        this.idSolicitud = idSolicitud;
        this.prenda = new CatPrendaDTO(prenda, descripcionP, precio, cantidadMax, (short)1, null);
        this.cantidad = cantidad;
        this.aprobada = aprobada;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.empleadoSol = new DetEmpleadoDTO(idEmpleadoSol);
        this.talla = new CatTallaDTO(talla, descripcionT, (short)1);
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public CatPrendaDTO getPrenda() {
        return prenda;
    }

    public void setPrenda(CatPrendaDTO prenda) {
        this.prenda = prenda;
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

    public Integer getIdEmpleadoSol() {
        return idEmpleadoSol;
    }

    public void setIdEmpleadoSol(Integer idEmpleadoSol) {
        this.idEmpleadoSol = idEmpleadoSol;
    }

    public Integer getIdEmpleadoRev() {
        return idEmpleadoRev;
    }

    public void setIdEmpleadoRev(Integer idEmpleadoRev) {
        this.idEmpleadoRev = idEmpleadoRev;
    }

    public CatTallaDTO getTalla() {
        return talla;
    }

    public void setTalla(CatTallaDTO talla) {
        this.talla = talla;
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
