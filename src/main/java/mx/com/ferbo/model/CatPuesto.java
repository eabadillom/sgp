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

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "cat_puesto")
@NamedQueries({
	@NamedQuery(name = "CatPuesto.findActive", query = "SELECT c FROM CatPuesto c WHERE c.activo = 1 "),
    @NamedQuery(name = "CatPuesto.findAll", query = "SELECT new mx.com.ferbo.dto.CatPuestoDTO("
            + " c.idPuesto,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPuesto c"),
    @NamedQuery(name = "CatPuesto.findByActive", query = "SELECT NEW mx.com.ferbo.dto.CatPuestoDTO("
            + " c.idPuesto,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPuesto c"
            + " WHERE c.activo = 1")})
public class CatPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_puesto")
    private Integer idPuesto;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;

    public CatPuesto() {
    }

    public CatPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public CatPuesto(Integer idPuesto, String descripcion, short activo) {
        this.idPuesto = idPuesto;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
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
		return Objects.hash(idPuesto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPuesto other = (CatPuesto) obj;
		return Objects.equals(idPuesto, other.idPuesto);
	}

	@Override
	public String toString() {
		return "CatPuesto [idPuesto=" + idPuesto + ", descripcion=" + descripcion + ", activo=" + activo + "]";
	}
}
