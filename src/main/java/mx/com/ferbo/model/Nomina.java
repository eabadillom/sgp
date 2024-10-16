package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import mx.com.ferbo.model.sat.CatMetodoPago;

public class Nomina {
	
	private Integer id;
	private String moneda;
	private Date fechaEmision;
	private String numeroCertificado;
	private Date fechaTimbrado;
	private String uuid = null;
	private String idPac;
	private String tipoComprobante;
	private String claveExportacion;
	private CatMetodoPago metodoPago;
	private String serie;
	private String folio;
	private BigDecimal subtotal;
	private BigDecimal descuento;
	private BigDecimal total;
	private DetNominaEmisor emisor;
	private DetNominaReceptor receptor;
	private List<NominaConcepto> conceptos;
	private List<NominaPercepcion> percepciones;
	private List<NominaOtroPago> otrosPagos;
	private List<NominaDeduccion> deducciones;
	
	//TODO Revisar si los siguientes datos NO son parte del CFDI.
	private Integer ejercicio;
	private Integer periodo;
	private Integer diasLaborados;
	private Integer diasPagados;
	private Integer diasAsueto;
	private Integer diasNoLaborados;
	
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
	public CatMetodoPago getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(CatMetodoPago metodoPago) {
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
	public DetNominaEmisor getEmisor() {
		return emisor;
	}
	public void setEmisor(DetNominaEmisor emisor) {
		this.emisor = emisor;
	}
	public DetNominaReceptor getReceptor() {
		return receptor;
	}
	public void setReceptor(DetNominaReceptor receptor) {
		this.receptor = receptor;
	}
	public List<NominaConcepto> getConceptos() {
		return conceptos;
	}
	public void setConceptos(List<NominaConcepto> conceptos) {
		this.conceptos = conceptos;
	}
	public List<NominaPercepcion> getPercepciones() {
		return percepciones;
	}
	public void setPercepciones(List<NominaPercepcion> percepciones) {
		this.percepciones = percepciones;
	}
	public List<NominaDeduccion> getDeducciones() {
		return deducciones;
	}
	public void setDeducciones(List<NominaDeduccion> deducciones) {
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
		Nomina other = (Nomina) obj;
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
	
	public List<NominaOtroPago> getOtrosPagos() {
		return otrosPagos;
	}
	public void setOtrosPagos(List<NominaOtroPago> otrosPagos) {
		this.otrosPagos = otrosPagos;
	}
	public Integer getDiasLaborados() {
		return diasLaborados;
	}
	public void setDiasLaborados(Integer diasLaborados) {
		this.diasLaborados = diasLaborados;
	}
	public Integer getDiasPagados() {
		return diasPagados;
	}
	public void setDiasPagados(Integer diasPagados) {
		this.diasPagados = diasPagados;
	}
	public Integer getDiasAsueto() {
		return diasAsueto;
	}
	public void setDiasAsueto(Integer diasAsueto) {
		this.diasAsueto = diasAsueto;
	}
	public Integer getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public Integer getDiasNoLaborados() {
		return diasNoLaborados;
	}
	public void setDiasNoLaborados(Integer diasNoLaborados) {
		this.diasNoLaborados = diasNoLaborados;
	}
	
}
