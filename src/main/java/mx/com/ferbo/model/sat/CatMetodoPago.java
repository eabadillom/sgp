package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_metodo_pago")
public class CatMetodoPago implements Serializable {

	private static final long serialVersionUID = -5565704335159326092L;
	
	@Id
	@Size(max = 5)
	@Column(name = "cd_metodo_pago")
	@Basic(optional = false)
	private String clave;
	
	@Column(name = "nb_metodo_pago")
	@Size(max = 100)
	@Basic(optional = false)
	private String nombre;
	
	@Column(name = "fh_vigencia_ini")
	@Basic(optional = false)
	private Date vigenciaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Basic(optional = true)
	private Date vigenciaFin;
	
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
	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}
	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}
	public Date getVigenciaFin() {
		return vigenciaFin;
	}
	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}
	@Override
	public int hashCode() {
		return Objects.hash(clave);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatMetodoPago other = (CatMetodoPago) obj;
		return Objects.equals(clave, other.clave);
	}
	@Override
	public String toString() {
		return "CatMetodoPago [clave=" + clave + ", nombre=" + nombre + ", vigenciaInicio=" + vigenciaInicio
				+ ", vigenciaFin=" + vigenciaFin + "]";
	}
}
