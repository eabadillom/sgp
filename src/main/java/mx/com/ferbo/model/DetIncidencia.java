package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_incidencia")
@NamedQueries({
    @NamedQuery(name = "DetIncidencia.findAll", query = "SELECT d FROM DetIncidencia d JOIN d.idEmpleado e JOIN d.idTipo ct JOIN d.idEstatus ce LEFT JOIN d.idSolPermiso sp LEFT JOIN d.idSolArticulo sa LEFT JOIN d.idSolPrenda spr LEFT JOIN sp.idTipoSolicitud tp LEFT JOIN sa.idArticulo a LEFT JOIN spr.idPrenda p LEFT JOIN spr.idTalla t ORDER BY d.fechaCap"),
    @NamedQuery(name = "DetIncidencia.findByIdEmpleado", query = "SELECT d FROM DetIncidencia d JOIN d.idEmpleado e JOIN d.idTipo ct JOIN d.idEstatus ce LEFT JOIN d.idSolPermiso sp JOIN sp.idTipoSolicitud tp WHERE e.idEmpleado = :idEmpleado AND ce.idEstatus = 2")
})
public class DetIncidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_incidencia")
    private Integer idIncidencia;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne()
    private DetEmpleado idEmpleado;
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    @ManyToOne()
    private DetEmpleado idEmpleadoRev;
    @Column(name = "visible")
    private Short visible;
    @JoinColumn(name = "id_estatus", referencedColumnName = "id_estatus")
    @ManyToOne()
    private CatEstatusIncidencia idEstatus;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne()
    private CatTipoIncidencia idTipo;
    @JoinColumn(name = "id_sol_articulo", referencedColumnName = "id_solicitud")
    @ManyToOne()
    private DetSolicitudArticulo idSolArticulo;
    @JoinColumn(name = "id_sol_permiso", referencedColumnName = "id_solicitud")
    @ManyToOne()
    private DetSolicitudPermiso idSolPermiso;
    @JoinColumn(name = "id_sol_prenda", referencedColumnName = "id_solicitud")
    @ManyToOne()
    private DetSolicitudPrenda idSolPrenda;
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

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public DetEmpleado getIdEmpleadoRev() {
        return idEmpleadoRev;
    }

    public void setIdEmpleadoRev(DetEmpleado idEmpleadoRev) {
        this.idEmpleadoRev = idEmpleadoRev;
    }

    public Short getVisible() {
        return visible;
    }

    public void setVisible(Short visible) {
        this.visible = visible;
    }

    public CatEstatusIncidencia getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(CatEstatusIncidencia idEstatus) {
        this.idEstatus = idEstatus;
    }

    public CatTipoIncidencia getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(CatTipoIncidencia idTipo) {
        this.idTipo = idTipo;
    }

    public DetSolicitudArticulo getIdSolArticulo() {
        return idSolArticulo;
    }

    public void setIdSolArticulo(DetSolicitudArticulo idSolArticulo) {
        this.idSolArticulo = idSolArticulo;
    }

    public DetSolicitudPermiso getIdSolPermiso() {
        return idSolPermiso;
    }

    public void setIdSolPermiso(DetSolicitudPermiso idSolPermiso) {
        this.idSolPermiso = idSolPermiso;
    }

    public DetSolicitudPrenda getIdSolPrenda() {
        return idSolPrenda;
    }

    public void setIdSolPrenda(DetSolicitudPrenda idSolPrenda) {
        this.idSolPrenda = idSolPrenda;
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
    
}
