package mx.com.ferbo.dao.sat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoIncapacidadDTO;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;

public class CatTipoIncapacidadDAO extends DAO<TipoIncapacidadDTO,CatTipoIncapacidadSAT,String> {

    @Override
    public TipoIncapacidadDTO getDTO(CatTipoIncapacidadSAT model) {
        TipoIncapacidadDTO dto = new TipoIncapacidadDTO();

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
    public CatTipoIncapacidadSAT getModel(TipoIncapacidadDTO dto) {

        CatTipoIncapacidadSAT model = new CatTipoIncapacidadSAT();

        try {
            
            model.setClave(dto.getClave());
            model.setDescripcion(dto.getDescripcion());

        } catch (Exception e) {
            e.getMessage();
            model = null;
        }

        return model;

    }

    @Override
    public TipoIncapacidadDTO buscarPorId(String id) {
        EntityManager em = null;
        CatTipoIncapacidadSAT model = null;
        TipoIncapacidadDTO dto = null;

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoIncapacidadSAT.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return dto;
    }

    @Override
    public List<TipoIncapacidadDTO> buscarTodos() {

        List<CatTipoIncapacidadSAT> listModel = new ArrayList<>();
        List<TipoIncapacidadDTO> listDto = new ArrayList<>();
        EntityManager em = null;

        try{

            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoIncapacidad.findByAll",CatTipoIncapacidadSAT.class).getResultList();
            listDto = toDTOList(listModel);

        }catch(Exception e){
            e.getMessage();            
        }finally{
            close(em);
        }

        return listDto;
        
    }

    @Override
    public List<TipoIncapacidadDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoIncapacidadDTO> buscarPorCriterios(TipoIncapacidadDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }

	@Override
	public TipoIncapacidadDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}
}
