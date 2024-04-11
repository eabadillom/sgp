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
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabriel
 */
@Stateless
@LocalBean
public class IncidenciaDAO extends IBaseDAO<DetIncidenciaDTO, Integer>  implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(IncidenciaDAO.class);
    
    @Override
    public DetIncidenciaDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetIncidenciaDTO> buscarTodos() {
        return emSGP.createNamedQuery("DetIncidencia.findAll", DetIncidenciaDTO.class).getResultList();
    }
    
    @Override
    public List<DetIncidenciaDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetIncidenciaDTO> buscarPorCriterios(DetIncidenciaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void actualizar(DetIncidenciaDTO e) throws SGPException {
        try {
            emSGP.getTransaction().begin();
            final DetIncidencia incidencia = new DetIncidencia();
            incidencia.setIdIncidencia(e.getIdIncidencia());
            incidencia.setFechaCap(e.getFechaCap());
            incidencia.setFechaMod(new Date());
            incidencia.setIdEmpleado(emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoDTO().getIdEmpleado()));
            incidencia.setIdEmpleadoRev(emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoRevDTO().getIdEmpleado()));
            incidencia.setIdEstatus(emSGP.getReference(CatEstatusIncidencia.class, e.getCatEstatusIncidenciaDTO().getIdEstatus()));
            if (e.getDetSolicitudPermisoDTO().getIdSolicitud() != null){
                incidencia.setIdSolPermiso(e.getDetSolicitudPermisoDTO() != null
                    ? emSGP.getReference(DetSolicitudPermiso.class, e.getDetSolicitudPermisoDTO().getIdSolicitud())
                    : null);
                
                SolicitudPermisoDAO solicitud = new SolicitudPermisoDAO();
                e.getDetSolicitudPermisoDTO().setEmpleadoRev(new DetEmpleadoDTO(e.getDetEmpleadoRevDTO().getIdEmpleado()));
                e.getDetSolicitudPermisoDTO().setAprobada(e.getCatEstatusIncidenciaDTO().getIdEstatus() == 2 ? (short) 1 : (short) 0);
                
                solicitud.actualizar(e.getDetSolicitudPermisoDTO());
            }else if(e.getDetSolicitudPrendaDTO().getIdSolicitud() != null){
                incidencia.setIdSolPrenda(e.getDetSolicitudPrendaDTO() != null
                    ? emSGP.getReference(DetSolicitudPrenda.class, e.getDetSolicitudPrendaDTO().getIdSolicitud())
                    : null);
                
                DetSolicitudPrendaDAO solicitud = new DetSolicitudPrendaDAO();
                e.getDetSolicitudPrendaDTO().setEmpleadoRev(new DetEmpleadoDTO(e.getDetEmpleadoRevDTO().getIdEmpleado()));
                e.getDetSolicitudPrendaDTO().setAprobada(e.getCatEstatusIncidenciaDTO().getIdEstatus() == 2 ? (short) 1 : (short) 0);
                
                solicitud.actualizar(e.getDetSolicitudPrendaDTO());
            }else if(e.getDetSolicitudArticuloDTO().getIdSolicitud() != null){
                incidencia.setIdSolArticulo(e.getDetSolicitudArticuloDTO() != null
                    ? emSGP.getReference(DetSolicitudArticulo.class, e.getDetSolicitudArticuloDTO().getIdSolicitud())
                    : null);
                DetSolicitudArticulosDAO solicitud = new DetSolicitudArticulosDAO();
                e.getDetSolicitudArticuloDTO().setEmpleadoRev(new DetEmpleadoDTO(e.getDetEmpleadoRevDTO().getIdEmpleado()));
                e.getDetSolicitudArticuloDTO().setAprobada(e.getCatEstatusIncidenciaDTO().getIdEstatus() == 2 ? (short) 1 : (short) 0);
                
                solicitud.actualizar(e.getDetSolicitudArticuloDTO());
            }
            
            incidencia.setIdTipo(emSGP.getReference(CatTipoIncidencia.class, e.getCatTipoIncidenciaDTO().getIdTipo()));
            incidencia.setVisible(e.getVisible());
            
            emSGP.merge(incidencia);
            emSGP.getTransaction().commit();
            emSGP.detach(incidencia);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0027: " + ex.getMessage() + ". Error al actualizar incidente del empleado: " + e.getDetEmpleadoRevDTO().getNumEmpleado() != null ? e.getDetEmpleadoRevDTO().getNumEmpleado() : null);
        }
        
    }
    
    @Override
    public void eliminar(DetIncidenciaDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void guardar(DetIncidenciaDTO e) throws SGPException {
        try {
            emSGP.getTransaction().begin();
            final DetIncidencia incidencia = new DetIncidencia();
            if(e.getDetSolicitudPrendaDTO() != null){
                incidencia.setIdSolPrenda(emSGP.getReference(DetSolicitudPrenda.class, e.getDetSolicitudPrendaDTO().getIdSolicitud()));
                incidencia.setIdTipo(emSGP.getReference(CatTipoIncidencia.class, 3));
            } else if(e.getDetSolicitudArticuloDTO() != null){
                incidencia.setIdSolArticulo(emSGP.getReference(DetSolicitudArticulo.class, e.getDetSolicitudArticuloDTO().getIdSolicitud()));
                incidencia.setIdTipo(emSGP.getReference(CatTipoIncidencia.class, 4));
            } else if(e.getDetSolicitudPermisoDTO() != null){
                incidencia.setIdSolPermiso(emSGP.getReference(DetSolicitudPermiso.class, e.getDetSolicitudPermisoDTO().getIdSolicitud()));
                incidencia.setIdTipo(emSGP.getReference(CatTipoIncidencia.class, 1));
            }
            incidencia.setVisible((short) 1);
            incidencia.setIdEstatus(emSGP.getReference(CatEstatusIncidencia.class, 1));
            incidencia.setIdEmpleado(emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoDTO().getIdEmpleado()));
            incidencia.setFechaCap(new Date());
            emSGP.persist(incidencia);
            emSGP.getTransaction().commit();
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0020: " + ex.getMessage() + ". Error al guardar la solicitud de permiso (incidencia) del empleado: " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
    }
    
    public List<DetIncidenciaDTO> consultaPorIdEmpleado(Integer idEmpleado){
        return emSGP.createNamedQuery("DetIncidencia.findByIdEmpleado", DetIncidenciaDTO.class)
                .setParameter("idEmpleado", idEmpleado).getResultList();
    }
    
}
