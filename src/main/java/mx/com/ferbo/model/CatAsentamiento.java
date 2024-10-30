package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_asentamiento")
@NamedQueries({
    @NamedQuery(name = "CatAsentamiento.findAll", query = "SELECT ca FROM CatAsentamiento ca"),
    @NamedQuery(name = "CatAsentamiento.findByCodigoPostal", query = "SELECT ca FROM CatAsentamiento ca WHERE ca.cp = :codigoPostal")
})
public class CatAsentamiento implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private CatAsentamientoPK key;
    @Size(max = 150)
    @Column(name = "nb_asentamiento")
    private String descripcion;
    @Size(max = 5)
    @Column(name = "cd_codPostal")
    private String cp;
    @JoinColumn(name = "cd_localidad", referencedColumnName = "cd_localidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CatLocalidad localidad;
    @JoinColumn(name = "cd_tipoasntmnto", referencedColumnName = "cd_tipoasntmnto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CatTipoAsentamiento tipoAsentamiento;
    @JoinColumn(name = "cd_entidadPostal", referencedColumnName = "cd_entidadPostal", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CatEntidadPostal entidadPostal;

    public CatAsentamiento() 
    {
    }

    public CatAsentamiento(CatAsentamientoPK key) 
    {
        this.key = key;
    }
    
    public CatAsentamiento(Pais pais, CatEstado estado, CatMunicipio municipio, CatLocalidad localidad, CatTipoAsentamiento tipoasntmnto, CatEntidadPostal entidadpostal, Integer id) 
    {
        this.key = new CatAsentamientoPK(pais, estado, municipio, localidad, tipoasntmnto, entidadpostal, id);
    }

    public CatAsentamientoPK getKey() 
    {
        return key;
    }

    public void setKey(CatAsentamientoPK key) 
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

    public String getCp() 
    {
        return cp;
    }

    public void setCp(String cp) 
    {
        this.cp = cp;
    }

    public CatLocalidad getLocalidad() 
    {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) 
    {
        this.localidad = localidad;
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
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.key);
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
        final CatAsentamiento other = (CatAsentamiento) obj;
        return Objects.equals(this.key, other.key);
    }

    @Override
    public String toString() {
        return "CatAsentamiento[" + "key=" + key + ", descripcion=" + descripcion + ", cp=" + cp + ']';
    }
    
}
