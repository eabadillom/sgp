package mx.com.ferbo.dto;

import java.util.Objects;

import mx.com.ferbo.dto.sat.TipoDeduccionDTO;

public class TipoPrestamoDTO {
	
	private String clave;
	private String descripcion;
	private TipoDeduccionDTO tipoDeduccion;
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoDeduccionDTO getTipoDeduccion() {
		return tipoDeduccion;
	}
	public void setTipoDeduccion(TipoDeduccionDTO tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}
	@Override
	public int hashCode() {
		return Objects.hash(clave, descripcion);
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
		return Objects.equals(clave, other.clave) && Objects.equals(descripcion, other.descripcion);
	}
	@Override
	public String toString() {
		return "TipoPrestamoDTO [clave=" + clave + ", descripcion=" + descripcion + "]";
	}
}
