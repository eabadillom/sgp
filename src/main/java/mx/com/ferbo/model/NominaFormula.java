package mx.com.ferbo.model;

import java.time.LocalDate;
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
@Table(name = "det_nom_formula")
@NamedQueries({
	@NamedQuery(name = "NominaFormula.buscarVigente", query = "SELECT f FROM NominaFormula f WHERE f.clave = :clave AND ( (:fecha BETWEEN f.vigenciaInicio AND f.vigenciaFin) OR (f.vigenciaInicio <= :fecha AND f.vigenciaFin IS NULL) )")
})
public class NominaFormula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "id_formula")
	private Integer id;
	
	@Basic(optional = false)
	@Column(name = "cd_formula")
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_formula")
	private String nombre;
	
	@Basic(optional = false)
	@Column(name = "fh_inicio")
	private LocalDate vigenciaInicio;
	
	@Basic(optional = true)
	@Column(name = "fh_fin")
	private LocalDate vigenciaFin;
	
	@Basic(optional = false)
	@Column(name = "nb_clase")
	private String clase;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NominaFormula)) {
			return false;
		}
		NominaFormula other = (NominaFormula) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "NominaFormula [id=" + id + ", clave=" + clave + ", nombre=" + nombre + ", vigenciaInicio="
				+ vigenciaInicio + ", vigenciaFin=" + vigenciaFin + ", clase=" + clase + "]";
	}

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}
}
