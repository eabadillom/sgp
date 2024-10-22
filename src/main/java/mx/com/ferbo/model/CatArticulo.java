package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
@Table(name = "cat_articulo")
@NamedQueries({
    @NamedQuery(name = "CatArticulo.findAll", query = "SELECT c FROM CatArticulo c"),
    @NamedQuery(name = "CatArticulo.findAllActive", query = "SELECT c FROM CatArticulo c WHERE c.activo = 1")
})

public class CatArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_articulo")
    private Integer idArticulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cantidadMax")
    private Integer cantidadMax;
    @Column(name = "unidad")
    private String unidad;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @Column(name = "detalle")
    private String detalle;
    @OneToMany(mappedBy = "idArticulo")
    private List<DetInventario> detInventarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArticulo")
    private List<DetSolicitudArticulo> detSolicitudArticuloList;

    public CatArticulo() {
    }

    public CatArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public CatArticulo(Integer idArticulo, short activo) {
        this.idArticulo = idArticulo;
        this.activo = activo;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadMax() {
        return cantidadMax;
    }

    public void setCantidadMax(Integer cantidadMax) {
        this.cantidadMax = cantidadMax;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<DetInventario> getDetInventarioList() {
        return detInventarioList;
    }

    public void setDetInventarioList(List<DetInventario> detInventarioList) {
        this.detInventarioList = detInventarioList;
    }

    public List<DetSolicitudArticulo> getDetSolicitudArticuloList() {
        return detSolicitudArticuloList;
    }

    public void setDetSolicitudArticuloList(List<DetSolicitudArticulo> detSolicitudArticuloList) {
        this.detSolicitudArticuloList = detSolicitudArticuloList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.idArticulo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatArticulo other = (CatArticulo) obj;
        return Objects.equals(this.idArticulo, other.idArticulo);
    }

    @Override
    public String toString() {
        return "CatArticulo{" + "idArticulo=" + idArticulo + ", descripcion=" + descripcion + ", cantidadMax=" + cantidadMax + ", unidad=" + unidad + ", activo=" + activo + ", detalle=" + detalle + '}';
    }
    
}
