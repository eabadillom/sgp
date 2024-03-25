package mx.com.ferbo.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.SGPException;

public abstract class IBaseDAO<E, ID> {

    protected EntityManager emSGP = EntityManagerUtil.getEntityManager();

    public abstract E buscarPorId(ID id);

    public abstract List<E> buscarTodos();
    
    public abstract List<E> buscarActivo();

    public abstract List<E> buscarPorCriterios(E e);

    public abstract void actualizar(E e)throws SGPException;
    
    public abstract void eliminar(E e)throws SGPException;

    public abstract void guardar(E e) throws SGPException;
    
}
