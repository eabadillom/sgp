package mx.com.ferbo.dto;

import java.util.Objects;

public class NominaPercepcionDTOPK {
	
	private Integer idPercepcion;
	private NominaDTO nomina;
	
	public NominaPercepcionDTOPK() {
		
	}
	public NominaPercepcionDTOPK(Integer idPercepcion, NominaDTO nomina) {
		this.idPercepcion = idPercepcion;
		this.nomina = nomina;
	}
	public Integer getIdPercepcion() {
		return idPercepcion;
	}
	public void setIdPercepcion(Integer idPercepcion) {
		this.idPercepcion = idPercepcion;
	}
	public NominaDTO getNomina() {
		return nomina;
	}
	public void setNomina(NominaDTO nomina) {
		this.nomina = nomina;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idPercepcion, nomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaPercepcionDTOPK other = (NominaPercepcionDTOPK) obj;
		return Objects.equals(idPercepcion, other.idPercepcion) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaPercepcionDTOPK [idPercepcion=" + idPercepcion + ", nomina=" + nomina + "]";
	}
}
