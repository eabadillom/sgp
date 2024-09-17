package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatUnidadSAT;

public class NominaConcepto {
	private NominaConceptoPK key;
	private CatConcepto concepto;
	private BigDecimal cantidad;
	private CatUnidadSAT unidad;
	private String nombreConcepto;
	private String objetoImpuesto;
	private BigDecimal valorUnitario;
	private BigDecimal importe;
	private BigDecimal descuento;
	
	public NominaConceptoPK getKey() {
		return key;
	}
	public void setKey(NominaConceptoPK key) {
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
		NominaConcepto other = (NominaConcepto) obj;
		return Objects.equals(key, other.key);
	}
	@Override
	public String toString() {
		return "NominaConceptoDTO [key=" + key + ", cantidad=" + cantidad + ", nombreConcepto=" + nombreConcepto
				+ ", objetoImpuesto=" + objetoImpuesto + ", valorUnitario=" + valorUnitario + ", importe=" + importe
				+ ", descuento=" + descuento + "]";
	}
}
