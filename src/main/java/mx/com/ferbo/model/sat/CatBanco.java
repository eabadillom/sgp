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
@Table(name = "cat_banco")
@NamedQueries({
    @NamedQuery(name = "CatBanco.findByAll", query = "SELECT b FROM CatBanco b"),
    @NamedQuery(name = "CatBanco.findById",query = "SELECT b FROM CatBanco b WHERE b.idBanco = :idBanco"),
    @NamedQuery(name = "CatBanco.findByDescripcion",query = "SELECT b FROM CatBanco b WHERE b.descripcion = :descripcion"),
    @NamedQuery(name = "CatBanco.findByNombre", query = "SELECT b FROM CatBanco b WHERE b.nombre = :nombre"),
    @NamedQuery(name = "CatBanco.findByVigenciaIni", query = "SELECT b FROM CatBanco b WHERE b.vigenciaInicio = :vigenciaIni"),
    @NamedQuery(name = "CatBanco.findByVigenciaFin",query = "SELECT b FROM CatBanco b WHERE b.vigenciaFin = :vigenciaFin")
})
public class CatBanco implements Serializable{
 
    @Id
    @Size(max = 3)
    @Column(name = "cod_banco")
    @Basic(optional = false)//permite que el campo no sea null si contiene true permite que el campo pueda ser null
    private String idBanco;

    @Column(name = "nb_descripcion")
    @Size(max = 40)
    @Basic(optional = false)
    private String descripcion;

    @Column(name = "nb_razon_social")
    @Size(max = 120)
    @Basic(optional = false)
    private String nombre;

    @Column(name = "fh_vigencia_ini")
    @Basic(optional = false)
    private Date vigenciaInicio;
    
    @Column(name = "fh_vigencia_fin")
    @Basic(optional = true)
    private Date vigenciaFin;

    public CatBanco(){

    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(String idBanco) {
        this.idBanco = idBanco;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        return "CatBanco [idBanco=" + idBanco + ", descripcion=" + descripcion + ", nombre=" + nombre
                + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idBanco == null) ? 0 : idBanco.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
        CatBanco other = (CatBanco) obj;
        if (idBanco == null) {
            if (other.idBanco != null)
                return false;
        } else if (!idBanco.equals(other.idBanco))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
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
