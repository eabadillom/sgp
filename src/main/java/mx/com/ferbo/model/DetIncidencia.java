package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_incidencia")
@NamedQuery(name = "DetIncidencia.findAll", query = "SELECT d FROM DetIncidencia d JOIN d.empleado e JOIN d.tipoIncidencia ct JOIN d.estatusIncidencia ce LEFT JOIN d.solPermiso sp LEFT JOIN d.solArticulo sa LEFT JOIN d.solPrenda spr LEFT JOIN sp.tipoSolicitud tp LEFT JOIN sa.articulo a LEFT JOIN spr.prenda p LEFT JOIN spr.talla t ORDER BY d.fechaCap")
@NamedQuery(name = "DetIncidencia.findByIdEmpleado", query = "SELECT d FROM DetIncidencia d JOIN d.empleado e WHERE e.idEmpleado = :idEmpleado")
@NamedQuery(name = "DetIncidencia.findByArticulo", query = "SELECT d FROM DetIncidencia d INNER JOIN d.empleado e INNER JOIN d.solArticulo sa WHERE e.idEmpleado = :idEmpleado AND sa.idSolicitud = :idSolicitud")
@NamedQuery(name = "DetIncidencia.findByPermiso", query = "SELECT d FROM DetIncidencia d INNER JOIN d.empleado e INNER JOIN d.solPermiso sp WHERE e.idEmpleado = :idEmpleado AND sp.idSolicitud = :idSolicitud")
@NamedQuery(name = "DetIncidencia.findByPrenda", query = "SELECT d FROM DetIncidencia d INNER JOIN d.empleado e INNER JOIN d.solPrenda sp WHERE e.idEmpleado = :idEmpleado AND sp.idSolicitud = :idSolicitud")
@NamedQuery(name = "DetIncidencia.findByIdEmpleadoPrenda", query = "SELECT d FROM DetIncidencia d JOIN d.empleado e JOIN d.tipoIncidencia ct JOIN d.estatusIncidencia ce INNER JOIN d.solPrenda sp WHERE e.idEmpleado = :idEmpleado AND ct.clave = 'PR'")
@NamedQuery(name = "DetIncidencia.findByIdEmpleadoArticulo", query = "SELECT d FROM DetIncidencia d JOIN d.empleado e JOIN d.tipoIncidencia ct JOIN d.estatusIncidencia ce INNER JOIN d.solArticulo sp WHERE e.idEmpleado = :idEmpleado AND ct.clave = 'A'")
@NamedQuery(name = "DetIncidencia.findByIdEmpleadoPermiso", query = "SELECT d FROM DetIncidencia d JOIN d.empleado e JOIN d.tipoIncidencia ct JOIN d.estatusIncidencia ce INNER JOIN d.solPermiso sp WHERE e.idEmpleado = :idEmpleado AND (ct.clave = 'PE' OR ct.clave = 'V')")
@NamedQuery(name = "DetIncidencia.findPermisoByPeriodo", query = "SELECT i FROM DetIncidencia i INNER JOIN i.solPermiso p WHERE i.tipoIncidencia.clave IN ('PE', 'V') AND p.fechaFin >= :periodoInicio AND p.fechaInicio <= :periodoFin ORDER BY p.fechaCap ")
@NamedQuery(name = "DetIncidencia.findPermisoByEmpleadoPeriodo", query = "SELECT i FROM DetIncidencia i INNER JOIN i.solPermiso p WHERE i.empleado.idEmpleado = :idEmpleado AND i.tipoIncidencia.clave IN ('PE', 'V') AND p.fechaFin >= :periodoInicio AND p.fechaInicio <= :periodoFin ORDER BY p.fechaCap ")
public class DetIncidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_incidencia")
    private Integer idIncidencia;
    
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne()
    private DetEmpleado empleado;
    
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne()
    @Null
    private DetEmpleado empleadoRev;
    
    @Column(name = "visible")
    private Short visible;
    
    @JoinColumn(name = "id_estatus", referencedColumnName = "id_estatus")
    @ManyToOne()
    private CatEstatusIncidencia estatusIncidencia;
    
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne()
    private CatTipoIncidencia tipoIncidencia;
    
    @OneToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_sol_articulo", referencedColumnName = "id_solicitud")
    private DetSolicitudArticulo solArticulo;
    
    @OneToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_sol_permiso", referencedColumnName = "id_solicitud")
    private DetSolicitudPermiso solPermiso;
    
    @OneToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_sol_prenda", referencedColumnName = "id_solicitud")
    private DetSolicitudPrenda solPrenda;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cap")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCap;
    
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;

    public DetIncidencia() {
    }

    public DetIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public DetEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) {
        this.empleado = empleado;
    }

    public DetEmpleado getEmpleadoRev() {
        return empleadoRev;
    }

    public void setEmpleadoRev(DetEmpleado empleadoRev) {
        this.empleadoRev = empleadoRev;
    }

    public Short getVisible() {
        return visible;
    }

    public void setVisible(Short visible) {
        this.visible = visible;
    }

    public CatEstatusIncidencia getEstatusIncidencia() {
        return estatusIncidencia;
    }

    public void setEstatusIncidencia(CatEstatusIncidencia estatusIncidencia) {
        this.estatusIncidencia = estatusIncidencia;
    }

    public CatTipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(CatTipoIncidencia tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public DetSolicitudArticulo getSolArticulo() {
        return solArticulo;
    }

    public void setSolArticulo(DetSolicitudArticulo solArticulo) {
        this.solArticulo = solArticulo;
    }

    public DetSolicitudPermiso getSolPermiso() {
        return solPermiso;
    }

    public void setSolPermiso(DetSolicitudPermiso solPermiso) {
        this.solPermiso = solPermiso;
    }

    public DetSolicitudPrenda getSolPrenda() {
        return solPrenda;
    }

    public void setSolPrenda(DetSolicitudPrenda solPrenda) {
        this.solPrenda = solPrenda;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idIncidencia);
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
        final DetIncidencia other = (DetIncidencia) obj;
        return Objects.equals(this.idIncidencia, other.idIncidencia);
    }

    @Override
    public String toString() {
        return "DetIncidencia[" + "idIncidencia=" + idIncidencia + ", idEmpleado=" + empleado.getIdEmpleado() + 
                ", idEmpleadoRev=" + ((empleadoRev != null) ? empleadoRev.getIdEmpleado() : "null") + ", visible=" + visible + 
                ", idEstatus=" + estatusIncidencia.getClave() + ", idTipo=" + tipoIncidencia.getClave() + 
                ", idSolArticulo=" + ((solArticulo != null) ? solArticulo.getIdSolicitud() : "null") + 
                ", idSolPermiso=" + ((solPermiso != null) ? solPermiso.getIdSolicitud() : "null") + 
                ", idSolPrenda=" + ((solPrenda != null) ? solPrenda.getIdSolicitud() : "null") + 
                ", fechaCap=" + fechaCap + ", fechaMod=" + fechaMod + ']';
    }
    
}
