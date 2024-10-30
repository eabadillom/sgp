package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "cat_estado")
@NamedQueries({
    @NamedQuery(name = "CatEstado.findAll", query = "SELECT ce FROM CatEstado ce")
})
public class CatEstado implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    CatEstadoPK key;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cat_estado")
    private List<CatMunicipio> municipios;
    @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pais pais;

    public CatEstado() 
    {
    }

    public CatEstado(CatEstadoPK key) 
    {
        this.key = key;
    }

    public CatEstado(CatEstadoPK key, String clave, String descripcion) 
    {
        this.key = key;
        this.clave = clave;
        this.descripcion = descripcion;
    }

    public CatEstadoPK getEstadoPK() 
    {
        return key;
    }

    public void setEstadoPK(CatEstadoPK key) 
    {
        this.key = key;
    }

    public String getEstadoClave() 
    {
        return clave;
    }

    public void setEstadoClave(String estadoClave) 
    {
        this.clave = estadoClave;
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

    public Pais getPais() 
    {
        return pais;
    }

    public void setPais(Pais pais) 
    {
        this.pais = pais;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.key);
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
        return Objects.equals(this.key, other.key);
    }

    @Override
    public String toString() 
    {
        return "CatEstado[" + "estadoPK=" + key + ", estadoClave=" + clave + ", estadoDesc=" + descripcion + ']';
    }
    
}
