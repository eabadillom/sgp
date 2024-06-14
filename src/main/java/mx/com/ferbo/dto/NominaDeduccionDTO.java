package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.dto.sat.TipoDeduccionDTO;

public class NominaDeduccionDTO {
	
	private Integer idNomina;
	private Integer idDeduccion;
	private TipoDeduccionDTO tipoDeduccion;
	private String clave;
	private String nombre;
	private BigDecimal importe;
	
	public Integer getIdNomina() {
		return idNomina;
	}
	public void setIdNomina(Integer idNomina) {
		this.idNomina = idNomina;
	}
	public Integer getIdDeduccion() {
		return idDeduccion;
	}
	public void setIdDeduccion(Integer idDeduccion) {
		this.idDeduccion = idDeduccion;
	}
	public TipoDeduccionDTO getTipoDeduccion() {
		return tipoDeduccion;
	}
	public void setTipoDeduccion(TipoDeduccionDTO tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idDeduccion, idNomina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaDeduccionDTO other = (NominaDeduccionDTO) obj;
		return Objects.equals(idDeduccion, other.idDeduccion) && Objects.equals(idNomina, other.idNomina);
	}
	@Override
	public String toString() {
		return "NominaDeduccionDTO [idNomina=" + idNomina + ", idDeduccion=" + idDeduccion + ", tipoDeduccion="
				+ tipoDeduccion + ", clave=" + clave + ", nombre=" + nombre + ", importe=" + importe + "]";
	}
}
