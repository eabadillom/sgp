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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "cat_area")
@NamedQueries({
	@NamedQuery(name = "CatArea.findActive", query = "SELECT c FROM CatArea c WHERE c.activo = 1"),
    @NamedQuery(name = "CatArea.findAll", query = "SELECT new mx.com.ferbo.dto.CatAreaDTO("
            + " c.idArea,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatArea c"),
    @NamedQuery(name = "CatArea.findByActive", query = "SELECT new mx.com.ferbo.dto.CatAreaDTO("
            + " c.idArea,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatArea c"
            + " WHERE c.activo = 1")})
public class CatArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_area")
    private Integer idArea;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private short activo;

    public CatArea() {
    }

    public CatArea(Integer idArea) {
        this.idArea = idArea;
    }

    public CatArea(Integer idArea, String descripcion, short activo) {
        this.idArea = idArea;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

	@Override
	public int hashCode() {
		return Objects.hash(idArea);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatArea other = (CatArea) obj;
		return Objects.equals(idArea, other.idArea);
	}

	@Override
	public String toString() {
		return "CatArea [idArea=" + idArea + ", descripcion=" + descripcion + ", activo=" + activo + "]";
	}
}
