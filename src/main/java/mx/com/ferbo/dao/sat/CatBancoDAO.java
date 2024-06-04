package mx.com.ferbo.dao.sat;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.DAO;
import mx.com.ferbo.dto.sat.BancoDTO;
import mx.com.ferbo.model.sat.CatBanco;

public class CatBancoDAO extends DAO<BancoDTO,CatBanco,String> {

    @Override
    public BancoDTO getDTO(CatBanco model) {

        BancoDTO bancoDTO = new BancoDTO();

        try{
            bancoDTO.setIdBanco(model.getIdBanco());
            bancoDTO.setDescripcion(model.getDescripcion());
            bancoDTO.setNombre(model.getNombre());
            bancoDTO.setFechaInicio(model.getVigenciaInicio());
            bancoDTO.setFechaFin(model.getVigenciaFin());
        }catch(Exception e){
            e.printStackTrace();
            bancoDTO = null;
        }

        return bancoDTO;
    }

    @Override
    public CatBanco getModel(BancoDTO dto) {
        
        CatBanco catBanco = new CatBanco();
        
        try {

            catBanco.setIdBanco(dto.getIdBanco());
            catBanco.setDescripcion(dto.getDescripcion());
            catBanco.setNombre(dto.getNombre());
            catBanco.setVigenciaInicio(dto.getFechaInicio());
            catBanco.setVigenciaFin(dto.getFechaFin());

        } catch (Exception e) {
            e.printStackTrace();
            catBanco = null;
        }

        return catBanco;
    }

    @Override
    public BancoDTO buscarPorId(String id) {
        BancoDTO dto = null;
        CatBanco model = null;
        EntityManager em = null;
        try {

            em = getEntityManager();
            model = em.find(CatBanco.class,id);
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
    public List<BancoDTO> buscarTodos() {
        
        List<BancoDTO> listDTO = null;
        List<CatBanco> listModel = null;
        EntityManager em = null;

        try {
            em = getEntityManager();
            listModel = em.createNamedQuery("CatBanco.findByAll", CatBanco.class).getResultList();
            listDTO = toDTOList(listModel);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close(em);
        }

        return listDTO;

    }

    @Override
    public List<BancoDTO> buscarActivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarActivo'");
    }

    @Override
    public List<BancoDTO> buscarPorCriterios(BancoDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
    }
    


}
