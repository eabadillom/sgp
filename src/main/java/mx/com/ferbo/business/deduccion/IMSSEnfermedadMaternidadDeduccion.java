package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class IMSSEnfermedadMaternidadDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfermedadMaternidadDeduccion.class);
	
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio = null;
	private BigDecimal totalDiasPeriodo = null;
	private BigDecimal uma = null;
	private BigDecimal salarioDiarioIntegrado = null;
	private CuotaIMSSDAO tarifaIMSSDAO = null;
	
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	
	public IMSSEnfermedadMaternidadDeduccion(Date fechaInicioAnio, Date fechaFinAnio, BigDecimal totalDiasPeriodo, BigDecimal uma, BigDecimal salarioDiarioIntegrado) {
		this.fechaInicioAnio = fechaInicioAnio;
		this.fechaFinAnio = fechaFinAnio;
		this.totalDiasPeriodo = totalDiasPeriodo;
		this.uma = uma;
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion dIMSS = null;
		BigDecimal cuota = null;
		BigDecimal cero = null;
		BigDecimal tres = null;
		BigDecimal limiteUMAs = null;
		BigDecimal excedente = null;
		BigDecimal tarifa = null;
		CatCuotaIMSS tarifaIMSS = null;
		CatTipoDeduccion tdIMSS = null;
		try {
			if(this.tarifaIMSSDAO == null)
				this.tarifaIMSSDAO = new CuotaIMSSDAO();
			
			if(this.tipoDeduccionDAO == null)
				this.tipoDeduccionDAO = new TipoDeduccionDAO();
			this.tipoDeduccionDAO.buscarPorId("001");
			
			//Constantes necesarias para el cálculo de Enfermedad y Maternidad.
			cero = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tres = new BigDecimal("3.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			cuota = cero;
			
			limiteUMAs = this.uma.multiply(tres).setScale(2, BigDecimal.ROUND_HALF_UP);
			excedente = this.salarioDiarioIntegrado.subtract(limiteUMAs);
			
			if(this.salarioDiarioIntegrado.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = tarifaIMSSDAO.buscarPor("O", "EM1", this.fechaInicioAnio, this.fechaFinAnio, tres);
				tarifa = tarifaIMSS.getCuota();
				cuota = excedente
						.multiply(tarifa).setScale(2, BigDecimal.ROUND_HALF_UP)
						.multiply(this.totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			dIMSS = new DetNominaDeduccion();
			dIMSS.setKey(new DetNominaDeduccionPK(nomina, index));
			dIMSS.setTipoDeduccion(tdIMSS);
			dIMSS.setClave("---");
			dIMSS.setNombre("I.M.S.S. (Enfermedad y Maternidad)");
			dIMSS.setImporte(cuota);
			dIMSS.setProcesar(false);
		}
		
		return dIMSS;
	}

	public TipoDeduccionDAO getTipoDeduccionDAO() {
		return tipoDeduccionDAO;
	}

	public void setTipoDeduccionDAO(TipoDeduccionDAO tipoDeduccionDAO) {
		this.tipoDeduccionDAO = tipoDeduccionDAO;
	}

}
