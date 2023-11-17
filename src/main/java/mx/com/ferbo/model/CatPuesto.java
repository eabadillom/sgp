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
@Table(name = "cat_puesto")
@NamedQueries({
    @NamedQuery(name = "CatPuesto.findAll", query = "SELECT new mx.com.ferbo.dto.CatPuestoDTO("
            + " c.idPuesto,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPuesto c"),
    @NamedQuery(name = "CatPuesto.findByActive", query = "SELECT NEW mx.com.ferbo.dto.CatPuestoDTO("
            + " c.idPuesto,"
            + " c.descripcion,"
            + " c.activo"
            + ")"
            + " FROM CatPuesto c"
            + " WHERE c.activo = 1")})
public class CatPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_puesto")
    private Integer idPuesto;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuesto")
    private List<DetEmpleado> detEmpleadoList;

    public CatPuesto() {
    }

    public CatPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public CatPuesto(Integer idPuesto, String descripcion, short activo) {
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

    public List<DetEmpleado> getDetEmpleadoList() {
        return detEmpleadoList;
    }

    public void setDetEmpleadoList(List<DetEmpleado> detEmpleadoList) {
        this.detEmpleadoList = detEmpleadoList;
    }
    
}
