package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetIncidenciaDTO;
import mx.com.ferbo.dto.DetSolicitudPermisoDTO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabo
 */
@LocalBean
@Stateless
public class SolicitudPermisoDAO extends IBaseDAO<DetSolicitudPermisoDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(SolicitudPermisoDAO.class);
    
    @Override
    public DetSolicitudPermisoDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetSolicitudPermisoDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetSolicitudPermisoDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetSolicitudPermisoDTO> buscarPorCriterios(DetSolicitudPermisoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void actualizar(DetSolicitudPermisoDTO e) throws SGPException {
        try {
            emSGP.getTransaction().begin();
            final DetSolicitudPermiso solicitud = new DetSolicitudPermiso();
            solicitud.setIdSolicitud(e.getIdSolicitud());
            solicitud.setAprobada(e.getAprobada());
            solicitud.setFechaCap(e.getFechaCap());
            solicitud.setFechaInicio(e.getFechaInicio());
            solicitud.setFechaFin(e.getFechaFin());
            solicitud.setFechaMod(new Date());
            solicitud.setIdEmpleadoRev(emSGP.getReference(DetEmpleado.class,e.getEmpleadoRev().getIdEmpleado()));
            solicitud.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            solicitud.setIdTipoSolicitud(emSGP.getReference(CatTipoSolicitud.class, e.getCatTipoSolicitud().getIdTipoSolicitud()));
            solicitud.setDescripcionRechazo(e.getDescripcionRechazo());
            emSGP.merge(solicitud);
            emSGP.getTransaction().commit();
            emSGP.detach(solicitud);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0024: " + ex.getMessage() + ". Error al actualizar solicitud de permiso del empleado: " + e.getEmpleadoSol().getNumEmpleado() != null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }
    }
    
    @Override
    public void eliminar(DetSolicitudPermisoDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void guardar(DetSolicitudPermisoDTO e) throws SGPException {
        /*Se guarda Solicitud*/
        try {
            final DetSolicitudPermiso solicitud = new DetSolicitudPermiso();
            emSGP.getTransaction().begin();
            solicitud.setFechaCap(new Date());
            solicitud.setFechaInicio(e.getFechaInicio());
            solicitud.setFechaFin(e.getFechaFin());
            solicitud.setIdTipoSolicitud(emSGP.getReference(CatTipoSolicitud.class, e.getCatTipoSolicitud().getIdTipoSolicitud()));
            solicitud.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            emSGP.persist(solicitud);
            emSGP.getTransaction().commit();
            e.setIdSolicitud(solicitud.getIdSolicitud());
            e.getCatTipoSolicitud().setDescripcion(solicitud.getIdTipoSolicitud().getDescripcion());
            
            IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
            DetIncidenciaDTO incidencia = new DetIncidenciaDTO();
            incidencia.setDetSolicitudPermisoDTO(new DetSolicitudPermisoDTO(solicitud.getIdSolicitud()));
            incidencia.setDetEmpleadoDTO(new DetEmpleadoDTO(e.getEmpleadoSol().getIdEmpleado()));
            incidenciaDAO.guardar(incidencia);
        } catch (SGPException ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0021: " + ex.getMessage() + ". Error al guardar la solicitud de permiso el empleado: " + e.getEmpleadoSol().getNumEmpleado() != null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }
    }
    
    public List<DetSolicitudPermisoDTO> consultaPorIdEmpleado(Integer idEmpleado) {
        List<DetSolicitudPermisoDTO> listado = emSGP.createNamedQuery("DetSolicitudPermiso.findByIdEmp", DetSolicitudPermisoDTO.class)
                .setParameter("idEmp", idEmpleado)
                .getResultList();
        return listado;
    }
    
}
