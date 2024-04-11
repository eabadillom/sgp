package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_regimen_fiscal")
@NamedQueries({
	@NamedQuery(name = "CatRegimenFiscal.findAll", query = "SELECT r FROM CatRegimenFiscal r ORDER BY r.nombre"),
	@NamedQuery(name = "CatRegimenFiscal.findByActivos", query = "SELECT r FROM CatRegimenFiscal r WHERE r.vigenciaInicio <= :fecha AND (r.vigenciaFin >= :fecha OR r.vigenciaFin IS NULL)")
})
public class CatRegimenFiscal implements Serializable {

	private static final long serialVersionUID = 1157055719181440892L;
	
	@Id
	@Basic(optional = false)
	@Column(name="cd_regimen")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_regimen")
	@Size(max = 255)
	private String nombre;
	
	@Basic(optional = false)
	@Column(name = "st_per_fisica")
	private Boolean personaFisica;
	
	@Basic(optional = false)
	@Column(name = "st_per_moral")
	private Boolean personaMoral;
	
	@Basic(optional = false)
	@Column(name = "fh_vigencia_ini")
	private Date vigenciaInicio;
	
	@Basic(optional = true)
	@Column(name = "fh_vigencia_fin")
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
		CatRegimenFiscal other = (CatRegimenFiscal) obj;
		return Objects.equals(clave, other.clave);
	}
	
	@Override
	public String toString() {
		return "CatRegimenFiscal [clave=" + clave + ", nombre=" + nombre + ", personaFisica=" + personaFisica
				+ ", personaMoral=" + personaMoral + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin="
				+ vigenciaFin + "]";
	}
}
