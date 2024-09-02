package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class NominaPercepcion {
	private NominaPercepcionPK key;
	private CatTipoPercepcion tipoPercepcion;
	private String clave;
	private String nombre;
	private BigDecimal importeGravado;
	private BigDecimal importeExcento;
	
	public NominaPercepcionPK getKey() {
		return key;
	}
	public void setKey(NominaPercepcionPK key) {
		this.key = key;
	}
	
	public CatTipoPercepcion getTipoPercepcion() {
		return tipoPercepcion;
	}
	public void setTipoPercepcion(CatTipoPercepcion tipoPercepcion) {
		this.tipoPercepcion = tipoPercepcion;
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
	public BigDecimal getImporteGravado() {
		return importeGravado;
	}
	public void setImporteGravado(BigDecimal importeGravado) {
		this.importeGravado = importeGravado;
	}
	public BigDecimal getImporteExcento() {
		return importeExcento;
	}
	public void setImporteExcento(BigDecimal importeExcento) {
		this.importeExcento = importeExcento;
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
		NominaPercepcion other = (NominaPercepcion) obj;
		return Objects.equals(key, other.key);
	}
	@Override
	public String toString() {
		return "NominaPercepcionDTO [key=" + key + ", clave=" + clave + ", nombre=" + nombre + ", importeGravado="
				+ importeGravado + ", importeExcento=" + importeExcento + "]";
	}
}