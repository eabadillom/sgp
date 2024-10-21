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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_solicitud_permiso")
@NamedQueries({
    @NamedQuery(name = "DetSolicitudPermiso.findAll", query = "SELECT d FROM DetSolicitudPermiso d"),
    @NamedQuery(name = "DetSolicitudPermiso.findByIdEmp", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.idEmpleadoSol de INNER JOIN dsp.idTipoSolicitud cts WHERE de.idEmpleado = :idEmp ORDER BY dsp.fechaCap")
})
public class DetSolicitudPermiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cap")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCap;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "aprobada")
    private Short aprobada;
    @Column(name = "descripcion_rechazo")
    private String descripcionRechazo;
    @OneToMany(mappedBy = "idSolPermiso")
    private List<DetIncidencia> detIncidenciaList;
    @JoinColumn(name = "id_tipo_solicitud", referencedColumnName = "id_tipo_solicitud")
    @ManyToOne(optional = false)
    private CatTipoSolicitud idTipoSolicitud;
    @JoinColumn(name = "id_empleado_sol", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado idEmpleadoSol;
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado idEmpleadoRev;

    public DetSolicitudPermiso() {
    }

    public DetSolicitudPermiso(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public DetSolicitudPermiso(Integer idSolicitud, Date fechaCap, Date fechaInicio, Date fechaFin) {
        this.idSolicitud = idSolicitud;
        this.fechaCap = fechaCap;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public List<DetIncidencia> getDetIncidenciaList() {
        return detIncidenciaList;
    }

    public void setDetIncidenciaList(List<DetIncidencia> detIncidenciaList) {
        this.detIncidenciaList = detIncidenciaList;
    }

    public CatTipoSolicitud getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(CatTipoSolicitud idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
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
        hash = 89 * hash + Objects.hashCode(this.idSolicitud);
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
        final DetSolicitudPermiso other = (DetSolicitudPermiso) obj;
        return Objects.equals(this.idSolicitud, other.idSolicitud);
    }

    @Override
    public String toString() {
        return "DetSolicitudPermiso{" + "idSolicitud=" + idSolicitud + ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", aprobada=" + aprobada + ", descripcionRechazo=" + descripcionRechazo + ", detIncidenciaList=" + detIncidenciaList + ", idTipoSolicitud=" + idTipoSolicitud + ", idEmpleadoSol=" + idEmpleadoSol + ", idEmpleadoRev=" + idEmpleadoRev + '}';
    }
    
}