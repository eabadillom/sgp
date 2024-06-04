package mx.com.ferbo.dto.sat;

public class TipoNominaDTO {
    
    private String clave;
    private String descripcion;

    public TipoNominaDTO(){

    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoNominaDTO [clave=" + clave + ", descripcion=" + descripcion + "]";
    }

    

}
