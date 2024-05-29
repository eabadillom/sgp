package mx.com.ferbo.model.sat;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_tipo_incapacidad")
@NamedQueries({
    @NamedQuery(name = "CatTipoIncapacidad.findByAll",query = "SELECT ti FROM CatTipoIncapacidad ti")
})
public class CatTipoIncapacidad implements Serializable{
    
    @Id
    @Size(max = 4)
    @Column(name = "cd_tipo_incapacidad")
    @Basic(optional = false)    
    private String clave;

    @Column(name = "nb_descripcion")
    @Size(max = 80)
    @Basic(optional = false)
    private String descripcion;

    public CatTipoIncapacidad(){

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
        CatTipoIncapacidad other = (CatTipoIncapacidad) obj;
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
