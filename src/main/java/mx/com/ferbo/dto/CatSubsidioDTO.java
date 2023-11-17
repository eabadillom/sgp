package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatSubsidioDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer idSubsidio;
    private Float paraIngresosDe;
    private Float hastaIngresosDe;
    private Float cantidadSubsidio;
    private Date fechaubsidio;

    public CatSubsidioDTO() {
    }

    public CatSubsidioDTO(Integer idSubsidio, Float paraIngresosDe, Float hastaIngresosDe, Float cantidadSubsidio, Date fechaubsidio) {
        this.idSubsidio = idSubsidio;
        this.paraIngresosDe = paraIngresosDe;
        this.hastaIngresosDe = hastaIngresosDe;
        this.cantidadSubsidio = cantidadSubsidio;
        this.fechaubsidio = fechaubsidio;
    }

    public Integer getIdSubsidio() {
        return idSubsidio;
    }

    public void setIdSubsidio(Integer idSubsidio) {
        this.idSubsidio = idSubsidio;
    }

    public Float getParaIngresosDe() {
        return paraIngresosDe;
    }

    public void setParaIngresosDe(Float paraIngresosDe) {
        this.paraIngresosDe = paraIngresosDe;
    }

    public Float getHastaIngresosDe() {
        return hastaIngresosDe;
    }

    public void setHastaIngresosDe(Float hastaIngresosDe) {
        this.hastaIngresosDe = hastaIngresosDe;
    }

    public Float getCantidadSubsidio() {
        return cantidadSubsidio;
    }

    public void setCantidadSubsidio(Float cantidadSubsidio) {
        this.cantidadSubsidio = cantidadSubsidio;
    }

    public Date getFechaubsidio() {
        return fechaubsidio;
    }

    public void setFechaubsidio(Date fechaubsidio) {
        this.fechaubsidio = fechaubsidio;
    }
    
    
}
