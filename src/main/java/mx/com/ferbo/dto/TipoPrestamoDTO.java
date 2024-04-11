package mx.com.ferbo.dto;

import java.util.Objects;

public class TipoPrestamoDTO {
	
	private String tipoPrestamo;
	private String descripcion;
	
	public String getTipoPrestamo() {
		return tipoPrestamo;
	}
	public void setTipoPrestamo(String tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public String toString() {
		return "TipoPrestamoDTO [tipoPrestamo=" + tipoPrestamo + ", descripcion=" + descripcion + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(descripcion, tipoPrestamo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoPrestamoDTO other = (TipoPrestamoDTO) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(tipoPrestamo, other.tipoPrestamo);
	}
}
