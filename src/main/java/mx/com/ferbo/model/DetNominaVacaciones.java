package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "det_nom_vacaciones")
public class DetNominaVacaciones implements Serializable {

	private static final long serialVersionUID = -988966393256020145L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_nom_vacaciones")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_nomina", referencedColumnName = "id_nomina")
	private DetNomina nomina;
	
	@OneToOne
	@JoinColumn(name = "id_vacaciones", referencedColumnName = "id_vacaciones")
	private DetVacaciones vacaciones;
	
	@Column(name = "tp_prima")
	@Size(min = 1, max = 1)
	@Basic(optional = false)
	private String tipoPrima;

	@Override
	public int hashCode() {
		
		if(this.id == null)
			return System.identityHashCode(this);
		
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaVacaciones other = (DetNominaVacaciones) obj;
		
		if(this.id == null || other.id == null)
			return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
		
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "}";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
	}

	public DetVacaciones getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(DetVacaciones vacaciones) {
		this.vacaciones = vacaciones;
	}

	public String getTipoPrima() {
		return tipoPrima;
	}

	public void setTipoPrima(String tipoPrima) {
		this.tipoPrima = tipoPrima;
	}
}
