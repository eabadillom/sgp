package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.List;

import mx.com.ferbo.dao.n.EstatusIncidenciaDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;

/**
 *
 * @author alberto
 */
public class EstatusIncidenciaBL implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public static final String ST_ENVIADA    = "E";
    public static final String ST_APROBADA   = "A";
    public static final String ST_RECHAZADA  = "R";
    public static final String ST_CANCELADA  = "C";
    
    public static List<CatEstatusIncidencia> buscarTodos()
    {
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        List<CatEstatusIncidencia> listEstatusIncidencia = estatusIncidenciaDAO.buscarTodos();
        return listEstatusIncidencia;
    }
    
    public static CatEstatusIncidencia enviado()
    {
        String enviado = ST_ENVIADA;
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusEnviado = estatusIncidenciaDAO.buscarPorClave(enviado);
        return estatusEnviado;
    }
    
    public static CatEstatusIncidencia aprobado()
    {
        String aprobado = ST_APROBADA;
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusAprobado = estatusIncidenciaDAO.buscarPorClave(aprobado);
        return estatusAprobado;
    }
    
    public static CatEstatusIncidencia rechazado()
    {
        String rechazado = ST_RECHAZADA;
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusRechazado = estatusIncidenciaDAO.buscarPorClave(rechazado);
        return estatusRechazado;
    }
    
    public static CatEstatusIncidencia cancelado()
    {
        String cancelado = ST_CANCELADA;
        EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
        CatEstatusIncidencia estatusCancelado = estatusIncidenciaDAO.buscarPorClave(cancelado);
        return estatusCancelado;
    }
    
}
