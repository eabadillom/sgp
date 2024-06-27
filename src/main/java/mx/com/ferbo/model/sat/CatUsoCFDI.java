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
@Table(name = "cat_uso_cfdi")
public class CatUsoCFDI implements Serializable {
	
	private static final long serialVersionUID = -3238314433334666749L;

	@Id
	@Column(name = "cd_uso_cfdi")
	@Size(max = 5)
	@Basic(optional = false)
	private String clave;
	
	@Column(name = "nb_uso_cfdi")
	@Size(max = 100)
	@Basic(optional = false)
	private String nombre;
	
	@Column(name = "st_apl_per_fisica")
	@Basic(optional = false)
	private Boolean aplicaPersonaFisica;
	
	@Column(name = "st_apl_per_moral")
	@Basic(optional = false)
	private Boolean aplicaPersonaMoral;
	
	@Column(name = "fh_vigencia_ini")
	@Basic(optional = false)
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
		CatUsoCFDI other = (CatUsoCFDI) obj;
		return Objects.equals(clave, other.clave);
	}

	@Override
	public String toString() {
		return "CatUsoCFDI [clave=" + clave + ", nombre=" + nombre + ", aplicaPersonaFisica=" + aplicaPersonaFisica
				+ ", aplicaPersonaMoral=" + aplicaPersonaMoral + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin="
				+ vigenciaFin + "]";
	}
}
