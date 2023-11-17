package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetSolicitudArticuloDTO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.util.SGPException;
import org.apache.log4j.Logger;

@Stateless
@LocalBean
public class DetSolicitudArticulosDAO extends IBaseDAO<DetSolicitudArticuloDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = Logger.getLogger(DetSolicitudArticulosDAO.class);
    
    @Override
    public DetSolicitudArticuloDTO buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudArticuloDTO> buscarTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudArticuloDTO> buscarActivo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudArticuloDTO> buscarPorCriterios(DetSolicitudArticuloDTO e) {
        List<DetSolicitudArticuloDTO> solicitud = emSGP.createNamedQuery("DetSolicitudArticulo.findArticulosIdEmpleado", DetSolicitudArticuloDTO.class).setParameter("numEmpl", e.getEmpleadoSol().getIdEmpleado()).getResultList();
        return solicitud;
    }

    @Override
    public void actualizar(DetSolicitudArticuloDTO e) throws SGPException {
        try{
            emSGP.getTransaction().begin();
            final DetSolicitudArticulo solicitud = new DetSolicitudArticulo();
            solicitud.setIdSolicitud(e.getIdSolicitud());
            solicitud.setIdArticulo(emSGP.getReference(CatArticulo.class, e.getArticulo().getIdArticulo()));
            solicitud.setCantidad(e.getCantidad());
            solicitud.setAprobada(e.getAprobada());
            solicitud.setFechaCap(e.getFechaCap());
            solicitud.setFechaMod(new Date());
            solicitud.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            solicitud.setIdEmpleadoRev(emSGP.getReference(DetEmpleado.class, e.getEmpleadoRev().getIdEmpleado()));
            solicitud.setDescripcionRechazo(e.getDescripcionRechazo()); 
            emSGP.merge(solicitud);
            emSGP.getTransaction().commit();
            emSGP.detach(solicitud);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0026: " + ex.getMessage() + ". Error al actualizar el registro de articulos del empleado: " + e.getEmpleadoSol().getNumEmpleado() != null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }
    }

    @Override
    public void eliminar(DetSolicitudArticuloDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void guardar(DetSolicitudArticuloDTO e) throws SGPException {
        DetSolicitudArticulo registro = new DetSolicitudArticulo();
        try {
            emSGP.getTransaction().begin();
            registro.setCantidad(e.getCantidad());
            registro.setFechaCap(e.getFechaCap());
            registro.setIdArticulo(emSGP.getReference(CatArticulo.class, e.getArticulo().getIdArticulo()));
            registro.setIdEmpleadoRev(null);
            registro.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            emSGP.persist(registro);
            emSGP.getTransaction().commit();
            e.setIdSolicitud(registro.getIdSolicitud());
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0033: " + ex.getMessage() + ". Error al guardar la solicitud de articulos del empleado: " + e.getEmpleadoSol().getNumEmpleado()!= null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }

    }

}
