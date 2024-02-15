package mx.com.ferbo.dto;

import java.io.Serializable;


public class DetFpClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idFpClient;
	private String nbPassword;
	
	public DetFpClientDTO () {
		
	}
	
	public DetFpClientDTO (Integer idFpClient, String nbPassword) {		
		this.idFpClient = idFpClient;
		this.nbPassword = nbPassword;		
	}

	public Integer getIdFpClient() {
		return idFpClient;
	}

	public void setIdFpClient(Integer idFpClient) {
		this.idFpClient = idFpClient;
	}

	public String getNbPassword() {
		return nbPassword;
	}

	public void setNbPassword(String nbPassword) {
		this.nbPassword = nbPassword;
	}

	@Override
	public String toString() {
		return "DetFpClientDTO [idFpClient=" + idFpClient + ", nbPassword=" + nbPassword + "]";
	}
	
	
}
