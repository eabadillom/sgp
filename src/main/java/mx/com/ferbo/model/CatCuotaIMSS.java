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
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_cuota_imss")
@NamedQueries({
	@NamedQuery(name = "CatCuotaIMSS.findByClavePeriodoCuota", query = "SELECT new mx.com.ferbo.dto.CuotaIMSSDTO(c.clave, c.numero, c.descripcion, c.baseSalarial, c.baseMinimo, c.baseMaximo, c.cuota, c.tipoCuota, c.vigenciaInicio) from CatCuotaIMSS c WHERE c.tipoCuota = :tipoCuota AND c.clave = :clave and c.vigenciaInicio BETWEEN :fechaInicio AND :fechaFin and :cuota BETWEEN c.baseMinimo AND c.baseMaximo"),
	@NamedQuery(name = "CatCuotaIMSS.findByClavePeriodoBase", query = "SELECT c FROM CatCuotaIMSS c WHERE c.tipoCuota = :tipoCuota AND c.clave = :clave and c.vigenciaInicio BETWEEN :fechaInicio AND :fechaFin and :base BETWEEN c.baseMinimo AND c.baseMaximo"),
	@NamedQuery(name = "CatCuotaIMSS.findByPeriodo", query = "SELECT c FROM CatCuotaIMSS c WHERE (:fecha BETWEEN c.vigenciaInicio AND c.vigenciaFin) OR (c.vigenciaInicio <= :fecha AND c.vigenciaFin IS NULL)"),
	@NamedQuery(name = "CatCuotaIMSS.findAll", query = "SELECT c FROM CatCuotaIMSS c")
})
public class CatCuotaIMSS implements Serializable {

	private static final long serialVersionUID = 5778397805651350052L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional =  false)
	@Column(name = "id_cuota")
	private Integer id;
	
	@Column(name = "cd_cuota")
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_clave")
	private Integer numero;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nb_cuota")
	private String descripcion;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nb_base_salarial")
	private String baseSalarial;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nu_base_min")
	private BigDecimal baseMinimo;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nu_base_max")
	private BigDecimal baseMaximo;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nu_cuota")
	private BigDecimal cuota;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nb_tipo_cuota")
	private String tipoCuota;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "fh_vigencia_ini")
	private Date vigenciaInicio;
	
	@Basic(optional = true)
	@Column(name = "fh_vigencia_fin")
	private Date vigenciaFin;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
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

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}
}
