package mx.com.ferbo.dto.ui;

public class ImagenDTO {
	
	private String nombre = null;
	private byte[] contenido = null;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
}
