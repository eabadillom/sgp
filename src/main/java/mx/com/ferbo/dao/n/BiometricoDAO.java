package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetBiometrico;

public class BiometricoDAO extends BaseDAO<DetBiometrico, Integer> {

    private static Logger log = LogManager.getLogger(BiometricoDAO.class);

    public BiometricoDAO(Class<DetBiometrico> modelClass) {
        super(modelClass);
    }

    public BiometricoDAO() {
        super(DetBiometrico.class);
    }

    public DetBiometrico consultaBiometricoByNumEmpleado(String numeroEmpleado) {
        DetBiometrico model = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetBiometrico.findByNumeroEmpleado", modelClass)
                    .setParameter("numeroEmpleado", numeroEmpleado)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.warn("No se encontró informacion de biometricos para el empleado {}", numeroEmpleado);

        } catch (Exception ex) {
            log.error("Problema para obtener el biometrico del empleado " + numeroEmpleado, ex);
        } finally {
            this.close(em);
        }

        return model;
    }

    public DetBiometrico consultaBiometricoByIdEmpleado(Integer idEmpleado) {
        DetBiometrico model = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetBiometrico.findByIdEmpleado", modelClass)
                    .setParameter("idEmpleado", idEmpleado)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.warn("No se encontró informacion de biometricos para el empleado {}", idEmpleado);

        } catch (Exception ex) {
            log.error("Problema para obtener el biometrico del empleado " + idEmpleado, ex);
        } finally {
            this.close(em);
        }

        return model;
    }

}
