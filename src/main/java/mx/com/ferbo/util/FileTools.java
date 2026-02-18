package mx.com.ferbo.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileTools {
	
private static Logger log = LogManager.getLogger(FileTools.class);
	
	public static synchronized byte[] getBinContent(String fileName){
		byte[]                contenido = null;
		byte[]                buffer    = null;
		int                   length = 0;
		File                  f = null;
		FileInputStream       fis = null;
		DataInputStream       in = null;
		ByteArrayOutputStream out = null;;
		
		f = new File(fileName);
		buffer = new byte[4 * 1024]; // 4K buffer
		
		try {
			log.debug("Comenzando la lectura binaria del archivo " + fileName);
			fis = new FileInputStream(f);
			
			log.debug("Archivo abierto correctamente: " + fileName);
			in = new DataInputStream(fis);
			out = new ByteArrayOutputStream();
			
			log.debug("Se inicia lectura del archivo en bloques de 4k: " + fileName);
			while ((in != null) && ((length = in.read(buffer)) != -1)) {
				out.write(buffer, 0, length);
			}
			
			contenido = out.toByteArray();
			log.debug("Terminando la lectura binaria del archivo " + fileName);
			
		} catch (FileNotFoundException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		} finally {
			close(fis);
			close(in);
			close(out);
		}
		
		return contenido;
	}
	
	public static synchronized void close(DataInputStream in){
		try {
			if(in != null)
				in.close();
			log.debug("Objeto DataInputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto DataInputStream", ex);
		} finally {
			in = null;
		}
	}
	
	public static synchronized void close(FileInputStream in){
		try {
			if(in != null)
				in.close();
			log.debug("Objeto FileInputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto FileInputStream", ex);
		} finally {
			in = null;
		}
	}
	
	public static synchronized void close(ByteArrayOutputStream out){
		try{
			if(out != null)
				out.close();
			log.debug("Objeto ByteArrayOutputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto ByteArrayOutputStream", ex);
		} finally {
			out = null;
		}
	}
	
	
	
	public static synchronized String getBase64String(byte[] bytes) {
		String sOutput = null;
		Encoder encoder =  Base64.getEncoder();
		sOutput = encoder.encodeToString(bytes);
		return sOutput;
	}
	
	public static synchronized Optional<File> getFile(String path)
	throws SGPException {
		Optional<File> response = null;
		File archivo = null;
		
		try {
			archivo = new File(path);
			if(archivo.exists() == false)
				throw new SGPException("El archivo solicitado no existe: " + path);
			
			response = Optional.of(archivo);
		} catch(Exception ex) {
			response = Optional.empty();
		}
		
		return response;
	}
	
	public static synchronized Optional<String> getFullPath(String jasperPath) {
		Optional<String> response = null;
		
		String fullPath = null;
		File   file = null;
		URL resource = null;
		
		try {
			resource = JasperReportUtil.class.getResource(jasperPath);
			fullPath = resource.getFile();
			file = new File(fullPath);
			if(file.exists() == false)
				throw new SGPException("La ruta solicitada no existe: " + fullPath);
			
			response = Optional.of(file.getPath());
		} catch(Exception ex) {
			response = Optional.empty();
		}
		
		return response;
	}

}
