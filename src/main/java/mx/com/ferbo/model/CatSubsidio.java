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
@Table(name = "cat_subsidio")
@NamedQueries({
    @NamedQuery(name = "CatSubsidio.findAll", query = "SELECT s FROM CatSubsidio s"),
    @NamedQuery(name = "CatSubsidio.findAllBeforeDate", query = "SELECT s FROM CatSubsidio s where s.fecha < :fecha"),
    @NamedQuery(name = "CatSubsidio.findByPeriodoTipo", query = "SELECT s FROM CatSubsidio s WHERE s.fecha BETWEEN :fechaInicio AND :fechaFin AND s.periodo = :periodo"),
    @NamedQuery(name = "CatSubsidio.findByPeriodoTipoIngreso", query = "SELECT new mx.com.ferbo.dto.CatSubsidioDTO(s.idSubsidio, s.paraIngresosDe, s.hastaIngresosDe, s.cantidadSubsidio, s.fecha) FROM CatSubsidio s WHERE s.fecha BETWEEN :fechaInicio AND :fechaFin AND s.periodo = :periodo AND :ingreso BETWEEN s.paraIngresosDe AND s.hastaIngresosDe"),
    @NamedQuery(name = "CatSubsidio.findActual", query = "SELECT NEW mx.com.ferbo.dto.CatSubsidioDTO(s.idSubsidio, s.paraIngresosDe, s.hastaIngresosDe, s.cantidadSubsidio, s.fecha) FROM CatSubsidio s WHERE s.fecha LIKE :fecha")})
public class CatSubsidio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_subsidio")
    private Integer idSubsidio;
    
    @NotNull
    @Column(name = "para_ingresos_de")
    private BigDecimal paraIngresosDe;
    
    @NotNull
    @Column(name = "hasta_ingresos_de")
    private BigDecimal hastaIngresosDe;
    
    @NotNull
    @Column(name = "cantidad_subsidio")
    private BigDecimal cantidadSubsidio;
    
    @NotNull
    @Column(name = "periodo")
    private String periodo;
    
    @NotNull
    @Column(name = "fecha")
    private Date fecha;

    public CatSubsidio() {
    }

    public CatSubsidio(Integer idSubsidio) {
        this.idSubsidio = idSubsidio;
    }

    public CatSubsidio(Integer idSubsidio, BigDecimal paraIngresosDe, BigDecimal hastaIngresosDe, BigDecimal cantidadSubsidio, Date fecha) {
        this.idSubsidio = idSubsidio;
        this.paraIngresosDe = paraIngresosDe;
        this.hastaIngresosDe = hastaIngresosDe;
        this.cantidadSubsidio = cantidadSubsidio;
        this.fecha = fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
    
    
}
