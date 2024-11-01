package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
/*@NamedQueries({
    @NamedQuery(name = "CatDomicilioEmpleado.findAll", query = "SELECT cde FROM CatDomicilioEmpleado cde")
})*/
public class DetDomicilioEmpleado implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
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
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_empleado")
    private DetEmpleado empleado;
    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "cd_pais", referencedColumnName = "cd_pais"),
        @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado"),
        @JoinColumn(name = "cd_municipio", referencedColumnName = "cd_municipio"),
        @JoinColumn(name = "cd_localidad", referencedColumnName = "cd_localidad"),
        @JoinColumn(name = "cd_asentamiento", referencedColumnName = "cd_asentamiento"),
        @JoinColumn(name = "cd_entidadPostal", referencedColumnName = "cd_entidadPostal"),
        @JoinColumn(name = "cd_tipoasntmnto", referencedColumnName = "cd_tipoasntmnto")
    })
    private CatAsentamiento asentamiento;
    */
    public DetDomicilioEmpleado() 
    {
    }
    
}
