package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "cat_estado")
@NamedQueries({
    @NamedQuery(name = "CatEstado.findAll", query = "SELECT ce FROM CatEstado ce")
})
public class CatEstado implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private CatEstadoPK key;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nb_estado")
    private String descripcion;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "key.estado", fetch= FetchType.LAZY)
    private List<CatMunicipio> municipios;
    
    public CatEstado() 
    {
    }
    
    public CatEstado(Pais pais, Integer id)
    {
        this.key = new CatEstadoPK(pais, id);
    }

    public CatEstadoPK getKey() 
    {
        return key;
    }

    public void setKey(CatEstadoPK key) 
    {
        this.key = key;
    }

    public String getClave() 
    {
        return clave;
    }

    public void setClave(String clave) 
    {
        this.clave = clave;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }
    
    public List<CatMunicipio> getMunicipios() 
    {
        return municipios;
    }

    public void setMunicipios(List<CatMunicipio> municipios) 
    {
        this.municipios = municipios;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.key.getId());
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
        final CatEstado other = (CatEstado) obj;
        return Objects.equals(this.key.getId(), other.key.getId());
    }

    @Override
    public String toString() 
    {
        return "CatEstado[" + "id=" + key.getId() + ", clave=" + clave + ", descripcion=" + descripcion + ']';
    }
    
}
