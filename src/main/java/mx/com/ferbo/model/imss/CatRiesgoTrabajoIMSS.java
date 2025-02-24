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
@Table(name = "cat_secuela_riesgo_trabajo")
@NamedQueries({
    @NamedQuery(name = "CatRiesgoTrabajoIMSS.findAll", query = "SELECT crt FROM CatRiesgoTrabajoIMSS crt"),
    @NamedQuery(name = "CatRiesgoTrabajoIMSS.findByClave", query = "SELECT crt FROM CatRiesgoTrabajoIMSS crt WHERE crt.clave = :clave")
})
public class CatRiesgoTrabajoIMSS implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_sec_riesgo_trabajo")
    private Integer idSecRiesgoTrabajo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "secuelaRiesgoTrabajo")
    private List<DetIncapacidad> registroIncapacidad;

    public CatRiesgoTrabajoIMSS() 
    {
    }

    public CatRiesgoTrabajoIMSS(Integer idSecRiesgoTrabajo) 
    {
        this.idSecRiesgoTrabajo = idSecRiesgoTrabajo;
    }

    public Integer getIdSecRiesgoTrabajo() 
    {
        return idSecRiesgoTrabajo;
    }

    public void setIdSecRiesgoTrabajo(Integer idSecRiesgoTrabajo) 
    {
        this.idSecRiesgoTrabajo = idSecRiesgoTrabajo;
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
        hash = 23 * hash + Objects.hashCode(this.idSecRiesgoTrabajo);
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
        final CatRiesgoTrabajoIMSS other = (CatRiesgoTrabajoIMSS) obj;
        return Objects.equals(this.idSecRiesgoTrabajo, other.idSecRiesgoTrabajo);
    }

    @Override
    public String toString() 
    {
        return "CatRiesgoTrabajoIMSS[" + "idSecRiesgoTrabajo=" + idSecRiesgoTrabajo + ", clave=" + clave + ", descripcion=" + descripcion + ']';
    }
    
}
