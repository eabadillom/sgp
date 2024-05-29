package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoPercepcionDTO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class CatTipoPercepcionDAO extends DAO<TipoPercepcionDTO,CatTipoPercepcion,String>{

    @Override
    public TipoPercepcionDTO getDTO(CatTipoPercepcion model) {
        TipoPercepcionDTO dto = new TipoPercepcionDTO();

        try {
            
            dto.setClave(model.getClave());
            dto.setDescripcion(model.getDescripcion());
            dto.setVigenciaInicio(model.getVigenciaInicio());
            dto.setVigenciaFin(model.getVigenciaFin());

        } catch (Exception e) {
            dto = null;
            e.getMessage();
        }

        return dto;
    }

    @Override
    public CatTipoPercepcion getModel(TipoPercepcionDTO dto) {
        CatTipoPercepcion model = new CatTipoPercepcion();

        try {
            
            model.setClave(dto.getClave());
            model.setDescripcion(dto.getDescripcion());
            model.setVigenciaInicio(dto.getVigenciaInicio());
            model.setVigenciaFin(dto.getVigenciaFin());

        } catch (Exception e) {
            model = null;
            e.getMessage();
        }

        return model;
    }

    @Override
    public TipoPercepcionDTO buscarPorId(String id) {
        EntityManager em = null;
        CatTipoPercepcion model = null;
        TipoPercepcionDTO dto = null;

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoPercepcion.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return dto;
    }

    @Override
    public List<TipoPercepcionDTO> buscarTodos() {
        EntityManager em = null;
        List<CatTipoPercepcion> listModel = null;
        List<TipoPercepcionDTO> listDTO = null;

        try {
            
            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoPercepcion.findByAll", CatTipoPercepcion.class).getResultList();
            listDTO = toDTOList(listModel);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return listDTO;
    }

    @Override
    public List<TipoPercepcionDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoPercepcionDTO> buscarPorCriterios(TipoPercepcionDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }
    
}
