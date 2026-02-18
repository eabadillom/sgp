package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "det_empleado")
@NamedQuery(name = "DetEmpleado.findByNumero", query = "SELECT e FROM DetEmpleado e WHERE e.numEmpleado = :numero")
@NamedQuery(name = "DetEmpleado.getNumEmpleado", query = "SELECT COALESCE(MAX(e.idEmpleado),0) FROM DetEmpleado e")
@NamedQuery(name = "DetEmpleado.getAll", query = "SELECT e FROM DetEmpleado e ORDER BY e.numEmpleado asc")
@NamedQuery(name = "DetEmpleado.getActive", query = "SELECT e FROM DetEmpleado e WHERE e.activo = :activo")
@NamedQuery(name = "DetEmpleado.findByActiveEmpresaIngreso", query = "SELECT e FROM DetEmpleado e WHERE e.empleadoConfiguracion.procesarNomina = true AND e.datoEmpresa.empresa.idEmpresa = :idEmpresa AND ( (e.datoEmpresa.fechaIngreso <= :periodoPagoInicio AND e.datoEmpresa.fechaBaja IS NULL) OR (e.datoEmpresa.fechaIngreso <= :periodoPagoInicio AND e.datoEmpresa.fechaBaja >= :periodoPagoFin)) ORDER BY e.nombre, e.primerAp, e.segundoAp")
@NamedQuery(name = "DetEmpleado.findByRFC", query = "SELECT e FROM DetEmpleado e WHERE e.datoEmpresa.rfc = :rfc")
@NamedQuery(name = "DetEmpleado.findByCURP", query = "SELECT e FROM DetEmpleado e WHERE e.curp = :curp")
@NamedQuery(name = "DetEmpleado.findByActiveEmpresaPlanta", query = "SELECT e FROM DetEmpleado e WHERE (:idEmpresa is null or e.datoEmpresa.empresa.idEmpresa = :idEmpresa) AND (:idPlanta is null or e.datoEmpresa.planta.idPlanta = :idPlanta) and ( (e.datoEmpresa.fechaIngreso <= :fecha) and ( e.datoEmpresa.fechaBaja is null or :fecha <= e.datoEmpresa.fechaBaja ) ) ORDER BY e.nombre, e.primerAp, e.segundoAp ")
@NamedQuery(name = "DetEmpleado.fullNameContains", query = "SELECT e FROM DetEmpleado e WHERE (e.nombre like :query) OR (e.primerAp like :query) OR (e.segundoAp like :query)")
public class DetEmpleado implements Serializable, Cloneable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "num_empleado")
    private String numEmpleado;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "primer_ap")
    private String primerAp;

    @Size(max = 45)
    @Column(name = "segundo_ap")
    private String segundoAp;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Size(max = 18)
    @Column(name = "curp")
    private String curp;

    @Size(max = 45)
    @Column(name = "correo")
    private String correo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private short activo;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_empleado_empresa")
    private InfDatoEmpresa datoEmpresa;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado_foto")
    private DetEmpleadoFoto empleadoFoto;
    
    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<DetPercepcionEmpleado> percepcionesEmpleado;
    
    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<DetPrestamo> prestamos;
    
    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<DetVacaciones> vacaciones;
    
    @OneToOne(mappedBy = "empleado", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private DetDomicilioEmpleado domicilio;
    
    @OneToOne(mappedBy = "empleado", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private DetEmpleadoConfiguracion empleadoConfiguracion;
    
    @Override
    public int hashCode() {
    	Integer hash;
    	if(this.idEmpleado == null) {
    		hash = System.identityHashCode(this);
    		return hash;
    	}
    	hash = Objects.hashCode(this.idEmpleado);
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
        
        final DetEmpleado other = (DetEmpleado) obj;
        return Objects.equals(this.idEmpleado, other.idEmpleado);
    }
    
    @Override
    public String toString() {
        return "DetEmpleado[" + "idEmpleado=" + idEmpleado + ", nombre=" + nombre + " " + primerAp + " " + segundoAp + ']';
    }
    
    public static class Builder {
    	private Integer idEmpleado;
    	private String numEmpleado;
    	private String nombre;
    	private String primerAp;
    	private String segundoAp;
    	private Date fechaNacimiento;
    	private Date fechaRegistro;
    	private Date fechaModificacion;
    	private String curp;
    	private String correo;
    	private short activo;
    	private InfDatoEmpresa datoEmpresa;
    	private DetEmpleadoFoto empleadoFoto;
    	private List<DetPercepcionEmpleado> percepcionesEmpleado;
    	private List<DetPrestamo> prestamos;
    	private List<DetVacaciones> vacaciones;
    	private DetDomicilioEmpleado domicilio;
    	private DetEmpleadoConfiguracion empleadoConfiguracion;
    	
    	public DetEmpleado.Builder id(Integer id) {
    		this.idEmpleado = id;
    		return this;
    	}
    	
    	public DetEmpleado.Builder numEmpleado(String numEmpleado) {
    		this.numEmpleado = numEmpleado;
    		return this;
    	}
    	
    	public DetEmpleado.Builder nombre(String nombre) {
    		this.nombre = nombre;
    		return this;
    	}
    	
    	public DetEmpleado.Builder primerApellido(String primerApellido) {
    		this.primerAp = primerApellido;
    		return this;
    	}
    	
    	public DetEmpleado.Builder segundoApellido(String segundoApellido) {
    		this.segundoAp = segundoApellido;
    		return this;
    	}
    	
    	public DetEmpleado.Builder fechaNacimiento(Date fechaNacimiento) {
    		this.fechaNacimiento = fechaNacimiento;
    		return this;
    	}
    	
    	public DetEmpleado.Builder fechaRegistro(Date fechaRegistro) {
    		this.fechaRegistro = fechaRegistro;
    		return this;
    	}
    	
    	public DetEmpleado.Builder fechaModificacion(Date fechaModificacion) {
    		this.fechaModificacion = fechaModificacion;
    		return this;
    	}
    	
    	public DetEmpleado.Builder curp(String curp) {
    		this.curp = curp;
    		return this;
    	}
    	
    	public DetEmpleado.Builder correo(String correo) {
    		this.correo = correo;
    		return this;
    	}
    	
    	public DetEmpleado.Builder activo(short activo) {
    		this.activo = activo;
    		return this;
    	}
    	
    	public DetEmpleado.Builder datoEmpresa(InfDatoEmpresa datoEmpresa) {
    		this.datoEmpresa = datoEmpresa;
    		return this;
    	}
    	
    	public DetEmpleado.Builder empleadoFoto(DetEmpleadoFoto empleadoFoto) {
    		this.empleadoFoto = empleadoFoto;
    		return this;
    	}
    	
    	public DetEmpleado.Builder percepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
    		this.percepcionesEmpleado = percepcionesEmpleado;
    		return this;
    	}
    	
    	public DetEmpleado.Builder prestamos(List<DetPrestamo> prestamos) {
    		this.prestamos = prestamos;
    		return this;
    	}
    	
    	public DetEmpleado.Builder vacaciones(List<DetVacaciones> vacaciones) {
    		this.vacaciones = vacaciones;
    		return this;
    	}
    	
    	public DetEmpleado.Builder domicilio(DetDomicilioEmpleado domicilio) {
    		this.domicilio = domicilio;
    		return this;
    	}
    	
    	public DetEmpleado.Builder empleadoConfiguracion(DetEmpleadoConfiguracion empleadoConfiguracion) {
    		this.empleadoConfiguracion = empleadoConfiguracion;
    		return this;
    	}
    	
    	public DetEmpleado build() {
    		return new DetEmpleado(this);
    	}
    }
    
    public DetEmpleado() {
    }
    
    public DetEmpleado(Builder builder) {
    	this.idEmpleado = builder.idEmpleado;
    	this.numEmpleado = builder.numEmpleado;
    	this.nombre = builder.nombre;
    	this.primerAp = builder.primerAp;
    	this.segundoAp = builder.segundoAp;
    	this.fechaNacimiento = builder.fechaNacimiento;
    	this.fechaRegistro = builder.fechaRegistro;
    	this.fechaModificacion = builder.fechaModificacion;
    	this.curp = builder.curp;
    	this.correo = builder.correo;
    	this.activo = builder.activo;
    	this.datoEmpresa = builder.datoEmpresa;
    	this.empleadoFoto = builder.empleadoFoto;
    	this.percepcionesEmpleado = builder.percepcionesEmpleado;
    	this.prestamos = builder.prestamos;
    	this.vacaciones = builder.vacaciones;
    	this.domicilio = builder.domicilio;
    	this.empleadoConfiguracion = builder.empleadoConfiguracion;
    }

    public DetEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public DetEmpleado(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, Date fechaNacimiento, Date fechaRegistro, short activo) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }
    
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerAp() {
        return primerAp;
    }

    public void setPrimerAp(String primerAp) {
        this.primerAp = primerAp;
    }

    public String getSegundoAp() {
        return segundoAp;
    }

    public void setSegundoAp(String segundoAp) {
        this.segundoAp = segundoAp;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public InfDatoEmpresa getDatoEmpresa() {
        return datoEmpresa;
    }

    public void setDatoEmpresa(InfDatoEmpresa datoEmpresa) {
        this.datoEmpresa = datoEmpresa;
    }

    public DetEmpleadoFoto getEmpleadoFoto() {
        return empleadoFoto;
    }

    public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
        this.empleadoFoto = empleadoFoto;
    }

	public List<DetPercepcionEmpleado> getPercepcionesEmpleado() {
		return percepcionesEmpleado;
	}

	public void setPercepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
		this.percepcionesEmpleado = percepcionesEmpleado;
	}

	public List<DetPrestamo> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(List<DetPrestamo> prestamos) {
		this.prestamos = prestamos;
	}

    public DetDomicilioEmpleado getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DetDomicilioEmpleado domicilio) {
        this.domicilio = domicilio;
    }

    public DetEmpleadoConfiguracion getEmpleadoConfiguracion() {
        return empleadoConfiguracion;
    }

    public void setEmpleadoConfiguracion(DetEmpleadoConfiguracion empleadoConfiguracion) {
        this.empleadoConfiguracion = empleadoConfiguracion;
    }

    public List<DetVacaciones> getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(List<DetVacaciones> vacaciones) {
        this.vacaciones = vacaciones;
    }
}