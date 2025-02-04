package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CertificadoDAO extends BaseDAO<Certificado, Integer> {

    private static Logger log = LogManager.getLogger(Certificado.class);

    public CertificadoDAO(Class<Certificado> modelClass) {
        super(modelClass);
    }

    public CertificadoDAO() {
        super(Certificado.class);
    }

    public synchronized List<Certificado> findAll() throws SGPException {
        EntityManager em = null;
        List<Certificado> certificados = null;

        try {
            log.info("Inicia el preceso para obtener todos los certificados");
            em = getEntityManager();
            certificados = em.createNamedQuery("Certificado.findAll", modelClass).getResultList();
            log.info("Finaliza el proceso para obtener todos los certificados.");
        } catch (Exception ex) {
            log.error("Error al momento de extraer todos los certificados. " + ex.getMessage());
            rollback(em);
            throw new SGPException("No se pudieron cargar todos los certificados");
        } finally {
            close(em);
        }

        return certificados;
    }

    public synchronized Certificado buscarPorFecha() throws SGPException {

        EntityManager em = null;
        Certificado certificado = null;

        try {
            log.info("Inicia el preceso para obtener el certificado en base a una fecha");
            em = getEntityManager();
            certificado = em.createNamedQuery("Certificado.findByFecha", Certificado.class).getSingleResult();
            log.info("Finaliza el preceso para obtener el certificado en base a una fecha");
        } catch (Exception ex) {
            log.error("Error al momento de obtener el certificado en base a la fecha. " + ex.getMessage());
            rollback(em);
            throw new SGPException("No se pudo obtener el certificado en base a la fecha");
        } finally {
            close(em);
        }

        return certificado;
    }

    public synchronized List<Certificado> buscarPorEmpresa(Integer empresa) throws SGPException {
        List<Certificado> certificados = null;
        EntityManager em = null;

        try {
            log.info("Inicia el preceso para obtener los cetificados en base a una empresa");
            em = getEntityManager();
            certificados = em.createNamedQuery("Certificado.findByEmpresa", Certificado.class).setParameter("empresa", empresa).getResultList();
            log.info("Finaliza el preceso para obtener los cetificados en base a una empresa");
        } catch (Exception ex) {
            log.error("Error al momento de obtener todos los certificado en base a una empresa. " + ex.getMessage());
            rollback(em);
            throw new SGPException("No se pudieron cargar los certificados de la empresa");
        } finally {
            close(em);
        }
        return certificados;
    }

}
