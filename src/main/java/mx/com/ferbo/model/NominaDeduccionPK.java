package mx.com.ferbo.model;

import java.util.Objects;

public class NominaDeduccionPK {
	private Integer idDeduccion;
	private Nomina nomina;
	
	public NominaDeduccionPK() {
		
	}
	public NominaDeduccionPK(Integer idDeduccion, Nomina nomina) {
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
	public Nomina getNomina() {
		return nomina;
	}
	public void setNomina(Nomina nomina) {
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
		NominaDeduccionPK other = (NominaDeduccionPK) obj;
		return Objects.equals(idDeduccion, other.idDeduccion) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaDeduccionDTOPK [idDeduccion=" + idDeduccion + ", nomina=" + nomina + "]";
	}
}
