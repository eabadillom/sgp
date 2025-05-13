package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import mx.com.ferbo.model.imss.CatTipoIncapacidadIMSS;

@Entity
@Table(name = "cat_tipo_incapacidad")
@NamedQueries({
    @NamedQuery(name = "CatTipoIncapacidadSAT.findByAll",query = "SELECT ti FROM CatTipoIncapacidadSAT ti")
})
public class CatTipoIncapacidadSAT implements Serializable{
    
    @Id
    @Size(max = 4)
    @Column(name = "cd_tipo_incapacidad")
    @Basic(optional = false)    
    private String clave;

    @Column(name = "nb_descripcion")
    @Size(max = 80)
    @Basic(optional = false)
    private String descripcion;
    
    @OneToMany(mappedBy = "incapacidadSAT")
    private List<CatTipoIncapacidadIMSS> tipoIncapacidadIMSS;

    public CatTipoIncapacidadSAT(){

    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<CatTipoIncapacidadIMSS> getTipoIncapacidadIMSS() {
        return tipoIncapacidadIMSS;
    }

    public void setTipoIncapacidadIMSS(List<CatTipoIncapacidadIMSS> tipoIncapacidadIMSS) {
        this.tipoIncapacidadIMSS = tipoIncapacidadIMSS;
    }

    @Override
    public String toString() {
        return "CatTipoIncapacidad [clave=" + clave + ", descripcion=" + descripcion + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clave == null) ? 0 : clave.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CatTipoIncapacidadSAT other = (CatTipoIncapacidadSAT) obj;
        if (clave == null) {
            if (other.clave != null)
                return false;
        } else if (!clave.equals(other.clave))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        return true;
    }

    

}
