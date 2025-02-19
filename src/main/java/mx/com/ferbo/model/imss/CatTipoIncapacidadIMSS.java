package mx.com.ferbo.model.imss;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    private Integer idTpIncapacidad;
    
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
    
    @Column(name = "ct_semanas_cot")
    @NotNull
    @Basic(optional = false)
    private Integer semanasCotizadas;
    
    @Column(name = "ct_periodo_cot")
    @NotNull
    @Basic(optional = false)
    private Integer periodoCotAnterior;
    
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "cd_tipo_incapacidad", referencedColumnName = "cd_tipo_incapacidad")
    private CatTipoIncapacidadSAT incapacidadSAT;
    
    @OneToMany(mappedBy = "tipoIncapacidad")
    private List<DetIncapacidad> registroIncapacidad;

    public CatTipoIncapacidadIMSS() 
    {
    }

    public CatTipoIncapacidadIMSS(Integer idTpIncapacidad) 
    {
        this.idTpIncapacidad = idTpIncapacidad;
    }

    public CatTipoIncapacidadIMSS(Integer idTpIncapacidad, String clave) 
    {
        this.idTpIncapacidad = idTpIncapacidad;
        this.clave = clave;
    }

    public Integer getIdTpIncapacidad() 
    {
        return idTpIncapacidad;
    }

    public void setIdTpIncapacidad(Integer idTpIncapacidad) 
    {
        this.idTpIncapacidad = idTpIncapacidad;
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

    public Integer getSemanasCotizadas() 
    {
        return semanasCotizadas;
    }

    public void setSemanasCotizadas(Integer semanasCotizadas) 
    {
        this.semanasCotizadas = semanasCotizadas;
    }

    public Integer getPeriodoCotAnterior() 
    {
        return periodoCotAnterior;
    }

    public void setPeriodoCotAnterior(Integer periodoCotAnterior) 
    {
        this.periodoCotAnterior = periodoCotAnterior;
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
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.idTpIncapacidad);
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
        return Objects.equals(this.idTpIncapacidad, other.idTpIncapacidad);
    }

    @Override
    public String toString() 
    {
        return "CatTipoIncapacidadIMSS[" + "IdTpIncapacidad: " + idTpIncapacidad + ", Clave: " + clave + ", Descripcion: " + descripcion 
                + ", PorcentagePago: " + porcentagePago + ", MaxDias: " + maxDias + ", SemanasCotizadas: " + semanasCotizadas 
                + ", PeriodoCotAnterior: " + periodoCotAnterior + ']';
    }
    
}
