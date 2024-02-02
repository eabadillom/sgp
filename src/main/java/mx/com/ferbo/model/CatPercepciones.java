package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author erale
 */
@Entity
@Table(name = "cat_percepciones")
@NamedQueries({
    @NamedQuery(name = "CatPercepciones"
            + ".findAll", query = "SELECT p FROM CatPercepciones p"),
    @NamedQuery(name = "CatPercepciones.findByActive", query = "SELECT NEW mx.com.ferbo.dto.CatPercepcionesDTO("
                                                  + " cp.idPercepciones,"
                                                  + " cp.diasAguinaldo,"
                                                  + " cp.diasVacaciones,"
                                                  + " cp.primaVacacional,"
                                                  + " cp.bonoPuntualidad,"
                                                  + " cp.valeDespensa,"
                                                  + " cp.uma,"
                                                  + " cp.fechaCap,"
                                                  + " cp.activo"
                                                  + ")"
                                                  + " FROM CatPercepciones cp"
                                                  + " WHERE cp.activo = 1"),
    @NamedQuery(name = "CatPercepciones.findByFecha", query = "SELECT NEW mx.com.ferbo.dto.CatPercepcionesDTO("
                                                  + " cp.idPercepciones,"
                                                  + " cp.diasAguinaldo,"
                                                  + " cp.diasVacaciones,"
                                                  + " cp.primaVacacional,"
                                                  + " cp.bonoPuntualidad,"
                                                  + " cp.valeDespensa,"
                                                  + " cp.uma,"
                                                  + " cp.fechaCap,"
                                                  + " cp.activo"
                                                  + ")"
                                                  + " FROM CatPercepciones cp"
                                                  + " WHERE cp.fechaCap LIKE :fechaCap AND cp.activo = 1"),
    @NamedQuery(name = "CatPercepciones.findLastActive", query =
    		  "SELECT NEW mx.com.ferbo.dto.CatPercepcionesDTO("
            + " cp.idPercepciones,"
            + " cp.diasAguinaldo,"
            + " cp.diasVacaciones,"
            + " cp.primaVacacional,"
            + " cp.bonoPuntualidad,"
            + " cp.valeDespensa,"
            + " cp.uma,"
            + " cp.fechaCap,"
            + " cp.activo"
            + ")"
            + " FROM CatPercepciones cp"
            + " WHERE cp.fechaCap = (SELECT MAX(p.fechaCap) FROM CatPercepciones p WHERE p.activo = 1 and p.fechaCap <= :fechaCorte )")
})
public class CatPercepciones implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_percepciones")
    private Integer idPercepciones;
    
    @NotNull
    @Column(name = "dias_aguinaldo")
    private Integer diasAguinaldo;
    
    @NotNull
    @Column(name = "dias_vacaciones")
    private Integer diasVacaciones;
    
    @NotNull
    @Column(name = "prima_vacacional")
    private BigDecimal primaVacacional;
    
    @NotNull
    @Column(name = "bono_puntualidad")
    private BigDecimal bonoPuntualidad;
    
    @NotNull
    @Column(name = "vale_despensa")
    private BigDecimal valeDespensa;
    
    @NotNull
    @Column(name = "uma")
    private BigDecimal uma;
    
    @Column(name = "fecha_cap")
    private Date fechaCap;
    
    @NotNull
    @Column(name = "activo")
    private short activo;

    public CatPercepciones() {
    }

    public CatPercepciones(Integer idPercepciones) {
        this.idPercepciones = idPercepciones;
    }

    public CatPercepciones(Integer idPercepciones, Integer diasAguinaldo, Integer diasVacaciones, BigDecimal primaVacacional, BigDecimal bonoPuntualidad, BigDecimal valeDespensa, BigDecimal uma, Date fechaCap, short activo) {
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
