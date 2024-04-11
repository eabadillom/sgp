package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cat_dia_no_laboral")
@NamedQueries({
	@NamedQuery(name = "CatDiaNoLaboral.buscaPorPeriodo", query = "SELECT d FROM CatDiaNoLaboral d WHERE d.pais.clavePais = :clavePais AND (d.fecha >= :fechaInicio AND d.fecha <= :fechaFin) ORDER BY d.fecha ASC")
})
public class CatDiaNoLaboral implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional =  false)
	@Column(name = "id_fecha")
	private Integer id;
	
	@Column(name = "fh_fecha")
	private Date fecha;
	
	@Column(name = "nb_descripcion")
	private String descripcion;
	
	@JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais")
	@ManyToOne
	private Pais pais;
	
	public CatDiaNoLaboral() {
	}
	
	public CatDiaNoLaboral(Integer id, Date fecha, String descripcion, Pais pais) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.pais = pais;
	}
	
	public CatDiaNoLaboral(Integer id, Date fecha, String descripcion, String clavePais, String nombrePais) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.pais = new Pais(clavePais, nombrePais);
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

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
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
		CatDiaNoLaboral other = (CatDiaNoLaboral) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(pais, other.pais);
	}

	@Override
	public String toString() {
		return "DetDiaNoLaboral [id=" + id + ", fecha=" + fecha + ", descripcion=" + descripcion + "]";
	}
}
