package mx.com.ferbo.dto.sat;

import java.util.Date;
import java.util.Objects;

public class UnidadSATDTO {
	private String clave;
	private String nombre;
	private String descripcion;
	private String nota;
	private Date vigenciaInicio;
	private Date vigenciaFin;
	private String simbolo;
	
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
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
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
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
		UnidadSATDTO other = (UnidadSATDTO) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "UnidadSATDTO [clave=" + clave + ", nombre=" + nombre + ", descripcion=" + descripcion + ", nota=" + nota
				+ ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + ", simbolo=" + simbolo + "]";
	}
}
