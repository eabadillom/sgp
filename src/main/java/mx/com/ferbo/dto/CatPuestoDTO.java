package mx.com.ferbo.dto;

import java.io.Serializable;

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

}
