package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Embeddable
public class CatEstadoPK implements Serializable
{
    private static final long serialVersionUID = -4501534761783764337L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_pais")
    private int paisCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_estado")
    private int estadoCve;

    public CatEstadoPK() 
    {
        
    }

    public CatEstadoPK(int paisCve, int estadoCve) 
    {
        this.paisCve = paisCve;
        this.estadoCve = estadoCve;
    }

    public int getPaisCve() 
    {
        return paisCve;
    }

    public void setPaisCve(int paisCve) 
    {
        this.paisCve = paisCve;
    }

    public int getEstadoCve() 
    {
        return estadoCve;
    }

    public void setEstadoCve(int estadoCve) 
    {
        this.estadoCve = estadoCve;
    }

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 17 * hash + this.paisCve;
        hash = 17 * hash + this.estadoCve;
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
        final CatEstadoPK other = (CatEstadoPK) obj;
        if (this.paisCve != other.paisCve) {
            return false;
        }
        return this.estadoCve == other.estadoCve;
    }

    @Override
    public String toString() 
    {
        return "CatEstadoPK[" + "paisCve=" + paisCve + ", estadoCve=" + estadoCve + ']';
    }
    
}
