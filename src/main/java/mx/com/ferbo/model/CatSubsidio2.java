package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cat_subsidio_2")
@NamedQuery(name = "Subsidio2.buscarVigente", query = "SELECT s FROM CatSubsidio2 s WHERE (:fecha BETWEEN s.vigenciaInicio AND s.vigenciaFin) OR (s.vigenciaInicio <= :fecha AND s.vigenciaFin IS NULL) ")
@NamedQuery(name = "Subsidio2.buscarTodos", query = "SELECT s FROM CatSubsidio2 s ORDER BY s.vigenciaInicio")
public class CatSubsidio2 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_subsidio")
	private Integer id;
	
	@Column(name = "im_maximo")
	@Basic(optional = false)
	private BigDecimal importeMaximo;
	
	@Column(name = "im_tasa")
	@Basic(optional = false)
	private BigDecimal tasa;
	
	@Column(name = "fh_inicio")
	@Basic(optional = false)
	private LocalDate vigenciaInicio;
	
	@Column(name = "fh_fin")
	@Basic(optional = true)
	private LocalDate vigenciaFin;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CatSubsidio2)) {
			return false;
		}
		CatSubsidio2 other = (CatSubsidio2) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CatSubsidio2 [id=" + id + ", importeMaximo=" + importeMaximo + ", tasa=" + tasa + ", vigenciaInicio="
				+ vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getImporteMaximo() {
		return importeMaximo;
	}

	public void setImporteMaximo(BigDecimal importeMaximo) {
		this.importeMaximo = importeMaximo;
	}

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public LocalDate getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(LocalDate vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public LocalDate getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(LocalDate vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}
}
