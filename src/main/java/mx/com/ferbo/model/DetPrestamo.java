package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_prestamo")
public class DetPrestamo implements Serializable{
	
	private static final long serialVersionUID = -5791927611476079717L;

	@Id
	@Basic(optional = false)
	@Column(name = "id_prestamo")
	private Integer idPrestamo;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
	private DetEmpleado empleado;
	
	@Basic(optional = false)
	@Column(name = "fh_inicio")
	private Date fechaInicio;
	
	@Basic(optional = false)
	@Column(name = "fh_fin")
	private Date fechaFin;
	
	@Basic(optional = false)
	@Column(name = "nu_importe")
	private BigDecimal importe;
	
	@ManyToOne
	@JoinColumn(name = "periodicidad", referencedColumnName = "periodicidad")
	private CatPeriodicidadPago periodicidadPago;
	
	@ManyToOne
	@JoinColumn(name = "tp_prestamo", referencedColumnName = "tp_prestamo")
	private CatTipoPrestamo tipoPrestamo;

	public Integer getIdPrestamo() {
		return idPrestamo;
	}

	public void setIdPrestamo(Integer idPrestamo) {
		this.idPrestamo = idPrestamo;
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
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

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public CatPeriodicidadPago getPeriodicidadPago() {
		return periodicidadPago;
	}

	public void setPeriodicidadPago(CatPeriodicidadPago periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}

	public CatTipoPrestamo getTipoPrestamo() {
		return tipoPrestamo;
	}

	public void setTipoPrestamo(CatTipoPrestamo tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}

	@Override
	public String toString() {
		return "DetPrestamo [idPrestamo=" + idPrestamo + ", empleado=" + empleado + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + ", importe=" + importe + ", periodicidadPago=" + periodicidadPago
				+ ", tipoPrestamo=" + tipoPrestamo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idPrestamo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetPrestamo other = (DetPrestamo) obj;
		return Objects.equals(idPrestamo, other.idPrestamo);
	}
}
