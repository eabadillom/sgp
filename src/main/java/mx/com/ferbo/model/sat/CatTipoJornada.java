package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cat_tipo_jornada")
@NamedQueries({
	@NamedQuery(name = "CatTipoJornada.findAll", query = "SELECT t FROM CatTipoJornada t ORDER BY t.clave")
})
public class CatTipoJornada implements Serializable {

	private static final long serialVersionUID = -1241778305132256548L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_jornada")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_jornada")
	private String nombre;

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

	@Override
	public String toString() {
		return "{\"clave\":\"" + clave + "\", \"nombre\":\"" + nombre + "\"}";
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
		CatTipoJornada other = (CatTipoJornada) obj;
		return Objects.equals(clave, other.clave);
	}
}
