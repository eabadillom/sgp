package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoHorasDTO;
import mx.com.ferbo.model.sat.CatTipoHoras;

public class CatTipoHorasDAO extends DAO<TipoHorasDTO,CatTipoHoras,String> {

    @Override
    public TipoHorasDTO getDTO(CatTipoHoras model) {
        TipoHorasDTO dto = new TipoHorasDTO();

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
    public CatTipoHoras getModel(TipoHorasDTO dto) {
        
        CatTipoHoras model = new CatTipoHoras();

        try {

            model.setClave(dto.getClave());
            model.setDescripcion(dto.getDescripcion());
            model.setVigenciaInicio(dto.getVigenciaInicio());
            model.setVigenciaFin(dto.getVigenciaFin());
            
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }

        return model;

    }

    @Override
    public TipoHorasDTO buscarPorId(String id) {
        EntityManager em = null;
        TipoHorasDTO dto = new TipoHorasDTO();
        CatTipoHoras model = new CatTipoHoras();

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoHoras.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            
            e.printStackTrace();

        }finally{
            close(em);
        }

        return dto;

    }

    @Override
    public List<TipoHorasDTO> buscarTodos() {
        
        EntityManager em = null;
        List<TipoHorasDTO> listDto = new ArrayList<>();
        List<CatTipoHoras> listModel = new ArrayList<>();

        try {
            
            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoHoras.findByAll",CatTipoHoras.class).getResultList();
            listDto = toDTOList(listModel);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close(em);
        }

        return listDto;

    }

    @Override
    public List<TipoHorasDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoHorasDTO> buscarPorCriterios(TipoHorasDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }
    
}
