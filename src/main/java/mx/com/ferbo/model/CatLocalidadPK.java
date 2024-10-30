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
public class CatLocalidadPK implements Serializable 
{
    private static final long serialVersionUID = 3833888070514352421L;
    
    @ManyToOne
    @JoinColumn(name = "cd_pais")
    private Pais pais;
    @ManyToOne
    @Column(name = "cd_estado")
    private CatEstado estado;
    @ManyToOne
    @Column(name = "cd_municipio")
    private CatMunicipio municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_localidad")
    private Integer id;

    public CatLocalidadPK() 
    {
    }

    public CatLocalidadPK(Pais pais, CatEstado estado, CatMunicipio municipio, Integer id) 
    {
        this.pais = pais;
        this.estado = estado;
        this.municipio = municipio;
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

    public CatMunicipio getMunicipio() 
    {
        return municipio;
    }

    public void setMunicipio(CatMunicipio municipio) 
    {
        this.municipio = municipio;
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
        hash = 89 * hash + Objects.hashCode(this.pais);
        hash = 89 * hash + Objects.hashCode(this.estado);
        hash = 89 * hash + Objects.hashCode(this.municipio);
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatLocalidadPK other = (CatLocalidadPK) obj;
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.municipio, other.municipio)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() 
    {
        return "CatLocalidadPK[" + "pais=" + pais + ", estado=" + estado + ", municipio=" + municipio + ", id=" + id + ']';
    }
    
}
