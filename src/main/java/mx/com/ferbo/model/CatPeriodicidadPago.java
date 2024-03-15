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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(name = "CatPeriodicidadPago.buscarActivo", query = "SELECT p FROM CatPeriodicidadPago p WHERE (p.vigenciaInicio IS NOT NULL AND p.vigenciaInicio <= :fecha) AND (p.vigenciaFin IS NULL OR p.vigenciaFin >= :fecha)")
})
@Entity
@Table(name = "cat_periodicidad_pago")
public class CatPeriodicidadPago implements Serializable {

	private static final long serialVersionUID = 1890135287600496909L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "periodicidad")
	private String periodicidad;
	
	@Basic(optional = false)
	@Column(name = "descripcion")
	private String descripcion;
	
	@Basic(optional = false)
	@Column(name = "vigencia_ini")
	private Date vigenciaInicio;
	
	@Basic(optional = true)
	@Column(name = "vigencia_fin")
	private Date vigenciaFin;
	
	public CatPeriodicidadPago() {
	}

	public CatPeriodicidadPago(String periodicidad, String descripcion, Date vigenciaInicio,
			Date vigenciaFin) {
		super();
		this.periodicidad = periodicidad;
		this.descripcion = descripcion;
		this.vigenciaInicio = vigenciaInicio;
		this.vigenciaFin = vigenciaFin;
	}
	
	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}

	@Override
	public String toString() {
		return "CatPeriodicidadPago [periodicidad=" + periodicidad + ", descripcion=" + descripcion
				+ ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(periodicidad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPeriodicidadPago other = (CatPeriodicidadPago) obj;
		return Objects.equals(periodicidad, other.periodicidad);
	}
}
