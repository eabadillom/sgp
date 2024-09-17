package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class NominaDeduccion {
	
	private NominaDeduccionPK key;
	private CatTipoDeduccion tipoDeduccion;
	private String clave;
	private String nombre;
	private BigDecimal importe;
	
	public NominaDeduccionPK getKey() {
		return key;
	}
	public void setKey(NominaDeduccionPK key) {
		this.key = key;
	}
	public CatTipoDeduccion getTipoDeduccion() {
		return tipoDeduccion;
	}
	public void setTipoDeduccion(CatTipoDeduccion tipoDeduccion) {
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
		return Objects.hash(key);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NominaDeduccion other = (NominaDeduccion) obj;
		return Objects.equals(key, other.key);
	}
	@Override
	public String toString() {
		return "NominaDeduccionDTO [key=" + key + ", clave=" + clave + ", nombre=" + nombre + ", importe=" + importe
				+ "]";
	}
}
