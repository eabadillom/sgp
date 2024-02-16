package mx.com.ferbo.model;

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
@Table(name="cat_pais")
@NamedQueries({
	@NamedQuery(name = "Pais.findById", query = "SELECT new mx.com.ferbo.dto.PaisDTO(d.clavePais, d.nombrePais) FROM Pais d WHERE d.clavePais = :clavePais"),
	@NamedQuery(name = "Pais.findAll", query = "SELECT new mx.com.ferbo.dto.PaisDTO(d.clavePais, d.nombrePais) FROM Pais d ORDER BY d.nombrePais")
})
public class Pais implements Serializable {

	private static final long serialVersionUID = 869199289584873346L;
	
	@Id
	@Basic(optional = false)
	@Column(name="cd_pais")
	private String clavePais;

	@Column(name = "nb_pais")
	private String nombrePais;
	
	public Pais() {
	}
	
	public Pais(String clavePais, String nombrePais) {
		super();
		this.clavePais = clavePais;
		this.nombrePais = nombrePais;
	}

	public String getClavePais() {
		return clavePais;
	}

	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}

	public String getNombrePais() {
		return nombrePais;
	}

	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clavePais, nombrePais);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pais other = (Pais) obj;
		return Objects.equals(clavePais, other.clavePais) && Objects.equals(nombrePais, other.nombrePais);
	}

	@Override
	public String toString() {
		return "Pais [clavePais=" + clavePais + ", nombrePais=" + nombrePais + "]";
	}
}
