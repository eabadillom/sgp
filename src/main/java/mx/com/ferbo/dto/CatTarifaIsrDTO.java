package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatTarifaIsrDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer idIsr;
    private BigDecimal limiteInferior;
    private BigDecimal limiteSuperior;
    private BigDecimal cuotaFija;
    /**Porcentaje para aplicarse sobre el excedente
     */
    private BigDecimal porcAplExceLimInf;
    private String tipo;
    private Date fecha;

    public CatTarifaIsrDTO() {
    }

    public CatTarifaIsrDTO(Integer idIsr, BigDecimal limiteInferior, BigDecimal limiteSuperior, BigDecimal cuotaFija, BigDecimal porcAplExceLimInf, String tipo, Date fecha) {
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

    public BigDecimal getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(BigDecimal limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public BigDecimal getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(BigDecimal limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public BigDecimal getCuotaFija() {
        return cuotaFija;
    }

    public void setCuotaFija(BigDecimal cuotaFija) {
        this.cuotaFija = cuotaFija;
    }

    public BigDecimal getPorcAplExceLimInf() {
        return porcAplExceLimInf;
    }

    public void setPorcAplExceLimInf(BigDecimal porcAplExceLimInf) {
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
