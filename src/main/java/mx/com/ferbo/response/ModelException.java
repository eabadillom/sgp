package mx.com.ferbo.response;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class ModelException 
{
    @SerializedName(value="Message")
    private String message = null;

    @SerializedName(value="ModelState")
    private Map<String, String[]> modelState = new HashMap<>();

    public String getMessage() {
            return message;
    }

    public void setMessage(String message) {
            this.message = message;
    }

    public Map<String, String[]> getModelState() {
            return modelState;
    }

    public void setModelState(Map<String, String[]> modelState) {
            this.modelState = modelState;
    }

    @Override
    public String toString() {
            return "{\"message\":\"" + message + "\", \"modelState\":\"" + modelState + "\"}";
    }
    
}
