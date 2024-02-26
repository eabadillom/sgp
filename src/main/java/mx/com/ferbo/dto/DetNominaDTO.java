package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author erale
 */
public class DetNominaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idNomina;
    private DetEmpleadoDTO idEmpleado;
    
    //PERCEPCIONES
    private BigDecimal sueldo;
    private BigDecimal salarioDiario;
    private BigDecimal salarioDiarioIntegrado;
    private BigDecimal asistencia;
    private BigDecimal septimoDia;
    private BigDecimal horasExtras;
    private BigDecimal destajos;
    private BigDecimal premiosEficiencia;
    private BigDecimal bonoPuntualidad;
    private BigDecimal despensa;
    private BigDecimal otrasPercepciones;
    private BigDecimal totalPercepciones;
    
    //DEDUCCIONES
    private Float retInvYVida;
    private Float retCesantia;
    private Float retEnfYMatObrero;
    private Float prestamoInfonavitFd;
    private Float prestamoInfonavitCf;
    private BigDecimal subsAlEmpleoAcreditado;
    private BigDecimal subsAlEmpleoMes;
    private BigDecimal isrAntesDeSubsAlEmpleo;
    private BigDecimal isr;
    private BigDecimal imss;
    private Float prestamoFonacot;
    private Float ajusteEnSubsidioParaElEmpleo;
    private Float subsEntregadoQueNoCorrespondia;
    private Float ajusteAlNeto;
    private Float isrDeAjusteMensual;
    private Float isrAjustadoPorSubsidio;
    private Float ajusteAlSubsidioCausado;
    private Float pensionAlimienticia;
    private Float otrasDeducciones;
    private Float totalDeducciones;
    
    //TOTAL PERCEPCIONES - DEDUCCIONES
    private Float neto;
    
    // ¿¿APORTACIONES PATRONALES??
    private Float invalidezYVida;
    private Float cesantiaYVejez;
    private Float enfYMatPatron;
    private Float fondoRetiroSar;
    private Float impuestoEstatal;
    private Float riesgoDeTrabajo9;
    private Float imssEmpresa;
    private Float infonavitEmpresa;
    private Float guarderiaImss7;
    private Float otrasObligaciones;
    private Float totalObligaciones;
    private Date fechaCreacion;
    private DetEmpleadoDTO idEmpleadoCreador;

    public DetNominaDTO() {
    	
    }

    public DetNominaDTO(Integer idNomina, DetEmpleadoDTO idEmpleado, BigDecimal sueldo, BigDecimal septimoDia, BigDecimal horasExtras, BigDecimal destajos, BigDecimal premiosEficiencia, BigDecimal bonoPuntualidad, BigDecimal despensa, BigDecimal otrasPercepciones, BigDecimal totalPercepciones, Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, BigDecimal subsAlEmpleoAcreditado, BigDecimal subsAlEmpleoMes, BigDecimal isrAntesDeSubsAlEmpleo, BigDecimal isrMes, BigDecimal imss, Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, Float totalDeducciones, Float neto, Float invalidezYVida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, DetEmpleadoDTO idEmpleadoCreador) {
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
        this.isr = isrMes;
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
        this.invalidezYVida = invalidezYVida;
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
    
    public DetNominaDTO(Integer idNomina, Integer idEmpleado, String numEmpleado, String nombre, String primerAp, 
                        String segundoAp, BigDecimal sueldo, BigDecimal septimoDia, BigDecimal horasExtras, BigDecimal destajos, 
                        BigDecimal premiosEficiencia, BigDecimal bonoPuntualidad, BigDecimal despensa, BigDecimal otrasPercepciones, BigDecimal totalPercepciones, 
                        Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, 
                        BigDecimal subsAlEmpleoAcreditado, BigDecimal subsAlEmpleoMes, BigDecimal isrAntesDeSubsAlEmpleo, BigDecimal isrMes, BigDecimal imss, 
                        Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, 
                        Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, Float totalDeducciones, 
                        Float neto, Float invalidezYVida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, 
                        Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, 
                        Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, Integer idEmpleadoC, String numEmpleadoC, 
                        String nombreC, String primerApC, String segundoApC) {
        this.idNomina = idNomina;
        this.idEmpleado = new DetEmpleadoDTO(idEmpleado, numEmpleado, nombre, primerAp, segundoAp);
        
        //PERCEPCIONES
        this.sueldo = sueldo;
        this.septimoDia = septimoDia;
        this.horasExtras = horasExtras;
        this.destajos = destajos;
        this.premiosEficiencia = premiosEficiencia;
        this.bonoPuntualidad = bonoPuntualidad;
        this.despensa = despensa;
        this.otrasPercepciones = otrasPercepciones;
        this.totalPercepciones = totalPercepciones;
        
        //DEDUCCIONES
        this.retInvYVida = retInvYVida;
        this.retCesantia = retCesantia;
        this.retEnfYMatObrero = retEnfYMatObrero;
        this.prestamoInfonavitFd = prestamoInfonavitFd;
        this.prestamoInfonavitCf = prestamoInfonavitCf;
        this.subsAlEmpleoAcreditado = subsAlEmpleoAcreditado;
        this.subsAlEmpleoMes = subsAlEmpleoMes;
        this.isrAntesDeSubsAlEmpleo = isrAntesDeSubsAlEmpleo;
        this.isr = isrMes;
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
        
        //TOTAL PERCEPCIONES - DEDUCCIONES
        this.neto = neto;
        
        //¿¿CUOTAS PATRONALES??
        this.invalidezYVida = invalidezYVida;
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
        this.idEmpleadoCreador = new DetEmpleadoDTO(idEmpleadoC, numEmpleadoC, nombreC, primerApC, segundoApC);
    }

    public Integer getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Integer idNomina) {
        this.idNomina = idNomina;
    }

    public DetEmpleadoDTO getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleadoDTO idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }
    
    public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
	
	public BigDecimal getAsistencia() {
		return asistencia;
	}

	public void setAsistencia(BigDecimal asistencia) {
		this.asistencia = asistencia;
	}
    
    public BigDecimal getSalarioDiarioIntegrado() {
		return salarioDiarioIntegrado;
	}

	public void setSalarioDiarioIntegrado(BigDecimal salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
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

    public BigDecimal getIsr() {
        return isr;
    }

    public void setIsr(BigDecimal isr) {
        this.isr = isr;
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

    public DetEmpleadoDTO getIdEmpleadoCreador() {
        return idEmpleadoCreador;
    }

    public void setIdEmpleadoCreador(DetEmpleadoDTO idEmpleadoCreador) {
        this.idEmpleadoCreador = idEmpleadoCreador;
    }
}
