package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DetNominaDeduccionPK implements Serializable {

	private static final long serialVersionUID = -9009642024703318411L;
	
	@ManyToOne
	@JoinColumn(name = "id_nomina", referencedColumnName = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "id_deduccion")
	private Integer id;
	
	public DetNominaDeduccionPK() {
		
	}

	public DetNominaDeduccionPK(DetNomina nomina, Integer id) {
		this.nomina = nomina;
		this.id = id;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nomina);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaDeduccionPK other = (DetNominaDeduccionPK) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomina, other.nomina);
	}

	@Override
	public String toString() {
		return "DetNominaDeduccionPK [nomina=" + nomina + ", id=" + id + "]";
	}
}