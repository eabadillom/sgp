package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "cat_prenda")
@NamedQueries({
    @NamedQuery(name = "CatPrenda.findAll", query = "SELECT c FROM CatPrenda c"),
    @NamedQuery(name = "CatPrenda.findAllActive", query = "SELECT c FROM CatPrenda c WHERE c.activo = 1"),
    @NamedQuery(name = "CatPrenda.findById", query = "SELECT c FROM CatPrenda c WHERE c.activo = 1 AND c.idPrenda = :idPrenda")
})
public class CatPrenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prenda")
    private Integer idPrenda;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "precio", precision = 5, scale = 2)
    private BigDecimal precio;
    @Column(name = "cantidadMax")
    private Integer cantidadMax;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @Column(name = "detalle")
    private String detalle;
    @OneToMany(mappedBy = "idPrenda")
    private List<DetInventario> detInventarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPrenda")
    private List<DetSolicitudPrenda> detSolicitudPrendaList;

    public CatPrenda() {
    }

    public CatPrenda(Integer idPrenda) {
        this.idPrenda = idPrenda;
    }

    public CatPrenda(Integer idPrenda, short activo) {
        this.idPrenda = idPrenda;
        this.activo = activo;
    }

    public Integer getIdPrenda() {
        return idPrenda;
    }

    public void setIdPrenda(Integer idPrenda) {
        this.idPrenda = idPrenda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidadMax() {
        return cantidadMax;
    }

    public void setCantidadMax(Integer cantidadMax) {
        this.cantidadMax = cantidadMax;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public List<DetInventario> getDetInventarioList() {
        return detInventarioList;
    }

    public void setDetInventarioList(List<DetInventario> detInventarioList) {
        this.detInventarioList = detInventarioList;
    }

    public List<DetSolicitudPrenda> getDetSolicitudPrendaList() {
        return detSolicitudPrendaList;
    }

    public void setDetSolicitudPrendaList(List<DetSolicitudPrenda> detSolicitudPrendaList) {
        this.detSolicitudPrendaList = detSolicitudPrendaList;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    
}
