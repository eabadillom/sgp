package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author erale
 */
public class DetNominaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idNomina;
    private DetEmpleadoDTO idEmpleado;
    private Float sueldo;
    private Float septimoDia;
    private Float horasExtras;
    private Float destajos;
    private Float premiosEficiencia;
    private Float bonoPuntualidad;
    private Float despensa;
    private Float otrasPercepciones;
    private Float totalPercepciones;
    private Float retInvYVida;
    private Float retCesantia;
    private Float retEnfYMatObrero;
    private Float prestamoInfonavitFd;
    private Float prestamoInfonavitCf;
    private Float subsAlEmpleoAcreditado;
    private Float subsAlEmpleoMes;
    private Float isrAntesDeSubsAlEmpleo;
    private Float isrMes;
    private Float imss;
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
    private Float neto;
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

    public DetNominaDTO(Integer idNomina, DetEmpleadoDTO idEmpleado, Float sueldo, Float septimoDia, Float horasExtras, Float destajos, Float premiosEficiencia, Float bonoPuntualidad, Float despensa, Float otrasPercepciones, Float totalPercepciones, Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, Float subsAlEmpleoAcreditado, Float subsAlEmpleoMes, Float isrAntesDeSubsAlEmpleo, Float isrMes, Float imss, Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, Float totalDeducciones, Float neto, Float invalidezYVida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, DetEmpleadoDTO idEmpleadoCreador) {
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
                        String segundoAp, Float sueldo, Float septimoDia, Float horasExtras, Float destajos, 
                        Float premiosEficiencia, Float bonoPuntualidad, Float despensa, Float otrasPercepciones, Float totalPercepciones, 
                        Float retInvYVida, Float retCesantia, Float retEnfYMatObrero, Float prestamoInfonavitFd, Float prestamoInfonavitCf, 
                        Float subsAlEmpleoAcreditado, Float subsAlEmpleoMes, Float isrAntesDeSubsAlEmpleo, Float isrMes, Float imss, 
                        Float prestamoFonacot, Float ajusteEnSubsidioParaElEmpleo, Float subsEntregadoQueNoCorrespondia, Float ajusteAlNeto, Float isrDeAjusteMensual, 
                        Float isrAjustadoPorSubsidio, Float ajusteAlSubsidioCausado, Float pensionAlimienticia, Float otrasDeducciones, Float totalDeducciones, 
                        Float neto, Float invalidezYVida, Float cesantiaYVejez, Float enfYMatPatron, Float fondoRetiroSar, 
                        Float impuestoEstatal, Float riesgoDeTrabajo9, Float imssEmpresa, Float infonavitEmpresa, Float guarderiaImss7, 
                        Float otrasObligaciones, Float totalObligaciones, Date fechaCreacion, Integer idEmpleadoC, String numEmpleadoC, 
                        String nombreC, String primerApC, String segundoApC) {
        this.idNomina = idNomina;
        this.idEmpleado = new DetEmpleadoDTO(idEmpleado, numEmpleado, nombre, primerAp, segundoAp);
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

    public DetEmpleadoDTO getIdEmpleadoCreador() {
        return idEmpleadoCreador;
    }

    public void setIdEmpleadoCreador(DetEmpleadoDTO idEmpleadoCreador) {
        this.idEmpleadoCreador = idEmpleadoCreador;
    }
    
}
