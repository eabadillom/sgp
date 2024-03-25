package mx.com.ferbo.util;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.convert.DateTimeConverter;

public class MxDateTimeConverter extends DateTimeConverter {
	private static Logger log = LogManager.getLogger(MxDateTimeConverter.class);
	
	public MxDateTimeConverter() {
		Locale locale = new Locale("es-MX");
		log.debug("MxDateTimeConverter Locale: {}", locale);
		setLocale(locale);
	}
}
