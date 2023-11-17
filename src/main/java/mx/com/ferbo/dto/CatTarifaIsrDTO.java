package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatTarifaIsrDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer idIsr;
    private Float limiteInferior;
    private Float limiteSuperior;
    private Float cuotaFija;
    private Float porcAplExceLimInf;
    private String tipo;
    private Date fecha;

    public CatTarifaIsrDTO() {
    }

    public CatTarifaIsrDTO(Integer idIsr, Float limiteInferior, Float limiteSuperior, Float cuotaFija, Float porcAplExceLimInf, String tipo, Date fecha) {
        this.idIsr = idIsr;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.cuotaFija = cuotaFija;
        this.porcAplExceLimInf = porcAplExceLimInf;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public Integer getIdIsr() {
        return idIsr;
    }

    public void setIdIsr(Integer idIsr) {
        this.idIsr = idIsr;
    }

    public Float getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(Float limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public Float getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(Float limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public Float getCuotaFija() {
        return cuotaFija;
    }

    public void setCuotaFija(Float cuotaFija) {
        this.cuotaFija = cuotaFija;
    }

    public Float getPorcAplExceLimInf() {
        return porcAplExceLimInf;
    }

    public void setPorcAplExceLimInf(Float porcAplExceLimInf) {
        this.porcAplExceLimInf = porcAplExceLimInf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
