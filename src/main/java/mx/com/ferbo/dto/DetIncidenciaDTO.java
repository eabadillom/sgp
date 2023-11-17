package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class DetIncidenciaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idIncidencia;
    private DetEmpleadoDTO detEmpleadoDTO;
    private DetEmpleadoDTO detEmpleadoRevDTO;
    private Short visible;
    private Date fechaCap;
    private Date fechaMod;
    private CatEstatusIncidenciaDTO catEstatusIncidenciaDTO;
    private CatTipoIncidenciaDTO catTipoIncidenciaDTO;
    private DetSolicitudArticuloDTO detSolicitudArticuloDTO;
    private DetSolicitudPermisoDTO detSolicitudPermisoDTO;
    private DetSolicitudPrendaDTO detSolicitudPrendaDTO;

    public DetIncidenciaDTO() {
    }

    /*
     * Método que se utilizar para la NamedQuery:
     * DetIncidencia.findAll
     */
    public DetIncidenciaDTO(Integer idIncidencia, Short visible, Date fechaCap, Date fechaMod, 
                            Integer idEmpleado, String numEmpleado, String nombre, String primerAp, String segundoAp,
                            Integer idTipoIncidencia, String descripcionTipo, 
                            Integer idEstatus, String descripcionEstatus,
                            Integer idSolicitud, Date fechaCapSol, Date fechaModSol, Date fechaInicio, Date fechaFin, Short aprobada, 
                            Integer idTipoSolicitud, String descripcion,
                            Integer idSolicitudA, Integer cantidadA, Short aprobadaA, Date fechaCapSolA, Date fechaModSolA,
                            Integer idArticulo, String descripcionA, Integer cantidadMax, String unidad,  
                            Integer idSolicitudP, Integer cantidadP, Short aprobadaP, Date fechaCapSolP, Date fechaModSolP,
                            Integer idPrenda, String descripcionP,
                            Integer idTalla, String descripcionT) {
        this.idIncidencia = idIncidencia;
        this.visible = visible;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado, numEmpleado, nombre, primerAp, segundoAp);
        this.catTipoIncidenciaDTO = new CatTipoIncidenciaDTO(idTipoIncidencia, descripcionTipo);
        this.catEstatusIncidenciaDTO = new CatEstatusIncidenciaDTO(idEstatus, descripcionEstatus, (short)1);
        this.detSolicitudPermisoDTO = new DetSolicitudPermisoDTO(idSolicitud, fechaCapSol, fechaModSol, fechaInicio, fechaFin, aprobada, idTipoSolicitud, descripcion, idEmpleado);
        this.detSolicitudArticuloDTO = new DetSolicitudArticuloDTO(idSolicitudA, idArticulo, descripcionA, unidad, cantidadMax, cantidadA, aprobadaA, fechaCapSolA, fechaModSolA, idEmpleado);
        this.detSolicitudPrendaDTO = new DetSolicitudPrendaDTO(idSolicitudP, idPrenda, descripcionP, cantidadP, aprobadaP, fechaCapSolP, fechaModSolP, idEmpleado, idTalla, descripcionT);
    }
    
    /*
     * Método que se utilizar para la NamedQuery:
     * DetIncidencia.findByIdEmpleado
     */
    public DetIncidenciaDTO(Integer idIncidencia, Short visible, Date fechaCap, Date fechaMod, Integer idEmpleado, String numEmpleado, 
            String nombre, String primerAp, String segundoAp,Integer idTipoIncidencia, String descripcionTipo, Integer idEstatus, String descripcionEstatus,
            Integer idSolicitud, Date fechaCapSol, Date fechaModSol, Date fechaInicio, Date fechaFin, Short aprobada, Integer idTipoSolicitud, String descripcion) {
        this.idIncidencia = idIncidencia;
        this.visible = visible;
        this.fechaCap = fechaCap;
        this.fechaMod = fechaMod;
        this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado, numEmpleado, nombre, primerAp, segundoAp);
        this.catTipoIncidenciaDTO = new CatTipoIncidenciaDTO(idTipoIncidencia, descripcionTipo);
        this.catEstatusIncidenciaDTO = new CatEstatusIncidenciaDTO(idEstatus, descripcionEstatus, (short)1);
        this.detSolicitudPermisoDTO = new DetSolicitudPermisoDTO(idSolicitud, fechaCapSol, fechaModSol, fechaInicio, fechaFin, aprobada, idTipoSolicitud, descripcion, idEmpleado);
    }
    
    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public DetEmpleadoDTO getDetEmpleadoDTO() {
        return detEmpleadoDTO;
    }

    public void setDetEmpleadoDTO(DetEmpleadoDTO detEmpleadoDTO) {
        this.detEmpleadoDTO = detEmpleadoDTO;
    }

    public DetEmpleadoDTO getDetEmpleadoRevDTO() {
        return detEmpleadoRevDTO;
    }

    public void setDetEmpleadoRevDTO(DetEmpleadoDTO detEmpleadoRevDTO) {
        this.detEmpleadoRevDTO = detEmpleadoRevDTO;
    }

    public Short getVisible() {
        return visible;
    }

    public void setVisible(Short visible) {
        this.visible = visible;
    }

    public CatEstatusIncidenciaDTO getCatEstatusIncidenciaDTO() {
        return catEstatusIncidenciaDTO;
    }

    public void setCatEstatusIncidenciaDTO(CatEstatusIncidenciaDTO catEstatusIncidenciaDTO) {
        this.catEstatusIncidenciaDTO = catEstatusIncidenciaDTO;
    }

    public CatTipoIncidenciaDTO getCatTipoIncidenciaDTO() {
        return catTipoIncidenciaDTO;
    }

    public void setCatTipoIncidenciaDTO(CatTipoIncidenciaDTO catTipoIncidenciaDTO) {
        this.catTipoIncidenciaDTO = catTipoIncidenciaDTO;
    }

    public DetSolicitudArticuloDTO getDetSolicitudArticuloDTO() {
        return detSolicitudArticuloDTO;
    }

    public void setDetSolicitudArticuloDTO(DetSolicitudArticuloDTO detSolicitudArticuloDTO) {
        this.detSolicitudArticuloDTO = detSolicitudArticuloDTO;
    }

    public DetSolicitudPermisoDTO getDetSolicitudPermisoDTO() {
        return detSolicitudPermisoDTO;
    }

    public void setDetSolicitudPermisoDTO(DetSolicitudPermisoDTO detSolicitudPermisoDTO) {
        this.detSolicitudPermisoDTO = detSolicitudPermisoDTO;
    }

    public DetSolicitudPrendaDTO getDetSolicitudPrendaDTO() {
        return detSolicitudPrendaDTO;
    }

    public void setDetSolicitudPrendaDTO(DetSolicitudPrendaDTO detSolicitudPrendaDTO) {
        this.detSolicitudPrendaDTO = detSolicitudPrendaDTO;
    }

    public Date getFechaCap() {
        return fechaCap;
    }

    public void setFechaCap(Date fechaCap) {
        this.fechaCap = fechaCap;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

}
