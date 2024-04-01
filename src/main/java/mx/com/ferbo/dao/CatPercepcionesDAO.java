package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.util.SGPException; 

@Stateless
@LocalBean
public class CatPercepcionesDAO extends IBaseDAO<CatPercepcionesDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L; 
    private static final Logger log = LogManager.getLogger(CatPercepcionesDAO.class); 

    @Override
    public CatPercepcionesDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatPercepcionesDTO> buscarTodos() {
        List<CatPercepcionesDTO> solicitud = emSGP.createNamedQuery("CatPercepciones.findAll", CatPercepcionesDTO.class).getResultList();
        return solicitud;
    }

    @Override
    public List<CatPercepcionesDTO> buscarActivo() {
        return emSGP.createNamedQuery("CatPercepciones.findByActive", CatPercepcionesDTO.class).getResultList();
    }
    
    public CatPercepcionesDTO buscarActual(Date fechaCorte) {
    	CatPercepcionesDTO percepcion = null;
    	percepcion = emSGP.createNamedQuery("CatPercepciones.findLastActive", CatPercepcionesDTO.class)
    			.setParameter("fechaCorte", fechaCorte)
    			.getSingleResult();
    	return percepcion;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<CatPercepcionesDTO> buscarPorCriterios(CatPercepcionesDTO e) {
        return (List<CatPercepcionesDTO>) emSGP.createNamedQuery("CatPercepciones.findByFecha", CatPercepcionesDTO.class).setParameter("fechaCap", e.getFechaCap()).getSingleResult();
    }

    @Override
    public void actualizar(CatPercepcionesDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(CatPercepcionesDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatPercepcionesDTO e) throws SGPException {
        CatPercepciones catPercepciones = new CatPercepciones();
        try {
            emSGP.getTransaction().begin();
            catPercepciones.setDiasAguinaldo(e.getDiasAguinaldo());
            catPercepciones.setDiasVacaciones(e.getDiasVacaciones());
            catPercepciones.setPrimaVacacional(e.getPrimaVacacional());
            catPercepciones.setBonoPuntualidad(e.getBonoPuntualidad());
            catPercepciones.setValeDespensa(e.getValeDespensa());
            catPercepciones.setUma(e.getUma());
            catPercepciones.setFechaCap(e.getFechaCap());
            catPercepciones.setActivo((short) 1);
            emSGP.persist(catPercepciones);
            emSGP.getTransaction().commit();
            emSGP.detach(catPercepciones);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0035: " + ex.getMessage() + ". Error al guardar la percepción. " + e.getUma() != null ? e.getUma() : null); 
        }
    }

}
