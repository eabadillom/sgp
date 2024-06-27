package mx.com.ferbo.dto.sat;

import java.util.Date;
import java.util.Objects;

public class ConceptoDTO {
	private String clave;
	private String nombre;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	private String palabraSimilar;
	
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
	public String getPalabraSimilar() {
		return palabraSimilar;
	}
	public void setPalabraSimilar(String palabraSimilar) {
		this.palabraSimilar = palabraSimilar;
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
		ConceptoDTO other = (ConceptoDTO) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "ConceptoDTO [clave=" + clave + ", nombre=" + nombre + ", vigenciaInicio=" + vigenciaInicio
				+ ", vigenciaFin=" + vigenciaFin + ", palabraSimilar=" + palabraSimilar + "]";
	}
}
