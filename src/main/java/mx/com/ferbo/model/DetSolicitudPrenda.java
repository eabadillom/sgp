package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_solicitud_prenda")
@NamedQuery(name = "DetSolicitudPrenda.findAll", query = "SELECT d FROM DetSolicitudPrenda d")
@NamedQuery(name = "DetSolicitudPrenda.findPrendasIdEmpleado", query = "SELECT dsp FROM DetSolicitudPrenda dsp INNER JOIN dsp.empleadoSol e INNER JOIN dsp.prenda p INNER JOIN dsp.talla t WHERE e.idEmpleado = :numEmpl")
public class DetSolicitudPrenda implements Serializable {

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
    
    @OneToMany(mappedBy = "solPrenda")
    private List<DetIncidencia> detIncidenciaList;
    
    @JoinColumn(name = "id_prenda", referencedColumnName = "id_prenda")
    @ManyToOne(optional = false)
    private CatPrenda prenda;
    
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne()
    private DetEmpleado empleadoRev;
    
    @JoinColumn(name = "id_empleado_sol", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado empleadoSol;
    
    @JoinColumn(name = "id_talla", referencedColumnName = "id_talla")
    @ManyToOne(optional = false)
    private CatTalla talla;
    
    @Column(name = "descripcion_rechazo")
    private String descripcionRechazo;

    public DetSolicitudPrenda() {
    }

    public DetSolicitudPrenda(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public DetSolicitudPrenda(Integer idSolicitud, Integer cantidad, Date fechaCap) {
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
    
    public CatPrenda getPrenda() {
        return prenda;
    }

    public void setPrenda(CatPrenda prenda) {
        this.prenda = prenda;
    }

    public DetEmpleado getEmpleadoRev() {
        return empleadoRev;
    }

    public void setEmpleadoRev(DetEmpleado empleadoRev) {
        this.empleadoRev = empleadoRev;
    }

    public DetEmpleado getEmpleadoSol() {
        return empleadoSol;
    }

    public void setEmpleadoSol(DetEmpleado empleadoSol) {
        this.empleadoSol = empleadoSol;
    }

    public CatTalla getTalla() {
        return talla;
    }

    public void setTalla(CatTalla talla) {
        this.talla = talla;
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
        hash = 23 * hash + Objects.hashCode(this.idSolicitud);
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
        final DetSolicitudPrenda other = (DetSolicitudPrenda) obj;
        return Objects.equals(this.idSolicitud, other.idSolicitud);
    }

    @Override
    public String toString() {
        return "DetSolicitudPrenda[" + "idSolicitud=" + idSolicitud + ", cantidad=" + cantidad + ", aprobada=" + estatus.getClave() + ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ", descripcionRechazo=" + descripcionRechazo + ']';
    }
    
}
