package mx.com.ferbo.business.notifmovil;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.dto.NotificacionMovilDTO;

public interface NotifMovilBL {

    public static NotificacionMovilDTO obtenerMensaje(String operacion, DetEmpleado empleado) {
        String incidencia;
        String descripcion;

        switch (operacion) {

            case "retardo":
                incidencia = "Notificiacion";
                descripcion = " tiene ";
                break;

            default:
                incidencia = "Solicitud";
                descripcion = " ha solicitado ";
                break;
        }

        String titulo = String.format("%s de %s pendiente", incidencia, operacion);
        String contenido = String.format(
                "El empleado %s %s %s%s",
                empleado.getNombre(),
                empleado.getPrimerAp(),
                descripcion,
                operacion
        );

        NotificacionMovilDTO notificacionMovilDTO = new NotificacionMovilDTO(titulo, contenido);

        return notificacionMovilDTO;

    }
}
