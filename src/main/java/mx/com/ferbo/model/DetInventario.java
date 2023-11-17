package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "det_inventario")
@NamedQueries({
    @NamedQuery(name = "DetInventario.findAll", query = "SELECT d FROM DetInventario d")})
public class DetInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_inventario")
    private Integer idInventario;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @Basic(optional = false)
    @Column(name = "visible")
    private short visible;
    @Basic(optional = false)
    @Column(name = "fecha_captura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaptura;
    @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo")
    @ManyToOne
    private CatArticulo idArticulo;
    @JoinColumn(name = "id_prenda", referencedColumnName = "id_prenda")
    @ManyToOne
    private CatPrenda idPrenda;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idInventario")
    private List<BitacoraInventario> bitacoraInventarioList;

    public DetInventario() {
    }

    public DetInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public DetInventario(Integer idInventario, int cantidad, short activo, short visible, Date fechaCaptura) {
        this.idInventario = idInventario;
        this.cantidad = cantidad;
        this.activo = activo;
        this.visible = visible;
        this.fechaCaptura = fechaCaptura;
    }

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public short getVisible() {
        return visible;
    }

    public void setVisible(short visible) {
        this.visible = visible;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public CatArticulo getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(CatArticulo idArticulo) {
        this.idArticulo = idArticulo;
    }

    public CatPrenda getIdPrenda() {
        return idPrenda;
    }

    public void setIdPrenda(CatPrenda idPrenda) {
        this.idPrenda = idPrenda;
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
        hash += (idInventario != null ? idInventario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetInventario)) {
            return false;
        }
        DetInventario other = (DetInventario) object;
        if ((this.idInventario == null && other.idInventario != null) || (this.idInventario != null && !this.idInventario.equals(other.idInventario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetInventario[ idInventario=" + idInventario + " ]";
    }
    
}
