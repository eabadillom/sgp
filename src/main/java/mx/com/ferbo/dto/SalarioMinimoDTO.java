package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class SalarioMinimoDTO {
	private Integer id;
	private Date vigencia;
	private BigDecimal zonaG;
	private BigDecimal zonaLFN;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getVigencia() {
		return vigencia;
	}
	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}
	public BigDecimal getZonaG() {
		return zonaG;
	}
	public void setZonaG(BigDecimal zonaG) {
		this.zonaG = zonaG;
	}
	public BigDecimal getZonaLFN() {
		return zonaLFN;
	}
	public void setZonaLFN(BigDecimal zonaLFN) {
		this.zonaLFN = zonaLFN;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalarioMinimoDTO other = (SalarioMinimoDTO) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"vigencia\":\"" + vigencia + "\", \"zonaG\":\"" + zonaG + "\", \"zonaLFN\":\""
				+ zonaLFN + "\"}";
	}
}
