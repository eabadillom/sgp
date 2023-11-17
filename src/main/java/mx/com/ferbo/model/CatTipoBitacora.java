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
@Table(name = "cat_tipo_bitacora")
@NamedQueries({
    @NamedQuery(name = "CatTipoBitacora.findAll", query = "SELECT c FROM CatTipoBitacora c")})
public class CatTipoBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Integer idTipo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipo")
    private List<BitacoraInventario> bitacoraInventarioList;

    public CatTipoBitacora() {
    }

    public CatTipoBitacora(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public CatTipoBitacora(Integer idTipo, String descripcion) {
        this.idTipo = idTipo;
        this.descripcion = descripcion;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<BitacoraInventario> getBitacoraInventarioList() {
        return bitacoraInventarioList;
    }

    public void setBitacoraInventarioList(List<BitacoraInventario> bitacoraInventarioList) {
        this.bitacoraInventarioList = bitacoraInventarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatTipoBitacora)) {
            return false;
        }
        CatTipoBitacora other = (CatTipoBitacora) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CatTipoBitacora[ idTipo=" + idTipo + " ]";
    }
    
}
