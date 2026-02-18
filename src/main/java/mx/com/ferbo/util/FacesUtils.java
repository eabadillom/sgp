package mx.com.ferbo.util;

import java.io.ByteArrayInputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
public class FacesUtils {
	private static Logger log = LogManager.getLogger(FacesUtils.class);
	
    public static void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }
    
    public static void requireNonNull(Object obj, String mensaje) throws SGPException {
        if (obj == null) {
            throw new SGPException(mensaje);
        }
    }
    
    public static <T> T requireNonNullWithReturn(T obj, String mensaje) throws SGPException {
        if (obj == null) {
            throw new SGPException(mensaje);
        }
        return obj;
    }

    public static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }
    
    public static synchronized StreamedContent getPDF(String fileName, byte[] bytes) {
    	StreamedContent content = null;
    	log.info("Exportando {} byte[] a StreamedContent", fileName);
    	content = DefaultStreamedContent.builder()
    			.contentType("application/pdf")
    			.name(fileName)
    			.stream(() -> new ByteArrayInputStream(bytes))
    			.build();
    	
    	return content;
    }
    
    public static synchronized StreamedContent getXLSX(String fileName, byte[] bytes) {
    	StreamedContent content = null;
    	log.info("Exportando {} byte[] a StreamedContent", fileName);
    	content = DefaultStreamedContent.builder()
    			.contentType("application/vnd.ms-excel")
    			.name(fileName)
    			.stream(() -> new ByteArrayInputStream(bytes))
    			.build();
    	
    	return content;
    }
    
}
