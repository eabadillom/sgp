package mx.com.ferbo.dto;

import java.util.Objects;

public class ParametroDTO {
	
	private Integer id;
	private String clave;
	private String valor;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroDTO other = (ParametroDTO) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "ParametroDTO [id=" + id + ", clave=" + clave + ", valor=" + valor + "]";
	}
}
