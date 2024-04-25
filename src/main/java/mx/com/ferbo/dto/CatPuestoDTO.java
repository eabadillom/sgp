package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Gabo
 */
public class CatPuestoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idPuesto;
    private String descripcion;
    private short activo;

    public CatPuestoDTO() {
    }

    public CatPuestoDTO(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public CatPuestoDTO(Integer idPuesto, String descripcion, short activo) {
        this.idPuesto = idPuesto;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
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
		return "{\"idPuesto\":\"" + idPuesto + "\", \"descripcion\":\"" + descripcion + "\", \"activo\":\"" + activo
				+ "\"}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idPuesto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPuestoDTO other = (CatPuestoDTO) obj;
		return Objects.equals(idPuesto, other.idPuesto);
	}
}
