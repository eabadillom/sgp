package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "det_nom_incidencia")
public class DetNominaIncidencia implements Serializable {

	private static final long serialVersionUID = 4266221685052842691L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_incidencia")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@Column(name = "fh_entrada")
	private Date fechaHoraEntrada;
	
	@Column(name = "fh_salida")
	private Date fechaHoraSalida;
	
	@Column(name = "cd_incidencia")
	@Size(min = 1, max = 1)
	private String clave;
	
	@Size(min = 1, max = 45)
	@Column(name = "nb_incidencia")
	private String descripcion;

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
		DetNominaIncidencia other = (DetNominaIncidencia) obj;
		if(this.id == null || other.id == null)
			return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
		
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",  fechaHoraEntrada\":\"" + fechaHoraEntrada + "\",  fechaHoraSalida\":\""
				+ fechaHoraSalida + "\",  descripcion\":\"" + descripcion + "}";
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

	public Date getFechaHoraEntrada() {
		return fechaHoraEntrada;
	}

	public void setFechaHoraEntrada(Date fechaHoraEntrada) {
		this.fechaHoraEntrada = fechaHoraEntrada;
	}

	public Date getFechaHoraSalida() {
		return fechaHoraSalida;
	}

	public void setFechaHoraSalida(Date fechaHoraSalida) {
		this.fechaHoraSalida = fechaHoraSalida;
	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
