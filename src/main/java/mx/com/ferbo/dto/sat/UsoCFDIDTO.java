package mx.com.ferbo.dto.sat;

import java.util.Date;
import java.util.Objects;

public class UsoCFDIDTO {
	
	private String clave;
	private String nombre;
	private Boolean aplicaPersonaFisica;
	private Boolean aplicaPersonaMoral;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	
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
	public Boolean getAplicaPersonaFisica() {
		return aplicaPersonaFisica;
	}
	public void setAplicaPersonaFisica(Boolean aplicaPersonaFisica) {
		this.aplicaPersonaFisica = aplicaPersonaFisica;
	}
	public Boolean getAplicaPersonaMoral() {
		return aplicaPersonaMoral;
	}
	public void setAplicaPersonaMoral(Boolean aplicaPersonaMoral) {
		this.aplicaPersonaMoral = aplicaPersonaMoral;
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
		UsoCFDIDTO other = (UsoCFDIDTO) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "UsoCFDIDTO [clave=" + clave + ", nombre=" + nombre + ", aplicaPersonaFisica=" + aplicaPersonaFisica
				+ ", aplicaPersonaMoral=" + aplicaPersonaMoral + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin="
				+ vigenciaFin + "]";
	}
}
