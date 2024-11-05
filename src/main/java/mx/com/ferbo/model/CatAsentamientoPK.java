package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
    @JoinColumns({
        @JoinColumn(name = "cd_localidad", referencedColumnName = "cd_localidad"),
        @JoinColumn(name = "cd_municipio", referencedColumnName = "cd_municipio"),
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado"),
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais")
    })
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
        hash = 89 * hash + Objects.hashCode(this.localidad.getKey().getId());
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
        if (!Objects.equals(this.localidad.getKey().getId(), other.localidad.getKey().getId())) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatAsentamientoPK[" + "localidad=" + localidad.getKey().getId() + ", asentamiento=" + id + ']';
    }
    
}
