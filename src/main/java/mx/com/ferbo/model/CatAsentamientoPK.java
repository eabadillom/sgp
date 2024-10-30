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
public class CatAsentamientoPK implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "cd_pais")
    private Pais pais;
    @ManyToOne
    @JoinColumn(name = "cd_estado")
    private CatEstado estado;
    @ManyToOne
    @JoinColumn(name = "cd_municipio")
    private CatMunicipio municipio;
    @ManyToOne
    @JoinColumn(name = "cd_localidad")
    private CatLocalidad localidad;
    @ManyToOne
    @JoinColumn(name = "cd_tipoasntmnto")
    private CatTipoAsentamiento tipoasntmnto;
    @ManyToOne
    @JoinColumn(name = "cd_entidadpostal")
    private CatEntidadPostal entidadpostal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_asentamiento")
    private Integer asentamiento;

    public CatAsentamientoPK() 
    {
    }

    public CatAsentamientoPK(Pais pais, CatEstado estado, CatMunicipio municipio, CatLocalidad localidad, CatTipoAsentamiento tipoasntmnto, CatEntidadPostal entidadpostal, Integer asentamiento) 
    {
        this.pais = pais;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
        this.tipoasntmnto = tipoasntmnto;
        this.entidadpostal = entidadpostal;
        this.asentamiento = asentamiento;
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

    public CatLocalidad getLocalidad() 
    {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) 
    {
        this.localidad = localidad;
    }

    public CatTipoAsentamiento getTipoasntmnto() 
    {
        return tipoasntmnto;
    }

    public void setTipoasntmnto(CatTipoAsentamiento tipoasntmnto) 
    {
        this.tipoasntmnto = tipoasntmnto;
    }

    public CatEntidadPostal getEntidadpostal() 
    {
        return entidadpostal;
    }

    public void setEntidadpostal(CatEntidadPostal entidadpostal) 
    {
        this.entidadpostal = entidadpostal;
    }

    public Integer getAsentamiento() 
    {
        return asentamiento;
    }

    public void setAsentamiento(Integer asentamiento) 
    {
        this.asentamiento = asentamiento;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.pais);
        hash = 89 * hash + Objects.hashCode(this.estado);
        hash = 89 * hash + Objects.hashCode(this.municipio);
        hash = 89 * hash + Objects.hashCode(this.localidad);
        hash = 89 * hash + Objects.hashCode(this.tipoasntmnto);
        hash = 89 * hash + Objects.hashCode(this.entidadpostal);
        hash = 89 * hash + Objects.hashCode(this.asentamiento);
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
        final CatAsentamientoPK other = (CatAsentamientoPK) obj;
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.municipio, other.municipio)) {
            return false;
        }
        if (!Objects.equals(this.localidad, other.localidad)) {
            return false;
        }
        if (!Objects.equals(this.tipoasntmnto, other.tipoasntmnto)) {
            return false;
        }
        if (!Objects.equals(this.entidadpostal, other.entidadpostal)) {
            return false;
        }
        return Objects.equals(this.asentamiento, other.asentamiento);
    }

    @Override
    public String toString() {
        return "CatAsentamientoPK[" + "pais=" + pais + ", estado=" + estado + ", municipio=" + municipio + ", localidad=" + localidad + ", tipoasntmnto=" + tipoasntmnto + ", entidadpostal=" + entidadpostal + ", asentamiento=" + asentamiento + ']';
    }
    
}
