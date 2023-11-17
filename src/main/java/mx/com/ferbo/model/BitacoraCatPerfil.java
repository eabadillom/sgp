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
@Table(name = "bitacora_cat_perfil")
@NamedQueries({
    @NamedQuery(name = "BitacoraCatPerfil.findAll", query = "SELECT b FROM BitacoraCatPerfil b")})
public class BitacoraCatPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    @Column(name = "fecha_captura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaptura;
    @Column(name = "modificacion")
    private Short modificacion;
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @ManyToOne
    private CatPerfil idPerfil;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne
    private DetEmpleado idEmpleado;

    public BitacoraCatPerfil() {
    }

    public BitacoraCatPerfil(Integer idBitacora) {
        this.idBitacora = idBitacora;
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

    public Short getModificacion() {
        return modificacion;
    }

    public void setModificacion(Short modificacion) {
        this.modificacion = modificacion;
    }

    public CatPerfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(CatPerfil idPerfil) {
        this.idPerfil = idPerfil;
    }

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
}
