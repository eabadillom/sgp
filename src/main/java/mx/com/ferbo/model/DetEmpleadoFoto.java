package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_empleado_foto")
@NamedQueries({
	@NamedQuery(name = "DetEmpleadoFoto.findByNumeroEmpleado", query = "SELECT f FROM DetEmpleadoFoto f WHERE f.empleado.numEmpleado = :numeroEmpleado")
})
public class DetEmpleadoFoto implements Serializable {

	private static final long serialVersionUID = 8183960341006171158L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional =  false)
	@Column(name = "id_empleado_foto")
	private Integer id;
	
	@Column(name = "nb_fotografia")
	@Basic(optional = false)
	private String fotografia;
	
	@OneToOne(mappedBy = "empleadoFoto")
	private DetEmpleado empleado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFotografia() {
		return fotografia;
	}

	public void setFotografia(String fotografia) {
		this.fotografia = fotografia;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"fotografia\":\"" + fotografia + "\"}";
	}

	@Override
	public int hashCode() {
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
		DetEmpleadoFoto other = (DetEmpleadoFoto) obj;
		return Objects.equals(id, other.id);
	}
}
