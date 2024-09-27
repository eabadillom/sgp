package mx.com.ferbo.model.sat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cat_tipo_otro_pago")
@NamedQueries({
    @NamedQuery(name = "CatTipoOtroPago.findByAll", query = "SELECT op FROM CatTipoOtroPago op ORDER BY op.clave")
})
public class CatTipoOtroPago implements Serializable{

    private static final long serialVersionUID = -6102720088570298774L;

	@Id
    @Column(name = "cd_tipo_otro_pago")
    @Size(max = 3)    
    @Basic(optional = false)
    private String clave;

    @Column(name = "nb_descripcion")
    @Size(max = 180)
    @Basic(optional = false)
    private String descripcion;

    @Column(name = "fh_vigencia_ini")    
    @Basic(optional = false)
    private Date vigenciaInicio;

    @Column(name = "fh_vigencia_fin")
    @Basic(optional = true)
    private Date vigenciaFin;

    public CatTipoOtroPago (){

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

    public Date getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Date vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    @Override
    public String toString() {
        return "CatTipoOtroPago [clave=" + clave + ", descripcion=" + descripcion + ", vigenciaInicio=" + vigenciaInicio
                + ", vigenciaFin=" + vigenciaFin + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clave == null) ? 0 : clave.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((vigenciaInicio == null) ? 0 : vigenciaInicio.hashCode());
        result = prime * result + ((vigenciaFin == null) ? 0 : vigenciaFin.hashCode());
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
        CatTipoOtroPago other = (CatTipoOtroPago) obj;
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
        if (vigenciaInicio == null) {
            if (other.vigenciaInicio != null)
                return false;
        } else if (!vigenciaInicio.equals(other.vigenciaInicio))
            return false;
        if (vigenciaFin == null) {
            if (other.vigenciaFin != null)
                return false;
        } else if (!vigenciaFin.equals(other.vigenciaFin))
            return false;
        return true;
    }

    
    
}
