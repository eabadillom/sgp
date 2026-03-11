package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
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

import org.bouncycastle.util.Objects;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "cat_estatus_incidencia")
@NamedQueries({
    @NamedQuery(name = "CatEstatusIncidencia.findAll", query = "SELECT c FROM CatEstatusIncidencia c"),
    @NamedQuery(name = "CatEstatusIncidencia.findByClave", query = "SELECT c FROM CatEstatusIncidencia c WHERE c.clave = :clave")
})
public class CatEstatusIncidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estatus")
    private Integer idEstatus;
    
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "clave")
    private String clave;
    
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    
    @OneToMany(mappedBy = "estatusIncidencia")
    private List<DetIncidencia> detIncidenciaList;
    
    @Override
    public int hashCode() {
    	if(this.idEstatus == null)
    		return System.identityHashCode(this);
    	return Objects.hashCode(this.idEstatus);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CatEstatusIncidencia))
            return false;
        
        CatEstatusIncidencia other = (CatEstatusIncidencia) object;
        if ((this.idEstatus == null && other.idEstatus != null) || (this.idEstatus != null && !this.idEstatus.equals(other.idEstatus)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return "CatEstatusIncidencia[" + "idEstatus=" + idEstatus + ", descripcion=" + descripcion + ", clave=" + clave + ']';
    }

    public CatEstatusIncidencia() {
    }

    public CatEstatusIncidencia(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public CatEstatusIncidencia(Integer idEstatus, String descripcion, short activo) {
        this.idEstatus = idEstatus;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public List<DetIncidencia> getDetIncidenciaList() {
        return detIncidenciaList;
    }

    public void setDetIncidenciaList(List<DetIncidencia> detIncidenciaList) {
        this.detIncidenciaList = detIncidenciaList;
    }
}
