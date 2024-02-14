package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

public class DetTokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idToken;
	private DetEmpleadoDTO detEmpleadoDTO;
	private String nbToken;
	private Date caducidad;
	private boolean valido;

	
	public Integer getIdToken() {
		return idToken;
	}
	public void setIdToken(Integer idToken) {
		this.idToken = idToken;
	}
	
	public DetEmpleadoDTO getDetEmpleadoDTO() {
		return detEmpleadoDTO;
	}
	public void setDetEmpleadoDTO(DetEmpleadoDTO detEmpleadoDTO) {
		this.detEmpleadoDTO = detEmpleadoDTO;
	}
	public String getNbToken() {
		return nbToken;
	}
	public void setNbToken(String nbToken) {
		this.nbToken = nbToken;
	}
	public Date getCaducidad() {
		return caducidad;
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
	public DetTokenDTO() {		
	}
	
	public DetTokenDTO(Integer idToken, Integer idEmpleado, String nbToken, Date caducidad, boolean valido) {
		
		this.idToken = idToken;
		this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado);
		this.nbToken = nbToken;
		this.caducidad = caducidad;
		this.valido = valido;
	}
	
	/*public DetTokenDTO(Date caducidad, Integer idEmpleado) {
		
		this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado);
		this.caducidad = caducidad;
		
	}*/
	
	@Override
	public String toString() {
		return "DetTokenDTO [idToken=" + idToken + ", detEmpleadoDTO=" + detEmpleadoDTO + ", nbToken=" + nbToken
				+ ", caducidad=" + caducidad + ", valido=" + valido + "]";
	}
			
	
}
