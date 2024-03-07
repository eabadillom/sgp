package mx.com.ferbo.response;

public class ChallengeResponse {
	Integer numEmpleado;
	String token;
	String huella;
	String huella2;
	Integer codigoError;
	String mensajeError;
	public ChallengeResponse() {
	
	}
	public Integer getNumEmpleado() {
		return numEmpleado;
	}
	public void setNumEmpleado(Integer numEmpleado) {
		this.numEmpleado = numEmpleado;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getHuella() {
		return huella;
	}
	public void setHuella(String huella) {
		this.huella = huella;
	}
	public String getHuella2() {
		return huella2;
	}
	public void setHuella2(String huella2) {
		this.huella2 = huella2;
	}
	
	public Integer getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	@Override
	public String toString() {
		return "{\"numEmpleado\":\"" + numEmpleado + "\", \"token\":\"" + token + "\", \"huella\":\"" + huella
				+ "\", \"huella2\":\"" + huella2 + "\", \"codigoError\":\"" + codigoError + "\", \"mensajeError\":\""
				+ mensajeError + "\"}";
	}
}
