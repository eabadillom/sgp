package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Gabo
 */
public class CatPerfilDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idPerfil;
    private String descripcion;
    private Short activo;

    public CatPerfilDTO() {
    }

    public CatPerfilDTO(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public CatPerfilDTO(Integer idPerfil, String descripcion, Short activo) {
        this.idPerfil = idPerfil;
        this.descripcion = descripcion;
        this.activo = activo;
    }
   
    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
        this.activo = activo;
    }

	@Override
	public String toString() {
		return "{\"idPerfil\":\"" + idPerfil + "\", \"descripcion\":\"" + descripcion + "\", \"activo\":\"" + activo
				+ "\"}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idPerfil);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatPerfilDTO other = (CatPerfilDTO) obj;
		return Objects.equals(idPerfil, other.idPerfil);
	}
}
