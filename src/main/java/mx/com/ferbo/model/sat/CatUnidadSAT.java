package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_unidad_sat")
public class CatUnidadSAT implements Serializable {

	private static final long serialVersionUID = 2052232807712229268L;
	
	@Id
	@Column(name = "cd_unidad")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_unidad")
	@Basic(optional = false)
	@Size(max = 200)
	private String nombre;
	
	@Column(name = "nb_descripcion")
	@Basic(optional = true)
	@Size(max = 1000)
	private String descripcion;
	
	@Column(name = "nb_nota")
	@Basic(optional = true)
	@Size(max = 500)
	private String nota;
	
	@Column(name = "fh_vigencia_ini")
	@Basic(optional = false)
	private Date vigenciaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Basic(optional = true)
	private Date vigenciaFin;
	
	@Column(name = "nb_simbolo")
	@Basic(optional = true)
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
		CatUnidadSAT other = (CatUnidadSAT) obj;
		return Objects.equals(clave, other.clave);
	}

	@Override
	public String toString() {
		return "CatUnidadSAT [clave=" + clave + ", nombre=" + nombre + ", descripcion=" + descripcion + ", nota=" + nota
				+ ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + ", simbolo=" + simbolo + "]";
	}
	
}
