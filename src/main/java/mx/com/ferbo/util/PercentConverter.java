package mx.com.ferbo.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;

@FacesConverter("percentConverter")
public class PercentConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // 15 → 0.15
        return new BigDecimal(value).divide(BigDecimal.valueOf(100));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        if (value == null) {
            return "";
        }
        // 0.15 → 15
        return value.multiply(BigDecimal.valueOf(100)).toPlainString();
    }
}
