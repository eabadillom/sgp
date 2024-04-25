package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Gabo
 */
public class CatAreaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idArea;
    private String descripcion;
    private short activo;

    public CatAreaDTO() {
    }

    public CatAreaDTO(Integer idArea, String descripcion, short activo) {
        this.idArea = idArea;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

	@Override
	public String toString() {
		return "{\"idArea\":\"" + idArea + "\", \"descripcion\":\"" + descripcion + "\", \"activo\":\"" + activo
				+ "\"}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idArea);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatAreaDTO other = (CatAreaDTO) obj;
		return Objects.equals(idArea, other.idArea);
	}
}
