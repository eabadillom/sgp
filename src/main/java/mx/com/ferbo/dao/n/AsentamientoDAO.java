package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class AsentamientoDAO extends BaseDAO<CatAsentamiento, CatAsentamientoPK> {

    private static Logger log = LogManager.getLogger(AsentamientoDAO.class);

    public AsentamientoDAO(Class<CatAsentamiento> modelClass) {
        super(modelClass);
    }

    public AsentamientoDAO() {
        super(CatAsentamiento.class);
    }

    public List<CatAsentamiento> buscarTodos() {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findAll", CatAsentamiento.class).getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener la lista de asentamientos...", ex);
        } finally {
            this.close(em);
        }

        return modelList;
    }

    public synchronized void guardar(CatAsentamiento asentamiento) throws SGPException {
        EntityManager em = null;

        try {
            log.info("Inicia el proceso para guardar el registro en la tabla de asentamietos");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(asentamiento);
            em.getTransaction().commit();
            log.info("Finaliza el proceso para guardar el registro en la tabla de asentamietos");
        } catch (Exception ex) {
            log.error("Hubo algun problema al momento de guardar el registro en la tabla de asentamientos");
            super.rollback(em);
            throw new SGPException("No se pudo guardar el registro de asentamiento" + ex);
        } finally {
            super.close(em);
        }
    }

    public synchronized void actualizar(CatAsentamiento asentamiento) throws SGPException {
        EntityManager em = null;

        try {
            log.info("Inicia el proceso para actualizar el registro en la tabla de asentamietos");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(asentamiento);
            em.getTransaction().commit();
            log.info("Finaliza el proceso para actualizar el registro en la tabla de asentamietos");
        } catch (Exception ex) {
            log.error("Hubo algun problema al momento de actualizar el registro en la tabla de asentamientos");
            super.rollback(em);
            throw new SGPException("No se pudo actualizar el registro de asentamiento" + ex);
        } finally {
            super.close(em);
        }
    }

    public synchronized void eliminar(Integer idpais, Integer idestado, Integer idmunicipio, Integer idlocalidad, Integer idasentamiento,short idtipo, Integer identidad) throws SGPException {
        EntityManager em = null;
        String consulta = ""; 

        try {
            log.info("Inicia el proceso para eliminar el registro en la tabla de asentamietos");
            em = super.getEntityManager();
            em.getTransaction().begin();
            consulta += "delete from CatAsentamiento e where e.key.localidad.key.municipio.key.estado.key.pais.id = :idpais ";
            consulta += "and e.key.localidad.key.municipio.key.estado.key.id = :idestado ";
            consulta += "and e.key.localidad.key.municipio.key.id = :idmunicipio ";
            consulta += "and e.key.localidad.key.id = :idlocalidad ";
            consulta += "and e.key.id = :idasentamiento ";
            consulta += "and e.tipoAsentamiento.id = :idtipo ";
            consulta += "and e.entidadPostal.id = :identidad";
            Query query = em.createQuery(consulta);
            query.setParameter("idpais", idpais);
            query.setParameter("idestado", idestado);
            query.setParameter("idmunicipio", idmunicipio);
            query.setParameter("idlocalidad", idlocalidad);
            query.setParameter("idasentamiento", idasentamiento);
            query.setParameter("idtipo", idtipo);
            query.setParameter("identidad", identidad);
            query.executeUpdate();
            em.getTransaction().commit();
            log.info("Finaliza el proceso para eliminar el registro en la tabla de asentamietos");
        } catch (Exception ex) {
            log.error("Hubo algun problema al momento de eliminar el registro en la tabla de asentamientos");
            super.rollback(em);
            throw new SGPException("No se pudo eliminar el registro de asentamiento" + ex);
        } finally {
            super.close(em);
        }
    }

    public List<CatAsentamiento> buscarPorCodigoPostal(String codigoPostal) {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findByCodigoPostal", CatAsentamiento.class)
                    .setParameter("codigoPostal", codigoPostal)
                    .getResultList();

            log.trace("Info Asentamiento: {}", modelList.toString());

            for (CatAsentamiento aux : modelList) {
                log.debug("Id Asentamiento: {}", aux.getKey().getId());
                log.debug("Id Tipo Asentamiento: {}", aux.getTipoAsentamiento().getId());
                log.debug("Id Entidad Postal: {}", aux.getEntidadPostal().getId());
                log.debug("Id Localidad: {}", aux.getKey().getLocalidad().getKey().getId());
                log.debug("Id Municipio: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                log.debug("Id Estado: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                log.debug("Id Pais: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener la lista de asentamientos...", ex);
        } finally {
            this.close(em);
        }

        return modelList;
    }

    public synchronized List<CatAsentamiento> obtenerTodosPorLocalidad(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idLocalidad) throws SGPException {
        EntityManager em = null;
        List<CatAsentamiento> asentamientos = null;
        try {
            log.info("Inicia proceso de obtener todos los asentamientos.");
            em = super.getEntityManager();
            TypedQuery<CatAsentamiento> resultado = em.createQuery("select e from CatAsentamiento e where e.key.localidad.key.municipio.key.estado.key.pais.id = :idPais and e.key.localidad.key.municipio.key.estado.key.id = :idEstado and e.key.localidad.key.municipio.key.id = :idMunicipio and e.key.localidad.key.id = :idLocalidad", CatAsentamiento.class);
            resultado.setParameter("idPais", idPais);
            resultado.setParameter("idEstado", idEstado);
            resultado.setParameter("idMunicipio", idMunicipio);
            resultado.setParameter("idLocalidad", idLocalidad);
            asentamientos = resultado.getResultList();
            log.info("Finaliza proceso de obtener todos los asentamientos");
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatLAsentamiento");
            throw new SGPException("Problema al obtener los registros" + ex);
        } finally {
            super.close(em);
        }
        return asentamientos;
    }

    public CatAsentamiento buscarPorParametros(Integer idAsentamiento, Integer idLocalidad, Integer idMunicipio, Integer idEstado, Integer idPais) {
        CatAsentamiento model = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatAsentamiento.findByParametros", CatAsentamiento.class)
                    .setParameter("idAsentamiento", idAsentamiento)
                    .setParameter("idLocalidad", idLocalidad)
                    .setParameter("idMunicipio", idMunicipio)
                    .setParameter("idEstado", idEstado)
                    .setParameter("idPais", idPais)
                    .getSingleResult();

            log.trace("Info Asentamiento: {}", model.toString());

            if (model != null) {
                log.debug("Id Asentamiento: {}", model.getKey().getId());
                log.debug("Id Tipo Asentamiento: {}", model.getTipoAsentamiento().getId());
                log.debug("Id Entidad Postal: {}", model.getEntidadPostal().getId());
                log.debug("Id Localidad: {}", model.getKey().getLocalidad().getKey().getId());
                log.debug("Id Municipio: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                log.debug("Id Estado: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                log.debug("Id Pais: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el asentamiento...", ex);
        } finally {
            this.close(em);
        }

        return model;
    }

    public synchronized List<CatAsentamiento> obtenerPorTipoAsentamiento(Integer idpais, Integer idestado, Integer idmunicipio, Integer idlocalidad, short idTipo) throws SGPException {
        List<CatAsentamiento> resultado = null;
        EntityManager em = null;
        String query = "";

        try {
            log.info("Inicia proceso de obtener todos los asentamientos en base al tipo.");
            em = super.getEntityManager();
            query += "select e from CatAsentamiento e where e.key.localidad.key.municipio.key.estado.key.pais.id = :idpais ";
            query += "and e.key.localidad.key.municipio.key.estado.key.id = :idestado ";
            query += "and e.key.localidad.key.municipio.key.id = :idmunicipio ";
            query += "and e.key.localidad.key.id = :idlocalidad ";
            query += "and e.tipoAsentamiento.id = :idTipo";
            TypedQuery<CatAsentamiento> consulta = em.createQuery(query, CatAsentamiento.class);
            consulta.setParameter("idpais", idpais);
            consulta.setParameter("idestado", idestado);
            consulta.setParameter("idmunicipio", idmunicipio);
            consulta.setParameter("idlocalidad", idlocalidad);
            consulta.setParameter("idTipo", idTipo);
            resultado = consulta.getResultList();
            log.info("finaliza proceso de obtener todos los asentamientos en base al tipo.");
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema obtener todos los asentamientos por el tipo");
            throw new SGPException("Problema al obtener todos los registros" + ex);
        } finally {
            super.close(em);
        }

        return resultado;
    }

    public synchronized List<CatAsentamiento> obtenerPorEntidadPostal(Integer idpais, Integer idestado, Integer idmunicipio, Integer idlocalidad, Integer idEntidad) throws SGPException {
        List<CatAsentamiento> resultado = null;
        EntityManager em = null;
        String query = "";

        try {
            log.info("Inicia proceso de obtener todos los asentamientos en base a la entidad.");
            em = super.getEntityManager();
            query += "select e from CatAsentamiento e where e.key.localidad.key.municipio.key.estado.key.pais.id = :idpais ";
            query += "and e.key.localidad.key.municipio.key.estado.key.id = :idestado ";
            query += "and e.key.localidad.key.municipio.key.id = :idmunicipio ";
            query += "and e.key.localidad.key.id = :idlocalidad ";
            query += "and e.entidadPostal.id = :idEntidad";
            TypedQuery<CatAsentamiento> consulta = em.createQuery(query, CatAsentamiento.class);
            consulta.setParameter("idpais", idpais);
            consulta.setParameter("idestado", idestado);
            consulta.setParameter("idmunicipio", idmunicipio);
            consulta.setParameter("idlocalidad", idlocalidad);
            consulta.setParameter("idEntidad", idEntidad);
            resultado = consulta.getResultList();
            log.info("finaliza proceso de obtener todos los asentamientos en base a la entidad.");
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema obtener todos los asentamientos por la entidad postal");
            throw new SGPException("Problema al obtener todos los registros" + ex);
        } finally {
            super.close(em);
        }

        return resultado;
    }
}
