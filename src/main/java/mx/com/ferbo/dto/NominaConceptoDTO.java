package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.dto.sat.ConceptoDTO;
import mx.com.ferbo.dto.sat.UnidadSATDTO;

public class NominaConceptoDTO {
	private Integer idNomina;
	private Integer idConcepto;
	private ConceptoDTO concepto;
	private BigDecimal cantidad;
	private UnidadSATDTO unidad;
	private String nombreConcepto;
	private String objetoImpuesto;
	private BigDecimal valorUnitario;
	private BigDecimal importe;
	private BigDecimal descuento;
	
	public Integer getIdNomina() {
		return idNomina;
	}
	public void setIdNomina(Integer idNomina) {
		this.idNomina = idNomina;
	}
	public Integer getIdConcepto() {
		return idConcepto;
	}
	public void setIdConcepto(Integer idConcepto) {
		this.idConcepto = idConcepto;
	}
	public ConceptoDTO getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoDTO concepto) {
		this.concepto = concepto;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public UnidadSATDTO getUnidad() {
		return unidad;
	}
	public void setUnidad(UnidadSATDTO unidad) {
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
		return Objects.hash(idConcepto, idNomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaConceptoDTO other = (NominaConceptoDTO) obj;
		return Objects.equals(idConcepto, other.idConcepto) && Objects.equals(idNomina, other.idNomina);
	}
	@Override
	public String toString() {
		return "NominaConceptoDTO [idNomina=" + idNomina + ", idConcepto=" + idConcepto + ", concepto=" + concepto
				+ ", cantidad=" + cantidad + ", unidad=" + unidad + ", nombreConcepto=" + nombreConcepto
				+ ", objetoImpuesto=" + objetoImpuesto + ", valorUnitario=" + valorUnitario + ", importe=" + importe
				+ ", descuento=" + descuento + "]";
	}
}
