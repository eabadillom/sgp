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
@Table(name = "cat_tipo_contrato")
@NamedQueries({
	@NamedQuery(name = "CatTipoContrato.findAll", query = "SELECT t FROM CatTipoContrato t ORDER BY t.clave ASC")
})
public class CatTipoContrato implements Serializable {

	private static final long serialVersionUID = 8215351760254626710L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_contrato")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_contrato")
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
		CatTipoContrato other = (CatTipoContrato) obj;
		return Objects.equals(clave, other.clave);
	}
}
