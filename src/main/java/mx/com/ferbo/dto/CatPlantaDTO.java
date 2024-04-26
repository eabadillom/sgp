package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Gabo
 */
public class CatPlantaDTO implements Serializable {

    private static final long serialVersionUID = 2L;
    private Integer idPlanta;
    private String descripcion;
    private short activo;

    public CatPlantaDTO() {
    }

    public CatPlantaDTO(Integer idPlanta) {
        this.idPlanta = idPlanta;
    }

    public CatPlantaDTO(Integer idPlanta, String descripcion, short activo) {
        this.idPlanta = idPlanta;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
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
        return "mx.com.ferbo.model.CatPlanta[ idPlanta=" + idPlanta + " ]";
    }

	@Override
	public int hashCode() {
		return Objects.hash(idPlanta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPlantaDTO other = (CatPlantaDTO) obj;
		return Objects.equals(idPlanta, other.idPlanta);
	}
}
