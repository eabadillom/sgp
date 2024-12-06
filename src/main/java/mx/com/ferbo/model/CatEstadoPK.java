package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Embeddable
public class CatEstadoPK implements Serializable
{
    private static final long serialVersionUID = -4501534761783764337L;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "cd_pais")
    private Pais pais;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_estado")
    private Integer id;

    public CatEstadoPK() 
    {
        
    }

    public CatEstadoPK(Pais pais, Integer id) 
    {
        this.pais = pais;
        this.id = id;
    }

    public Pais getPais() 
    {
        return pais;
    }

    public void setPais(Pais pais) 
    {
        this.pais = pais;
    }

    public int getId() 
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
    }

    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() 
    {
        return "CatEstadoPK[" + "paisCve=" + pais.getId() + ", estadoCve=" + id + ']';
    }
    
}
