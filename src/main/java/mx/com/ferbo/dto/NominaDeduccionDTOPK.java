package mx.com.ferbo.dto;

import java.util.Objects;

public class NominaDeduccionDTOPK {
	private Integer idDeduccion;
	private NominaDTO nomina;
	
	public NominaDeduccionDTOPK() {
		
	}
	public NominaDeduccionDTOPK(Integer idDeduccion, NominaDTO nomina) {
		super();
		this.idDeduccion = idDeduccion;
		this.nomina = nomina;
	}
	public Integer getIdDeduccion() {
		return idDeduccion;
	}
	public void setIdDeduccion(Integer idDeduccion) {
		this.idDeduccion = idDeduccion;
	}
	public NominaDTO getNomina() {
		return nomina;
	}
	public void setNomina(NominaDTO nomina) {
		this.nomina = nomina;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idDeduccion, nomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaDeduccionDTOPK other = (NominaDeduccionDTOPK) obj;
		return Objects.equals(idDeduccion, other.idDeduccion) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaDeduccionDTOPK [idDeduccion=" + idDeduccion + ", nomina=" + nomina + "]";
	}
}
