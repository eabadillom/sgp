package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.SGPException;
import org.apache.log4j.Logger;

/**
 *
 * @author Gabo
 */
@LocalBean
@Stateless
public class RegistroDAO extends IBaseDAO<DetRegistroDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(RegistroDAO.class);

    @Override
    public DetRegistroDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetRegistroDTO> buscarPorCriterios(DetRegistroDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(DetRegistroDTO e) throws SGPException {
        DetRegistro registro = new DetRegistro();
        try {
            emSGP.getTransaction().begin();
            registro = emSGP.getReference(DetRegistro.class, e.getIdRegistro());
            registro.setFechaSalida(e.getFechaSalida());
            emSGP.merge(registro);
            emSGP.getTransaction().commit();
            emSGP.detach(registro);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            throw new SGPException("Error al actualizar al fecha de salida del empleado");
        }
    }

    @Override
    public void eliminar(DetRegistroDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(DetRegistroDTO e) throws SGPException {
        final DetRegistro registro = new DetRegistro();
        try {
            emSGP.getTransaction().begin();
            registro.setIdEmpleado(e.getDetEmpleadoDTO() != null ? emSGP.getReference(DetEmpleado.class, e.getDetEmpleadoDTO().getIdEmpleado()) : null);
            registro.setFechaEntrada(e.getFechaEntrada());
            registro.setFechaSalida(e.getFechaSalida());
            registro.setIdEstatus(e.getDetEmpleadoDTO() != null ? emSGP.getReference(CatEstatusRegistro.class, e.getCatEstatusRegistroDTO().getIdEstatus()) : null);
            emSGP.persist(registro);
            emSGP.getTransaction().commit();
            emSGP.detach(registro);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0003: " + ex.getMessage() + ". Error al guardar el registro del empleado: " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
    }

    public List<DetRegistroDTO> consultaRegistrosPorIdEmp(Integer idEmpleado) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByIdEmp", DetRegistroDTO.class).setParameter("idEmp", idEmpleado)
                .getResultList();
        return registros;
    }

    public List<DetRegistroDTO> buscarPorIdFechaEntrada(Integer id, String fechaEntrada) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByIdEmplActivo", DetRegistroDTO.class).setParameter("idEmp", id).setParameter("fechaEntrada", fechaEntrada).getResultList();
        return registros;
    }
    
    public List<DetRegistroDTO> buscarRegistroPorAnio(String year) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByYear", DetRegistroDTO.class).setParameter("fechaEntrada", year).getResultList();
        return registros;
    }
    
    public List<DetRegistroDTO> buscarRegistroNomina(Date fechaEntrada, Date fechaSalida) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findByNomina", DetRegistroDTO.class).setParameter("fechaEntrada", fechaEntrada).setParameter("fechaSalida", fechaSalida).getResultList();
        return registros;
    }
    
    public DetRegistroDTO buscarEntradaHoy(Integer id, Date today) {
        List<DetRegistroDTO> registros = emSGP.createNamedQuery("DetRegistro.findToday", DetRegistroDTO.class)
                .setParameter("idEmp", id)
                .setParameter("today", today, TemporalType.TIMESTAMP).getResultList();
        return registros.isEmpty() ? null : registros.get(0);
    }
}
