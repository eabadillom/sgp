package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "det_domicilio_empleado")
@NamedQueries({
    @NamedQuery(name = "DetDomicilioEmpleado.findAll", query = "SELECT cde FROM DetDomicilioEmpleado cde"),
    @NamedQuery(name = "DetDomicilioEmpleado.findIdEmpleado", query = "SELECT cde FROM DetDomicilioEmpleado cde INNER JOIN cde.empleado e WHERE e.idEmpleado = :idEmpleado")
})
public class DetDomicilioEmpleado implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_domicilioEmp")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nb_calle")
    private String calle;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nu_numExt")
    private String numeroExterior;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nu_numInt")
    private String numeroInterior;
    
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "id_empleado")
    private DetEmpleado empleado;
    
    @OneToOne(optional = false)
    @NotNull
    @JoinColumns({
        @JoinColumn(name = "cd_asentamiento", referencedColumnName = "cd_asentamiento"),
        @JoinColumn(name = "cd_localidad", referencedColumnName = "cd_localidad"),
        @JoinColumn(name = "cd_municipio", referencedColumnName = "cd_municipio"),
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado"),
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais")
    })
    private CatAsentamiento asentamiento;
    
    public DetDomicilioEmpleado() 
    {
    }

    public DetDomicilioEmpleado(Integer id) 
    {
        this.id = id;
    }

    public DetDomicilioEmpleado(Integer id, String calle, String numeroExterior, String numeroInterior) 
    {
        this.id = id;
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
    }
    
    public Integer getId() 
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public String getCalle() 
    {
        return calle;
    }

    public void setCalle(String calle) 
    {
        this.calle = calle;
    }

    public String getNumeroExterior() 
    {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) 
    {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() 
    {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) 
    {
        this.numeroInterior = numeroInterior;
    }

    public DetEmpleado getEmpleado() 
    {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) 
    {
        this.empleado = empleado;
    }

    public CatAsentamiento getAsentamiento() 
    {
        return asentamiento;
    }

    public void setAsentamiento(CatAsentamiento asentamiento) 
    {
        this.asentamiento = asentamiento;
    }

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final DetDomicilioEmpleado other = (DetDomicilioEmpleado) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return "DetDomicilioEmpleado[" + "id=" + id + ", calle=" + calle + ", numeroExterior=" + numeroExterior + ", numeroInterior=" + numeroInterior + ']';
    }
    
    
}
