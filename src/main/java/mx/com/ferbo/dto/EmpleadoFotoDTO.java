package mx.com.ferbo.dto;

import java.util.Objects;

public class EmpleadoFotoDTO {
	
	private Integer id = null;
	private String fotografia = null;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFotografia() {
		return fotografia;
	}
	public void setFotografia(String fotografia) {
		this.fotografia = fotografia;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"fotografia\":\"" + fotografia + "\"}";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpleadoFotoDTO other = (EmpleadoFotoDTO) obj;
		return Objects.equals(id, other.id);
	}
}
