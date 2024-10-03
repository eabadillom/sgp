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

/**
 *
 * @author erale
 */
@Entity
@Table(name = "cat_tarifa_isr")
@NamedQueries({
    @NamedQuery(name = "CatTarifaISR.findAll", query = "SELECT i FROM CatTarifaISR i"),
    @NamedQuery(name = "CatTarifaISR.findByTipoAnioBaseISR", query = "SELECT i FROM CatTarifaISR i WHERE i.tipo = :tipo AND i.fecha BETWEEN :fechaInicio AND :fechaFin")})
public class CatTarifaISR implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_isr")
    private Integer idIsr;
    
    @Column(name = "limite_inferior")
    private BigDecimal limiteInferior;
    
    @Column(name = "limite_superior")
    private BigDecimal limiteSuperior;
    
    @Column(name = "cuota_fija")
    private BigDecimal cuotaFija;
    
    /**Porcentaje para aplicarse sobre el excedente
     */
    @Column(name = "porc_apl_exce_lim_inf")
    private BigDecimal porcAplExceLimInf;
    
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "fecha")
    private Date fecha;

    public CatTarifaISR() {
    }

    public CatTarifaISR(Integer idIsr) {
        this.idIsr = idIsr;
    }

    public CatTarifaISR(Integer idIsr, BigDecimal limiteInferior, BigDecimal limiteSuperior, BigDecimal cuotaFija, BigDecimal porcAplExceLimInf, String tipo, Date fecha) {
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
