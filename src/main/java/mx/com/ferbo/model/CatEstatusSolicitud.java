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

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_st_solicitud")
@NamedQueries({
    @NamedQuery(name = "CatEstatusSolicitud.findAll", query = "SELECT ces FROM CatEstatusSolicitud ces"),
    @NamedQuery(name = "CatEstatusSolicitud.findByClave", query = "SELECT ces FROM CatEstatusSolicitud ces WHERE ces.clave = :clave")
})
public class CatEstatusSolicitud implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_st_solicitud")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "clave")
    private String clave;
    
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "estatus")
    private List<DetSolicitudArticulo> solicitudArticuloList;
    
    @OneToMany(mappedBy = "estatus")
    private List<DetSolicitudPermiso> solicitudPermisoList;
    
    @OneToMany(mappedBy = "estatus")
    private List<DetSolicitudPrenda> solicitudPrendaList;

    public CatEstatusSolicitud() 
    {
    }

    public CatEstatusSolicitud(String clave, String descripcion) 
    {
        this.clave = clave;
        this.descripcion = descripcion;
    }

    public Integer getId() 
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
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

    public List<DetSolicitudArticulo> getSolicitudArticuloList() 
    {
        return solicitudArticuloList;
    }

    public void setSolicitudArticuloList(List<DetSolicitudArticulo> solicitudArticuloList) 
    {
        this.solicitudArticuloList = solicitudArticuloList;
    }

    public List<DetSolicitudPermiso> getSolicitudPermisoList() 
    {
        return solicitudPermisoList;
    }

    public void setSolicitudPermisoList(List<DetSolicitudPermiso> solicitudPermisoList) 
    {
        this.solicitudPermisoList = solicitudPermisoList;
    }

    public List<DetSolicitudPrenda> getSolicitudPrendaList() 
    {
        return solicitudPrendaList;
    }

    public void setSolicitudPrendaList(List<DetSolicitudPrenda> solicitudPrendaList) 
    {
        this.solicitudPrendaList = solicitudPrendaList;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final CatEstatusSolicitud other = (CatEstatusSolicitud) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() 
    {
        return "CatEstatusSolicitud[" + "id=" + id + ", clave=" + clave + ", descripcion=" + descripcion + ']';
    }
    
}
    

