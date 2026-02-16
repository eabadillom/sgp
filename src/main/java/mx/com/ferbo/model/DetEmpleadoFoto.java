package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_empleado_foto")
@NamedQuery(name = "DetEmpleadoFoto.findByNumeroEmpleado", query = "SELECT f FROM DetEmpleadoFoto f WHERE f.empleado.numEmpleado = :numeroEmpleado")
@NamedQuery(name = "DetEmpleadoFoto.findByIdEmpleado", query = "SELECT f FROM DetEmpleadoFoto f WHERE f.empleado.idEmpleado = :idEmpleado")
public class DetEmpleadoFoto implements Serializable {
	
	private static final long serialVersionUID = 8183960341006171158L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional =  false)
	@Column(name = "id_empleado_foto")
	private Integer id;
	
	@Column(name = "nb_fotografia")
	@Basic(optional = true)
	private String fotografia;
	
	@OneToOne(mappedBy = "empleadoFoto")
	private DetEmpleado empleado;
	
	public DetEmpleadoFoto() {
	}

	private DetEmpleadoFoto(Builder builder) {
		this.id = builder.id;
		this.fotografia = builder.fotografia;
		this.empleado = builder.empleado;
	}
	
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\"}";
	}

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
		DetEmpleadoFoto other = (DetEmpleadoFoto) obj;
		return Objects.equals(id, other.id);
	}

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
	
	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public static final class Builder {
		private Integer id;
		private String fotografia;
		private DetEmpleado empleado;

		public DetEmpleadoFoto build() {
			return new DetEmpleadoFoto(this);
		}

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder fotografia(String fotografia) {
			this.fotografia = fotografia;
			return this;
		}

		public Builder empleado(DetEmpleado empleado) {
			this.empleado = empleado;
			return this;
		}
	}
}
