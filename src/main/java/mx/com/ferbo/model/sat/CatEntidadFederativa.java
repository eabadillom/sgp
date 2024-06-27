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
@Table(name = "cat_ent_fed")
public class CatEntidadFederativa implements Serializable {

	private static final long serialVersionUID = -7186224031916960721L;
	
	@Id
	@Column(name = "cd_estado")
	@Size(max = 5)
	private String clave;
	
	@Column(name = "cd_pais")
	@Size(max = 5)
	private String pais;
	
	@Column(name = "nb_estado")
	@Basic(optional = false)
	@Size(max = 50)
	private String nombre;
	
	@Column(name = "fh_vigencia_ini")
	@Basic(optional = true)
	private Date vigenciaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Basic(optional = true)
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
		return this.nombre;
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
		CatEntidadFederativa other = (CatEntidadFederativa) obj;
		return Objects.equals(clave, other.clave);
	}

	@Override
	public String toString() {
		return "CatEntidadFederativa [clave=" + clave + ", pais=" + pais + ", nombre=" + nombre + ", vigenciaInicio="
				+ vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}
}
