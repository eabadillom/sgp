package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class PrestamoDTO {
	
	private Integer idPrestamo;
	private DetEmpleadoDTO empleado;
	private Date fechaInicio;
	private Date fechaFin;
	private BigDecimal importe;
	private PeriodicidadPagoDTO periodicidadPago;
	private TipoPrestamoDTO tipoPrestamo;
	
	public Integer getIdPrestamo() {
		return idPrestamo;
	}
	public void setIdPrestamo(Integer idPrestamo) {
		this.idPrestamo = idPrestamo;
	}
	public DetEmpleadoDTO getEmpleado() {
		return empleado;
	}
	public void setEmpleado(DetEmpleadoDTO empleado) {
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
	public PeriodicidadPagoDTO getPeriodicidadPago() {
		return periodicidadPago;
	}
	public void setPeriodicidadPago(PeriodicidadPagoDTO periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}
	public TipoPrestamoDTO getTipoPrestamo() {
		return tipoPrestamo;
	}
	public void setTipoPrestamo(TipoPrestamoDTO tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}
	@Override
	public String toString() {
		return "PrestamoDTO [idPrestamo=" + idPrestamo + ", empleado=" + empleado + ", fechaInicio=" + fechaInicio
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
		PrestamoDTO other = (PrestamoDTO) obj;
		return Objects.equals(idPrestamo, other.idPrestamo);
	}
}
