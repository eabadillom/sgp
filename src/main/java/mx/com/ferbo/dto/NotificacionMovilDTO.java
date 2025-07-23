
package mx.com.ferbo.dto;

public class NotificacionMovilDTO {
    
    String title;
    String body;

    public NotificacionMovilDTO() {
    }

    public NotificacionMovilDTO(String titulo, String contenido) {
        this.title = titulo;
        this.body = contenido;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
    
}
