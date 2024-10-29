package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import mx.com.ferbo.model.sat.CatMetodoPago;

@Entity
@Table(name = "det_nomina")
@NamedQueries({
    @NamedQuery(name = "DetNomina.findAll", query = "SELECT n FROM DetNomina n"),
    @NamedQuery(name = "DetNomina.findByPeriodo", query = "SELECT n FROM DetNomina n WHERE n.periodoInicio = :periodoInicio AND n.periodoFin = :periodoFin"),
    @NamedQuery(name = "DetNomina.findByPeriodoRfc", query = "SELECT n FROM DetNomina n WHERE n.periodoInicio = :periodoInicio AND n.periodoFin = :periodoFin AND n.receptor.rfc = :rfc"),
    @NamedQuery(name = "DetNomina.findBySemanaRfc", query = "SELECT n FROM DetNomina n WHERE n.periodo between :semanaInicio AND :semanaFin AND n.receptor.rfc = :rfc")
})
public class DetNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nomina")
    private Integer id;
    
    @Column(name = "cd_moneda")
    @Size(max = 5)
    @Basic(optional = false)
    private String moneda;
    
    @Column(name = "fh_emision")
    @Basic(optional = false)
    private Date fechaEmision;
    
    @Column(name = "nu_certificado")
    @Basic(optional = true)
    private String numeroCertificado;
    
    @Column(name = "fh_timbrado")
    @Basic(optional = true)
    private Date fechaTimbrado;
    
    @Column(name = "cd_uuid")
    @Basic(optional = true)
    private String uuid = null;
    
    @Column(name = "cd_id_pac")
    @Basic(optional = true)
    private String idPac;

    @Column(name = "cd_tp_comp")
    @Basic(optional = false)
    private String tipoComprobante;
    
    @Column(name = "cd_exportacion")
    @Basic(optional = false)
    private String claveExportacion;
    
    @JoinColumn(name = "cd_metodo_pago", referencedColumnName = "cd_metodo_pago")
    @ManyToOne(optional = false)
    private CatMetodoPago metodoPago;
    
    @Column(name = "nb_serie")
    @Basic(optional = true)
    private String serie;
    
    @Column(name = "nb_folio")
    @Basic(optional = true)
    private String folio;
    
    @Column(name = "nb_lugar_exped")
    @Basic(optional = false)
    private String lugarExpedicion;
    
    @Column(name = "nu_subtotal")
    @Basic(optional = false)
    private BigDecimal subtotal;
    
    @Column(name = "nu_descuento")
    @Basic(optional = false)
    private BigDecimal descuento;
    
    @Column(name = "nu_total")
    @Basic(optional = false)
    private BigDecimal total;
    
    @Column(name = "fh_periodo_inicio")
    @Basic(optional = false)
    private LocalDate periodoInicio;
    
    @Column(name = "fh_periodo_fin")
    @Basic(optional = false)
    private LocalDate periodoFin;
    
    @Column(name = "nu_ejercicio")
    @Basic(optional = true)
    private Integer ejercicio;
    
    @Column(name = "nu_periodo")
    @Basic(optional = true)
  	private Integer periodo;
    
    @Column(name = "nu_dias_laborados")
    @Basic(optional = true)
  	private Integer diasLaborados;
    
    @Column(name = "nu_dias_pagados")
    @Basic(optional = true)
  	private Integer diasPagados;
    
    @Column(name = "nu_dias_asueto")
    @Basic(optional = true)
  	private Integer diasAsueto;
    
    @Column(name = "nu_dias_no_laborados")
    @Basic(optional = true)
  	private Integer diasNoLaborados;
    
    @OneToOne(mappedBy = "nomina", cascade = CascadeType.PERSIST)
    private DetNominaEmisor emisor;
    
    @OneToOne(mappedBy = "nomina", cascade = CascadeType.PERSIST)
    private DetNominaReceptor receptor;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "key.nomina", cascade = CascadeType.ALL)
    private List<DetNominaConcepto> conceptos;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "key.nomina", cascade = CascadeType.ALL)
    private List<DetNominaPercepcion> percepciones;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "key.nomina", cascade = CascadeType.ALL)
    private List<DetNominaOtroPago> otrosPagos;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "key.nomina", cascade = CascadeType.ALL)
    private List<DetNominaDeduccion> deducciones;
    
    
    public DetNomina() {
    }

    public DetNomina(Integer idNomina) {
        this.id = idNomina;
    }

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

	public List<DetNominaConcepto> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<DetNominaConcepto> conceptos) {
		this.conceptos = conceptos;
	}

	public List<DetNominaPercepcion> getPercepciones() {
		return percepciones;
	}

	public void setPercepciones(List<DetNominaPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	public List<DetNominaDeduccion> getDeducciones() {
		return deducciones;
	}

	public void setDeducciones(List<DetNominaDeduccion> deducciones) {
		this.deducciones = deducciones;
	}

	public List<DetNominaOtroPago> getOtrosPagos() {
		return otrosPagos;
	}

	public void setOtrosPagos(List<DetNominaOtroPago> otrosPagos) {
		this.otrosPagos = otrosPagos;
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

	public Integer getDiasNoLaborados() {
		return diasNoLaborados;
	}

	public void setDiasNoLaborados(Integer diasNoLaborados) {
		this.diasNoLaborados = diasNoLaborados;
	}

	public String getLugarExpedicion() {
		return lugarExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
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
		DetNomina other = (DetNomina) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DetNomina [idNomina=" + id + "]";
	}

	public LocalDate getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(LocalDate periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public LocalDate getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(LocalDate periodoFin) {
		this.periodoFin = periodoFin;
	}
}
