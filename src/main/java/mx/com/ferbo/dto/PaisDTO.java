package mx.com.ferbo.dto;

import java.util.Objects;

public class PaisDTO {
        private Integer idPais;
	private String clavePais;
	private String nombrePais;
	
	public PaisDTO(Integer idPais, String clavePais, String nombrePais) {
		super();
                this.idPais = idPais;
		this.clavePais = clavePais;
		this.nombrePais = nombrePais;
	}
        public Integer getIdPais() {
            return idPais;
        }

        public void setIdPais(Integer idPais) {
            this.idPais = idPais;
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
