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
@Table(name = "cat_control_incapacidad")
@NamedQueries({
    @NamedQuery(name = "CatControlIncapacidadIMSS.findAll", query = "SELECT tci FROM CatControlIncapacidadIMSS tci"),
    @NamedQuery(name = "CatControlIncapacidadIMSS.findByClave", query = "SELECT tci FROM CatControlIncapacidadIMSS tci WHERE tci.clave = :clave")
})
public class CatControlIncapacidadIMSS implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_control_incapacidad")
    private Integer idControlIncapacidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "controlIncapacidad")
    private List<DetIncapacidad> registroIncapacidad;

    public CatControlIncapacidadIMSS() 
    {
    }

    public CatControlIncapacidadIMSS(Integer idControlIncapacidad) 
    {
        this.idControlIncapacidad = idControlIncapacidad;
    }

    public Integer getIdControlIncapacidad() 
    {
        return idControlIncapacidad;
    }

    public void setIdControlIncapacidad(Integer idControlIncapacidad) 
    {
        this.idControlIncapacidad = idControlIncapacidad;
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
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.idControlIncapacidad);
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
        final CatControlIncapacidadIMSS other = (CatControlIncapacidadIMSS) obj;
        return Objects.equals(this.idControlIncapacidad, other.idControlIncapacidad);
    }

    @Override
    public String toString() {
        return "CatControlIncapacidadIMSS[" + "idControlIncapacidad=" + idControlIncapacidad + ", clave=" + clave + ", descripcion=" + descripcion + ']';
    }
    
}
