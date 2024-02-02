package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CatImssCuotasDTO;
import mx.com.ferbo.model.CatImssCuotas;
import mx.com.ferbo.util.SGPException; 

/**
 *
 * @author erale
 */
@Stateless
@LocalBean
public class CatImssCuotasDAO extends IBaseDAO<CatImssCuotasDTO, Integer> implements Serializable {
    
    private static final long serialVersionUID = 1L; 
 
    private static final Logger log = LogManager.getLogger(CatImssCuotasDAO.class);

    @Override
    public CatImssCuotasDTO buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CatImssCuotasDTO> buscarTodos() {
        return (List<CatImssCuotasDTO>) emSGP.createNamedQuery("CatImssCuotas.findAll", CatImssCuotasDTO.class).getResultList();
    }

    @Override
    public List<CatImssCuotasDTO> buscarActivo() {
        return (List<CatImssCuotasDTO>) emSGP.createNamedQuery("CatImssCuotas.findByActive", CatImssCuotasDTO.class).getResultList();
    }

    @Override
    public List<CatImssCuotasDTO> buscarPorCriterios(CatImssCuotasDTO e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CatImssCuotasDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(CatImssCuotasDTO e) throws SGPException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void guardar(CatImssCuotasDTO e) throws SGPException {
        CatImssCuotas catImssCuotas = new CatImssCuotas();
        try {
            emSGP.getTransaction().begin();
            catImssCuotas.setRiesgoTrabajo(e.getRiesgoTrabajo());
            catImssCuotas.setEnfMatEspCtFija(e.getEnfMatEspCtFija());
            catImssCuotas.setEnfMatEspCtAd(e.getEnfMatEspCtAd());
            catImssCuotas.setEnfMatGastosMed(e.getEnfMatGastosMed());
            catImssCuotas.setEnfMatDinero(e.getEnfMatDinero());
            catImssCuotas.setInvVida(e.getInvVida());
            catImssCuotas.setRetCesantiaVejezRetiro(e.getRetCesantiaVejezRetiro());
            catImssCuotas.setRetCesantiaVejezCeav(e.getRetCesantiaVejezCeav());
            catImssCuotas.setGuarderia(e.getGuarderia());
            catImssCuotas.setInfonavit(e.getInfonavit());
            catImssCuotas.setCuota(e.getCuota());
            catImssCuotas.setFechaCap(new Date());   
            catImssCuotas.setActivo((short) 1);   
            emSGP.persist(catImssCuotas);
            emSGP.getTransaction().commit();
            emSGP.detach(catImssCuotas);
        } catch (Exception ex) {
            emSGP.getTransaction().rollback();
            log.warn("EX-0036: " + ex.getMessage() + ". Error al guardar las cuotas del IMSS. " + e.getCuota() != null ? e.getCuota() : null); 
        }
    }
    
    public List<CatImssCuotasDTO> buscarActuales(Integer fechaCap){
        return emSGP.createNamedQuery("CatImssCuotas.findActual", CatImssCuotasDTO.class).setParameter("fechaCap", fechaCap + "%").getResultList();
    }
}
