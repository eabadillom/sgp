package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegimenFiscalDAO extends BaseDAO<CatRegimenFiscal, String> {

    private static Logger log = LogManager.getLogger(RegimenFiscalDAO.class);

    public RegimenFiscalDAO(Class<CatRegimenFiscal> modelClass) {
        super(modelClass);
    }

    public RegimenFiscalDAO() {
        super(CatRegimenFiscal.class);
    }

    public List<CatRegimenFiscal> buscarActivo(Date fecha) {
        List<CatRegimenFiscal> result = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            result = em.createNamedQuery("CatRegimenFiscal.findByActivos", CatRegimenFiscal.class)
                    .setParameter("fecha", fecha)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Probleam para obtener la lista de regímenes fiscales", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return result;
    }

}
