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
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "id_deduccion")
	private Integer id;
	
	@Override
	public int hashCode() {
		System.out.println("Hashcode Deduccion...");
		if (this.nomina != null && this.nomina.getId() != null && this.id != null) {
            return Objects.hash(this.nomina.getId(), this.id);
        }
        return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals Deduccion...");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaDeduccionPK other = (DetNominaDeduccionPK) obj;
		
		return this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return "DetNominaDeduccionPK [id=" + id + "]";
	}
	
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
}