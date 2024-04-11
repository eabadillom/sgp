package mx.com.ferbo.dto.sat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class RegimenFiscalDTO implements Serializable {

	private static final long serialVersionUID = -7200013916718006951L;
	
	private String clave;
	private String nombre;
	private Boolean personaFisica;
	private Boolean personaMoral;
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
	public Boolean getPersonaFisica() {
		return personaFisica;
	}
	public void setPersonaFisica(Boolean personaFisica) {
		this.personaFisica = personaFisica;
	}
	public Boolean getPersonaMoral() {
		return personaMoral;
	}
	public void setPersonaMoral(Boolean personaMoral) {
		this.personaMoral = personaMoral;
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
		RegimenFiscalDTO other = (RegimenFiscalDTO) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "{\"clave\":\"" + clave + "\", \"nombre\":\"" + nombre + "\", \"personaFisica\":\"" + personaFisica
				+ "\", \"personaMoral\":\"" + personaMoral + "\", \"vigenciaInicio\":\"" + vigenciaInicio
				+ "\", \"vigenciaFin\":\"" + vigenciaFin + "\"}";
	}
}
