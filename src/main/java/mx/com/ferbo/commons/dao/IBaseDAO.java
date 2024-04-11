package mx.com.ferbo.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.SGPException;

public abstract class IBaseDAO<DTO, ID> {

    protected EntityManager emSGP = EntityManagerUtil.getEntityManager();

    public abstract DTO buscarPorId(ID id);

    public abstract List<DTO> buscarTodos();
    
    public abstract List<DTO> buscarActivo();

    public abstract List<DTO> buscarPorCriterios(DTO e);

    public abstract void actualizar(DTO e)throws SGPException;
    
    public abstract void eliminar(DTO e)throws SGPException;

    public abstract void guardar(DTO e) throws SGPException;
    
}
