package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
    @NotNull
    @JoinColumns(value = {
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado"),
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais")
    },
        foreignKey = @ForeignKey(name="FK_Municipio_Estado"))
    private CatEstado estado;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_municipio")
    private Integer id;

    public CatMunicipioPK() 
    {
    }

    public CatMunicipioPK(CatEstado estado, Integer id) 
    {
        this.estado = estado;
        this.id = id;
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
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CatMunicipioPK[" + "id=" + id + ']';
    }
    
    
}
