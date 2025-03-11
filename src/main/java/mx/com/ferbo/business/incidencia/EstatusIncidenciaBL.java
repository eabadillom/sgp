package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.List;

import mx.com.ferbo.dao.n.EstatusIncidenciaDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class EstatusIncidenciaBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(EstatusIncidenciaBL.class);
    private static final long serialVersionUID = 1L;
    
    public static List<CatEstatusIncidencia> buscarTodos()
    {
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        List<CatEstatusIncidencia> listEstatusIncidencia = estatusIncidenciaDAO.buscarTodos();
        return listEstatusIncidencia;
    }
    
    public static CatEstatusIncidencia estatusEnviado()
    {
        String enviado = "E";
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusEnviado = estatusIncidenciaDAO.buscarPorClave(enviado);
        return estatusEnviado;
    }
    
    public static CatEstatusIncidencia estatusAprobado()
    {
        String aprobado = "A";
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusAprobado = estatusIncidenciaDAO.buscarPorClave(aprobado);
        return estatusAprobado;
    }
    
    public static CatEstatusIncidencia estatusRechazado()
    {
        String rechazado = "R";
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusRechazado = estatusIncidenciaDAO.buscarPorClave(rechazado);
        return estatusRechazado;
    }
    
    public static CatEstatusIncidencia estatusCancelado()
    {
        String cancelado = "C";
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusCancelado = estatusIncidenciaDAO.buscarPorClave(cancelado);
        return estatusCancelado;
    }
    
}
