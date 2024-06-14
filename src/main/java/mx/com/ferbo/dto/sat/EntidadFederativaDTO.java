package mx.com.ferbo.dto.sat;

import java.util.Date;
import java.util.Objects;

public class EntidadFederativaDTO {
	private String clave;
	private String pais;
	private String nombre;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
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
		EntidadFederativaDTO other = (EntidadFederativaDTO) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "EntidadFederativaDTO [clave=" + clave + ", pais=" + pais + ", nombre=" + nombre + ", vigenciaInicio="
				+ vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}
}
