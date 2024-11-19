package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
    @NamedQuery(name = "CatAsentamiento.findByCodigoPostal", query = "SELECT ca FROM CatAsentamiento ca WHERE ca.cp = :codigoPostal"),
    @NamedQuery(name = "CatAsentamiento.findByParametros", query = "SELECT ca FROM CatAsentamiento ca WHERE ca.key.id = :idAsentamiento and ca.key.localidad.key.id = :idLocalidad and ca.key.localidad.key.municipio.key.id = :idMunicipio and ca.key.localidad.key.municipio.key.estado.key.id = :idEstado and ca.key.localidad.key.municipio.key.estado.key.pais.id = :idPais")
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
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_tipoasntmnto", referencedColumnName = "cd_tipoasntmnto")
    private CatTipoAsentamiento tipoAsentamiento;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_entidadPostal", referencedColumnName = "cd_entidadPostal")
    private CatEntidadPostal entidadPostal;
    
    //@OneToOne(mappedBy = "asentamiento", fetch= FetchType.LAZY)
    //private DetDomicilioEmpleado domicilioEmpleado;
    
    public CatAsentamiento() 
    {
    }
    
    public CatAsentamiento(CatLocalidad localidad, Integer id) 
    {
        this.key = new CatAsentamientoPK(localidad, id);
    }

    public CatAsentamiento(CatLocalidad localidad, Integer id, String descripcion, String cp, CatTipoAsentamiento tipoAsentamiento, CatEntidadPostal entidadPostal) 
    {
        this.key = new CatAsentamientoPK(localidad, id);
        this.descripcion = descripcion;
        this.cp = cp;
        this.tipoAsentamiento = tipoAsentamiento;
        this.entidadPostal = entidadPostal;
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
    
    /*public DetDomicilioEmpleado getDomicilioEmpleado() 
    {
        return domicilioEmpleado;
    }

    public void setDomicilioEmpleado(DetDomicilioEmpleado domicilioEmpleado) 
    {
        this.domicilioEmpleado = domicilioEmpleado;
    }*/
    
    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.key.getId());
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
        return Objects.equals(this.key.getId(), other.key.getId());
    }
    
    @Override
    public String toString() {
        return "CatAsentamiento[" + "IdAsentamiento=" + key.getId() + ", descripcion=" + descripcion + ", cp=" + cp + ']';
    }
    
}
