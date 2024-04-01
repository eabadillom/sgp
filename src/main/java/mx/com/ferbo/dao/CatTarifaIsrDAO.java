
package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatTarifaIsrDTO;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author erale
 */
@Stateless
@LocalBean
public class CatTarifaIsrDAO extends IBaseDAO<CatTarifaIsrDTO, Integer>{
    
    private static final long serialVersionUID = 1L; 
 
    private static final Logger log = LogManager.getLogger(CatTarifaIsrDAO.class); 

    @Override
    public CatTarifaIsrDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatTarifaIsrDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatTarifaIsrDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatTarifaIsrDTO> buscarPorCriterios(CatTarifaIsrDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatTarifaIsrDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(CatTarifaIsrDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatTarifaIsrDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public List<CatTarifaIsrDTO> buscarActualesSemanal(Integer fecha){
        return emSGP.createNamedQuery("CatTarifaISR.findActualSemanal", CatTarifaIsrDTO.class).setParameter("tipo", "s").setParameter("fecha", fecha + "%").getResultList();
    }
    
    public CatTarifaIsrDTO buscar(Date fechaInicio, Date fechaFin, String tipo, BigDecimal baseISR ) {
    	CatTarifaIsrDTO tarifa = null;
    	
    	tarifa = emSGP.createNamedQuery("CatTarifaISR.findByTipoAndAnioandBaseISR", CatTarifaIsrDTO.class)
    			.setParameter("fechaInicio", fechaInicio)
    			.setParameter("fechaFin", fechaFin)
    			.setParameter("tipo", tipo)
    			.setParameter("baseISR", baseISR)
    			.getSingleResult()
    			;
    	
    	return tarifa;
    }
    
    public List<CatTarifaIsrDTO> buscarActualesMensual(Integer fecha){
        return emSGP.createNamedQuery("CatTarifaISR.findActualMensual", CatTarifaIsrDTO.class).setParameter("tipo", "m").setParameter("fecha", fecha + "%").getResultList();
    }
}
