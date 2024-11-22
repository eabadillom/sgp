package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Embeddable
public class CatLocalidadPK implements Serializable 
{
    private static final long serialVersionUID = 3833888070514352421L;
    
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumns(value = {
        @JoinColumn(name = "cd_municipio", referencedColumnName = "cd_municipio"),
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado"),
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais")
    },
        foreignKey = @ForeignKey(name="FK_Localidad_Estado"))
    private CatMunicipio municipio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_localidad")
    private Integer id;

    public CatLocalidadPK() 
    {
    }

    public CatLocalidadPK(CatMunicipio municipio, Integer id) 
    {
        this.municipio = municipio;
        this.id = id;
    }

    public CatMunicipio getMunicipio() 
    {
        return municipio;
    }

    public void setMunicipio(CatMunicipio municipio) 
    {
        this.municipio = municipio;
    }

    public Integer getId() 
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
    }
    
    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatLocalidadPK other = (CatLocalidadPK) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() 
    {
        return "CatLocalidadPK[" + "id=" + id + ']';
    }
    
}
