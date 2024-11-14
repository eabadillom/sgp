package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_entidad_postal")
@NamedQueries({
    @NamedQuery(name = "CatEntidadPostal.findAll", query = "SELECT ep FROM CatEntidadPostal ep")
})
public class CatEntidadPostal implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_entidadpostal")
    private Integer id;
    
    @Size(max = 100)
    @Column(name = "nu_entidadpostal")
    private String descripcion;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "entidadPostal")
    private List<CatAsentamiento> asentamientos;

    public CatEntidadPostal() 
    {
    }

    public CatEntidadPostal(Integer id) 
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }

    public List<CatAsentamiento> getAsentamientos() 
    {
        return asentamientos;
    }

    public void setAsentamientos(List<CatAsentamiento> asentamientos) 
    {
        this.asentamientos = asentamientos;
    }

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatEntidadPostal other = (CatEntidadPostal) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatEntidadPostal[" + "id=" + id + ", descripcion=" + descripcion + ']';
    }
    
}
