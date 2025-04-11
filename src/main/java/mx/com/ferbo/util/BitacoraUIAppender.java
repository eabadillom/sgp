package mx.com.ferbo.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "BitacoraUIAppender", category = "Core", elementType = "appender", printObject = true)
public class BitacoraUIAppender extends AbstractAppender {

	private static final List<String> mensajes = Collections.synchronizedList(new LinkedList<>());

	protected BitacoraUIAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout, false);
	}

	@PluginFactory
	public static BitacoraUIAppender createAppender(@PluginAttribute("name") String name) {
		return new BitacoraUIAppender(name, null, PatternLayout.createDefaultLayout());
	}

	@Override
	public void append(LogEvent event) {
		// Solo guardar si el mensaje contiene cierta palabra o nivel
		if ((event.getLevel() == Level.INFO || event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR)
				&& event.getMessage().getFormattedMessage().contains("[UI]")) {
			String logMsg = new String(getLayout().toByteArray(event));
			mensajes.add(logMsg);
		}
	}

	public static List<String> getMensajes() {
		return Collections.unmodifiableList(mensajes);
	}

	public static void limpiar() {
		mensajes.clear();
	}

}
