package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import mx.com.ferbo.model.imss.DetIncapacidad;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_estatus_incapacidad")
@NamedQueries({
    @NamedQuery(name = "CatEstatusIncapacidad.findAll", query = "SELECT ces FROM CatEstatusIncapacidad ces"),
    @NamedQuery(name = "CatEstatusIncapacidad.findByClave", query = "SELECT ces FROM CatEstatusIncapacidad ces WHERE ces.clave = :clave")
})
public class CatEstatusIncapacidad implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_estatus_inc")
    private Integer idEstatusSolicitud;
    
    @Basic(optional = false)
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "estatusSolicitud")
    private List<DetIncapacidad> registroEstatusSolicitudes;

    public CatEstatusIncapacidad() 
    {
    }

    public CatEstatusIncapacidad(Integer idEstatusSolicitud) 
    {
        this.idEstatusSolicitud = idEstatusSolicitud;
    }

    public CatEstatusIncapacidad(String clave) 
    {
        this.clave = clave;
    }

    public Integer getIdEstatusSolicitud() 
    {
        return idEstatusSolicitud;
    }

    public void setIdEstatusSolicitud(Integer idEstatusSolicitud) 
    {
        this.idEstatusSolicitud = idEstatusSolicitud;
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
    
    public List<DetIncapacidad> getRegistroEstatusSolicitudes() 
    {
        return registroEstatusSolicitudes;
    }

    public void setRegistroEstatusSolicitudes(List<DetIncapacidad> registroEstatusSolicitudes) 
    {
        this.registroEstatusSolicitudes = registroEstatusSolicitudes;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.idEstatusSolicitud);
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
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final CatEstatusIncapacidad other = (CatEstatusIncapacidad) obj;
        return Objects.equals(this.idEstatusSolicitud, other.idEstatusSolicitud);
    }

    @Override
    public String toString() 
    {
        return "CatEstatusIncapacidad[" + "IdEstatusSolicitud: " + idEstatusSolicitud + ", Clave: " + clave + ", Descripcion: " + descripcion + ']';
    }
    
}
