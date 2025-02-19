package mx.com.ferbo.model.imss;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import mx.com.ferbo.model.CatEstatusSolicitud;
import mx.com.ferbo.model.DetEmpleado;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "det_incapacidad")
@NamedQueries({
    @NamedQuery(name = "DetIncapacidad.findAll", query = "SELECT dri FROM DetIncapacidad dri"),
    @NamedQuery(name = "DetIncapacidad.findByEmpleado", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.idEmpleadoInc dei WHERE dei.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "DetIncapacidad.findByIncapacidadClave", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.tipoIncapacidad cti WHERE cti.clave = :clave"),
    @NamedQuery(name = "DetIncapacidad.findByEstatus", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.estatusSolicitud es WHERE es.clave = :clave"),
    @NamedQuery(name = "DetIncapacidad.findByPeriodo", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.idEmpleadoInc dei WHERE dei.idEmpleado = :idEmpleado AND ((:fechaInicio = dri.fechaInicio AND :fechaFin = dri.fechaFin) OR ((:fechaInicio = dri.fechaFin) AND (:fechaFin = dri.fechaInicio)) OR ((:fechaInicio BETWEEN dri.fechaInicio AND dri.fechaFin) OR (:fechaFin BETWEEN dri.fechaInicio AND dri.fechaFin)) OR ((dri.fechaInicio BETWEEN :fechaInicio AND :fechaFin) OR (dri.fechaFin BETWEEN :fechaInicio AND :fechaFin))) ORDER BY dri.fechaInicio DESC, dri.idIncapacidad DESC"),
    @NamedQuery(name = "DetIncapacidad.findByEmpleadoUltimoPeriodo", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.idEmpleadoInc dei WHERE dei.idEmpleado = :idEmpleado AND (:fechaInicio BETWEEN dri.fechaInicio AND dri.fechaFin) ORDER BY dri.fechaInicio DESC, dri.idIncapacidad DESC"),
    @NamedQuery(name = "DetIncapacidad.findByParametros", query = "SELECT dri FROM DetIncapacidad dri INNER JOIN dri.idEmpleadoInc dei WHERE dei.idEmpleado = :idEmpleado AND ((:fechaInicio = dri.fechaInicio AND :fechaFin = dri.fechaFin) OR ((:fechaInicio = dri.fechaFin) AND (:fechaFin = dri.fechaInicio)) OR ((:fechaInicio BETWEEN dri.fechaInicio AND dri.fechaFin) OR (:fechaFin BETWEEN dri.fechaInicio AND dri.fechaFin)) OR ((dri.fechaInicio BETWEEN :fechaInicio AND :fechaFin) OR (dri.fechaFin BETWEEN :fechaInicio AND :fechaFin))) ORDER BY dri.fechaInicio DESC, dri.idIncapacidad DESC")
})
public class DetIncapacidad implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_incapacidad")
    private Integer idIncapacidad;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empleado_inc", referencedColumnName = "id_empleado")
    private DetEmpleado idEmpleadoInc;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empleado_rev", referencedColumnName = "id_empleado")
    private DetEmpleado idEmpleadoRev;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_tp_incapacidad", referencedColumnName = "cd_tp_incapacidad")
    private CatTipoIncapacidadIMSS tipoIncapacidad;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_cont_incapacidad", referencedColumnName = "cd_control_incapacidad")
    private CatControlIncapacidadIMSS controlIncapacidad;
    
    @ManyToOne(optional = true)
    @Null
    @JoinColumn(name = "cd_secuela_rt", referencedColumnName = "cd_sec_riesgo_trabajo")
    private CatRiesgoTrabajoIMSS secuelaRiesgoTrabajo;
    
    @ManyToOne(optional = true)
    @Null
    @JoinColumn(name = "cd_tp_riesgo", referencedColumnName = "cd_tp_riesgo")
    private CatTipoRiesgoIMSS tipoRiesgo;
    
    @Column(name = "nb_folio")
    @NotNull
    @Basic(optional = false)
    private String folio;
    
    @Column(name = "nu_dias_autorizados")
    @NotNull
    @Basic(optional = false)
    private Integer diasAutorizados;
    
    @Column(name = "fh_inicio")
    @NotNull
    @Basic(optional = false)
    private Date fechaInicio;
    
    @Column(name = "fh_fin")
    @NotNull
    @Basic(optional = false)
    private Date fechaFin;
    
    @Column(name = "fh_captura")
    @NotNull
    @Basic(optional = false)
    private Date fechaCaptura;
    
    @Column(name = "tx_descripcion")
    @NotNull
    @Basic(optional = false)
    private String descripcion;
    
    @ManyToOne(optional = true)
    @NotNull
    @JoinColumn(name = "cd_estatus", referencedColumnName = "cd_estatus_solicitud")
    private CatEstatusSolicitud estatusSolicitud;
    
    public DetIncapacidad() 
    {
    }

    public DetIncapacidad(Integer idIncapacidad) 
    {
        this.idIncapacidad = idIncapacidad;
    }

    public Integer getIdIncapacidad() 
    {
        return idIncapacidad;
    }

    public void setIdIncapacidad(Integer idIncapacidad) 
    {
        this.idIncapacidad = idIncapacidad;
    }

    public DetEmpleado getIdEmpleadoInc() 
    {
        return idEmpleadoInc;
    }

    public void setIdEmpleadoInc(DetEmpleado idEmpleadoInc) 
    {
        this.idEmpleadoInc = idEmpleadoInc;
    }

    public DetEmpleado getIdEmpleadoRev() 
    {
        return idEmpleadoRev;
    }

    public void setIdEmpleadoRev(DetEmpleado idEmpleadoRev) 
    {
        this.idEmpleadoRev = idEmpleadoRev;
    }

    public CatTipoIncapacidadIMSS getTipoIncapacidad() 
    {
        return tipoIncapacidad;
    }

    public void setTipoIncapacidad(CatTipoIncapacidadIMSS tipoIncapacidad) 
    {
        this.tipoIncapacidad = tipoIncapacidad;
    }

    public CatControlIncapacidadIMSS getControlIncapacidad() 
    {
        return controlIncapacidad;
    }

    public void setControlIncapacidad(CatControlIncapacidadIMSS controlIncapacidad) 
    {
        this.controlIncapacidad = controlIncapacidad;
    }

    public CatRiesgoTrabajoIMSS getSecuelaRiesgoTrabajo() 
    {
        return secuelaRiesgoTrabajo;
    }

    public void setSecuelaRiesgoTrabajo(CatRiesgoTrabajoIMSS secuelaRiesgoTrabajo) 
    {
        this.secuelaRiesgoTrabajo = secuelaRiesgoTrabajo;
    }

    public CatTipoRiesgoIMSS getTipoRiesgo() 
    {
        return tipoRiesgo;
    }

    public void setTipoRiesgo(CatTipoRiesgoIMSS tipoRiesgo) 
    {
        this.tipoRiesgo = tipoRiesgo;
    }

    public String getFolio() 
    {
        return folio;
    }

    public void setFolio(String folio) 
    {
        this.folio = folio;
    }

    public Integer getDiasAutorizados() 
    {
        return diasAutorizados;
    }

    public void setDiasAutorizados(Integer diasAutorizados) 
    {
        this.diasAutorizados = diasAutorizados;
    }

    public Date getFechaInicio() 
    {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) 
    {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() 
    {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) 
    {
        this.fechaFin = fechaFin;
    }

    public Date getFechaCaptura() 
    {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) 
    {
        this.fechaCaptura = fechaCaptura;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }

    public CatEstatusSolicitud getEstatusSolicitud() 
    {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(CatEstatusSolicitud estatusSolicitud) 
    {
        this.estatusSolicitud = estatusSolicitud;
    }

    @Override
    public int hashCode() 
    {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.idIncapacidad);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final DetIncapacidad other = (DetIncapacidad) obj;
        return Objects.equals(this.idIncapacidad, other.idIncapacidad);
    }

    @Override
    public String toString() {
        return "DetRegistroIncapacidad[" + "registroIncidencia=" + idIncapacidad + ", folio=" + folio + ", diasAutorizados=" + diasAutorizados + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", fechaCaptura=" + fechaCaptura + ", descripcion=" + descripcion + ']';
    }
    
}
