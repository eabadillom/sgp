package mx.com.ferbo.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageTool {
	private static Logger log = LogManager.getLogger(ImageTool.class);
	
	public static OutputStream resize(InputStream imagenEntrada, int anchoFinal, int altoFinal, String formatoSalida)
	throws IOException {
		OutputStream imagenSalida = null;
		
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
        while (anchoActual > anchoFinal * 2 || altoActual > altoFinal * 2) {
            anchoActual = Math.max(anchoFinal, anchoActual / 2);
            altoActual = Math.max(altoFinal, altoActual / 2);

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
        BufferedImage imagenFinal = new BufferedImage(anchoFinal, altoFinal, tipoColor);
        Graphics2D g2dFinal = imagenFinal.createGraphics();
        g2dFinal.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2dFinal.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2dFinal.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2dFinal.drawImage(imagenEscalada, 0, 0, anchoFinal, altoFinal, null);
        g2dFinal.dispose();
        
        imagenSalida = new ByteArrayOutputStream();
        ImageIO.write(imagenFinal, formatoSalida, imagenSalida);
        
        return imagenSalida;
    }
}
