package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "cat_perfil")
@NamedQueries({
    @NamedQuery(name = "CatPerfil.findAll", query = "SELECT new mx.com.ferbo.dto.CatPerfilDTO("
            + " c.idPerfil,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPerfil c"),
    @NamedQuery(name = "CatPerfil.findByActive", query = "SELECT new mx.com.ferbo.dto.CatPerfilDTO("
            + " c.idPerfil,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPerfil c"
            + " WHERE c.activo = 1")})
public class CatPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_perfil")
    private Integer idPerfil;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private Short activo;
    @OneToMany(mappedBy = "idPerfil")
    private List<BitacoraCatPerfil> bitacoraCatPerfilList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfil")
    private List<DetEmpleado> detEmpleadoList;

    public CatPerfil() {
    }

    public CatPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
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

    public List<BitacoraCatPerfil> getBitacoraCatPerfilList() {
        return bitacoraCatPerfilList;
    }

    public void setBitacoraCatPerfilList(List<BitacoraCatPerfil> bitacoraCatPerfilList) {
        this.bitacoraCatPerfilList = bitacoraCatPerfilList;
    }

    public List<DetEmpleado> getDetEmpleadoList() {
        return detEmpleadoList;
    }

    public void setDetEmpleadoList(List<DetEmpleado> detEmpleadoList) {
        this.detEmpleadoList = detEmpleadoList;
    }    
}
