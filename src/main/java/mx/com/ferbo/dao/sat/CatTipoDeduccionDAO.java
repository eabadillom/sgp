package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoDeduccionDTO;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class CatTipoDeduccionDAO extends DAO<TipoDeduccionDTO,CatTipoDeduccion,String>{

    @Override
    public TipoDeduccionDTO getDTO(CatTipoDeduccion model) {
        TipoDeduccionDTO dto = new TipoDeduccionDTO();

        try {            
            
            dto.setClave(model.getClave());
            dto.setDescripcion(model.getDescripcion());
            dto.setVigenciaInicio(model.getVigenciaInicio());
            dto.setVigenciaFin(model.getVigenciaFin());

        } catch (Exception e) {
            e.printStackTrace();
            dto = null;
        }

        return dto;

    }

    @Override
    public CatTipoDeduccion getModel(TipoDeduccionDTO dto) {
        CatTipoDeduccion catTipoDeduccion = new CatTipoDeduccion();

        try {
            
            catTipoDeduccion.setClave(dto.getClave());
            catTipoDeduccion.setDescripcion(dto.getDescripcion());
            catTipoDeduccion.setVigenciaInicio(dto.getVigenciaInicio());
            catTipoDeduccion.setVigenciaFin(dto.getVigenciaFin());

        } catch (Exception e) {
            e.printStackTrace();
            catTipoDeduccion = null;
        }

        return catTipoDeduccion;
    }

    @Override
    public TipoDeduccionDTO buscarPorId(String id) {

        TipoDeduccionDTO dto = null;
        CatTipoDeduccion model = null;
        EntityManager em = null;

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoDeduccion.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close(em);
        }

        return dto;
        
    }

    @Override
    public List<TipoDeduccionDTO> buscarTodos() {

        List<TipoDeduccionDTO> listDTO = null;
        List<CatTipoDeduccion> listModel = null;
        EntityManager em = null;

        try {
            
            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoDeduccion.findByAll", CatTipoDeduccion.class).getResultList();
            listDTO = toDTOList(listModel);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close(em);
        }

        return listDTO;
    }

    @Override
    public List<TipoDeduccionDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoDeduccionDTO> buscarPorCriterios(TipoDeduccionDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }
    
}
