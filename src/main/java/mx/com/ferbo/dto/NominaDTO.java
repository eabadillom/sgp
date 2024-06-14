package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import mx.com.ferbo.dto.sat.MetodoPagoDTO;

public class NominaDTO {
	
	private Integer id;
	private String moneda;
	private Date fechaEmision;
	private String numeroCertificado;
	private Date fechaTimbrado;
	private String uuid = null;
	private String idPac;
	private String tipoComprobante;
	private String claveExportacion;
	private MetodoPagoDTO metodoPago;
	private String serie;
	private String folio;
	private BigDecimal subtotal;
	private BigDecimal descuento;
	private BigDecimal total;
	private NominaEmisorDTO emisor;
	private NominaReceptorDTO receptor;
	private List<NominaConceptoDTO> conceptos;
	private List<NominaPercepcionDTO> percepciones;
	private List<NominaDeduccionDTO> deducciones;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getNumeroCertificado() {
		return numeroCertificado;
	}
	public void setNumeroCertificado(String numeroCertificado) {
		this.numeroCertificado = numeroCertificado;
	}
	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}
	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getIdPac() {
		return idPac;
	}
	public void setIdPac(String idPac) {
		this.idPac = idPac;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getClaveExportacion() {
		return claveExportacion;
	}
	public void setClaveExportacion(String claveExportacion) {
		this.claveExportacion = claveExportacion;
	}
	public MetodoPagoDTO getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(MetodoPagoDTO metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public NominaEmisorDTO getEmisor() {
		return emisor;
	}
	public void setEmisor(NominaEmisorDTO emisor) {
		this.emisor = emisor;
	}
	public NominaReceptorDTO getReceptor() {
		return receptor;
	}
	public void setReceptor(NominaReceptorDTO receptor) {
		this.receptor = receptor;
	}
	public List<NominaConceptoDTO> getConceptos() {
		return conceptos;
	}
	public void setConceptos(List<NominaConceptoDTO> conceptos) {
		this.conceptos = conceptos;
	}
	public List<NominaPercepcionDTO> getPercepciones() {
		return percepciones;
	}
	public void setPercepciones(List<NominaPercepcionDTO> percepciones) {
		this.percepciones = percepciones;
	}
	public List<NominaDeduccionDTO> getDeducciones() {
		return deducciones;
	}
	public void setDeducciones(List<NominaDeduccionDTO> deducciones) {
		this.deducciones = deducciones;
	}
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
		NominaDTO other = (NominaDTO) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "NominaDTO [id=" + id + ", moneda=" + moneda + ", fechaEmision=" + fechaEmision + ", numeroCertificado="
				+ numeroCertificado + ", fechaTimbrado=" + fechaTimbrado + ", uuid=" + uuid + ", idPac=" + idPac
				+ ", tipoComprobante=" + tipoComprobante + ", claveExportacion=" + claveExportacion + ", metodoPago="
				+ metodoPago + ", serie=" + serie + ", folio=" + folio + ", subtotal=" + subtotal + ", descuento="
				+ descuento + ", total=" + total + ", emisor=" + emisor + ", receptor=" + receptor + ", conceptos="
				+ conceptos + ", percepciones=" + percepciones + ", deducciones=" + deducciones + "]";
	}
	
}
