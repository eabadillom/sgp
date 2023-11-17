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
@Table(name = "cat_empresa")
@NamedQueries({
    @NamedQuery(name = "CatEmpresa.findAll", query = "SELECT new mx.com.ferbo.dto.CatEmpresaDTO("
            + " c.idEmpresa,"
            + " c.descripcion,"
            + " c.activo"
            + ") "
            + "FROM CatEmpresa c"),
    @NamedQuery(name = "CatEmpresa.findByActive", query = "SELECT new mx.com.ferbo.dto.CatEmpresaDTO("
            + " c.idEmpresa,"
            + " c.descripcion,"
            + " c.activo"
            + ") "
            + " FROM CatEmpresa c"
            + " WHERE c.activo = 1"),
    @NamedQuery(name = "CatEmpresa.findById", query = "SELECT new mx.com.ferbo.dto.CatEmpresaDTO("
            + " c.idEmpresa,"
            + " c.descripcion,"
            + " c.activo"
            + ") "
            + " FROM CatEmpresa c"
            + " WHERE c.activo = 1 AND c.idEmpresa = :idEmpresa")})
public class CatEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Integer idEmpresa;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private List<DetEmpleado> detEmpleadoList;

    public CatEmpresa() {
    }

    public CatEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public CatEmpresa(Integer idEmpresa, String descripcion, short activo) {
        this.idEmpresa = idEmpresa;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
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
