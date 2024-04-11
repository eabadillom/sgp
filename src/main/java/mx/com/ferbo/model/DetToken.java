package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "det_token_empleado")
@NamedQueries({
	@NamedQuery(name = "DetToken.findAll", query = "SELECT DT FROM DetToken DT"),
	@NamedQuery(name = "DetToken.findIdEmpleado", query= "SELECT NEW mx.com.ferbo.dto.DetTokenDTO( DT.idToken, em.idEmpleado, DT.nbToken, DT.caducidad, DT.valido) FROM DetToken DT INNER JOIN DT.empleado em WHERE em.numEmpleado = :numEmpleado")
	//@NamedQuery(name = "DetToken.findByEmpleadoAndFecha", query = "SELECT te FROM DetToken te WHERE te.empleado.idEmpleado = :idEmpleado AND te.caducidad = (SELECT MAX(d.caducidad) FROM DetToken d WHERE d.empleado.idEmpleado = :idEmpleado)")
})
public class DetToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_token")
    private Integer idToken;
	
	@JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @OneToOne
    private DetEmpleado empleado;
    
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Column(name = "nb_token")
    private String nbToken;
    
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "caducidad")
    private Date caducidad;
    
    @Column(name = "valido")
    private boolean valido;
    
	@Override
	public int hashCode() {
		return Objects.hash(caducidad, empleado, idToken, nbToken);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetToken other = (DetToken) obj;
		return Objects.equals(caducidad, other.caducidad) && Objects.equals(empleado, other.empleado)
				&& Objects.equals(idToken, other.idToken) && Objects.equals(nbToken, other.nbToken);
	}

	public Integer getIdToken() {
		return idToken;
	}

	public void setIdToken(Integer idToken) {
		this.idToken = idToken;
	}
	
	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public String getNbToken() {
		return nbToken;
	}

	public void setNbToken(String nbToken) {
		this.nbToken = nbToken;
	}	

	public Date getCaducidad() {
		return this.caducidad;
	}

	public void setCaducidad(Date caducidad) {
		this.caducidad = caducidad;
	}	

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
