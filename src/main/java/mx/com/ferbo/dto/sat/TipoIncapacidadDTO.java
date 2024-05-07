package mx.com.ferbo.dto.sat;

public class TipoIncapacidadDTO {
    
    private String clave;
    private String descripcion;

    public TipoIncapacidadDTO(){

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
        return "TipoIncapacidadDTO [clave=" + clave + ", descripcion=" + descripcion + "]";
    }

}
