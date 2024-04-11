package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PeriodicidadPagoDTO implements Serializable {
	private static final long serialVersionUID = 6369042627072519109L;
	private String periodicidad;
	private String descripcion;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	
	public PeriodicidadPagoDTO() {
	}
	
	public PeriodicidadPagoDTO(String periodicidad, String descripcion, Date vigenciaInicio,
			Date vigenciaFin) {
		super();
		this.periodicidad = periodicidad;
		this.descripcion = descripcion;
		this.vigenciaInicio = vigenciaInicio;
		this.vigenciaFin = vigenciaFin;
	}
	public String getPeriodicidad() {
		return periodicidad;
	}
	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}
	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}
	public Date getVigenciaFin() {
		return vigenciaFin;
	}
	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(periodicidad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodicidadPagoDTO other = (PeriodicidadPagoDTO) obj;
		return Objects.equals(periodicidad, other.periodicidad);
	}

	@Override
	public String toString() {
		return "PeriodicidadPagoDTO [periodicidad=" + periodicidad + ", descripcion=" + descripcion
				+ ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}
}
