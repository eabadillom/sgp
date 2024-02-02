package mx.com.ferbo.dto;

import java.util.Objects;

public class PaisDTO {
	private String clavePais;
	private String nombrePais;
	
	public PaisDTO(String clavePais, String nombrePais) {
		super();
		this.clavePais = clavePais;
		this.nombrePais = nombrePais;
	}
	
	public String getClavePais() {
		return clavePais;
	}
	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}
	public String getNombrePais() {
		return nombrePais;
	}
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}
	@Override
	public String toString() {
		return "DiaNoLaboralDTO [clavePais=" + clavePais + ", nombrePais=" + nombrePais + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(clavePais, nombrePais);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaisDTO other = (PaisDTO) obj;
		return Objects.equals(clavePais, other.clavePais) && Objects.equals(nombrePais, other.nombrePais);
	}
	
}
