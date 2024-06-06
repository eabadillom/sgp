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
	@JoinColumn(name = "id_nomina", referencedColumnName = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "id_otro_pago")
	private Integer id;

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
		DetNominaOtroPagoPK other = (DetNominaOtroPagoPK) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomina, other.nomina);
	}

	@Override
	public String toString() {
		return "DetNominaOtroPagoPK [nomina=" + nomina + ", id=" + id + "]";
	}
}
