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
import javax.persistence.NamedQueries;
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
@Table(name = "det_solicitud_articulo")
@NamedQueries({
    @NamedQuery(name = "DetSolicitudArticulo.findAll", query = "SELECT d FROM DetSolicitudArticulo d"),
    @NamedQuery(name = "DetSolicitudArticulo.findArticulosIdEmpleado", query = "SELECT dsa FROM DetSolicitudArticulo dsa INNER JOIN dsa.idEmpleadoSol e INNER JOIN dsa.idArticulo a WHERE e.idEmpleado = :numEmpl")
})
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
    @Column(name = "aprobada")
    private Short aprobada;
    @Basic(optional = false)
    @Column(name = "fecha_cap")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCap;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @OneToMany(mappedBy = "idSolArticulo")
    private List<DetIncidencia> detIncidenciaList;
    @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo")
    @ManyToOne(optional = false)
    private CatArticulo idArticulo;
    @JoinColumn(name = "id_empleado_sol", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado idEmpleadoSol;
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado idEmpleadoRev;
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

    public List<DetIncidencia> getDetIncidenciaList() {
        return detIncidenciaList;
    }

    public void setDetIncidenciaList(List<DetIncidencia> detIncidenciaList) {
        this.detIncidenciaList = detIncidenciaList;
    }

    public CatArticulo getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(CatArticulo idArticulo) {
        this.idArticulo = idArticulo;
    }

    public DetEmpleado getIdEmpleadoSol() {
        return idEmpleadoSol;
    }

    public void setIdEmpleadoSol(DetEmpleado idEmpleadoSol) {
        this.idEmpleadoSol = idEmpleadoSol;
    }

    public DetEmpleado getIdEmpleadoRev() {
        return idEmpleadoRev;
    }

    public void setIdEmpleadoRev(DetEmpleado idEmpleadoRev) {
        this.idEmpleadoRev = idEmpleadoRev;
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
        return "DetSolicitudArticulo{" + "idSolicitud=" + idSolicitud + ", cantidad=" + cantidad + ", aprobada=" + aprobada + ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ", detIncidenciaList=" + detIncidenciaList + ", idArticulo=" + idArticulo + ", idEmpleadoSol=" + idEmpleadoSol + ", idEmpleadoRev=" + idEmpleadoRev + ", descripcionRechazo=" + descripcionRechazo + '}';
    }
    
}
