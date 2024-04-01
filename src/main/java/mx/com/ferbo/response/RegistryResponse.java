package mx.com.ferbo.response;

public class RegistryResponse {
	
	Integer codigo = null;
	String mensaje = null;
	String url = null;
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "RegistryResponse [codigo=" + codigo + ", mensaje=" + mensaje + ", url=" + url + "]";
	}
	
	
	
}
