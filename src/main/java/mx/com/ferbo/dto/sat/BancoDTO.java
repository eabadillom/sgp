package mx.com.ferbo.dto.sat;

import java.io.Serializable;
import java.util.Date;

public class BancoDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
	private String idBanco;
    private String descripcion;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    
    public String getIdBanco() {
        return idBanco;
    }
    public void setIdBanco(String idBanco) {
        this.idBanco = idBanco;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public Date getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idBanco == null) ? 0 : idBanco.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
        result = prime * result + ((fechaFin == null) ? 0 : fechaFin.hashCode());
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
        BancoDTO other = (BancoDTO) obj;
        if (idBanco == null) {
            if (other.idBanco != null)
                return false;
        } else if (!idBanco.equals(other.idBanco))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (fechaInicio == null) {
            if (other.fechaInicio != null)
                return false;
        } else if (!fechaInicio.equals(other.fechaInicio))
            return false;
        if (fechaFin == null) {
            if (other.fechaFin != null)
                return false;
        } else if (!fechaFin.equals(other.fechaFin))
            return false;
        return true;
    }

    

}
