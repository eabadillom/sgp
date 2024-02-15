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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

@Entity
@Table(name = "det_token_empleado")
@NamedQueries({
@NamedQuery(name = "DetToken.findAll", query = "SELECT DT FROM DetToken DT"),
@NamedQuery(name = "DetToken.findIdEmpleado", query= "SELECT NEW mx.com.ferbo.dto.DetTokenDTO( DT.idToken, em.idEmpleado, DT.nbToken, DT.Caducidad, DT.valido) FROM DetToken DT INNER JOIN DT.idEmpleado em WHERE em.numEmpleado = :numEmpleado"),
@NamedQuery(name = "DetToken.findByIdEmpAndFecha", query = "SELECT NEW mx.com.ferbo.dto.DetTokenDTO( MAX(DT.idToken) , em.idEmpleado, MAX(DT.nbToken), MAX(DT.Caducidad) , MAX(DT.valido) ) FROM DetToken Dt INNER JOIN Dt.idEmpleado em WHERE em.idEmpleado = :idEmpleado GROUP BY em.idEmpleado " )
})
public class DetToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_token")
    private Integer idToken;
	
	@JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado idEmpleado;
    
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Column(name = "nb_token")
    private String nbToken;
    
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "caducidad")
    private Date Caducidad;
    
    @Column(name = "valido")
    private boolean valido;
    
	@Override
	public int hashCode() {
		return Objects.hash(Caducidad, idEmpleado, idToken, nbToken);
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
		return Objects.equals(Caducidad, other.Caducidad) && Objects.equals(idEmpleado, other.idEmpleado)
				&& Objects.equals(idToken, other.idToken) && Objects.equals(nbToken, other.nbToken);
	}

	public Integer getIdToken() {
		return idToken;
	}

	public void setIdToken(Integer idToken) {
		this.idToken = idToken;
	}
	
	public DetEmpleado getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(DetEmpleado idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNbToken() {
		return nbToken;
	}

	public void setNbToken(String nbToken) {
		this.nbToken = nbToken;
	}	

	public Date getCaducidad() {
		return Caducidad;
	}

	public void setCaducidad(Date caducidad) {
		Caducidad = caducidad;
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
