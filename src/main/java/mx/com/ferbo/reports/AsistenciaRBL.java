package mx.com.ferbo.reports;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.FileTools;
import mx.com.ferbo.util.SGPException;

public class AsistenciaRBL extends ReportBL {
	
	private static Logger log = LogManager.getLogger(AsistenciaRBL.class);
	
	private static final String LOGO_PATH  = "/images/logo.png";
	private static final String JRXML_PATH = "/jasper/ReporteAsistencia.jrxml";
	
	public synchronized byte[] getPDF(Integer idPlanta, Date fechaInicio, Date fechaFin) {
		byte[] bytes = null;
		
		Map<String, Object> parameters = null;
		String logoPath = null;
		String jrxmlPath = null;
		
		try {
			logoPath = FileTools.getFullPath(LOGO_PATH)
					.orElseThrow(() -> new SGPException("La ruta al logo es incorrecta."));
			
			jrxmlPath = FileTools.getFullPath(JRXML_PATH)
					.orElseThrow(() -> new SGPException("La ruta al archivo del reporte de conciliación es incorrecta."));
			
			parameters = getParameters();
			parameters.put("idPlanta", idPlanta);
			parameters.put("fechaInicio", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			parameters.put("imagen", logoPath);
			
			bytes = createPDF(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el reporte de asistencia...", ex);
		} finally {
			close(parameters);
		}
		
		return bytes;
	}
	
	public synchronized byte[] getXLSX(Integer idPlanta, Date fechaInicio, Date fechaFin) {
		byte[] bytes = null;
		
		Map<String, Object> parameters = null;
		String logoPath = null;
		String jrxmlPath = null;
		
		try {
			logoPath = FileTools.getFullPath(LOGO_PATH)
					.orElseThrow(() -> new SGPException("La ruta al logo es incorrecta."));
			
			jrxmlPath = FileTools.getFullPath(JRXML_PATH)
					.orElseThrow(() -> new SGPException("La ruta al archivo del reporte de conciliación es incorrecta."));
			
			parameters = getParameters();
			parameters.put("idPlanta", idPlanta);
			parameters.put("fechaInicio", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			parameters.put("imagen", logoPath);
			
			bytes = createXLSX(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el reporte de asistencia...", ex);
		} finally {
			close(parameters);
		}
		
		return bytes;
	}

}
