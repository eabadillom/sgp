package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Embeddable
public class CatMunicipioPK implements Serializable
{
    private static final long serialVersionUID = -4501534761783764337L;
    
    @ManyToOne
    @JoinColumn(name = "cd_pais")
    private Pais pais;
    @ManyToOne
    @JoinColumn(name = "cd_estado")
    private CatEstado estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_municipio")
    private Integer id;

    public CatMunicipioPK() 
    {
    }

    public CatMunicipioPK(Pais pais, CatEstado estado, int id) 
    {
        this.pais = pais;
        this.estado = estado;
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

    public CatEstado getEstado() 
    {
        return estado;
    }

    public void setEstado(CatEstado estado) 
    {
        this.estado = estado;
    }

    public Integer getId() 
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
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.pais);
        hash = 79 * hash + Objects.hashCode(this.estado);
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final CatMunicipioPK other = (CatMunicipioPK) obj;
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatMunicipioPK[" + "paisCve=" + pais + ", estadoCve=" + estado + ", municipioCve=" + id + ']';
    }
    
    
}
