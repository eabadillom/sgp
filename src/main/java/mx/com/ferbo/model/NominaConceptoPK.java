package mx.com.ferbo.model;

import java.util.Objects;

public class NominaConceptoPK {
	private Integer idConcepto;
	private Nomina nomina;
	
	public NominaConceptoPK() {
		
	}
	public NominaConceptoPK(Integer idConcepto, Nomina nomina) {
		super();
		this.idConcepto = idConcepto;
		this.nomina = nomina;
	}
	public Integer getIdConcepto() {
		return idConcepto;
	}
	public void setIdConcepto(Integer idConcepto) {
		this.idConcepto = idConcepto;
	}
	public Nomina getNomina() {
		return nomina;
	}
	public void setNomina(Nomina nomina) {
		this.nomina = nomina;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idConcepto, nomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaConceptoPK other = (NominaConceptoPK) obj;
		return Objects.equals(idConcepto, other.idConcepto) && Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaConceptoPK [idConcepto=" + idConcepto + ", nomina=" + nomina + "]";
	}
	
}
