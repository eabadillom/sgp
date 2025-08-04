package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_solicitud_articulo")
@NamedQuery(name = "DetSolicitudArticulo.findAll", query = "SELECT d FROM DetSolicitudArticulo d")
@NamedQuery(name = "DetSolicitudArticulo.findArticulosIdEmpleado", query = "SELECT dsa FROM DetSolicitudArticulo dsa INNER JOIN dsa.empleadoSol e INNER JOIN dsa.articulo a WHERE e.idEmpleado = :numEmpl")
public class DetSolicitudArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;
    
    @Basic(optional = false)
    @Column(name = "cantidad")
    private Integer cantidad;
    
    @JoinColumn(name = "aprobada", referencedColumnName = "cd_st_solicitud")
    @ManyToOne()
    private CatEstatusSolicitud estatus;
    
    @Basic(optional = false)
    @Column(name = "fecha_cap")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCap;
    
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    
    @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo")
    @ManyToOne(optional = false)
    private CatArticulo articulo;
    
    @JoinColumn(name = "id_empleado_sol", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado empleadoSol;
    
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado empleadoRev;
    
    @Column(name = "descripcion_rechazo")
    private String descripcionRechazo;

    public DetSolicitudArticulo() {
    }

    public DetSolicitudArticulo(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public DetSolicitudArticulo(Integer idSolicitud, Integer cantidad, Date fechaCap) {
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
        this.fechaCap = fechaCap;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public CatEstatusSolicitud getEstatus() {
        return estatus;
    }

    public void setEstatus(CatEstatusSolicitud estatus) {
        this.estatus = estatus;
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

    public CatArticulo getArticulo() {
        return articulo;
    }

    public void setArticulo(CatArticulo articulo) {
        this.articulo = articulo;
    }

    public DetEmpleado getEmpleadoSol() {
        return empleadoSol;
    }

    public void setEmpleadoSol(DetEmpleado empleadoSol) {
        this.empleadoSol = empleadoSol;
    }

    public DetEmpleado getEmpleadoRev() {
        return empleadoRev;
    }

    public void setEmpleadoRev(DetEmpleado empleadoRev) {
        this.empleadoRev = empleadoRev;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idSolicitud);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetSolicitudArticulo other = (DetSolicitudArticulo) obj;
        return Objects.equals(this.idSolicitud, other.idSolicitud);
    }

    @Override
    public String toString() {
        return "DetSolicitudArticulo[" + "idSolicitud=" + idSolicitud + ", cantidad=" + cantidad + ", aprobada=" + estatus.getClave() + ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ", descripcionRechazo=" + descripcionRechazo + ']';
    }
    
}
