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
@Table(name = "cat_municipio")
@NamedQueries({
    @NamedQuery(name = "CatMunicipio.findAll", query = "SELECT cm FROM CatMunicipio cm")
})
public class CatMunicipio implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private CatMunicipioPK key;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_municipio")
    private String descripcion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "key.municipio", fetch= FetchType.LAZY)
    private List<CatLocalidad> localidades;
    
    public CatMunicipio() 
    {
    }

    public CatMunicipio(CatMunicipioPK key) 
    {
        this.key = key;
    }

    public CatMunicipio(CatMunicipioPK key, String descripcion) 
    {
        this.key = key;
        this.descripcion = descripcion;
    }
    
    public CatMunicipio(CatEstado estado, Integer id) 
    {
        this.key = new CatMunicipioPK(estado, id);
    }

    public CatMunicipioPK getKey() 
    {
        return key;
    }

    public void setKey(CatMunicipioPK key) 
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
    
    public List<CatLocalidad> getLocalidades() 
    {
        return localidades;
    }

    public void setLocalidades(List<CatLocalidad> localidades) 
    {
        this.localidades = localidades;
    }
    
    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.key.getId());
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
        final CatMunicipio other = (CatMunicipio) obj;
        return Objects.equals(this.key.getId(), other.key.getId());
    }

    @Override
    public String toString() {
        return "CatMunicipio[" + "IdMunicipio=" + key.getId() + ", descripcion=" + descripcion + ", localidades=" + localidades + ']';
    }
    
}
