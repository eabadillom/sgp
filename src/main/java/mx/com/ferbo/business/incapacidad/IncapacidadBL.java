package mx.com.ferbo.business.incapacidad;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import mx.com.ferbo.dao.n.imss.IncapacidadIMSSDAO;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.imss.DetIncapacidad;

import mx.com.ferbo.util.SGPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class IncapacidadBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(IncapacidadBL.class);
    private static final long serialVersionUID = 1L;
    
    public static void validarRegistrosIncapacidades(DetEmpleado empleado, Date fechaInicio, Date fechaFinal) throws SGPException
    {
        IncapacidadIMSSDAO incapacidadIMSSDAO = new IncapacidadIMSSDAO();
        List<DetIncapacidad> listIncapacidades = incapacidadIMSSDAO.buscarPorParametros(empleado.getIdEmpleado(), fechaInicio, fechaFinal);
        
        if(!listIncapacidades.isEmpty())
        {
            log.warn("El empleado {} tiene las siguientes incapacidades: {}", empleado.getIdEmpleado(), listIncapacidades.toString());
            throw new SGPException("Ya existe un registro de incapacidad");
        }
    }
    
}
