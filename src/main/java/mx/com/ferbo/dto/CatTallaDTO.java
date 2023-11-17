package mx.com.ferbo.dto;

import java.io.Serializable;

public class CatTallaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTalla;
    private String descripcion;
    private short activo;

    public CatTallaDTO() {
        super();
    }

    public CatTallaDTO(Integer idTalla, String descripcion, short activo) {
        super();
        this.idTalla = idTalla;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Integer idTalla) {
        this.idTalla = idTalla;
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
