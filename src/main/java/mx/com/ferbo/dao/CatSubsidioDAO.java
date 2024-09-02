package mx.com.ferbo.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatSubsidioDTO;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author erale
 */
@Stateless
@LocalBean
public class CatSubsidioDAO extends IBaseDAO<CatSubsidioDTO, Integer> implements Serializable { 
    
    private static final long serialVersionUID = 1L; 
 
    private static final Logger log = LogManager.getLogger(CatSubsidioDAO.class);

    @Override
    public CatSubsidioDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatSubsidioDTO> buscarPorCriterios(CatSubsidioDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatSubsidioDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public List<CatSubsidioDTO> buscarActuales(Integer fecha){
        return emSGP.createNamedQuery("CatSubsidio.findActual", CatSubsidioDTO.class).setParameter("fecha", fecha + "%").getResultList();
    }
    
    public CatSubsidioDTO buscar(Date fechaInicio, Date fechaFin, String periodo, BigDecimal ingreso) {
    	CatSubsidioDTO subsidio = null;
    	try {
    		subsidio = emSGP.createNamedQuery("CatSubsidio.findByPeriodoTipoIngreso", CatSubsidioDTO.class)
    				.setParameter("fechaInicio", fechaInicio)
    				.setParameter("fechaFin", fechaFin)
    				.setParameter("periodo", periodo)
    				.setParameter("ingreso", ingreso)
    				.getSingleResult()
    				;
    	} catch(NoResultException ex) {
    		subsidio = new CatSubsidioDTO();
    		subsidio.setCantidadSubsidio(BigDecimal.ZERO);
    		subsidio.setFechaSubsidio(fechaInicio);
    		subsidio.setHastaIngresosDe(BigDecimal.ZERO);
    		subsidio.setParaIngresosDe(BigDecimal.ZERO);
    	}
    	
    	return subsidio;
    }
}
