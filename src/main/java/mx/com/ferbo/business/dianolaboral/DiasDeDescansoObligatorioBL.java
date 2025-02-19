package mx.com.ferbo.business.dianolaboral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class DiasDeDescansoObligatorioBL implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(DiasDeDescansoObligatorioBL.class);

    private final DiaNoLaboralDAO diaNoLaboralDAO;
    private List<CatDiaNoLaboral> diasNoLaboralSelected;
    private Date fechaFin;
    private Date fechaInicio;
    private List<Date> diasAsueto;

    public DiasDeDescansoObligatorioBL() {
        this.diaNoLaboralDAO = new DiaNoLaboralDAO();
        Integer anioEnCurso = DateUtil.getAnio(new Date());
        this.fechaInicio = DateUtil.inicializaFechaInicioAnioCurso(anioEnCurso - 1);
        this.fechaFin = DateUtil.inicializaFechaTerminoAnioCurso(anioEnCurso + 1);
        this.diasNoLaboralSelected = diaNoLaboralDAO.buscarPorPeriodo("MX", this.fechaInicio, this.fechaFin);
        this.diasAsueto = this.diasDeAsueto();
    }

    public final List<Date> diasDeAsueto() {
        List<Date> diasDeAsueto = new ArrayList();

        for (CatDiaNoLaboral aux : diasNoLaboralSelected) {
            diasDeAsueto.add(aux.getFecha());
        }

        return diasDeAsueto;
    }

    public List<CatDiaNoLaboral> diasDescansoAnual() {
        List<CatDiaNoLaboral> descanso = null;
        Integer anio = DateUtil.getAnio(DateUtil.now());
        Date inicial = DateUtil.inicializaFechaInicioAnioCurso(anio);
        anio++;
        Date terminal = DateUtil.inicializaFechaInicioAnioCurso(anio);
        descanso = diaNoLaboralDAO.buscarPorPeriodo("MX", inicial, terminal);
        return descanso;
    }

    public List<CatDiaNoLaboral> getDiasNoLaboralSelected() {
        return diasNoLaboralSelected;
    }

    public void setDiasNoLaboralSelected(List<CatDiaNoLaboral> diasNoLaboralSelected) {
        this.diasNoLaboralSelected = diasNoLaboralSelected;
    }

    public Date getFechaHoy() {
        return fechaFin;
    }

    public void setFechaHoy(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicioAnio() {
        return fechaInicio;
    }

    public void setFechaInicioAnio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public List<Date> getDiasAsueto() {
        return diasAsueto;
    }

    public void setDiasAsueto(List<Date> diasAsueto) {
        this.diasAsueto = diasAsueto;
    }

    public void diasDescansoEstanActualizados() throws SGPException {
        DiasDeDescansoObligatorioBL diasDeDescansoObligatorio = new DiasDeDescansoObligatorioBL();
        List<CatDiaNoLaboral> diasNoLaborables = new ArrayList<CatDiaNoLaboral>();
        diasNoLaborables = diasDeDescansoObligatorio.diasDescansoAnual();

        if (diasNoLaborables.isEmpty() || diasNoLaborables.size() < 2) {
            throw new SGPException("Error: Dias de asueto no actualizados. Por favor contactar a RH");
        }
    }

}
