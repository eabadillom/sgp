package mx.com.ferbo.dto;

import java.util.Date;
import java.util.Objects;

public class DiaNoLaboralDTO {
	private Integer id;
	private Date fecha;
	private String descripcion;
	private PaisDTO pais;
	public DiaNoLaboralDTO(Integer id, Date fecha, String descripcion, PaisDTO pais) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.pais = pais;
	}
	public DiaNoLaboralDTO(Integer id, Date fecha, String descripcion, String clavePais, String nombrePais) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.pais = new PaisDTO(clavePais, nombrePais);
	} 
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public PaisDTO getPais() {
		return pais;
	}
	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}
	@Override
	public int hashCode() {
		return Objects.hash(descripcion, fecha, id, pais);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiaNoLaboralDTO other = (DiaNoLaboralDTO) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(pais, other.pais);
	}
	@Override
	public String toString() {
		return "DiaNoLaboralDTO [id=" + id + ", fecha=" + fecha + ", descripcion=" + descripcion + ", pais=" + pais
				+ "]";
	}
	
}
