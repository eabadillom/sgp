package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

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

import mx.com.ferbo.model.imss.DetIncapacidad;

@Entity
@Table(name = "det_registro_incapacidad")
public class DetRegistroIncapacidad implements Serializable {
	
	private static final long serialVersionUID = -7093540887466311791L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reg_incapacidad", nullable = false)
	private Integer id;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "id_registro", nullable = false)
	private DetRegistro registro;
	
	@ManyToOne
	@JoinColumn(name = "cd_incapacidad", nullable = false)
	private DetIncapacidad incapacidad;

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
		DetRegistroIncapacidad other = (DetRegistroIncapacidad) obj;
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

	public DetIncapacidad getIncapacidad() {
		return incapacidad;
	}

	public void setIncapacidad(DetIncapacidad incapacidad) {
		this.incapacidad = incapacidad;
	}
}
