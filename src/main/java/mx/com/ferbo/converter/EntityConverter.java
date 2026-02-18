package mx.com.ferbo.converter;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter<Object> {
	private static Logger log = LogManager.getLogger(EntityConverter.class);
    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String uuid) {
        for (Map.Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(uuid)) {
            	log.debug("Retornando key: {}", entry.getKey());
                return entry.getKey();
            }
        }
        log.debug("Retornando null...");
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object entity) {
        synchronized (entities) {
            if (!entities.containsKey(entity)) {
                String uuid = UUID.randomUUID().toString();
                entities.put(entity, uuid);
                log.debug("Retornando uuid: {}", uuid);
                return uuid;
            } else {
            	log.debug("Retornando entity: {}", entity);
                return entities.get(entity);
            }
        }
    }
}
