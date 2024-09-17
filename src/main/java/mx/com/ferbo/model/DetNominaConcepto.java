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

import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatUnidadSAT;

@Entity
@Table(name = "det_nom_concepto")
public class DetNominaConcepto implements Serializable {

	private static final long serialVersionUID = -7162671144734951082L;
	
	@EmbeddedId
	private DetNominaConceptoPK key;
	
	@ManyToOne
	@JoinColumn(name = "cd_concepto", referencedColumnName = "cd_concepto")
	private CatConcepto concepto;
	
	@Column(name = "nu_cantidad")
	private BigDecimal cantidad;
	
	@ManyToOne
	@JoinColumn(name = "cd_unidad", referencedColumnName = "cd_unidad")
	private CatUnidadSAT unidad;
	
	@Column(name = "nb_concepto")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombreConcepto;
	
	@Column(name = "cd_obj_imp")
	@Basic(optional = false)
	@Size(max = 5)
	private String objetoImpuesto;
	
	@Column(name = "nu_valor_unitario", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal valorUnitario;
	
	@Column(name = "nu_importe", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importe;
	
	@Column(name = "nu_descuento", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal descuento;

	public DetNominaConceptoPK getKey() {
		return key;
	}

	public void setKey(DetNominaConceptoPK key) {
		this.key = key;
	}

	public CatConcepto getConcepto() {
		return concepto;
	}

	public void setConcepto(CatConcepto concepto) {
		this.concepto = concepto;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public CatUnidadSAT getUnidad() {
		return unidad;
	}

	public void setUnidad(CatUnidadSAT unidad) {
		this.unidad = unidad;
	}

	public String getNombreConcepto() {
		return nombreConcepto;
	}

	public void setNombreConcepto(String nombreConcepto) {
		this.nombreConcepto = nombreConcepto;
	}

	public String getObjetoImpuesto() {
		return objetoImpuesto;
	}

	public void setObjetoImpuesto(String objetoImpuesto) {
		this.objetoImpuesto = objetoImpuesto;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
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
		DetNominaConcepto other = (DetNominaConcepto) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "DetNominaConcepto [key=" + key + ", cantidad=" + cantidad + ", nombreConcepto=" + nombreConcepto
				+ ", objetoImpuesto=" + objetoImpuesto + ", valorUnitario=" + valorUnitario + ", importe=" + importe
				+ ", descuento=" + descuento + "]";
	}
}
