package mx.com.ferbo.business.dianolaboral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class DiasNoLaboralesBL implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(DiasNoLaboralesBL.class);
    
    public static List<Date> diasDeAsueto() 
    {
        Integer anioEnCurso = DateUtil.getAnio(new Date());
        Date fechaInicio = DateUtil.inicializaFechaInicioAnioCurso(anioEnCurso - 1);
        Date fechaFin = DateUtil.inicializaFechaTerminoAnioCurso(anioEnCurso + 1);
        DiaNoLaboralDAO diaNoLaboralDAO = new DiaNoLaboralDAO();
        List<CatDiaNoLaboral> diasNoLaboral = diaNoLaboralDAO.buscarPorPeriodo("MX", fechaInicio, fechaFin);
        List<Date> diasDeAsueto = new ArrayList<Date>();

        for (CatDiaNoLaboral aux : diasNoLaboral) 
        {
            diasDeAsueto.add(aux.getFecha());
        }

        return diasDeAsueto;
    }
    
    public static List<Date> buscarPorPeriodo(Date inicio, Date fin) {
    	List<Date> diasNoLaborales;
    	DiaNoLaboralDAO diaNoLaboralDAO = null;
    	
    	try {
    		DateUtil.setTime(inicio, 0, 0, 0, 0);
    		DateUtil.setTime(fin, 0, 0, 0, 0);
    		
    		diaNoLaboralDAO = new DiaNoLaboralDAO();
    		diasNoLaborales = diaNoLaboralDAO.buscarPorPeriodo("MX", inicio, fin)
    				.stream()
    				.map(item -> item.getFecha())
    				.collect(Collectors.toList());
    	} catch(Exception ex) {
    		diasNoLaborales = new ArrayList<Date>();
    	}
    	
    	
    	return diasNoLaborales;
    }
    
    public static List<CatDiaNoLaboral> diasDescansoAnual() 
    {
        DiaNoLaboralDAO diaNoLaboralDAO = new DiaNoLaboralDAO();
        List<CatDiaNoLaboral> descanso = null;
        Integer anio = DateUtil.getAnio(DateUtil.now());
        Date inicial = DateUtil.inicializaFechaInicioAnioCurso(anio);
        anio++;
        Date terminal = DateUtil.inicializaFechaInicioAnioCurso(anio);
        descanso = diaNoLaboralDAO.buscarPorPeriodo("MX", inicial, terminal);
        return descanso;
    }

    public static void diasDescansoEstanActualizados() throws SGPException 
    {
        List<CatDiaNoLaboral> diasNoLaborables = new ArrayList<CatDiaNoLaboral>();
        diasNoLaborables = diasDescansoAnual();

        if (diasNoLaborables.isEmpty() || diasNoLaborables.size() < 2) {
            throw new SGPException("Error: Dias de asueto no actualizados. Por favor contactar a RH");
        }
    }

}
