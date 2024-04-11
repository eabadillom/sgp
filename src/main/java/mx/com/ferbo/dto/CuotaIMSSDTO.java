package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class CuotaIMSSDTO {
	
	private String clave;
	private Integer numeroClave;
	private String descripcion;
	private String baseSalarial;
	private BigDecimal baseMinimo;
	private BigDecimal baseMaximo;
	private BigDecimal cuota;
	private String tipoCuota;
	private Date vigencia;
	
	public CuotaIMSSDTO() {
	}
	
	public CuotaIMSSDTO(String clave, Integer numeroClave, String descripcion, String baseSalarial,
			BigDecimal baseMinimo, BigDecimal baseMaximo, BigDecimal cuota, String tipoCuota, Date vigencia) {
		super();
		this.clave = clave;
		this.numeroClave = numeroClave;
		this.descripcion = descripcion;
		this.baseSalarial = baseSalarial;
		this.baseMinimo = baseMinimo;
		this.baseMaximo = baseMaximo;
		this.cuota = cuota;
		this.tipoCuota = tipoCuota;
		this.vigencia = vigencia;
	}
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Integer getNumeroClave() {
		return numeroClave;
	}
	public void setNumeroClave(Integer numeroClave) {
		this.numeroClave = numeroClave;
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

	@Override
	public String toString() {
		return "CuotaIMSSDTO [clave=" + clave + ", numeroClave=" + numeroClave + ", descripcion=" + descripcion
				+ ", baseSalarial=" + baseSalarial + ", baseMinimo=" + baseMinimo + ", baseMaximo=" + baseMaximo
				+ ", cuota=" + cuota + ", tipoCuota=" + tipoCuota + ", vigencia=" + vigencia + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseMaximo, baseMinimo, baseSalarial, clave, cuota, descripcion, numeroClave, tipoCuota,
				vigencia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CuotaIMSSDTO other = (CuotaIMSSDTO) obj;
		return Objects.equals(baseMaximo, other.baseMaximo) && Objects.equals(baseMinimo, other.baseMinimo)
				&& Objects.equals(baseSalarial, other.baseSalarial) && Objects.equals(clave, other.clave)
				&& Objects.equals(cuota, other.cuota) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(numeroClave, other.numeroClave) && Objects.equals(tipoCuota, other.tipoCuota)
				&& Objects.equals(vigencia, other.vigencia);
	}
}
