package mx.com.ferbo.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.IOUtil;
import mx.com.ferbo.util.SGPException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ReportBL {
	
private static Logger log = LogManager.getLogger(ReportBL.class);
	
	protected static synchronized Optional<Connection> getConnection() {
		Optional<Connection> response = null;
		Connection conn = null;
		
		try {
			conn = EntityManagerUtil.getConnection();
			response = Optional.of(conn);
		} catch(Exception ex) {
			response = Optional.empty();
		}
		
		return response;
	}
	
	protected static synchronized void close(Connection conn) {
		EntityManagerUtil.close(conn);
	}
	
	protected static synchronized void close(Map<String, Object> parameters) {
		Connection conn = (Connection) parameters.get(JRParameter.REPORT_CONNECTION);
		close(conn);
	}
	
	public static Map<String, Object> getParameters()
	throws SGPException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection conn = getConnection()
				.orElseThrow(() -> new SGPException("No hay conexión con la base de datos"));
		
		parameters.put(JRParameter.REPORT_CONNECTION, conn);
		parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "MX"));
		parameters.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("GMT-06:00"));
		
		return parameters;
	}
	
	protected static byte[] createPDF(Map<String, Object> params, String jrxmlPath)
	throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream output = null;
		JasperDesign design = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		
		try {
			log.info("Parametros jasper: {}", params);
			output = new ByteArrayOutputStream();
			design = JRXmlLoader.load(jrxmlPath);
			report = JasperCompileManager.compileReport(design);
			jasperPrint = JasperFillManager.fillReport(report, params);
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			bytes = output.toByteArray();
		} catch(JRException ex) {
			log.error("Problema con la exportacíon a PDF del reporte...", ex);
		} finally {
			IOUtil.close(output);
		}
		
		return bytes;
	}
	
	protected static byte[] createXLSX(Map<String, Object> params, String path)
	throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream output = null;
		JasperDesign design = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		JRXlsxExporter exporter = null;
		SimpleXlsxReportConfiguration configuration = null;
		OutputStreamExporterOutput outputExporter = null;
		
		try {
			params.put(JRParameter.IS_IGNORE_PAGINATION, true);
			
			log.info("Parametros jasper: {}", params);
			output = new ByteArrayOutputStream();
			design = JRXmlLoader.load(path);
			report = JasperCompileManager.compileReport(design);
			jasperPrint = JasperFillManager.fillReport(report, params);
			outputExporter = new SimpleOutputStreamExporterOutput(output);
			exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(outputExporter);
			configuration = new SimpleXlsxReportConfiguration();
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setDetectCellType(true);
			configuration.setIgnoreGraphics(true);
			configuration.setIgnorePageMargins(true);
			configuration.setIgnoreCellBorder(true);
			configuration.setWhitePageBackground(false);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			bytes = output.toByteArray();
		} catch(JRException ex) {
			log.error("Problema con la exportación a XLSX del reporte...", ex);
		} finally {
			IOUtil.close(output);
		}
		
		return bytes;
	}

}
