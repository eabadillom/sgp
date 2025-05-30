package mx.com.ferbo.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageTool {
	private static Logger log = LogManager.getLogger(ImageTool.class);
	
	public static class ImageSize {
		private final Integer height;
		private final Integer width;
		private final BigDecimal aspectRatio;
		
		public ImageSize(Integer width, Integer height) {
			this.height = height;
			this.width = width;
			
			if(height == null)
				throw new IllegalArgumentException("Debe indicar el alto.");
			
			if(height == 0)
				throw new IllegalArgumentException("El alto no puede ser cero para calcular la relación de aspecto.");
			
			if(width == null)
				throw new IllegalArgumentException("Debe indicar el ancho.");
			
			if(width == 0)
				throw new IllegalArgumentException("El ancho no puede ser cero.");
			
			this.aspectRatio = new BigDecimal(width).setScale(0, RoundingMode.HALF_UP)
					.divide(new BigDecimal(this.height).setScale(0, RoundingMode.HALF_UP), RoundingMode.HALF_UP)
					.setScale(2, RoundingMode.HALF_UP)
					;
		}

		public Integer getHeight() {
			return height;
		}

		public Integer getWidth() {
			return width;
		}

		public BigDecimal getAspectRatio() {
			return aspectRatio;
		}

		@Override
		public String toString() {
			return "{\"width\":\"" + width + "\", \"height\":\"" + height + "\",  aspectRatio\":\"" + aspectRatio + "}";
		}
		
		
	}
	
	public static ImageSize getSize(File archivoImagen)
	throws IOException {
		ImageSize size = null;
		BufferedImage imagen = ImageIO.read(archivoImagen);
		if (imagen == null) {
            throw new IOException("No se pudo leer la imagen: " + archivoImagen.getPath());
        }
        size = new ImageSize(imagen.getWidth(), imagen.getHeight());
		return size;
	}
	
	public static ImageSize getSize(String imagePath)
	throws IOException {
		File imageFile = null;
		imageFile = new File(imagePath);
		return getSize(imageFile);
	}
	
	public static ImageSize resize(ImageSize size, BigDecimal zoomRatio) {
		ImageSize newImageSize = null;
		Integer newHeight = null;
		Integer newWidth = null;
		
		if(size == null)
			throw new IllegalArgumentException("El objeto ImageSize no debe ser null");
		
		if(zoomRatio == null)
			throw new IllegalArgumentException("El parámetro zoomRatio no debe ser null");
		
		if(zoomRatio.compareTo(BigDecimal.ZERO) <= 0)
			throw new IllegalArgumentException("El parámetro zoomRatio debe ser mayor a 0");
			
		
		newHeight = new BigDecimal(size.getHeight()).setScale(3, RoundingMode.HALF_UP)
				.multiply(zoomRatio)
				.setScale(0, RoundingMode.HALF_UP)
				.intValue();
		
		newWidth = new BigDecimal(size.getWidth()).setScale(3, RoundingMode.HALF_UP)
				.multiply(zoomRatio)
				.setScale(0, RoundingMode.HALF_UP)
				.intValue()
				;
		
		newImageSize = new ImageSize(newWidth, newHeight);
		
		return newImageSize;
	}
	
	public static ImageSize resizeToMaxHeight(ImageSize size, Integer maxHeight) {
		ImageSize newImageSize = null;
		Integer newHeight = null;
		Integer newWidth = null;
		
		BigDecimal scaleFactor = null;
		
		log.info("Original size: {}", size);
		
		if(maxHeight == null)
			throw new IllegalArgumentException("La altura máxima no debe ser null");
		
		if(maxHeight.compareTo(0) <= 0)
			throw new IllegalArgumentException("La altura máxima debe ser mayor a cero.");
		
		scaleFactor = new BigDecimal(maxHeight).setScale(3, RoundingMode.HALF_UP)
				.divide(new BigDecimal(size.getHeight()), RoundingMode.HALF_UP)
				.setScale(3, RoundingMode.HALF_UP);
		
		newHeight = new BigDecimal(size.getHeight())
				.multiply(scaleFactor)
				.setScale(0, RoundingMode.HALF_UP)
				.intValue();
		
		newWidth = new BigDecimal(size.getWidth())
				.multiply(scaleFactor)
				.setScale(0, RoundingMode.HALF_UP)
				.intValue();
		
		newImageSize = new ImageSize(newWidth, newHeight);
		
		return newImageSize;
	}
	
	public static InputStream getInputStream(String imagePath)
	throws IOException {
		return new FileInputStream(imagePath);
	}
	
	public static byte[] getBytes(String filePath)
	throws SGPException {
		byte[] contenido = null;
		File fImagen = null;
		FileInputStream isImagen = null;
		
		try {
			if(filePath == null)
				throw new SGPException("Debe indicar un nombre de archivo");
			
			fImagen = new File(filePath);
			
			if(fImagen.exists() == false)
				throw new SGPException("El archivo " + filePath + " no existe");
			
			isImagen = new FileInputStream(fImagen);
			
			contenido = IOUtil.read(isImagen);
			
		} catch (FileNotFoundException ex) {
			log.error("Problema para leer el archivo {}", filePath, ex);
		} catch (IOException ex) {
			log.error("Problema para leer el archivo {}", filePath, ex);
		} finally {
			IOUtil.close(isImagen);
		}
		
		return contenido;
	}
	
	public static void resize(InputStream imagenEntrada, OutputStream imagenSalida, ImageSize size, String formatoSalida)
	throws IOException {
		log.debug("Redimensionando imagen: ancho = {}, alto: {}", size.getWidth(), size.getHeight());
        BufferedImage imagenOriginal = ImageIO.read(imagenEntrada);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imagenEntrada);
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            String formato = reader.getFormatName();
            reader.setInput(imagenEntrada);
            log.info("Formato: {}", formato.toUpperCase()); 
        } else {
            log.info("Formato: DESCONOCIDO");
        }
        
        if (imagenOriginal == null) {
            throw new IOException("No se pudo leer la imagen desde el InputStream.");
        }

        int tipoColor = imagenOriginal.getType();
        if (tipoColor == BufferedImage.TYPE_CUSTOM) {
            // Fallback seguro si el tipo es desconocido
            tipoColor = BufferedImage.TYPE_INT_ARGB;
        }

        int anchoActual = imagenOriginal.getWidth();
        int altoActual = imagenOriginal.getHeight();
        BufferedImage imagenEscalada = imagenOriginal;

        // Reducción progresiva
        while (anchoActual > size.getWidth() * 2 || altoActual > size.getHeight() * 2) {
            anchoActual = Math.max(size.getWidth(), anchoActual / 2);
            altoActual = Math.max(size.getHeight(), altoActual / 2);

            BufferedImage temp = new BufferedImage(anchoActual, altoActual, tipoColor);
            Graphics2D g2d = temp.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(imagenEscalada, 0, 0, anchoActual, altoActual, null);
            g2d.dispose();

            imagenEscalada = temp;
        }
        
        // Redimensionado final
        BufferedImage imagenFinal = new BufferedImage(size.getWidth(), size.getHeight(), tipoColor);
        Graphics2D g2dFinal = imagenFinal.createGraphics();
        g2dFinal.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2dFinal.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2dFinal.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2dFinal.drawImage(imagenEscalada, 0, 0, size.getWidth(), size.getHeight(), null);
        g2dFinal.dispose();
        
        ImageIO.write(imagenFinal, formatoSalida, imagenSalida);
    }
}
