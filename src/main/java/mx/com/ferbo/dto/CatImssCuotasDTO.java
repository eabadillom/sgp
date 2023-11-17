package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatImssCuotasDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idImssCuotas;
    private float riesgoTrabajo;
    private float enfMatEspCtFija;
    private float enfMatEspCtAd;
    private float enfMatGastosMed;
    private float enfMatDinero;
    private float invVida;
    private float retCesantiaVejezRetiro;
    private float retCesantiaVejezCeav;
    private float guarderia;
    private float infonavit;
    private String cuota;
    private Date fechaCap;
    private short activo;

    public CatImssCuotasDTO() {
    }

    public CatImssCuotasDTO(Integer idImssCuotas, float riesgoTrabajo, float enfMatEspCtFija, float enfMatEspCtAd, float enfMatGastosMed, float enfMatDinero, float invVida, float retCesantiaVejezRetiro, float retCesantiaVejezCeav, float guarderia, float infonavit, String cuota, Date fechaCap, short activo) {
        this.idImssCuotas = idImssCuotas;
        this.riesgoTrabajo = riesgoTrabajo;
        this.enfMatEspCtFija = enfMatEspCtFija;
        this.enfMatEspCtAd = enfMatEspCtAd;
        this.enfMatGastosMed = enfMatGastosMed;
        this.enfMatDinero = enfMatDinero;
        this.invVida = invVida;
        this.retCesantiaVejezRetiro = retCesantiaVejezRetiro;
        this.retCesantiaVejezCeav = retCesantiaVejezCeav;
        this.guarderia = guarderia;
        this.infonavit = infonavit;
        this.cuota = cuota;
        this.fechaCap = fechaCap;
        this.activo = activo;
    }

    public Integer getIdImssCuotas() {
        return idImssCuotas;
    }

    public void setIdImssCuotas(Integer idImssCuotas) {
        this.idImssCuotas = idImssCuotas;
    }

    public float getRiesgoTrabajo() {
        return riesgoTrabajo;
    }

    public void setRiesgoTrabajo(float riesgoTrabajo) {
        this.riesgoTrabajo = riesgoTrabajo;
    }

    public float getEnfMatEspCtFija() {
        return enfMatEspCtFija;
    }

    public void setEnfMatEspCtFija(float enfMatEspCtFija) {
        this.enfMatEspCtFija = enfMatEspCtFija;
    }

    public float getEnfMatEspCtAd() {
        return enfMatEspCtAd;
    }

    public void setEnfMatEspCtAd(float enfMatEspCtAd) {
        this.enfMatEspCtAd = enfMatEspCtAd;
    }

    public float getEnfMatGastosMed() {
        return enfMatGastosMed;
    }

    public void setEnfMatGastosMed(float enfMatGastosMed) {
        this.enfMatGastosMed = enfMatGastosMed;
    }

    public float getEnfMatDinero() {
        return enfMatDinero;
    }

    public void setEnfMatDinero(float enfMatDinero) {
        this.enfMatDinero = enfMatDinero;
    }

    public float getInvVida() {
        return invVida;
    }

    public void setInvVida(float invVida) {
        this.invVida = invVida;
    }

    public float getRetCesantiaVejezRetiro() {
        return retCesantiaVejezRetiro;
    }

    public void setRetCesantiaVejezRetiro(float retCesantiaVejezRetiro) {
        this.retCesantiaVejezRetiro = retCesantiaVejezRetiro;
    }

    public float getRetCesantiaVejezCeav() {
        return retCesantiaVejezCeav;
    }

    public void setRetCesantiaVejezCeav(float retCesantiaVejezCeav) {
        this.retCesantiaVejezCeav = retCesantiaVejezCeav;
    }

    public float getGuarderia() {
        return guarderia;
    }

    public void setGuarderia(float guarderia) {
        this.guarderia = guarderia;
    }

    public float getInfonavit() {
        return infonavit;
    }

    public void setInfonavit(float infonavit) {
        this.infonavit = infonavit;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public Date getFechaCap() {
        return fechaCap;
    }

    public void setFechaCap(Date fechaCap) {
        this.fechaCap = fechaCap;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

}
