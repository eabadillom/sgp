package mx.com.ferbo.dto;

import java.io.Serializable;

public class CatArticuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idArticulo;
    private String descripcion;
    private Integer cantidadMax;
    private String unidad;
    private short activo;
    private String detalle;

    public CatArticuloDTO() {
    }
    
    public CatArticuloDTO(Integer idArticulo, String descripcion, short activo) {
        super();
        this.idArticulo = idArticulo;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    public CatArticuloDTO(Integer idArticulo, String descripcion, Integer cantidadMax, String unidad, short activo) {
        super();
        this.idArticulo = idArticulo;
        this.descripcion = descripcion;
        this.cantidadMax = cantidadMax;
        this.unidad = unidad;
        this.activo = activo;
    }

    public CatArticuloDTO(Integer idArticulo, String descripcion, Integer cantidadMax, String unidad, String detalle, short activo) {
        super();
        this.idArticulo = idArticulo;
        this.descripcion = descripcion;
        this.cantidadMax = cantidadMax;
        this.unidad = unidad;
        this.activo = activo;
        this.detalle = detalle;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadMax() {
        return cantidadMax;
    }

    public void setCantidadMax(Integer cantidadMax) {
        this.cantidadMax = cantidadMax;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}
