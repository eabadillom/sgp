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
@Table(name = "cat_concepto")
public class CatConcepto implements Serializable {

	private static final long serialVersionUID = -5127721911157982659L;
	
	@Id
	@Column(name = "cd_concepto")
	@Basic(optional = false)
	@Size(max = 10)
	private String clave;
	
	@Column(name = "nb_concepto")
	@Basic(optional = false)
	@Size(max = 200)
	private String nombre;
	
	@Column(name = "fh_vigencia_ini")
	@Basic(optional = false)
	private Date vigenciaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Basic(optional = true)
	private Date vigenciaFin;
	
	@Column(name = "nb_pal_similares")
	@Basic(optional = true)
	@Size(max = 1000)
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
		CatConcepto other = (CatConcepto) obj;
		return Objects.equals(clave, other.clave);
	}

	@Override
	public String toString() {
		return "CatConcepto [clave=" + clave + ", nombre=" + nombre + ", vigenciaInicio=" + vigenciaInicio
				+ ", vigenciaFin=" + vigenciaFin + ", palabraSimilar=" + palabraSimilar + "]";
	}
}
