package mx.com.ferbo.commons.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabo
 */
public abstract class IBaseDAO<DTO, ID> {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgpPU");

    @PersistenceContext(unitName = "sgpPU")
    protected EntityManager emSGP = emf.createEntityManager();

    public abstract DTO buscarPorId(ID id);

    public abstract List<DTO> buscarTodos();
    
    public abstract List<DTO> buscarActivo();

    public abstract List<DTO> buscarPorCriterios(DTO e);

    public abstract void actualizar(DTO e)throws SGPException;
    
    public abstract void eliminar(DTO e)throws SGPException;

    public abstract void guardar(DTO e) throws SGPException;
    
}
