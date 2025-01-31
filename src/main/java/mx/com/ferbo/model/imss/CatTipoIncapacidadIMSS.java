package mx.com.ferbo.model.imss;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_tp_incapacidad")
@NamedQueries({
    @NamedQuery(name = "CatTipoIncapacidadIMSS.findAll", query = "SELECT tpi FROM CatTipoIncapacidadIMSS tpi"),
    @NamedQuery(name = "CatTipoIncapacidadIMSS.findByClave", query = "SELECT tpi FROM CatTipoIncapacidadIMSS tpi WHERE tpi.clave = :clave")
})
public class CatTipoIncapacidadIMSS implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_tp_incapacidad")
    private Integer idIncapacidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_clave")
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @Column(name = "pc_pago")
    @NotNull
    @Basic(optional = false)
    private BigDecimal porcentagePago;
    
    @Column(name = "ct_max_dias")
    @NotNull
    @Basic(optional = false)
    private Integer maxDias;
    
    @OneToOne
    @NotNull
    @JoinColumn(name = "cd_tipo_incapacidad")
    private CatTipoIncapacidadSAT incapacidadSAT;

    public CatTipoIncapacidadIMSS() 
    {
    }

    public CatTipoIncapacidadIMSS(Integer idIncapacidad) 
    {
        this.idIncapacidad = idIncapacidad;
    }

    public CatTipoIncapacidadIMSS(Integer idIncapacidad, String clave) 
    {
        this.idIncapacidad = idIncapacidad;
        this.clave = clave;
    }

    public Integer getIdIncapacidad() 
    {
        return idIncapacidad;
    }

    public void setIdIncapacidad(Integer idIncapacidad) 
    {
        this.idIncapacidad = idIncapacidad;
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

    public BigDecimal getPorcentagePago() 
    {
        return porcentagePago;
    }

    public void setPorcentagePago(BigDecimal porcentagePago) 
    {
        this.porcentagePago = porcentagePago;
    }

    public Integer getMaxDias() 
    {
        return maxDias;
    }

    public void setMaxDias(Integer maxDias) 
    {
        this.maxDias = maxDias;
    }

    public CatTipoIncapacidadSAT getIncapacidadSAT() 
    {
        return incapacidadSAT;
    }

    public void setIncapacidadSAT(CatTipoIncapacidadSAT incapacidadSAT) 
    {
        this.incapacidadSAT = incapacidadSAT;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.idIncapacidad);
        hash = 43 * hash + Objects.hashCode(this.clave);
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
        final CatTipoIncapacidadIMSS other = (CatTipoIncapacidadIMSS) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return Objects.equals(this.idIncapacidad, other.idIncapacidad);
    }

    @Override
    public String toString() {
        return "CatTPIncapacidad[" + "idIncapacidad=" + idIncapacidad + ", clave=" + clave + ", descripcion=" + descripcion + ", porcentagePago=" + porcentagePago + ", maxDias=" + maxDias + ", incapacidadSAT=" + incapacidadSAT + ']';
    }
    
}
