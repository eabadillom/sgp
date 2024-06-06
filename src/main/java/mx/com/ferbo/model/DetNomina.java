package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatMetodoPago;

@Entity
@Table(name = "det_nomina")
@NamedQueries({
    @NamedQuery(name = "DetNomina.findAll", query = "SELECT n FROM DetNomina n")
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
    @Basic(optional = false)
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
    
    @Column(name = "nu_subtotal")
    @Basic(optional = false)
    private BigDecimal subtotal;
    
    @Column(name = "nu_descuento")
    @Basic(optional = false)
    private BigDecimal descuento;
    
    @Column(name = "nu_total")
    @Basic(optional = false)
    private BigDecimal total;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL)
    private List<DetNominaEmisor> emisores;
    
    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL)
    private List<DetNominaReceptor> receptores;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**********************************************************************************************************/
    
    
    
    @NotNull
    @Column(name = "sueldo")
    private BigDecimal sueldo;
    
    @NotNull
    @Column(name = "septimo_dia")
    private BigDecimal septimoDia;
    
    @NotNull
    @Column(name = "horas_extras")
    private BigDecimal horasExtras;
    
    @NotNull
    @Column(name = "destajos")
    private BigDecimal destajos;
    
    @NotNull
    @Column(name = "premios_eficiencia")
    private BigDecimal premiosEficiencia;
    
    @NotNull
    @Column(name = "bono_puntualidad")
    private BigDecimal bonoPuntualidad;
    
    @NotNull
    @Column(name = "despensa")
    private BigDecimal despensa;
    
    @NotNull
    @Column(name = "otras_percepciones")
    private BigDecimal otrasPercepciones;
    
    @NotNull
    @Column(name = "total_percepciones")
    private BigDecimal totalPercepciones;
    
    @NotNull
    @Column(name = "ret_inv_y_vida")
    private Float retInvYVida;
    @NotNull
    @Column(name = "ret_cesantia")
    private Float retCesantia;
    @NotNull
    @Column(name = "ret_enf_y_mat_obrero")
    private Float retEnfYMatObrero;
    @NotNull
    @Column(name = "prestamo_infonavit_fd")
    private Float prestamoInfonavitFd;
    @NotNull
    @Column(name = "prestamo_infonavit_cf")
    private Float prestamoInfonavitCf;
    @NotNull
    @Column(name = "subs_al_empleo_acreditado")
    private BigDecimal subsAlEmpleoAcreditado;
    @NotNull
    @Column(name = "subs_al_empleo_mes")
    private BigDecimal subsAlEmpleoMes;
    @NotNull
    @Column(name = "isr_antes_de_subs_al_empleo")
    private BigDecimal isrAntesDeSubsAlEmpleo;
    @NotNull
    @Column(name = "isr_mes")
    private BigDecimal isrMes;
    @NotNull
    @Column(name = "imss")
    private BigDecimal imss;
    @NotNull
    @Column(name = "prestamo_fonacot")
    private Float prestamoFonacot;
    @NotNull
    @Column(name = "ajuste_en_subsidio_para_el_empleo")
    private Float ajusteEnSubsidioParaElEmpleo;
    @NotNull
    @Column(name = "subs_entregado_que_no_correspondia")
    private Float subsEntregadoQueNoCorrespondia;
    @NotNull
    @Column(name = "ajuste_al_neto")
    private Float ajusteAlNeto;
    @NotNull
    @Column(name = "isr_de_ajuste_mensual")
    private Float isrDeAjusteMensual;
    @NotNull
    @Column(name = "isr_ajustado_por_subsidio")
    private Float isrAjustadoPorSubsidio;
    @NotNull
    @Column(name = "ajuste_al_subsidio_causado")
    private Float ajusteAlSubsidioCausado;
    @NotNull
    @Column(name = "pension_alimienticia")
    private Float pensionAlimienticia;
    @NotNull
    @Column(name = "otras_deducciones")
    private Float otrasDeducciones;
    @NotNull
    @Column(name = "total_deducciones")
    private BigDecimal totalDeducciones;
    @NotNull
    @Column(name = "neto")
    private BigDecimal neto;
    @NotNull
    @Column(name = "invalidez_y_vida")
    private Float invalidezYVida;
    @NotNull
    @Column(name = "cesantia_y_vejez")
    private Float cesantiaYVejez;
    @NotNull
    @Column(name = "enf_y_mat_patron")
    private Float enfYMatPatron;
    @NotNull
    @Column(name = "fondo_retiro_sar_8")
    private Float fondoRetiroSar;
    @NotNull
    @Column(name = "impuesto_estatal")
    private Float impuestoEstatal ;
    @NotNull
    @Column(name = "riesgo_de_trabajo_9")
    private Float riesgoDeTrabajo9;
    @NotNull
    @Column(name = "imss_empresa")
    private Float imssEmpresa;
    @NotNull
    @Column(name = "infonavit_empresa")
    private Float infonavitEmpresa;
    @NotNull
    @Column(name = "guarderia_imss_7")
    private Float guarderiaImss7;
    @NotNull
    @Column(name = "otras_obligaciones")
    private Float otrasObligaciones;
    @NotNull
    @Column(name = "total_obligaciones")
    private Float totalObligaciones;
    @NotNull
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @JoinColumn(name = "id_empleado_creador", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado idEmpleadoCreador;

    public DetNomina() {
    }

    public DetNomina(Integer idNomina) {
        this.id = idNomina;
    }

    public DetNomina(Integer idNomina, DetEmpleado idEmpleado, BigDecimal sueldo, BigDecimal septimoDia, BigDecimal horasExtras, BigDecimal destajos, BigDecimal premiosEficiencia, BigDecimal bonoPuntualidad, BigDecimal despensa, BigDecimal otrasPercepciones, BigDecimal totalPercepciones, Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, BigDecimal subsAlEmpleoAcreditado, BigDecimal subsAlEmpleoMes, BigDecimal isrAntesDeSubsAlEmpleo, BigDecimal isrMes, BigDecimal imss, Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, BigDecimal totalDeducciones, BigDecimal neto, Float invalidezYVvida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, DetEmpleado idEmpleadoCreador) {
        this.id = idNomina;
        this.sueldo = sueldo;
        this.septimoDia = septimoDia;
        this.horasExtras = horasExtras;
        this.destajos = destajos;
        this.premiosEficiencia = premiosEficiencia;
        this.bonoPuntualidad = bonoPuntualidad;
        this.despensa = despensa;
        this.otrasPercepciones = otrasPercepciones;
        this.totalPercepciones = totalPercepciones;
        this.retInvYVida = retInvYVida;
        this.retCesantia = retCesantia;
        this.retEnfYMatObrero = retEnfYMatObrero;
        this.prestamoInfonavitFd = prestamoInfonavitFd;
        this.prestamoInfonavitCf = prestamoInfonavitCf;
        this.subsAlEmpleoAcreditado = subsAlEmpleoAcreditado;
        this.subsAlEmpleoMes = subsAlEmpleoMes;
        this.isrAntesDeSubsAlEmpleo = isrAntesDeSubsAlEmpleo;
        this.isrMes = isrMes;
        this.imss = imss;
        this.prestamoFonacot = prestamoFonacot;
        this.ajusteEnSubsidioParaElEmpleo = ajusteEnSubsidioParaElEmpleo;
        this.subsEntregadoQueNoCorrespondia = subsEntregadoQueNoCorrespondia;
        this.ajusteAlNeto = ajusteAlNeto;
        this.isrDeAjusteMensual = isrDeAjusteMensual;
        this.isrAjustadoPorSubsidio = isrAjustadoPorSubsidio;
        this.ajusteAlSubsidioCausado = ajusteAlSubsidioCausado;
        this.pensionAlimienticia = pensionAlimienticia;
        this.otrasDeducciones = otrasDeducciones;
        this.totalDeducciones = totalDeducciones;
        this.neto = neto;
        this.invalidezYVida = invalidezYVvida;
        this.cesantiaYVejez = cesantiaYVejez;
        this.enfYMatPatron = enfYMatPatron;
        this.fondoRetiroSar = fondoRetiroSar;
        this.impuestoEstatal = impuestoEstatal;
        this.riesgoDeTrabajo9 = riesgoDeTrabajo9;
        this.imssEmpresa = imssEmpresa;
        this.infonavitEmpresa = infonavitEmpresa;
        this.guarderiaImss7 = guarderiaImss7;
        this.otrasObligaciones = otrasObligaciones;
        this.totalObligaciones = totalObligaciones;
        this.fechaCreacion = fechaCreacion;
        this.idEmpleadoCreador = idEmpleadoCreador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public BigDecimal getSeptimoDia() {
        return septimoDia;
    }

    public void setSeptimoDia(BigDecimal septimoDia) {
        this.septimoDia = septimoDia;
    }

    public BigDecimal getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(BigDecimal horasExtras) {
        this.horasExtras = horasExtras;
    }

    public BigDecimal getDestajos() {
        return destajos;
    }

    public void setDestajos(BigDecimal destajos) {
        this.destajos = destajos;
    }

    public BigDecimal getPremiosEficiencia() {
        return premiosEficiencia;
    }

    public void setPremiosEficiencia(BigDecimal premiosEficiencia) {
        this.premiosEficiencia = premiosEficiencia;
    }

    public BigDecimal getBonoPuntualidad() {
        return bonoPuntualidad;
    }

    public void setBonoPuntualidad(BigDecimal bonoPuntualidad) {
        this.bonoPuntualidad = bonoPuntualidad;
    }

    public BigDecimal getDespensa() {
        return despensa;
    }

    public void setDespensa(BigDecimal despensa) {
        this.despensa = despensa;
    }

    public BigDecimal getOtrasPercepciones() {
        return otrasPercepciones;
    }

    public void setOtrasPercepciones(BigDecimal otrasPercepciones) {
        this.otrasPercepciones = otrasPercepciones;
    }

    public BigDecimal getTotalPercepciones() {
        return totalPercepciones;
    }

    public void setTotalPercepciones(BigDecimal totalPercepciones) {
        this.totalPercepciones = totalPercepciones;
    }

    public Float getRetInvYVida() {
        return retInvYVida;
    }

    public void setRetInvYVida(Float retInvYVida) {
        this.retInvYVida = retInvYVida;
    }

    public Float getRetCesantia() {
        return retCesantia;
    }

    public void setRetCesantia(Float retCesantia) {
        this.retCesantia = retCesantia;
    }

    public Float getRetEnfYMatObrero() {
        return retEnfYMatObrero;
    }

    public void setRetEnfYMatObrero(Float retEnfYMatObrero) {
        this.retEnfYMatObrero = retEnfYMatObrero;
    }

    public Float getPrestamoInfonavitFd() {
        return prestamoInfonavitFd;
    }

    public void setPrestamoInfonavitFd(Float prestamoInfonavitFd) {
        this.prestamoInfonavitFd = prestamoInfonavitFd;
    }

    public Float getPrestamoInfonavitCf() {
        return prestamoInfonavitCf;
    }

    public void setPrestamoInfonavitCf(Float prestamoInfonavitCf) {
        this.prestamoInfonavitCf = prestamoInfonavitCf;
    }

    public BigDecimal getSubsAlEmpleoAcreditado() {
        return subsAlEmpleoAcreditado;
    }

    public void setSubsAlEmpleoAcreditado(BigDecimal subsAlEmpleoAcreditado) {
        this.subsAlEmpleoAcreditado = subsAlEmpleoAcreditado;
    }

    public BigDecimal getSubsAlEmpleoMes() {
        return subsAlEmpleoMes;
    }

    public void setSubsAlEmpleoMes(BigDecimal subsAlEmpleoMes) {
        this.subsAlEmpleoMes = subsAlEmpleoMes;
    }

    public BigDecimal getIsrAntesDeSubsAlEmpleo() {
        return isrAntesDeSubsAlEmpleo;
    }

    public void setIsrAntesDeSubsAlEmpleo(BigDecimal isrAntesDeSubsAlEmpleo) {
        this.isrAntesDeSubsAlEmpleo = isrAntesDeSubsAlEmpleo;
    }

    public BigDecimal getIsrMes() {
        return isrMes;
    }

    public void setIsrMes(BigDecimal isrMes) {
        this.isrMes = isrMes;
    }

    public BigDecimal getImss() {
        return imss;
    }

    public void setImss(BigDecimal imss) {
        this.imss = imss;
    }

    public Float getPrestamoFonacot() {
        return prestamoFonacot;
    }

    public void setPrestamoFonacot(Float prestamoFonacot) {
        this.prestamoFonacot = prestamoFonacot;
    }

    public Float getAjusteEnSubsidioParaElEmpleo() {
        return ajusteEnSubsidioParaElEmpleo;
    }

    public void setAjusteEnSubsidioParaElEmpleo(Float ajusteEnSubsidioParaElEmpleo) {
        this.ajusteEnSubsidioParaElEmpleo = ajusteEnSubsidioParaElEmpleo;
    }

    public Float getSubsEntregadoQueNoCorrespondia() {
        return subsEntregadoQueNoCorrespondia;
    }

    public void setSubsEntregadoQueNoCorrespondia(Float subsEntregadoQueNoCorrespondia) {
        this.subsEntregadoQueNoCorrespondia = subsEntregadoQueNoCorrespondia;
    }

    public Float getAjusteAlNeto() {
        return ajusteAlNeto;
    }

    public void setAjusteAlNeto(Float ajusteAlNeto) {
        this.ajusteAlNeto = ajusteAlNeto;
    }

    public Float getIsrDeAjusteMensual() {
        return isrDeAjusteMensual;
    }

    public void setIsrDeAjusteMensual(Float isrDeAjusteMensual) {
        this.isrDeAjusteMensual = isrDeAjusteMensual;
    }

    public Float getIsrAjustadoPorSubsidio() {
        return isrAjustadoPorSubsidio;
    }

    public void setIsrAjustadoPorSubsidio(Float isrAjustadoPorSubsidio) {
        this.isrAjustadoPorSubsidio = isrAjustadoPorSubsidio;
    }

    public Float getAjusteAlSubsidioCausado() {
        return ajusteAlSubsidioCausado;
    }

    public void setAjusteAlSubsidioCausado(Float ajusteAlSubsidioCausado) {
        this.ajusteAlSubsidioCausado = ajusteAlSubsidioCausado;
    }

    public Float getPensionAlimienticia() {
        return pensionAlimienticia;
    }

    public void setPensionAlimienticia(Float pensionAlimienticia) {
        this.pensionAlimienticia = pensionAlimienticia;
    }

    public Float getOtrasDeducciones() {
        return otrasDeducciones;
    }

    public void setOtrasDeducciones(Float otrasDeducciones) {
        this.otrasDeducciones = otrasDeducciones;
    }

    public BigDecimal getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(BigDecimal totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public BigDecimal getNeto() {
        return neto;
    }

    public void setNeto(BigDecimal neto) {
        this.neto = neto;
    }

    public Float getInvalidezYVida() {
        return invalidezYVida;
    }

    public void setInvalidezYVida(Float invalidezYVida) {
        this.invalidezYVida = invalidezYVida;
    }

    public Float getCesantiaYVejez() {
        return cesantiaYVejez;
    }

    public void setCesantiaYVejez(Float cesantiaYVejez) {
        this.cesantiaYVejez = cesantiaYVejez;
    }

    public Float getEnfYMatPatron() {
        return enfYMatPatron;
    }

    public void setEnfYMatPatron(Float enfYMatPatron) {
        this.enfYMatPatron = enfYMatPatron;
    }

    public Float getFondoRetiroSar() {
        return fondoRetiroSar;
    }

    public void setFondoRetiroSar(Float fondoRetiroSar) {
        this.fondoRetiroSar = fondoRetiroSar;
    }

    public Float getImpuestoEstatal() {
        return impuestoEstatal;
    }

    public void setImpuestoEstatal(Float impuestoEstatal) {
        this.impuestoEstatal = impuestoEstatal;
    }

    public Float getRiesgoDeTrabajo9() {
        return riesgoDeTrabajo9;
    }

    public void setRiesgoDeTrabajo9(Float riesgoDeTrabajo9) {
        this.riesgoDeTrabajo9 = riesgoDeTrabajo9;
    }

    public Float getImssEmpresa() {
        return imssEmpresa;
    }

    public void setImssEmpresa(Float imssEmpresa) {
        this.imssEmpresa = imssEmpresa;
    }

    public Float getInfonavitEmpresa() {
        return infonavitEmpresa;
    }

    public void setInfonavitEmpresa(Float infonavitEmpresa) {
        this.infonavitEmpresa = infonavitEmpresa;
    }

    public Float getGuarderiaImss7() {
        return guarderiaImss7;
    }

    public void setGuarderiaImss7(Float guarderiaImss7) {
        this.guarderiaImss7 = guarderiaImss7;
    }

    public Float getOtrasObligaciones() {
        return otrasObligaciones;
    }

    public void setOtrasObligaciones(Float otrasObligaciones) {
        this.otrasObligaciones = otrasObligaciones;
    }

    public Float getTotalObligaciones() {
        return totalObligaciones;
    }

    public void setTotalObligaciones(Float totalObligaciones) {
        this.totalObligaciones = totalObligaciones;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public DetEmpleado getIdEmpleadoCreador() {
        return idEmpleadoCreador;
    }

    public void setIdEmpleadoCreador(DetEmpleado idEmpleadoCreador) {
        this.idEmpleadoCreador = idEmpleadoCreador;
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
	
	

    
    
}
