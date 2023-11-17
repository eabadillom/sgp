package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.DetNominaDTO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.util.SGPException;
import org.apache.log4j.Logger;

/**
 *
 * @author erale
 */
@Stateless
@LocalBean
public class DetNominaDAO extends IBaseDAO<DetNominaDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L; 
 
    private static final Logger log = Logger.getLogger(DetNominaDAO.class); 

    @Override
    public DetNominaDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetNominaDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetNominaDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetNominaDTO> buscarPorCriterios(DetNominaDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(DetNominaDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(DetNominaDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(DetNominaDTO e) throws SGPException {
        final DetNomina nomina = new DetNomina();
        try {
            emSGP.getTransaction().begin();
            nomina.setIdEmpleado(e.getIdEmpleado()!= null ? emSGP.getReference(DetEmpleado.class, e.getIdEmpleado().getIdEmpleado()) : null);
            nomina.setSueldo(e.getSueldo());
            nomina.setSeptimoDia(e.getSeptimoDia());
            nomina.setHorasExtras(e.getHorasExtras());
            nomina.setDestajos(e.getDestajos());
            nomina.setPremiosEficiencia(e.getPremiosEficiencia());
            nomina.setBonoPuntualidad(e.getBonoPuntualidad());
            nomina.setDespensa(e.getDespensa());
            nomina.setOtrasPercepciones(e.getOtrasPercepciones());
            nomina.setTotalPercepciones(e.getTotalPercepciones());
            nomina.setRetInvYVida(e.getRetInvYVida());
            nomina.setRetCesantia(e.getRetCesantia());
            nomina.setRetEnfYMatObrero(e.getRetEnfYMatObrero());
            nomina.setPrestamoInfonavitFd(e.getPrestamoInfonavitFd());
            nomina.setPrestamoInfonavitCf(e.getPrestamoInfonavitCf());
            nomina.setSubsAlEmpleoAcreditado(e.getSubsAlEmpleoAcreditado());
            nomina.setSubsAlEmpleoMes(e.getSubsAlEmpleoMes());
            nomina.setIsrAntesDeSubsAlEmpleo(e.getIsrAntesDeSubsAlEmpleo());
            nomina.setIsrMes(e.getIsrMes());
            nomina.setImss(e.getImss());
            nomina.setPrestamoFonacot(e.getPrestamoFonacot());
            nomina.setAjusteEnSubsidioParaElEmpleo(e.getAjusteEnSubsidioParaElEmpleo());
            nomina.setSubsEntregadoQueNoCorrespondia(e.getSubsEntregadoQueNoCorrespondia());
            nomina.setAjusteAlNeto(e.getAjusteAlNeto());
            nomina.setIsrDeAjusteMensual(e.getIsrDeAjusteMensual());
            nomina.setIsrAjustadoPorSubsidio(e.getIsrAjustadoPorSubsidio());
            nomina.setAjusteAlSubsidioCausado(e.getAjusteAlSubsidioCausado());
            nomina.setPensionAlimienticia(e.getPensionAlimienticia());
            nomina.setOtrasDeducciones(e.getOtrasDeducciones());
            nomina.setTotalDeducciones(e.getTotalDeducciones());
            nomina.setNeto(e.getNeto());
            nomina.setInvalidezYVida(e.getInvalidezYVida());
            nomina.setCesantiaYVejez(e.getCesantiaYVejez());
            nomina.setEnfYMatPatron(e.getEnfYMatPatron());
            nomina.setFondoRetiroSar(e.getFondoRetiroSar());
            nomina.setImpuestoEstatal(e.getImpuestoEstatal());
            nomina.setRiesgoDeTrabajo9(e.getRiesgoDeTrabajo9());
            nomina.setImssEmpresa(e.getImssEmpresa());
            nomina.setInfonavitEmpresa(e.getInfonavitEmpresa());
            nomina.setGuarderiaImss7(e.getGuarderiaImss7());
            nomina.setOtrasObligaciones(e.getOtrasObligaciones());
            nomina.setTotalObligaciones(e.getTotalObligaciones());
            nomina.setFechaCreacion(e.getFechaCreacion());
            nomina.setIdEmpleadoCreador(e.getIdEmpleadoCreador()!= null ? emSGP.getReference(DetEmpleado.class, e.getIdEmpleadoCreador().getIdEmpleado()) : null);
            emSGP.persist(nomina);
            emSGP.getTransaction().commit();
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            throw new SGPException("Error al guardar el calculo de la nómina.");
        }
    }
    
    public List<DetNominaDTO> buscarNominaPorFecha(String fechaCreacion) throws SGPException {
        return emSGP.createNamedQuery("DetNomina.findByFecha", DetNominaDTO.class).setParameter(fechaCreacion, "fechaCreacion").getResultList();
    }
}
