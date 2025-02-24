package mx.com.ferbo.model.imss;

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
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_tipo_riesgo")
@NamedQueries({
    @NamedQuery(name = "CatTipoRiesgoIMSS.findAll", query = "SELECT tr FROM CatTipoRiesgoIMSS tr"),
    @NamedQuery(name = "CatTipoRiesgoIMSS.findByClave", query = "SELECT tr FROM CatTipoRiesgoIMSS tr WHERE tr.clave = :clave")
})
public class CatTipoRiesgoIMSS implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_tp_riesgo")
    private Integer idTipoRiesgo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "tipoRiesgo")
    private List<DetIncapacidad> registroIncapacidad;

    public CatTipoRiesgoIMSS() 
    {
    }

    public CatTipoRiesgoIMSS(Integer idTipoRiesgo) 
    {
        this.idTipoRiesgo = idTipoRiesgo;
    }

    public Integer getIdTipoRiesgo() 
    {
        return idTipoRiesgo;
    }

    public void setIdTipoRiesgo(Integer idTipoRiesgo) 
    {
        this.idTipoRiesgo = idTipoRiesgo;
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

    public List<DetIncapacidad> getRegistroIncapacidad() 
    {
        return registroIncapacidad;
    }

    public void setRegistroIncapacidad(List<DetIncapacidad> registroIncapacidad) 
    {
        this.registroIncapacidad = registroIncapacidad;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idTipoRiesgo);
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
        final CatTipoRiesgoIMSS other = (CatTipoRiesgoIMSS) obj;
        return Objects.equals(this.idTipoRiesgo, other.idTipoRiesgo);
    }

    @Override
    public String toString() {
        return "CatTipoRiesgo[" + "IdTipoRiesgo: " + idTipoRiesgo + ", Clave: " + clave + ", Descripcion: " + descripcion + ']';
    }
    
}
