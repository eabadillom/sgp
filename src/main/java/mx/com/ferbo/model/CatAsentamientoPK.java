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
public class CatAsentamientoPK implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumns(value = {
        @JoinColumn(name = "cd_localidad", referencedColumnName = "cd_localidad", updatable = false),
        @JoinColumn(name = "cd_municipio", referencedColumnName = "cd_municipio", updatable = false),
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado", updatable = false),
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais", updatable = false)
    },
        foreignKey = @ForeignKey(name="FK_Asentamiento_Loc_Mun_Est_Pais"))
    private CatLocalidad localidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_asentamiento")
    private Integer id;
    
    public CatAsentamientoPK() 
    {
    }

    public CatAsentamientoPK(CatLocalidad localidad, Integer id) 
    {
        this.localidad = localidad;
        this.id = id;
    }

    public CatLocalidad getLocalidad() 
    {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) 
    {
        this.localidad = localidad;
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
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final CatAsentamientoPK other = (CatAsentamientoPK) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatAsentamientoPK[" + "asentamiento=" + id + ']';
    }
    
}
