package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.RiesgoPuestoDTO;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;

public class RiesgoPuestoDAO extends DAO<RiesgoPuestoDTO,CatRiesgoPuesto,Integer>{

    @Override
    public RiesgoPuestoDTO getDTO(CatRiesgoPuesto model) {
        
        RiesgoPuestoDTO dto = new RiesgoPuestoDTO();

        try {
            
            dto.setClave(model.getClave());
            dto.setDescripcion(model.getDescripcion());
            dto.setVigenciaInicio(model.getVigenciaInicio());
            dto.setVigenciaFin(model.getVigenciaFin());

        } catch (Exception e) {
            e.getMessage();
        }

        return dto;
    }

    @Override
    public CatRiesgoPuesto getModel(RiesgoPuestoDTO dto) {
        
        CatRiesgoPuesto model = new CatRiesgoPuesto();

        try {
            
            model.setClave(dto.getClave());
            model.setDescripcion(dto.getDescripcion());
            model.setVigenciaInicio(dto.getVigenciaInicio());
            model.setVigenciaFin(dto.getVigenciaFin());

        } catch (Exception e) {
            e.getMessage();
        }

        return model;
    }

    @Override
    public RiesgoPuestoDTO buscarPorId(Integer id) {

        EntityManager em = null;
        RiesgoPuestoDTO dto = new RiesgoPuestoDTO();
        CatRiesgoPuesto model = new CatRiesgoPuesto();

        try {

            em = getEntityManager();
            model = em.find(CatRiesgoPuesto.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return dto;
    }

    @Override
    public List<RiesgoPuestoDTO> buscarTodos() {
        
        EntityManager em = null;
        List<CatRiesgoPuesto> listModel = new ArrayList<>();
        List<RiesgoPuestoDTO> listDTO = new ArrayList<>();

        try {
            
            em = getEntityManager();
            listModel = em.createNamedQuery("CatRiesgoPuesto.findByAll", CatRiesgoPuesto.class).getResultList();
            listDTO = toDTOList(listModel);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }
        
        return listDTO;
    }

    @Override
    public List<RiesgoPuestoDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<RiesgoPuestoDTO> buscarPorCriterios(RiesgoPuestoDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }

	@Override
	public RiesgoPuestoDTO buscarPorId(Integer id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}

    
    
}
