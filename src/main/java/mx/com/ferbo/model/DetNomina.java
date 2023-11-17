package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author erale
 */
@Entity
@Table(name = "det_nomina")
@NamedQueries({
    @NamedQuery(name = "DetNomina.findAll", query = "SELECT n FROM DetNomina n"),
    @NamedQuery(name = "DetNomina.findByFecha", query = "SELECT NEW mx.com.ferbo.dto.DetNominaDTO(n.idNomina, n.idEmpleado, n.sueldo, n.septimoDia, n.horasExtras, n.destajos, n.premiosEficiencia, n.bonoPuntualidad, n.despensa, n.otrasPercepciones, n.totalPercepciones, n.retInvYVida, n.retCesantia, n.retEnfYMatObrero, n.prestamoInfonavitFd, n.prestamoInfonavitCf, n.subsAlEmpleoAcreditado, n.subsAlEmpleoMes, n.isrAntesDeSubsAlEmpleo, n.isrMes, n.imss, n.prestamoFonacot, n.ajusteEnSubsidioParaElEmpleo, n.subsEntregadoQueNoCorrespondia, n.ajusteAlNeto, n.isrDeAjusteMensual, n.isrAjustadoPorSubsidio, n.ajusteAlSubsidioCausado, n.pensionAlimienticia, n.otrasDeducciones, n.totalDeducciones, n.neto, n.invalidezYVida, n.cesantiaYVejez, n.enfYMatPatron, n.fondoRetiroSar, n.impuestoEstatal, n.riesgoDeTrabajo9, n.imssEmpresa, n.infonavitEmpresa, n.guarderiaImss7, n.otrasObligaciones, n.totalObligaciones, n.fechaCreacion, n.idEmpleadoCreador) FROM DetNomina n WHERE n.fechaCreacion LIKE :fechaCreacion")
})
public class DetNomina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nomina")
    private Integer idNomina;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado idEmpleado;
    @NotNull
    @Column(name = "sueldo")
    private Float sueldo;
    @NotNull
    @Column(name = "septimo_dia")
    private Float septimoDia;
    @NotNull
    @Column(name = "horas_extras")
    private Float horasExtras;
    @NotNull
    @Column(name = "destajos")
    private Float destajos;
    @NotNull
    @Column(name = "premios_eficiencia")
    private Float premiosEficiencia;
    @NotNull
    @Column(name = "bono_puntualidad")
    private Float bonoPuntualidad;
    @NotNull
    @Column(name = "despensa")
    private Float despensa;
    @NotNull
    @Column(name = "otras_percepciones")
    private Float otrasPercepciones;
    @NotNull
    @Column(name = "total_percepciones")
    private Float totalPercepciones;
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
    private Float subsAlEmpleoAcreditado;
    @NotNull
    @Column(name = "subs_al_empleo_mes")
    private Float subsAlEmpleoMes;
    @NotNull
    @Column(name = "isr_antes_de_subs_al_empleo")
    private Float isrAntesDeSubsAlEmpleo;
    @NotNull
    @Column(name = "isr_mes")
    private Float isrMes;
    @NotNull
    @Column(name = "imss")
    private Float imss;
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
    private Float totalDeducciones;
    @NotNull
    @Column(name = "neto")
    private Float neto;
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
        this.idNomina = idNomina;
    }

    public DetNomina(Integer idNomina, DetEmpleado idEmpleado, Float sueldo, Float septimoDia, Float horasExtras, Float destajos, Float premiosEficiencia, Float bonoPuntualidad, Float despensa, Float otrasPercepciones, Float totalPercepciones, Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, Float subsAlEmpleoAcreditado, Float subsAlEmpleoMes, Float isrAntesDeSubsAlEmpleo, Float isrMes, Float imss, Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, Float totalDeducciones, Float neto, Float invalidezYVvida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, DetEmpleado idEmpleadoCreador) {
        this.idNomina = idNomina;
        this.idEmpleado = idEmpleado;
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

    public Integer getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Integer idNomina) {
        this.idNomina = idNomina;
    }

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Float getSueldo() {
        return sueldo;
    }

    public void setSueldo(Float sueldo) {
        this.sueldo = sueldo;
    }

    public Float getSeptimoDia() {
        return septimoDia;
    }

    public void setSeptimoDia(Float septimoDia) {
        this.septimoDia = septimoDia;
    }

    public Float getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Float horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Float getDestajos() {
        return destajos;
    }

    public void setDestajos(Float destajos) {
        this.destajos = destajos;
    }

    public Float getPremiosEficiencia() {
        return premiosEficiencia;
    }

    public void setPremiosEficiencia(Float premiosEficiencia) {
        this.premiosEficiencia = premiosEficiencia;
    }

    public Float getBonoPuntualidad() {
        return bonoPuntualidad;
    }

    public void setBonoPuntualidad(Float bonoPuntualidad) {
        this.bonoPuntualidad = bonoPuntualidad;
    }

    public Float getDespensa() {
        return despensa;
    }

    public void setDespensa(Float despensa) {
        this.despensa = despensa;
    }

    public Float getOtrasPercepciones() {
        return otrasPercepciones;
    }

    public void setOtrasPercepciones(Float otrasPercepciones) {
        this.otrasPercepciones = otrasPercepciones;
    }

    public Float getTotalPercepciones() {
        return totalPercepciones;
    }

    public void setTotalPercepciones(Float totalPercepciones) {
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

    public Float getSubsAlEmpleoAcreditado() {
        return subsAlEmpleoAcreditado;
    }

    public void setSubsAlEmpleoAcreditado(Float subsAlEmpleoAcreditado) {
        this.subsAlEmpleoAcreditado = subsAlEmpleoAcreditado;
    }

    public Float getSubsAlEmpleoMes() {
        return subsAlEmpleoMes;
    }

    public void setSubsAlEmpleoMes(Float subsAlEmpleoMes) {
        this.subsAlEmpleoMes = subsAlEmpleoMes;
    }

    public Float getIsrAntesDeSubsAlEmpleo() {
        return isrAntesDeSubsAlEmpleo;
    }

    public void setIsrAntesDeSubsAlEmpleo(Float isrAntesDeSubsAlEmpleo) {
        this.isrAntesDeSubsAlEmpleo = isrAntesDeSubsAlEmpleo;
    }

    public Float getIsrMes() {
        return isrMes;
    }

    public void setIsrMes(Float isrMes) {
        this.isrMes = isrMes;
    }

    public Float getImss() {
        return imss;
    }

    public void setImss(Float imss) {
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

    public Float getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(Float totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public Float getNeto() {
        return neto;
    }

    public void setNeto(Float neto) {
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

    
    
}
