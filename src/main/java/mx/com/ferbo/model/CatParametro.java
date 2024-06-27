package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_parametro")
@NamedQueries({
	@NamedQuery(name = "CatParametro.buscarPorClave", query = "SELECT p FROM CatParametro p WHERE p.clave = :clave")
})
public class CatParametro implements Serializable {

	private static final long serialVersionUID = 9034391699548706275L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "cd_id")
	private Integer id = null;
	
	@Column(name = "cd_parametro")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave = null;
	
	@Column(name = "nb_parametro")
	@Basic(optional = false)
	@Size(max = 255)
	private String valor = null;

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
		CatParametro other = (CatParametro) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CatParametro [id=" + id + ", clave=" + clave + ", valor=" + valor + "]";
	}
}
