package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetSolicitudPrendaDTO;
import mx.com.ferbo.model.CatPrenda;
import mx.com.ferbo.model.CatTalla;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.SGPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@LocalBean
public class DetSolicitudPrendaDAO extends IBaseDAO<DetSolicitudPrendaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(DetSolicitudPrendaDAO.class);

    @Override
    public DetSolicitudPrendaDTO buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudPrendaDTO> buscarTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudPrendaDTO> buscarActivo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DetSolicitudPrendaDTO> buscarPorCriterios(DetSolicitudPrendaDTO e) {
        List<DetSolicitudPrendaDTO> solicitud = emSGP.createNamedQuery("DetSolicitudPrenda.findPrendasIdEmpleado", DetSolicitudPrendaDTO.class).setParameter("numEmpl", e.getEmpleadoSol().getIdEmpleado()).getResultList();
        return solicitud;
    }

    @Override
    public void actualizar(DetSolicitudPrendaDTO e) throws SGPException {
        try{
            emSGP.getTransaction().begin();
            final DetSolicitudPrenda solicitud = new DetSolicitudPrenda();
            solicitud.setIdSolicitud(e.getIdSolicitud());
            solicitud.setIdPrenda(emSGP.getReference(CatPrenda.class, e.getPrenda().getIdPrenda()));
            solicitud.setCantidad(e.getCantidad());
            solicitud.setAprobada(e.getAprobada());
            solicitud.setFechaCap(e.getFechaCap());
            solicitud.setFechaMod(new Date());
            solicitud.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            solicitud.setIdEmpleadoRev(emSGP.getReference(DetEmpleado.class, e.getEmpleadoRev().getIdEmpleado()));
            solicitud.setIdTalla(emSGP.getReference(CatTalla.class, e.getTalla().getIdTalla()));
            solicitud.setDescripcionRechazo(e.getDescripcionRechazo());
            emSGP.merge(solicitud);
            emSGP.getTransaction().commit();
            emSGP.detach(solicitud);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0025: " + ex.getMessage() + ". Error al actualizar la solicitud de prenda del empleado: " + e.getEmpleadoSol().getNumEmpleado() != null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }
    }

    @Override
    public void eliminar(DetSolicitudPrendaDTO e) throws SGPException {
        // TODO Auto-generated method stub

    }

    @Override
    public void guardar(DetSolicitudPrendaDTO e) throws SGPException {
        DetSolicitudPrenda registro = new DetSolicitudPrenda();
        try {
            emSGP.getTransaction().begin();
            registro.setCantidad(e.getCantidad());
            registro.setFechaCap(e.getFechaCap());
            registro.setIdPrenda(emSGP.getReference(CatPrenda.class, e.getPrenda().getIdPrenda()));
            registro.setIdEmpleadoRev(null);
            registro.setIdEmpleadoSol(emSGP.getReference(DetEmpleado.class, e.getEmpleadoSol().getIdEmpleado()));
            registro.setIdTalla(emSGP.getReference(CatTalla.class, e.getTalla().getIdTalla()));
            emSGP.persist(registro);
            emSGP.getTransaction().commit();
            e.setIdSolicitud(registro.getIdSolicitud());
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0031: " + ex.getMessage() + ". Error al guardar la solicitud de prenda del empleado: " + e.getEmpleadoSol().getNumEmpleado() != null ? e.getEmpleadoSol().getNumEmpleado() : null);
        }

    }

}
