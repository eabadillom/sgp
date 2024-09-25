package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cat_cuota_imss")
@NamedQueries({
	@NamedQuery(name = "CatCuotaIMSS.findByClavePeriodoCuota", query = "SELECT new mx.com.ferbo.dto.CuotaIMSSDTO(c.key.clave, c.key.numeroClave, c.descripcion, c.baseSalarial, c.baseMinimo, c.baseMaximo, c.cuota, c.tipoCuota, c.vigencia) from CatCuotaIMSS c WHERE c.tipoCuota = :tipoCuota AND c.key.clave = :clave and c.vigencia BETWEEN :fechaInicio AND :fechaFin and :cuota BETWEEN c.baseMinimo AND c.baseMaximo"),
	@NamedQuery(name = "CatCuotaIMSS.findByClavePeriodoBase", query = "SELECT c FROM CatCuotaIMSS c WHERE c.tipoCuota = :tipoCuota AND c.key.clave = :clave and c.vigencia BETWEEN :fechaInicio AND :fechaFin and :base BETWEEN c.baseMinimo AND c.baseMaximo"),
        @NamedQuery(name = "CatCuotaIMSS.findAll", query = "SELECT c FROM CatCuotaIMSS c")
})
public class CatCuotaIMSS implements Serializable {

	private static final long serialVersionUID = 5778397805651350052L;
	
	@EmbeddedId
	private CatCuotaIMSSPK key;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "descripcion")
	private String descripcion;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "base_salarial")
	private String baseSalarial;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "base_min")
	private BigDecimal baseMinimo;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "base_max")
	private BigDecimal baseMaximo;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota")
	private BigDecimal cuota;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "tipo_cuota")
	private String tipoCuota;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "vigencia")
	private Date vigencia;

	public CatCuotaIMSSPK getKey() {
		return key;
	}

	public void setKey(CatCuotaIMSSPK primaryKey) {
		this.key = primaryKey;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getBaseSalarial() {
		return baseSalarial;
	}

	public void setBaseSalarial(String baseSalarial) {
		this.baseSalarial = baseSalarial;
	}

	public BigDecimal getBaseMinimo() {
		return baseMinimo;
	}

	public void setBaseMinimo(BigDecimal baseMinimo) {
		this.baseMinimo = baseMinimo;
	}

	public BigDecimal getBaseMaximo() {
		return baseMaximo;
	}

	public void setBaseMaximo(BigDecimal baseMaximo) {
		this.baseMaximo = baseMaximo;
	}

	public BigDecimal getCuota() {
		return cuota;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public String getTipoCuota() {
		return tipoCuota;
	}

	public void setTipoCuota(String tipoCuota) {
		this.tipoCuota = tipoCuota;
	}

	public Date getVigencia() {
		return vigencia;
	}

	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}
}
