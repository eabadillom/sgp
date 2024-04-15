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
@Table(name = "cat_tipo_regimen")
@NamedQueries({
	@NamedQuery(name = "CatTipoRegimen.findAll", query = "SELECT t FROM CatTipoRegimen t ORDER BY t.clave"),
	@NamedQuery(name = "CatTipoRegimen.findActive", query = "SELECT t FROM CatTipoRegimen t WHERE t.vigenciaInicio >= :fecha AND (t.vigenciaFin IS NULL OR t.vigenciaFin <= :fecha) ORDER BY t.clave")
})
public class CatTipoRegimen implements Serializable {

	private static final long serialVersionUID = -1997145549269690152L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_tp_regimen")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_tp_regimen")
	@Size(max = 150)
	private String nombre;
	
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
		CatTipoRegimen other = (CatTipoRegimen) obj;
		return Objects.equals(clave, other.clave);
	}
}
