package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.List;

import mx.com.ferbo.dao.n.EstatusSolicitudDAO;
import mx.com.ferbo.model.CatEstatusSolicitud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EstatusSolicitudBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(EstatusSolicitudBL.class);
    private static final long serialVersionUID = 1L;
    
    public static List<CatEstatusSolicitud> buscarTodos()
    {
        EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
        List<CatEstatusSolicitud> listEstatusSolicitud = estatusSolicitudDAO.buscarTodos();
        return listEstatusSolicitud;
    }
    
    public static CatEstatusSolicitud estatusEnviado()
    {
        String enviado = "E";
        EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
        CatEstatusSolicitud estatusEnviado = estatusSolicitudDAO.buscarPorClave(enviado);
        return estatusEnviado;
    }
    
    public static CatEstatusSolicitud estatusAprobado()
    {
        String aprobado = "A";
        EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
        CatEstatusSolicitud estatusAprobado = estatusSolicitudDAO.buscarPorClave(aprobado);
        return estatusAprobado;
    }
    
    public static CatEstatusSolicitud estatusRechazado()
    {
        String rechazado = "R";
        EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
        CatEstatusSolicitud estatusRechazado = estatusSolicitudDAO.buscarPorClave(rechazado);
        return estatusRechazado;
    }
    
    public static CatEstatusSolicitud estatusCancelado()
    {
        String cancelado = "C";
        EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
        CatEstatusSolicitud estatusCancelado = estatusSolicitudDAO.buscarPorClave(cancelado);
        return estatusCancelado;
    }
    
}
