package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoOtroPago;

@Entity
@Table(name = "det_nom_otro_pago")
public class DetNominaOtroPago implements Serializable {

	private static final long serialVersionUID = -3073795346585740482L;
	
	@EmbeddedId
	private DetNominaOtroPagoPK key;
	
	@ManyToOne
	@JoinColumn(name = "tp_otro_pago", referencedColumnName = "cd_tipo_otro_pago")
	@Basic(optional = false)
	private CatTipoOtroPago tipoOtroPago;
	
	@Column(name = "cd_otro_pago")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_otro_pago")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nu_importe", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importe;

	public DetNominaOtroPagoPK getKey() {
		return key;
	}

	public void setKey(DetNominaOtroPagoPK key) {
		this.key = key;
	}

	public CatTipoOtroPago getTipoOtroPago() {
		return tipoOtroPago;
	}

	public void setTipoOtroPago(CatTipoOtroPago otroPago) {
		this.tipoOtroPago = otroPago;
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
		DetNominaOtroPago other = (DetNominaOtroPago) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "DetNominaOtroPago [key=" + key + ", clave=" + clave + ", nombre=" + nombre + ", importe=" + importe
				+ "]";
	}
}
