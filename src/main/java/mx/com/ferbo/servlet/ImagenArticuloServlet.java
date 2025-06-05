package mx.com.ferbo.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.DataSourceManager;
import mx.com.ferbo.util.ImageTool;
import mx.com.ferbo.util.ImageTool.ImageSize;
import mx.com.ferbo.util.SGPException;

public class ImagenArticuloServlet extends HttpServlet {

	private static final long serialVersionUID = 4446082642491341937L;
	
	private static Logger log = LogManager.getLogger(ImagenArticuloServlet.class);
	
	public ImagenArticuloServlet() {
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		ServletContext application = null;
		String imageDirectory = null;
		String imageName = null;
		String imagePath = null;
		File   imageFile = null;
		String jndiName = null;
		
		try {
			imageName = request.getParameter("name");	
			if(imageName == null)
				throw new SGPException("No se proporcionó el nombre de la imgen.");
			
			if(imageName.isEmpty())
				throw new SGPException("El nombre de la imagen se encuentra vacío.");
			
			application = request.getSession().getServletContext();
			imagePath = application.getRealPath("resources/recursos/images/articulos/" + imageName);
			log.debug("Path a index.html: {}", imagePath);
			imageFile = new File(imagePath);
			
			if(imageFile.exists()) {
				this.procesaImagen(request, response, imagePath);
				return;
			}
			
			jndiName = "sgp/imagenes";
			imageDirectory = DataSourceManager.getJndiParameter(jndiName);
			imagePath = imageDirectory + File.separator + "articulos" + File.separator + imageName;
			
			imageFile = new File(imagePath);
			if(imageFile.exists() == false)
				throw new SGPException("Imagen no encontrada: " + imageName);
			this.procesaImagen(request, response, imagePath);
		} catch(Exception ex) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.error("Problema para pausar la aplicación por 5 segundos...", e);
			}
			log.error("Problema para obtener la imagen...", ex);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void procesaImagen(HttpServletRequest request, HttpServletResponse response, String imagePath)
	throws IOException {
		ImageSize size = null;
		ImageSize newSize = null;
		FileInputStream input = null;
		
		size = ImageTool.getSize(imagePath);
		newSize = ImageTool.resizeToMaxHeight(size, 200);
		input = new FileInputStream(imagePath);
		response.setContentType("image/jpeg");
		ImageTool.resize(input, response.getOutputStream(), newSize, "JPEG");
		response.getOutputStream().flush();
	}

}
