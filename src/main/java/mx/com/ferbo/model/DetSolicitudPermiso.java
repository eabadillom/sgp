package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @NamedQuery(name = "DetSolicitudPermiso.findByIdEmp", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.empleadoSol de INNER JOIN dsp.tipoSolicitud cts WHERE de.idEmpleado = :idEmp ORDER BY dsp.fechaCap"),
    @NamedQuery(name = "DetSolicitudPermiso.findByClave", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.empleadoSol de INNER JOIN dsp.tipoSolicitud cts WHERE de.idEmpleado = :idEmp AND cts.clave = :clave"),
    @NamedQuery(name = "DetSolicitudPermiso.findByTipoSolicitud", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.empleadoSol de INNER JOIN dsp.tipoSolicitud cts WHERE de.idEmpleado = :idEmp AND (cts.clave = :clavePermiso OR cts.clave = :claveVacaciones)"),
    @NamedQuery(name = "DetSolicitudPermiso.findByCriterios", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.empleadoSol de INNER JOIN dsp.tipoSolicitud ts INNER JOIN dsp.estatus es WHERE de.idEmpleado = :idEmp AND (:fechaInicio BETWEEN dsp.fechaInicio AND dsp.fechaFin OR :fechaFin BETWEEN dsp.fechaInicio AND dsp.fechaFin OR dsp.fechaInicio BETWEEN :fechaInicio AND :fechaFin OR dsp.fechaFin BETWEEN :fechaInicio AND :fechaFin) AND (ts.clave = :clave1 OR ts.clave = :clave2) AND (es.clave = :enviada OR es.clave = :aprobada)"),
    @NamedQuery(name = "DetSolicitudPermiso.findByPeriodo", query = "SELECT dsp FROM DetSolicitudPermiso dsp INNER JOIN dsp.empleadoSol de INNER JOIN dsp.estatus es WHERE de.idEmpleado = :idEmp AND ((:fechaInicio = dsp.fechaInicio AND :fechaFin = dsp.fechaFin) OR ((:fechaInicio = dsp.fechaFin) OR (:fechaFin = dsp.fechaInicio)) OR ((:fechaInicio BETWEEN dsp.fechaInicio AND dsp.fechaFin) OR (:fechaFin BETWEEN dsp.fechaInicio AND dsp.fechaFin)) OR ((dsp.fechaInicio BETWEEN :fechaInicio AND :fechaFin) OR (dsp.fechaFin BETWEEN :fechaInicio AND :fechaFin))) AND (es.clave = :enviada OR es.clave = :aprobada)")
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
    
    @JoinColumn(name = "aprobada", referencedColumnName = "cd_st_solicitud")
    @ManyToOne()
    private CatEstatusSolicitud estatus;
    
    @Column(name = "descripcion_rechazo")
    private String descripcionRechazo;
    
    @OneToMany(mappedBy = "solPermiso")
    private List<DetIncidencia> detIncidenciaList;
    
    @JoinColumn(name = "id_tipo_solicitud", referencedColumnName = "id_tipo_solicitud")
    @ManyToOne(optional = false)
    private CatTipoSolicitud tipoSolicitud;
    
    @JoinColumn(name = "id_empleado_sol", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado empleadoSol;
    
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado empleadoRev;
    
    @Basic(optional = true)
    @Column(name = "pc_goce_sueldo")
    private BigDecimal goceSueldo;

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

    public CatEstatusSolicitud getEstatus() {
        return estatus;
    }

    public void setEstatus(CatEstatusSolicitud estatus) {
        this.estatus = estatus;
    }
    
    public List<DetIncidencia> getDetIncidenciaList() {
        return detIncidenciaList;
    }

    public void setDetIncidenciaList(List<DetIncidencia> detIncidenciaList) {
        this.detIncidenciaList = detIncidenciaList;
    }

    public CatTipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(CatTipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
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

    public BigDecimal getGoceSueldo() {
        return goceSueldo;
    }

    public void setGoceSueldo(BigDecimal goceSueldo) {
        this.goceSueldo = goceSueldo;
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
        return "DetSolicitudPermiso[" + "idSolicitud=" + idSolicitud + ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", aprobada=" + estatus.getClave() + ", descripcionRechazo=" + descripcionRechazo + ']';
    }

}
