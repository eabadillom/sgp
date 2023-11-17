package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabo
 */
@Entity
@Table(name = "bitacora_inventario")
@NamedQueries({
    @NamedQuery(name = "BitacoraInventario.findAll", query = "SELECT b FROM BitacoraInventario b")})
public class BitacoraInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    @Basic(optional = false)
    @Column(name = "fecha_captura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaptura;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne(optional = false)
    private CatTipoBitacora idTipo;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private DetEmpleado idEmpleado;
    @JoinColumn(name = "id_inventario", referencedColumnName = "id_inventario")
    @ManyToOne(optional = false)
    private DetInventario idInventario;

    public BitacoraInventario() {
    }

    public BitacoraInventario(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    public BitacoraInventario(Integer idBitacora, Date fechaCaptura) {
        this.idBitacora = idBitacora;
        this.fechaCaptura = fechaCaptura;
    }

    public Integer getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public CatTipoBitacora getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(CatTipoBitacora idTipo) {
        this.idTipo = idTipo;
    }

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public DetInventario getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(DetInventario idInventario) {
        this.idInventario = idInventario;
    }
}
