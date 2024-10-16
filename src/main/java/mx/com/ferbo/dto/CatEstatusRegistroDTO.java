package mx.com.ferbo.dto;

import java.io.Serializable;

/**
 *
 * @author Gabo
 */
public class CatEstatusRegistroDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idEstatus;
    private String descripcion;
    private String codigo;
    private short activo;

    public CatEstatusRegistroDTO() {
    }
    
    public CatEstatusRegistroDTO(Integer idEstatus, String descripcion) {
        this.idEstatus = idEstatus;
        this.descripcion = descripcion;
    }

    public CatEstatusRegistroDTO(Integer idEstatus, String descripcion, String codigo) {
        this.idEstatus = idEstatus;
        this.descripcion = descripcion;
        this.codigo = codigo;
    }

    public CatEstatusRegistroDTO(Integer idEstatus, String descripcion, short activo) {
        this.idEstatus = idEstatus;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
