package mx.com.ferbo.dto;

import java.util.Objects;

import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.model.Nomina;

public class NominaEmisorDTO {
	
	private Nomina nomina;
	private String nombre;
	private String rfc;
	private RegimenFiscalDTO regimenFiscal;
	private String codigoPostal;
	private String registroPatronal;
	
	public Nomina getNomina() {
		return nomina;
	}
	public void setNomina(Nomina nomina) {
		this.nomina = nomina;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public RegimenFiscalDTO getRegimenFiscal() {
		return regimenFiscal;
	}
	public void setRegimenFiscal(RegimenFiscalDTO regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getRegistroPatronal() {
		return registroPatronal;
	}
	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}
	@Override
	public int hashCode() {
		return Objects.hash(nomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaEmisorDTO other = (NominaEmisorDTO) obj;
		return Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaEmisorDTO [nomina=" + nomina + ", nombre=" + nombre + ", rfc=" + rfc + ", regimenFiscal="
				+ regimenFiscal + ", codigoPostal=" + codigoPostal + ", registroPatronal=" + registroPatronal + "]";
	}
}
