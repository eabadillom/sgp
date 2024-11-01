package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "cat_localidad")
@NamedQueries({
    @NamedQuery(name = "CatLocalidad.findAll", query = "SELECT cl FROM CatLocalidad cl")
})
public class CatLocalidad implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private CatLocalidadPK key;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_localidad")
    private String descripcion;
    
    @OneToMany(mappedBy = "key.localidad")
    private List<CatAsentamiento> asentamientos;
    
    public CatLocalidad() 
    {
    }

    public CatLocalidad(CatLocalidadPK key) 
    {
        this.key = key;
    }

    public CatLocalidad(CatLocalidadPK key, String descripcion) 
    {
        this.key = key;
        this.descripcion = descripcion;
    }
    
    public CatLocalidad(CatMunicipio municipio, Integer id) 
    {
        this.key = new CatLocalidadPK(municipio, id);
    }

    public CatLocalidadPK getKey() 
    {
        return key;
    }

    public void setKey(CatLocalidadPK key) 
    {
        this.key = key;
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
        hash = 97 * hash + Objects.hashCode(this.key);
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
        final CatLocalidad other = (CatLocalidad) obj;
        return Objects.equals(this.key, other.key);
    }

    @Override
    public String toString() {
        return "CatLocalidad[" + "key=" + key + ", descripcion=" + descripcion + ", asentamientos=" + asentamientos + ']';
    }
    
}
