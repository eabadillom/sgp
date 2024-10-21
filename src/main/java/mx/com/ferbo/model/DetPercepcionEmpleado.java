package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.sat.CatTipoPercepcion;

@Entity
@Table(name = "det_percepcion_empleado")
@NamedQueries({
	@NamedQuery(name = "DetEmpleadoPercepcion.buscarPorEmpleado", query = "SELECT ep FROM DetPercepcionEmpleado ep WHERE ep.empleado.idEmpleado = :idEmpleado")
})
public class DetPercepcionEmpleado implements Serializable {
	
	private static final long serialVersionUID = 587498211081980349L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_empleado_imss")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
	@Basic(optional = false)
	private DetEmpleado empleado;
	
	@OneToOne
	@JoinColumn(name = "cd_tipo_percepcion", referencedColumnName = "cd_tipo_percepcion")
	@Basic(optional = false)
	private CatTipoPercepcion tipoPercepcion;
	
	@Column(name = "nu_importe_maximo")
	@Basic(optional = true)
	private BigDecimal importeMaximo;
	
	@Column(name = "st_activo")
	@Basic(optional = false)
	private Boolean activo;

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
		DetPercepcionEmpleado other = (DetPercepcionEmpleado) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DetEmpleadoPercepcion [id=" + id + ", importeMaximo=" + importeMaximo + ", activo=" + activo + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public CatTipoPercepcion getTipoPercepcion() {
		return tipoPercepcion;
	}

	public void setTipoPercepcion(CatTipoPercepcion tipoPercepcion) {
		this.tipoPercepcion = tipoPercepcion;
	}

	public BigDecimal getImporteMaximo() {
		return importeMaximo;
	}

	public void setImporteMaximo(BigDecimal importeMaximo) {
		this.importeMaximo = importeMaximo;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
