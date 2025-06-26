package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DetNominaConceptoPK implements Serializable {

	private static final long serialVersionUID = -8807108964938157094L;
	
	@ManyToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "id_concepto")
	private Integer id;
	
	@Override
	public int hashCode() {
		System.out.println("Hashcode Concepto...");
		if (this.nomina != null && this.nomina.getId() != null && this.id != null) {
            return Objects.hash(this.nomina.getId(), this.id);
        }
        return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals Concepto...");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaConceptoPK other = (DetNominaConceptoPK) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DetNominaConceptoPK [id=" + id + "]";
	}
	
	public DetNominaConceptoPK() {
		
	}

	public DetNominaConceptoPK(DetNomina nomina, Integer id) {
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
