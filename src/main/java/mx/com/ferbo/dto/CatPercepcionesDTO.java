package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author erale
 */
public class CatPercepcionesDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer idPercepciones;
    private Integer diasAguinaldo;
    private Integer diasVacaciones;
    private BigDecimal primaVacacional;
    private BigDecimal bonoPuntualidad;
    private BigDecimal valeDespensa;
    private BigDecimal uma;
    private Date fechaCap;
    private short activo;

    public CatPercepcionesDTO() {
    }
    
    public CatPercepcionesDTO(Integer idPercepciones, Integer diasAguinaldo, Integer diasVacaciones, BigDecimal primaVacacional, BigDecimal bonoPuntualidad, BigDecimal valeDespensa, BigDecimal uma, Date fechaCap, short activo) {
        this.idPercepciones = idPercepciones;
        this.diasAguinaldo = diasAguinaldo;
        this.diasVacaciones = diasVacaciones;
        this.primaVacacional = primaVacacional;
        this.bonoPuntualidad = bonoPuntualidad;
        this.valeDespensa = valeDespensa;
        this.uma = uma;
        this.fechaCap = fechaCap;
        this.activo = activo;
    }

    public Integer getIdPercepciones() {
        return idPercepciones;
    }

    public void setIdPercepciones(Integer idPercepciones) {
        this.idPercepciones = idPercepciones;
    }

    public Integer getDiasAguinaldo() {
        return diasAguinaldo;
    }

    public void setDiasAguinaldo(Integer diasAguinaldo) {
        this.diasAguinaldo = diasAguinaldo;
    }

    public Integer getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(Integer diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public BigDecimal getPrimaVacacional() {
        return primaVacacional;
    }

    public void setPrimaVacacional(BigDecimal primaVacacional) {
        this.primaVacacional = primaVacacional;
    }

    public BigDecimal getBonoPuntualidad() {
        return bonoPuntualidad;
    }

    public void setBonoPuntualidad(BigDecimal bonoPuntualidad) {
        this.bonoPuntualidad = bonoPuntualidad;
    }

    public BigDecimal getValeDespensa() {
        return valeDespensa;
    }

    public void setValeDespensa(BigDecimal valeDespensa) {
        this.valeDespensa = valeDespensa;
    }

    public BigDecimal getUma() {
        return uma;
    }

    public void setUma(BigDecimal uma) {
        this.uma = uma;
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
