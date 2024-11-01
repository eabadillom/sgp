package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
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
    
    @ManyToOne
    @NotNull
    private CatLocalidad localidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_asentamiento")
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_tipoasntmnto", referencedColumnName = "cd_tipoasntmnto")
    private CatTipoAsentamiento tipoAsentamiento;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_entidadPostal", referencedColumnName = "cd_entidadPostal")
    private CatEntidadPostal entidadPostal;

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

    public Integer getAsentamiento() 
    {
        return id;
    }

    public void setAsentamiento(Integer id) 
    {
        this.id = id;
    }
    
    public CatTipoAsentamiento getTipoAsentamiento() 
    {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(CatTipoAsentamiento tipoAsentamiento) 
    {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public CatEntidadPostal getEntidadPostal() 
    {
        return entidadPostal;
    }

    public void setEntidadPostal(CatEntidadPostal entidadPostal) 
    {
        this.entidadPostal = entidadPostal;
    }
    
    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.localidad);
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
        if (!Objects.equals(this.localidad, other.localidad)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatAsentamientoPK[" + "localidad=" + localidad + ", asentamiento=" + id + ']';
    }
    
}
