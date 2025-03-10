package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class IncidenciaBL implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(IncidenciaBL.class);
    
    public static List<Date> fechasSolicitudPermiso(DetSolicitudPermiso auxSolicitudPermiso)
    {
        Date fechaInicio = auxSolicitudPermiso.getFechaInicio();
        Date fechaFin = auxSolicitudPermiso.getFechaFin();
        List<Date> arregloFechas = DateUtil.generarArreglosFechas(fechaInicio, fechaFin);
        return arregloFechas;
    }
    
    public static List<Integer> obtenerDiasSeleccionados(InfDatoEmpresa empleadoEmpresa) {
        List<Integer> diasSeleccionados = new ArrayList<>();

        if (empleadoEmpresa.getDiaLunes() != true) {
            diasSeleccionados.add(1);
        }
        if (empleadoEmpresa.getDiaMartes() != true) {
            diasSeleccionados.add(2);
        }
        if (empleadoEmpresa.getDiaMiercoles() != true) {
            diasSeleccionados.add(3);
        }
        if (empleadoEmpresa.getDiaJueves() != true) {
            diasSeleccionados.add(4);
        }
        if (empleadoEmpresa.getDiaViernes() != true) {
            diasSeleccionados.add(5);
        }
        if (empleadoEmpresa.getDiaSabado() != true) {
            diasSeleccionados.add(6);
        }
        if (empleadoEmpresa.getDiaDomingo() != true) {
            diasSeleccionados.add(0);
        }

        log.trace("Dias de bloqueo: {}", diasSeleccionados.toString());
        return diasSeleccionados;
    }
    
}
