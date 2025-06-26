package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DetNominaOtroPagoPK implements Serializable {

	private static final long serialVersionUID = 3656250857514330296L;
	
	@ManyToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "id_otro_pago")
	private Integer id;
	
	@Override
	public int hashCode() {
		System.out.println("Hashcode Otro pago...");
		if (this.nomina != null && this.nomina.getId() != null && this.id != null) {
            return Objects.hash(this.nomina.getId(), this.id);
        }
        return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals Otro pago...");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaOtroPagoPK other = (DetNominaOtroPagoPK) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DetNominaOtroPagoPK [id=" + id + "]";
	}
	
	public DetNominaOtroPagoPK() {
		
	}
	
	public DetNominaOtroPagoPK(DetNomina nomina, Integer id) {
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
