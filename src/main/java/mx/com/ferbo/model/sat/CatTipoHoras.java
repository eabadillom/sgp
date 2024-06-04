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
@Table(name = "cat_tipo_horas")
@NamedQueries({
    @NamedQuery(name = "CatTipoHoras.findByAll", query = "SELECT th FROM CatTipoHoras th")
})
public class CatTipoHoras implements Serializable {

    private static final long serialVersionUID = -1997145549269690152L;

    @Id
    @Size(max = 4)
    @Column(name = "cd_tipo_hora")
    @Basic(optional = false)
    private String clave;

    @Size(max = 10)
    @Column(name = "nb_descripcion")
    @Basic(optional = false)
    private String descripcion;

    @Column(name = "fh_vigencia_ini")
    @Basic(optional = false)
    private Date vigenciaInicio;

    @Column(name = "fh_vigencia_fin")
    private Date vigenciaFin;
    
    public CatTipoHoras(){        

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
        return "CatTipoHoras [clave=" + clave + ", descripcion=" + descripcion + ", vigenciaInicio=" + vigenciaInicio
                + ", vigenciaFin=" + vigenciaFin + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clave == null) ? 0 : clave.hashCode());
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
        CatTipoHoras other = (CatTipoHoras) obj;
        if (clave == null) {
            if (other.clave != null)
                return false;
        } else if (!clave.equals(other.clave))
            return false;
        return true;
    }

    
    

    
}
