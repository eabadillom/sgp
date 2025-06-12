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
import javax.persistence.Transient;

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
	@JoinColumn(name = "cd_percepcion", referencedColumnName = "cd_percepcion")
	@Basic(optional = false)
	private CatPercepcion percepcion;
	
	@Column(name = "nu_valor", precision = 8, scale = 4)
	@Basic(optional = true)
	private BigDecimal valor;
	
	@Transient
	private BigDecimal importeExento;
	
	@Transient
	private BigDecimal importeGravado;
	
	@Column(name = "nu_importe_maximo", precision = 12, scale = 2)
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public CatPercepcion getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(CatPercepcion percepcion) {
		this.percepcion = percepcion;
	}

	public BigDecimal getImporteExento() {
		return importeExento;
	}

	public void setImporteExento(BigDecimal importeExento) {
		this.importeExento = importeExento;
	}

	public BigDecimal getImporteGravado() {
		return importeGravado;
	}

	public void setImporteGravado(BigDecimal importeGravado) {
		this.importeGravado = importeGravado;
	}
}
