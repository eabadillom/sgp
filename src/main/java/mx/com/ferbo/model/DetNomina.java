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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatMetodoPago;

@Entity
@Table(name = "det_nomina")
@NamedQueries({
    @NamedQuery(name = "DetNomina.findAll", query = "SELECT n FROM DetNomina n"),
    @NamedQuery(name = "DetNomina.findByPeriodo", query = "SELECT n FROM DetNomina n WHERE n.periodoInicio = :periodoInicio AND n.periodoFin = :periodoFin"),
    @NamedQuery(name = "DetNomina.findByPeriodoRfc", query = "SELECT n FROM DetNomina n WHERE n.periodoInicio = :periodoInicio AND n.periodoFin = :periodoFin AND n.receptor.rfc = :rfc"),
    @NamedQuery(name = "DetNomina.findByEmisorTipoNominaAnioPeriodoReceptor", query = "SELECT n FROM DetNomina n WHERE n.emisor.rfc = :rfcEmisor AND n.tipoNomina = :tipoNomina and n.ejercicio = :anio and n.periodo = :periodo and n.receptor.rfc = :rfcReceptor"),
    @NamedQuery(name = "DetNomina.findNominasDelMesPorNumeroPeriodo", query = "SELECT n FROM DetNomina n WHERE n.tipoNomina = :tipoNomina AND n.receptor.periodicidadPago.periodicidad = :periodicidad AND n.ejercicio = :ejercicio AND n.periodo between :periodoInicio AND :periodoFin AND n.receptor.rfc = :rfc "),
    @NamedQuery(name = "DetNomina.findNominasDelMesPorFechaPeriodo", query = "SELECT n FROM DetNomina n WHERE n.tipoNomina = :tipoNomina AND n.ejercicio = :ejercicio AND n.periodoFin between :periodoInicio and :periodoFin AND n.receptor.rfc = :rfc") 
})
public class DetNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nomina")
    private Integer id;
    
    @Column(name = "tp_nomina")
    @Basic(optional = false)
    @Size(max = 5)
    private String tipoNomina;
    
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
    
    @Column(name = "nu_subtotal", precision = 12, scale = 2)
    @Basic(optional = false)
    private BigDecimal subtotal;
    
    @Column(name = "nu_descuento", precision = 12, scale = 2)
    @Basic(optional = false)
    private BigDecimal descuento;
    
    @Column(name = "nu_total", precision = 12, scale = 2)
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
    
    @Transient
    private BigDecimal diasLaborales;
    
    @Transient
    private BigDecimal diasNoLaborales;
    
    @Column(name = "nu_dias_laborados", precision = 6, scale = 2)
    @Basic(optional = true)
  	private BigDecimal diasLaborados;
    
    @Column(name = "nu_dias_no_laborados", precision = 6, scale = 2)
    @Basic(optional = true)
    private BigDecimal diasNoLaborados;
    
    @Column(name = "nu_dias_vacaciones", precision = 6, scale = 2)
    @Basic(optional = true)
    private BigDecimal diasVacaciones;
    
    @Column(name = "nu_dias_pagados", precision = 6, scale = 2)
    @Basic(optional = true)
    private BigDecimal diasPagados;
    
    @Column(name = "nu_dias_asueto", precision = 6, scale = 2)
    @Basic(optional = true)
    private BigDecimal diasAsueto;
    
    @Column(name = "nu_dias_incapacidad", precision = 6, scale = 2)
    @Basic(optional = true)
    private BigDecimal diasIncapacidad;
    
    @OneToOne(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private DetNominaEmisor emisor;
    
    @OneToOne(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private DetNominaReceptor receptor;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaConcepto> conceptos;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaPercepcion> percepciones;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaOtroPago> otrosPagos;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaDeduccion> deducciones;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaIncidencia> incidencias;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetNominaVacaciones> nominaVacaciones;
    
    @Transient
    private List<DetVacaciones> vacaciones;
    
    @Override
	public int hashCode() {
    	if (this.id == null) {
    		return System.identityHashCode(this);
        }
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

	public BigDecimal getDiasLaborados() {
		return diasLaborados;
	}

	public void setDiasLaborados(BigDecimal diasLaborados) {
		this.diasLaborados = diasLaborados;
	}

	public BigDecimal getDiasPagados() {
		return diasPagados;
	}

	public void setDiasPagados(BigDecimal diasPagados) {
		this.diasPagados = diasPagados;
	}

	public BigDecimal getDiasAsueto() {
		return diasAsueto;
	}

	public void setDiasAsueto(BigDecimal diasAsueto) {
		this.diasAsueto = diasAsueto;
	}

	public BigDecimal getDiasNoLaborados() {
		return diasNoLaborados;
	}

	public void setDiasNoLaborados(BigDecimal diasNoLaborados) {
		this.diasNoLaborados = diasNoLaborados;
	}

	public String getLugarExpedicion() {
		return lugarExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
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

	public String getTipoNomina() {
		return tipoNomina;
	}

	public void setTipoNomina(String tipoNomina) {
		this.tipoNomina = tipoNomina;
	}

	public List<DetVacaciones> getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(List<DetVacaciones> vacaciones) {
		this.vacaciones = vacaciones;
	}

	public BigDecimal getDiasVacaciones() {
		return diasVacaciones;
	}

	public void setDiasVacaciones(BigDecimal diasVacaciones) {
		this.diasVacaciones = diasVacaciones;
	}

	public BigDecimal getDiasLaborales() {
		return diasLaborales;
	}

	public void setDiasLaborales(BigDecimal diasLaborales) {
		this.diasLaborales = diasLaborales;
	}

	public BigDecimal getDiasNoLaborales() {
		return diasNoLaborales;
	}

	public void setDiasNoLaborales(BigDecimal diasNoLaborales) {
		this.diasNoLaborales = diasNoLaborales;
	}

	public List<DetNominaIncidencia> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(List<DetNominaIncidencia> incidencias) {
		if(this.incidencias == null)
			this.incidencias = incidencias;
		else
			this.incidencias.addAll(incidencias);
	}

	public List<DetNominaVacaciones> getNominaVacaciones() {
		return nominaVacaciones;
	}

	public void setNominaVacaciones(List<DetNominaVacaciones> nominaVacaciones) {
		this.nominaVacaciones = nominaVacaciones;
	}

	public BigDecimal getDiasIncapacidad() {
		return diasIncapacidad;
	}

	public void setDiasIncapacidad(BigDecimal diasIncapacidad) {
		this.diasIncapacidad = diasIncapacidad;
	}
}
