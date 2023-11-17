package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "cat_estatus_registro")
@NamedQueries({
    @NamedQuery(name = "CatEstatusRegistro.findAll", query = "SELECT c FROM CatEstatusRegistro c")})
public class CatEstatusRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estatus")
    private Integer idEstatus;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @OneToMany(mappedBy = "idEstatus")
    private List<DetRegistro> detRegistroList;

    public CatEstatusRegistro() {
    }

    public CatEstatusRegistro(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public CatEstatusRegistro(Integer idEstatus, String descripcion, short activo) {
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

    public List<DetRegistro> getDetRegistroList() {
        return detRegistroList;
    }

    public void setDetRegistroList(List<DetRegistro> detRegistroList) {
        this.detRegistroList = detRegistroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstatus != null ? idEstatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatEstatusRegistro)) {
            return false;
        }
        CatEstatusRegistro other = (CatEstatusRegistro) object;
        if ((this.idEstatus == null && other.idEstatus != null) || (this.idEstatus != null && !this.idEstatus.equals(other.idEstatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CatEstatusRegistro[ idEstatus=" + idEstatus + " ]";
    }
    
}
