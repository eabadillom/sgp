package mx.com.ferbo.model;

import java.io.Serializable;
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

/**
 *
 * @author alberto
 */

@Entity
@Table(name = "det_empleado_conf")
@NamedQueries({
    @NamedQuery(name = "DetEmpleadoConfiguracion.findAll", query = "SELECT dec FROM DetEmpleadoConfiguracion dec"),
    @NamedQuery(name = "DetEmpleadoConfiguracion.findByEmpleado", query = "SELECT dec FROM DetEmpleadoConfiguracion dec where dec.empleado.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "DetEmpleadoConfiguracion.findByRetardos", query = "SELECT dec FROM DetEmpleadoConfiguracion dec where dec.retardo = :retardo")
})
public class DetEmpleadoConfiguracion implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_empleado_conf")
    private Integer idEmpleadoConf;
    
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "cd_empleado")
    private DetEmpleado empleado;
    
    @NotNull
    @Column(name = "st_proc_nomina")
    private Boolean procesarNomina;
    
    @Basic(optional = true)
    @Column(name = "st_retardo")
    private Boolean retardo;
    
    @Basic(optional = true)
    @Column(name = "st_horas_ext")
    private Boolean horasextra;
    
    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idEmpleadoConf);
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
        final DetEmpleadoConfiguracion other = (DetEmpleadoConfiguracion) obj;
        return Objects.equals(this.idEmpleadoConf, other.idEmpleadoConf);
    }

    @Override
    public String toString() {
        return "DetEmpleadoConfiguracion[" + "id_empleado_conf=" + idEmpleadoConf + ", empleado=" + empleado.getIdEmpleado() + ", retardo=" + retardo + ", horasextra=" + horasextra +']';
    }

    public DetEmpleadoConfiguracion() 
    {
    }

    public DetEmpleadoConfiguracion(Integer idEmpleadoConf, Boolean retardo) 
    {
        this.idEmpleadoConf = idEmpleadoConf;
        this.retardo = retardo;
    }

    public DetEmpleadoConfiguracion(Integer idEmpleadoConf, DetEmpleado empleado, Boolean retardo) 
    {
        this.idEmpleadoConf = idEmpleadoConf;
        this.empleado = empleado;
        this.retardo = retardo;
    }
    
    public Integer getIdEmpleadoConf() 
    {
        return idEmpleadoConf;
    }

    public void setIdEmpleadoConf(Integer id_empleado_conf) 
    {
        this.idEmpleadoConf = id_empleado_conf;
    }

    public DetEmpleado getEmpleado() 
    {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) 
    {
        this.empleado = empleado;
    }

    public Boolean getRetardo() 
    {
        return retardo;
    }

    public void setRetardo(Boolean retardo) 
    {
        this.retardo = retardo;
    }

    public Boolean getHorasextra() {
        return horasextra;
    }

    public void setHorasextra(Boolean horasextra) {
        this.horasextra = horasextra;
    }

	public Boolean getProcesarNomina() {
		return procesarNomina;
	}

	public void setProcesarNomina(Boolean procesarNomina) {
		this.procesarNomina = procesarNomina;
	}
    
}
