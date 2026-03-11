package mx.com.ferbo.business.incidencia;

import mx.com.ferbo.dao.n.TipoIncidenciaDAO;
import mx.com.ferbo.model.CatTipoIncidencia;

public class TipoIncidenciaBL {
	
	public static final String TP_PERMISO    = "PE";
    public static final String TP_VACACIONES = "V";
    public static final String TP_PRENDA     = "PR";
    public static final String TP_ARTICULO   = "A";
    
    public static CatTipoIncidencia permiso() {
    	TipoIncidenciaDAO dao = new TipoIncidenciaDAO();
    	CatTipoIncidencia tipo = dao.buscarPorClave(TP_PERMISO)
    			.orElse(new CatTipoIncidencia());
    	return tipo;
    }
    
    public static CatTipoIncidencia vacaciones() {
    	TipoIncidenciaDAO dao = new TipoIncidenciaDAO();
    	CatTipoIncidencia tipo = dao.buscarPorClave(TP_VACACIONES)
    			.orElse(new CatTipoIncidencia());
    	return tipo;
    }
    
    public static CatTipoIncidencia uniforme() {
    	TipoIncidenciaDAO dao = new TipoIncidenciaDAO();
    	CatTipoIncidencia tipo = dao.buscarPorClave(TP_PRENDA)
    			.orElse(new CatTipoIncidencia());
    	return tipo;
    }
    
    public static CatTipoIncidencia articulo() {
    	TipoIncidenciaDAO dao = new TipoIncidenciaDAO();
    	CatTipoIncidencia tipo = dao.buscarPorClave(TP_ARTICULO)
    			.orElse(new CatTipoIncidencia());
    	return tipo;
    }
}
