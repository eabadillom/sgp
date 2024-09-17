package mx.com.ferbo.model;

import java.util.Objects;

public class NominaPercepcionPK {
	
	private Integer idPercepcion;
	private Nomina nomina;
	
	public NominaPercepcionPK() {
		
	}
	public NominaPercepcionPK(Integer idPercepcion, Nomina nomina) {
		this.idPercepcion = idPercepcion;
		this.nomina = nomina;
	}
	public Integer getIdPercepcion() {
		return idPercepcion;
	}
	public void setIdPercepcion(Integer idPercepcion) {
		this.idPercepcion = idPercepcion;
	}
	public Nomina getNomina() {
		return nomina;
	}
	public void setNomina(Nomina nomina) {
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
		NominaPercepcionPK other = (NominaPercepcionPK) obj;
		return Objects.equals(idPercepcion, other.idPercepcion) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaPercepcionDTOPK [idPercepcion=" + idPercepcion + ", nomina=" + nomina + "]";
	}
}
