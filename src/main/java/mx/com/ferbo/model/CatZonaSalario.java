package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cat_zona_salario")
@NamedQuery(name = "ZonaSalario.buscarTodos", query = "SELECT z FROM CatZonaSalario z")
public class CatZonaSalario implements Serializable {

	private static final long serialVersionUID = -5780374919042753132L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_zona")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_zona")
	private String nombre;

	@Override
	public int hashCode() {
		if(this.clave == null)
			return System.identityHashCode(this);
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
		CatZonaSalario other = (CatZonaSalario) obj;
		return Objects.equals(clave, other.clave) && Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "CatZonaSalario [clave=" + clave + ", nombre=" + nombre + "]";
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
}
