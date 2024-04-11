package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatSubsidioDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer idSubsidio;
    private BigDecimal paraIngresosDe;
    private BigDecimal hastaIngresosDe;
    private BigDecimal cantidadSubsidio;
    private Date fechaSubsidio;

    public CatSubsidioDTO() {
    }

    public CatSubsidioDTO(Integer idSubsidio, BigDecimal paraIngresosDe, BigDecimal hastaIngresosDe, BigDecimal cantidadSubsidio, Date fechaSubsidio) {
        this.idSubsidio = idSubsidio;
        this.paraIngresosDe = paraIngresosDe;
        this.hastaIngresosDe = hastaIngresosDe;
        this.cantidadSubsidio = cantidadSubsidio;
        this.fechaSubsidio = fechaSubsidio;
    }

    public Integer getIdSubsidio() {
        return idSubsidio;
    }

    public void setIdSubsidio(Integer idSubsidio) {
        this.idSubsidio = idSubsidio;
    }

    public BigDecimal getParaIngresosDe() {
        return paraIngresosDe;
    }

    public void setParaIngresosDe(BigDecimal paraIngresosDe) {
        this.paraIngresosDe = paraIngresosDe;
    }

    public BigDecimal getHastaIngresosDe() {
        return hastaIngresosDe;
    }

    public void setHastaIngresosDe(BigDecimal hastaIngresosDe) {
        this.hastaIngresosDe = hastaIngresosDe;
    }

    public BigDecimal getCantidadSubsidio() {
        return cantidadSubsidio;
    }

    public void setCantidadSubsidio(BigDecimal cantidadSubsidio) {
        this.cantidadSubsidio = cantidadSubsidio;
    }

    public Date getFechaSubsidio() {
        return fechaSubsidio;
    }

    public void setFechaSubsidio(Date fechaSubsidio) {
        this.fechaSubsidio = fechaSubsidio;
    }
    
    
}
