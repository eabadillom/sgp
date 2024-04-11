package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

@Entity
@Table(name = "cat_salario_minimo")
@NamedQueries({
	@NamedQuery(name = "CatSalarioMinimo.findAll", query = "SELECT s FROM CatSalarioMinimo s ORDER BY s.vigencia ASC")
})
public class CatSalarioMinimo implements Serializable {

	private static final long serialVersionUID = -4452009701354540893L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_salario_minimo")
	private Integer id;
	
	@Basic(optional = false)
	@Column(name = "vigencia")
	private Date vigencia;
	
	@Basic(optional = false)
	@Column(name = "zona_g")
	private BigDecimal zonaG;
	
	@Basic(optional = false)
	@Column(name = "zona_lfn")
	private BigDecimal zonaLFN;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getVigencia() {
		return vigencia;
	}

	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}

	public BigDecimal getZonaG() {
		return zonaG;
	}

	public void setZonaG(BigDecimal zonaG) {
		this.zonaG = zonaG;
	}

	public BigDecimal getZonaLFN() {
		return zonaLFN;
	}

	public void setZonaLFN(BigDecimal zonaLFN) {
		this.zonaLFN = zonaLFN;
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
		CatSalarioMinimo other = (CatSalarioMinimo) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"vigencia\":\"" + vigencia + "\", \"zonaG\":\"" + zonaG + "\", \"zonaLFN\":\""
				+ zonaLFN + "\"}";
	}
}
