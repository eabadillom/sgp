package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class CatCuotaIMSSPK implements Serializable, Cloneable {

	private static final long serialVersionUID = -6679662869402625081L;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "clave")
	private String clave;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "no_clave")
	private Integer numeroClave;

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Integer getNumeroClave() {
		return numeroClave;
	}

	public void setNumeroClave(Integer numeroClave) {
		this.numeroClave = numeroClave;
	}

	@Override
	public String toString() {
		return "CatCuotaIMSSPK [clave=" + clave + ", numeroClave=" + numeroClave + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(clave, numeroClave);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatCuotaIMSSPK other = (CatCuotaIMSSPK) obj;
		return Objects.equals(clave, other.clave) && Objects.equals(numeroClave, other.numeroClave);
	}
	
	

}
