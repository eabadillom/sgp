package mx.com.ferbo.model.imss;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
    @Column(name = "cd_secuela_riesgo_trabajo")
    private Integer idControlIncapacidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_descripcion")
    private String descripcion;

    public CatRiesgoTrabajoIMSS() 
    {
    }

    public CatRiesgoTrabajoIMSS(Integer idControlIncapacidad) 
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

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.idControlIncapacidad);
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
        return Objects.equals(this.idControlIncapacidad, other.idControlIncapacidad);
    }

    @Override
    public String toString() {
        return "CatRiesgoTrabajo[" + "idControlIncapacidad=" + idControlIncapacidad + ", clave=" + clave + ", descripcion=" + descripcion + ']';
    }
    
}
