package mx.com.ferbo.dto;

import java.util.Objects;

public class NominaOtroPagoPK {
	private NominaDTO nomina;
	private Integer id;
	
	public NominaOtroPagoPK() {
		
	}
	public NominaOtroPagoPK(NominaDTO nomina, Integer id) {
		super();
		this.nomina = nomina;
		this.id = id;
	}
	public NominaDTO getNomina() {
		return nomina;
	}
	public void setNomina(NominaDTO nomina) {
		this.nomina = nomina;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, nomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaOtroPagoPK other = (NominaOtroPagoPK) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaOtroPagoPK [nomina=" + nomina + ", id=" + id + "]";
	}
}
