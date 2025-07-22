
package mx.com.ferbo.dto;

public class NotificacionMovilDTO {
    
    String titulo;
    String contenido;

    public NotificacionMovilDTO() {
    }

    public NotificacionMovilDTO(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }
    
}
