package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class NominaOtroPago {
	
	private NominaOtroPagoPK key;
	private CatTipoOtroPago tipoOtroPago;
	private String clave;
	private String nombre;
	private BigDecimal importe;
	public NominaOtroPagoPK getKey() {
		return key;
	}
	public void setKey(NominaOtroPagoPK key) {
		this.key = key;
	}
	public CatTipoOtroPago getTipoOtroPago() {
		return tipoOtroPago;
	}
	public void setTipoOtroPago(CatTipoOtroPago otroPago) {
		this.tipoOtroPago = otroPago;
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
		NominaOtroPago other = (NominaOtroPago) obj;
		return Objects.equals(key, other.key);
	}
	@Override
	public String toString() {
		return "NominaOtroPago [key=" + key + ", clave=" + clave + ", nombre=" + nombre + ", importe=" + importe + "]";
	}
}
