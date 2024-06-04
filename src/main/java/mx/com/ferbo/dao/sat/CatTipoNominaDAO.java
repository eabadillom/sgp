package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoNominaDTO;
import mx.com.ferbo.model.sat.CatTipoNomina;

public class CatTipoNominaDAO extends DAO<TipoNominaDTO,CatTipoNomina,String> {

    @Override
    public TipoNominaDTO getDTO(CatTipoNomina model) {
        TipoNominaDTO dto = new TipoNominaDTO();

        try {
            
            dto.setClave(model.getClave());
            dto.setDescripcion(model.getDescripcion());

        } catch (Exception e) {
            dto = null;
            e.getMessage();
        }

        return dto;
    }

    @Override
    public CatTipoNomina getModel(TipoNominaDTO dto) {
        CatTipoNomina model = new CatTipoNomina();

        try {
            
            model.setClave(dto.getClave());
            model.setDescripcion(dto.getDescripcion());

        } catch (Exception e) {
            model = null;
            e.getMessage();
        }

        return model;

    }

    @Override
    public TipoNominaDTO buscarPorId(String id) {
        EntityManager em = null;
        CatTipoNomina model = null;
        TipoNominaDTO dto = null;

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoNomina.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return dto;

    }

    @Override
    public List<TipoNominaDTO> buscarTodos() {
        EntityManager em = null;
        List<CatTipoNomina> listModel = null;
        List<TipoNominaDTO> listDTO = null;

        try {
            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoNomina.findByAll", CatTipoNomina.class).getResultList();
            listDTO = toDTOList(listModel);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return listDTO;
    }

    @Override
    public List<TipoNominaDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoNominaDTO> buscarPorCriterios(TipoNominaDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }
    
}
