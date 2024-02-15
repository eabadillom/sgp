package mx.com.ferbo.model;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(name = "det_fp_client")
@NamedQueries({
	@NamedQuery(name = "DetFpClient.findByAll", query = "SELECT fpC FROM DetFpClient fpC" ),
	@NamedQuery(name = "DetFpClient.findById", query = "SELECT NEW mx.com.ferbo.dto.DetFpClientDTO( fpC.idFpClient, fpC.password) FROM DetFpClient fpC WHERE fpC.idFpClient = :id" )
})

public class DetFpClient implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id_fp_client")
	private Integer idFpClient;
	
	@Size(min = 5, max = 10)
	@Column(name = "nb_password")
	private String password;

	public DetFpClient() {
		
	}
	
	
	public Integer getIdFpClient() {
		return idFpClient;
	}

	public void setIdFpClient(Integer idFpClient) {
		this.idFpClient = idFpClient;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DetFpClient [idFpClient=" + idFpClient + ", password=" + password + "]";
	}
	

}
