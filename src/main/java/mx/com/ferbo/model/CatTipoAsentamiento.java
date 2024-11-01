package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "cat_tipo_asentamiento")
@NamedQueries({
    @NamedQuery(name = "CatTipoAsentamiento.findAll", query = "SELECT cl FROM CatLocalidad cl")
})
public class CatTipoAsentamiento implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_tipoasntmnto")
    private Short id;
    
    @Size(max = 100)
    @Column(name = "nb_tipoasntmnto")
    private String descripcion;
    
    @Size(max = 4)
    @Column(name = "nb_clave")
    private String clave;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "key.tipoAsentamiento")
    private List<CatAsentamiento> asentamientos;

    public CatTipoAsentamiento() 
    {
    }

    public CatTipoAsentamiento(Short id) 
    {
        this.id = id;
    }

    public Short getId() 
    {
        return id;
    }

    public void setId(Short id) 
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

    public String getClave() 
    {
        return clave;
    }

    public void setClave(String clave) 
    {
        this.clave = clave;
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
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final CatTipoAsentamiento other = (CatTipoAsentamiento) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() 
    {
        return "CatTipoAsentamiento[" + "id=" + id + ", descripcion=" + descripcion + ", clave=" + clave + ", asentamientos=" + asentamientos + ']';
    }
    
}
