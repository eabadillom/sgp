package mx.com.ferbo.dto.sat;

import java.io.Serializable;
import java.util.Date;

public class TipoHorasDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String clave;
    private String descripcion;
    private Date vigenciaInicio;
    private Date vigenciaFin;

    public TipoHorasDTO(){

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

    public Date getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Date vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    @Override
    public String toString() {
        return "TipoHorasDTO [clave=" + clave + ", descripcion=" + descripcion + ", vigenciaInicio=" + vigenciaInicio
                + ", vigenciaFin=" + vigenciaFin + "]";
    }

    

}
