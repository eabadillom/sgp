package mx.com.ferbo.dto.sat;

import java.util.Objects;

public class TipoJornadaDTO {
	private String clave;
	private String nombre;
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return "{\"clave\":\"" + clave + "\", \"nombre\":\"" + nombre + "\"}";
	}
	@Override
	public int hashCode() {
		return Objects.hash(clave);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoJornadaDTO other = (TipoJornadaDTO) obj;
		return Objects.equals(clave, other.clave);
	}
}
