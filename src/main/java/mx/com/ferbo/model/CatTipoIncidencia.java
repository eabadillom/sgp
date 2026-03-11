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
 * @author Gabo
 */
@Entity
@Table(name = "cat_tipo_incidencia")
@NamedQueries({
    @NamedQuery(name = "CatTipoIncidencia.findAll", query = "SELECT c FROM CatTipoIncidencia c"),
    @NamedQuery(name = "CatTipoIncidencia.findByClave", query = "SELECT c FROM CatTipoIncidencia c WHERE c.clave = :clave")
})
public class CatTipoIncidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Integer idTipo;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "clave")
    private String clave;
    
    @OneToMany(mappedBy = "tipoIncidencia")
    private List<DetIncidencia> detIncidenciaList;
    
    @Override
    public int hashCode() {
    	if(this.idTipo == null)
    		return System.identityHashCode(this);
    	return Objects.hashCode(this.idTipo);
    }

    @Override
    public boolean equals(Object object) {
    	if(object == null)
    		return false;
    	
        if ((object instanceof CatTipoIncidencia) == false)
            return false;
        
        CatTipoIncidencia other = (CatTipoIncidencia) object;
        
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        
        if(this.hashCode() != other.hashCode())
    		return false;
        
        return true;
    }

    @Override
    public String toString() {
        return "CatTipoIncidencia[" + "idTipo=" + idTipo + ", descripcion=" + descripcion + ", clave=" + clave + ']';
    }

    public CatTipoIncidencia() {
    }

    public CatTipoIncidencia(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
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

    public List<DetIncidencia> getDetIncidenciaList() {
        return detIncidenciaList;
    }

    public void setDetIncidenciaList(List<DetIncidencia> detIncidenciaList) {
        this.detIncidenciaList = detIncidenciaList;
    }
}
