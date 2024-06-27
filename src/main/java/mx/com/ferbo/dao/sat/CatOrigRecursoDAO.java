package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.OrigRecursoDTO;
import mx.com.ferbo.model.sat.CatOrigRecurso;

public class CatOrigRecursoDAO extends DAO<OrigRecursoDTO,CatOrigRecurso,String> {

    @Override
    public OrigRecursoDTO getDTO(CatOrigRecurso model) {
        OrigRecursoDTO dto = new OrigRecursoDTO();

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
    public CatOrigRecurso getModel(OrigRecursoDTO dto) {
        CatOrigRecurso model = new CatOrigRecurso();

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
    public OrigRecursoDTO buscarPorId(String id) {
        EntityManager em = null;
        OrigRecursoDTO dto = null;
        CatOrigRecurso model = null;

        try {
            em = getEntityManager();
            model = em.find(CatOrigRecurso.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            dto = null;
            e.printStackTrace();
        }finally{
            close(em);
        }
        
        return dto;
    }

    @Override
    public List<OrigRecursoDTO> buscarTodos() {
        List<OrigRecursoDTO> listDTO = new ArrayList<>();
        List<CatOrigRecurso> listModel = new ArrayList<>();
        EntityManager em = null;

        try {
            em = getEntityManager();
            listModel = em.createNamedQuery("CatOrigRecurso.findAll", CatOrigRecurso.class).getResultList();
            listDTO = toDTOList(listModel);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close(em);
        }

        return listDTO;
    }

    @Override
    public List<OrigRecursoDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<OrigRecursoDTO> buscarPorCriterios(OrigRecursoDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }

	@Override
	public OrigRecursoDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}
    
}
