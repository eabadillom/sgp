package mx.com.ferbo.dto.sat;

import java.io.Serializable;
import java.util.Date;

public class OrigRecursoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String clave;
    private String descripcion;
    private Date vigenciaInicio;
    private Date vigenciaFin;

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
        return "OrigRecursoDTO [clave=" + clave + ", descripcion=" + descripcion + ", vigenciaInicio=" + vigenciaInicio
                + ", vigenciaFin=" + vigenciaFin + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clave == null) ? 0 : clave.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((vigenciaInicio == null) ? 0 : vigenciaInicio.hashCode());
        result = prime * result + ((vigenciaFin == null) ? 0 : vigenciaFin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrigRecursoDTO other = (OrigRecursoDTO) obj;
        if (clave == null) {
            if (other.clave != null)
                return false;
        } else if (!clave.equals(other.clave))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (vigenciaInicio == null) {
            if (other.vigenciaInicio != null)
                return false;
        } else if (!vigenciaInicio.equals(other.vigenciaInicio))
            return false;
        if (vigenciaFin == null) {
            if (other.vigenciaFin != null)
                return false;
        } else if (!vigenciaFin.equals(other.vigenciaFin))
            return false;
        return true;
    }

    


}
