package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_registro")
@NamedQueries({
    @NamedQuery(name = "DetRegistro.findAll", query = "SELECT d FROM DetRegistro d"),
    @NamedQuery(name = "DetRegistro.findByIdEmpDesc", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado de INNER JOIN d.idEstatus cer WHERE de.idEmpleado = :idEmp"),
    @NamedQuery(name = "DetRegistro.findByIdEmp", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE e.idEmpleado = :idEmp"),
    @NamedQuery(name = "DetRegistro.findByYear", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE d.fechaEntrada LIKE :fechaEntrada"),
    @NamedQuery(name = "DetRegistro.findByNomina", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE d.fechaEntrada BETWEEN :fechaEntrada AND :fechaSalida"),
    @NamedQuery(name = "DetRegistro.findByIdEmpleadoPeriodo", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE d.idEmpleado.idEmpleado = :idEmpleado AND d.fechaEntrada BETWEEN :fechaEntrada AND :fechaSalida"),
    @NamedQuery(name = "DetRegistro.findByEmpleadoPeriodo", query = "SELECT d FROM DetRegistro d WHERE d.idEmpleado.idEmpleado = :idEmpleado AND d.fechaEntrada BETWEEN :fechaEntrada AND :fechaSalida"),
    @NamedQuery(name = "DetRegistro.findByIdEmplActivo", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE e.idEmpleado = :idEmp AND e.activo = 1 AND d.fechaEntrada LIKE :fechaEntrada"),
    @NamedQuery(name = "DetRegistro.findByIdEmpleadoAndFecha", query = "SELECT r FROM DetRegistro r WHERE r.idEmpleado.idEmpleado = :idEmpleado AND (r.fechaEntrada BETWEEN :fechaEntradaInicio AND :fechaEntradaFin)"),
    @NamedQuery(name = "DetRegistro.findToday", query = "SELECT d FROM DetRegistro d INNER JOIN d.idEmpleado e INNER JOIN d.idEstatus ce WHERE e.idEmpleado = :idEmp AND e.activo = 1 AND d.fechaEntrada >= :today ORDER BY d.fechaEntrada"),
    @NamedQuery(name = "DetRegistro.findByPlantaPeriodo", query = "SELECT r FROM DetRegistro r WHERE (r.idEmpleado.datoEmpresa.planta.idPlanta = :idPlanta OR :idPlanta IS NULL) AND r.fechaEntrada BETWEEN :fechaInicio AND :fechaFin ORDER BY r.idEmpleado.nombre, r.idEmpleado.primerAp, r.idEmpleado.segundoAp")
})
public class DetRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_registro")
    private Integer idRegistro;

    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrada;

    @Column(name = "fecha_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;

    @JoinColumn(name = "id_estatus", referencedColumnName = "id_estatus")
    @ManyToOne
    private CatEstatusRegistro idEstatus;

    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DetEmpleado idEmpleado;

    public DetRegistro() {
    }

    public DetRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public DetRegistro(Integer idRegistro, Date fechaEntrada, Date fechaSalida, Integer idEstatus, String descripcionEstatus) {
        this.idRegistro = idRegistro;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.idEstatus = new CatEstatusRegistro(idEstatus, descripcionEstatus, (short) 0);
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public CatEstatusRegistro getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(CatEstatusRegistro idEstatus) {
        this.idEstatus = idEstatus;
    }

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

}
