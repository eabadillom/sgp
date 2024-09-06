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

@Entity
@Table(name = "cat_planta")
@NamedQueries({
	@NamedQuery(name = "CatPlanta.findActive", query = "SELECT c FROM CatPlanta c WHERE c.activo = 1"),
    @NamedQuery(name = "CatPlanta.findAll", query = "SELECT new mx.com.ferbo.dto.CatPlantaDTO("
            + " c.idPlanta,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPlanta c"),
    @NamedQuery(name = "CatPlanta.getAll", query = "SELECT p FROM CatPlanta p ORDER BY p.descripcion"),
    @NamedQuery(name = "CatPlanta.findByActive", query = "SELECT new mx.com.ferbo.dto.CatPlantaDTO("
            + " c.idPlanta,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPlanta c"
            + " WHERE c.activo = 1")})
public class CatPlanta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_planta")
    private Integer idPlanta;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    
    public CatPlanta() {
    }

    public CatPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
    }

    public CatPlanta(Integer idPlanta, String descripcion, short activo) {
        this.idPlanta = idPlanta;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
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
    public String toString() {
        return "mx.com.ferbo.model.CatPlanta[ idPlanta=" + idPlanta + " ]";
    }

	@Override
	public int hashCode() {
		return Objects.hash(idPlanta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPlanta other = (CatPlanta) obj;
		return Objects.equals(idPlanta, other.idPlanta);
	}
    
}
