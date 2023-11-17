package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "cat_tipo_solicitud")
@NamedQueries({
    @NamedQuery(name = "CatTipoSolicitud.findAll", query = "SELECT c FROM CatTipoSolicitud c"),
    @NamedQuery(name = "CatTipoSolicitud.findByActive", query = "SELECT NEW mx.com.ferbo.dto.CatTipoSolicitudDTO("
            + " c.idTipoSolicitud,"
            + " c.descripcion"
            + ")"
            + " FROM CatTipoSolicitud c"
            + " WHERE c.activo = 1")
    
})
public class CatTipoSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_solicitud")
    private Integer idTipoSolicitud;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private Short activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoSolicitud")
    private List<DetSolicitudPermiso> detSolicitudPermisoList;

    public CatTipoSolicitud() {
    }

    public CatTipoSolicitud(Integer idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public CatTipoSolicitud(Integer idTipoSolicitud, String descripcion) {
        this.idTipoSolicitud = idTipoSolicitud;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(Integer idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
        this.activo = activo;
    }

    public List<DetSolicitudPermiso> getDetSolicitudPermisoList() {
        return detSolicitudPermisoList;
    }

    public void setDetSolicitudPermisoList(List<DetSolicitudPermiso> detSolicitudPermisoList) {
        this.detSolicitudPermisoList = detSolicitudPermisoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSolicitud != null ? idTipoSolicitud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatTipoSolicitud)) {
            return false;
        }
        CatTipoSolicitud other = (CatTipoSolicitud) object;
        if ((this.idTipoSolicitud == null && other.idTipoSolicitud != null) || (this.idTipoSolicitud != null && !this.idTipoSolicitud.equals(other.idTipoSolicitud))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CatTipoSolicitud[ idTipoSolicitud=" + idTipoSolicitud + " ]";
    }
    
}
