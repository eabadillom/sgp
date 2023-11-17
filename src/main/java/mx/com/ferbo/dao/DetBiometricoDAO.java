package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetBiometricoDTO;
import mx.com.ferbo.model.DetBiometrico;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.util.SGPException;
import org.apache.log4j.Logger;

/**
 *
 * @author Gabriel
 */
@Stateless
@LocalBean
public class DetBiometricoDAO extends IBaseDAO<DetBiometricoDTO, Integer> implements Serializable{

    private static final Logger log = Logger.getLogger(DetBiometricoDAO.class);

    @Override
    public DetBiometricoDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetBiometricoDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetBiometricoDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetBiometricoDTO> buscarPorCriterios(DetBiometricoDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(DetBiometricoDTO e) throws SGPException {
         final DetBiometrico biometrico = new DetBiometrico();
        try {
            emSGP.getTransaction().begin();
            biometrico.setIdBiometrico(e.getIdBiometrico());
            biometrico.setActivo(e.getActivo());
            biometrico.setFechaCaptura(new Date());
            biometrico.setIdEmpleado(emSGP.getReference(DetEmpleado.class,e.getDetEmpleadoDTO().getIdEmpleado()));
            biometrico.setHuella(e.getHuella());
            biometrico.setHuella2(e.getHuella2());
            
            emSGP.merge(biometrico);
            emSGP.getTransaction().commit();
            emSGP.detach(biometrico);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0014: " + ex.getMessage() + ". Error al actualizar la huella del empleado " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
    }

    @Override
    public void eliminar(DetBiometricoDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(DetBiometricoDTO e) throws SGPException {
        final DetBiometrico biometrico = new DetBiometrico();
        try {
            emSGP.getTransaction().begin();
            biometrico.setActivo((short) 1);
            biometrico.setFechaCaptura(new Date());
            biometrico.setIdEmpleado(emSGP.getReference(DetEmpleado.class,e.getDetEmpleadoDTO().getIdEmpleado()));
            biometrico.setHuella(e.getHuella());
            biometrico.setHuella2(e.getHuella2());
            emSGP.persist(biometrico);
            emSGP.getTransaction().commit();
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0013: " + ex.getMessage() + ". Error al guardar la huella del empleado " + e.getDetEmpleadoDTO().getNumEmpleado() != null ? e.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
    }

    public DetBiometricoDTO consultaBiometricoByNumEmpleado(String NumEmpl) {
        List<DetBiometricoDTO> biometrico = emSGP.createNamedQuery("DetBiometrico.findByNumEmpl", DetBiometricoDTO.class).setParameter("numEmpl", NumEmpl).getResultList();
        return !biometrico.isEmpty() ? biometrico.get(0) : new DetBiometricoDTO();
    }
}
