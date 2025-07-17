package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_registro_vacaciones")
public class DetRegistroVacaciones implements Serializable {

	private static final long serialVersionUID = 1531708079913467850L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reg_vacaciones")
	private Integer id;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_registro")
	@Basic(optional = false)
	private DetRegistro registro;
	
	@ManyToOne
	@JoinColumn(name = "id_vacaciones")
	@Basic(optional = false)
	private DetVacaciones vacaciones;

	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		DetRegistroVacaciones other = (DetRegistroVacaciones) obj;
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

	public DetRegistro getRegistro() {
		return registro;
	}

	public void setRegistro(DetRegistro registro) {
		this.registro = registro;
	}

	public DetVacaciones getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(DetVacaciones vacaciones) {
		this.vacaciones = vacaciones;
	}
}
