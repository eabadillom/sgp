package mx.com.ferbo.dto;

import java.io.Serializable;

/**
 *
 * @author Gabo
 */
public class CatTipoSolicitudDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTipoSolicitud;
    private String descripcion;
    private Short activo;

    public CatTipoSolicitudDTO() {
    }

    public CatTipoSolicitudDTO(Integer idTipoSolicitud, String descripcion) {
        this.idTipoSolicitud = idTipoSolicitud;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(Integer idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
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

}
