package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoPercepcion;

@Entity
@Table(name = "cat_percepcion")
@NamedQueries({
	@NamedQuery(name = "CatPercepcion.buscarTodos", query = "SELECT p FROM CatPercepcion p")
})
public class CatPercepcion implements Serializable{

	private static final long serialVersionUID = 4773374723856563623L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_percepcion")
	@Size(min = 1, max = 15)
	private String clave;
	
	@Column(name = "nb_percepcion")
	@Size(min = 1, max = 150)
	@Basic(optional = false)
	private String nombre;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "cd_tipo_percepcion")
	private CatTipoPercepcion tipoPercepcion;

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
		CatPercepcion other = (CatPercepcion) obj;
		return Objects.equals(clave, other.clave);
	}

	@Override
	public String toString() {
		return "CatPercepcion [clave=" + clave + ", nombre=" + nombre + ", tipoPercepcion=" + tipoPercepcion + "]";
	}

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

	public CatTipoPercepcion getTipoPercepcion() {
		return tipoPercepcion;
	}

	public void setTipoPercepcion(CatTipoPercepcion tipoPercepcion) {
		this.tipoPercepcion = tipoPercepcion;
	}
}
