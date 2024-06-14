package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.TipoOtroPagoDTO;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class CatTipoOtroPagoDAO extends DAO<TipoOtroPagoDTO,CatTipoOtroPago,String> {

    @Override
    public TipoOtroPagoDTO getDTO(CatTipoOtroPago model) {
        TipoOtroPagoDTO dto = new TipoOtroPagoDTO();

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
    public CatTipoOtroPago getModel(TipoOtroPagoDTO dto) {

        CatTipoOtroPago model = new CatTipoOtroPago();

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
    public TipoOtroPagoDTO buscarPorId(String id) {
        EntityManager em = null;
        CatTipoOtroPago model = null;
        TipoOtroPagoDTO dto = null;

        try {
            
            em = getEntityManager();
            model = em.find(CatTipoOtroPago.class, id);
            dto = this.getDTO(model);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);            
        }

        return dto;
    }

    @Override
    public List<TipoOtroPagoDTO> buscarTodos() {
        EntityManager em = null;
        List<CatTipoOtroPago> listModel = null;
        List<TipoOtroPagoDTO> listDto = null;

        try {
            
            em = getEntityManager();
            listModel = em.createNamedQuery("CatTipoOtroPago.findByAll", CatTipoOtroPago.class).getResultList();
            listDto = toDTOList(listModel);

        } catch (Exception e) {
            e.getMessage();
        }finally{
            close(em);
        }

        return listDto;
    }

    @Override
    public List<TipoOtroPagoDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<TipoOtroPagoDTO> buscarPorCriterios(TipoOtroPagoDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }

	@Override
	public TipoOtroPagoDTO buscarPorId(String id, boolean isFullInfo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
	}
    
}
