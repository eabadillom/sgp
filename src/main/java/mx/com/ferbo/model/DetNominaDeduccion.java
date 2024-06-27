package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoDeduccion;

@Entity
@Table(name = "det_nom_deduccion")
public class DetNominaDeduccion implements Serializable {

	private static final long serialVersionUID = 4306393726440622025L;
	
	@EmbeddedId
	private DetNominaDeduccionPK key;
	
	@OneToOne
	@JoinColumn(name = "tp_deduccion", referencedColumnName = "cd_tipo_deduccion")
	@Basic(optional = false)
	private CatTipoDeduccion tipoDeduccion;
	
	@Column(name = "cd_deduccion")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_deduccion")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nu_importe", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importe;

	public DetNominaDeduccionPK getKey() {
		return key;
	}

	public void setKey(DetNominaDeduccionPK key) {
		this.key = key;
	}

	public CatTipoDeduccion getTipoDeduccion() {
		return tipoDeduccion;
	}

	public void setTipoDeduccion(CatTipoDeduccion tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaDeduccion other = (DetNominaDeduccion) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "DetNominaDeduccion [key=" + key + ", claveDeduccion=" + clave + ", nombreDeduccion="
				+ nombre + ", importe=" + importe + "]";
	}
}
