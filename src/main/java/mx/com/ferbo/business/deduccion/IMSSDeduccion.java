package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSDeduccion extends AbstractIMSSDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(IMSSDeduccion.class);
	
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio = null;
	private BigDecimal diasPorPeriodo = null;
	private BigDecimal uma = null;
	private BigDecimal sdi = null;
	
	
	private IMSSEnfermedadMaternidadDeduccion imssEnfMatBO = null;
	private IMSSGastosMedicosPensionadosBeneficiariosDeduccion imssGastosMedicosBO = null;
	private IMSSEnDineroDeduccion imssEnDineroBO = null;
	private IMSSInvalidezVida imssInvalidezVidaBO = null;
	private IMSSCesantiaEdadAvanzadaVejezDeduccion imssCesantiaVejezBO = null;
	
	public IMSSDeduccion(List<CatTipoDeduccion> tiposDeduccion, List<CatCuotaIMSS> cuotasIMSS, Date fechaInicioAnio, Date fechaFinAnio, BigDecimal diasPorPeriodo, BigDecimal uma, BigDecimal sdi) {
		this.cuotasIMSS = cuotasIMSS;
		this.tiposDeduccion = tiposDeduccion;
		this.fechaInicioAnio = fechaInicioAnio;
		this.fechaFinAnio = fechaFinAnio;
		this.diasPorPeriodo = diasPorPeriodo;
		this.uma = uma;
		this.sdi = sdi;
	}
	
	@Override
	public List<DetNominaDeduccion> calcular(DetNomina nomina, Integer index) {
		List<DetNominaDeduccion> aportacionesIMSS = null;
		
		DetNominaDeduccion dEnfermedadMaternidad = null;
		DetNominaDeduccion dGastosMedicos = null;
		DetNominaDeduccion dEnDinero = null;
		DetNominaDeduccion dInvalidezVida = null;
		DetNominaDeduccion dCesantiaVejez = null;
		DetNominaDeduccion dIMSS = null;
		
		CatTipoDeduccion tdIMSS = null;
		
		BigDecimal imss = null;
		
		Integer idx = null;
		
		try {
			if(this.tiposDeduccion == null)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			aportacionesIMSS = new ArrayList<>();
			
			imssEnfMatBO = new IMSSEnfermedadMaternidadDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.uma, this.sdi);
			imssEnfMatBO.setTiposDeduccion(tiposDeduccion);
			imssEnfMatBO.setCuotasIMSS(cuotasIMSS);
			idx = imssEnfMatBO.nuevoIndiceDe(nomina.getDeducciones());
			dEnfermedadMaternidad = imssEnfMatBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnfermedadMaternidad);
			
			imssGastosMedicosBO = new IMSSGastosMedicosPensionadosBeneficiariosDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssGastosMedicosBO.setTiposDeduccion(tiposDeduccion);
			imssGastosMedicosBO.setCuotasIMSS(cuotasIMSS);
			dGastosMedicos = imssGastosMedicosBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dGastosMedicos);
			
			imssEnDineroBO = new IMSSEnDineroDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssEnDineroBO.setTiposDeduccion(tiposDeduccion);
			imssEnDineroBO.setCuotasIMSS(cuotasIMSS);
			dEnDinero = imssEnDineroBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnDinero);
			
			imssInvalidezVidaBO = new IMSSInvalidezVida(fechaInicioAnio, fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssInvalidezVidaBO.setTiposDeduccion(tiposDeduccion);
			imssInvalidezVidaBO.setCuotasIMSS(cuotasIMSS);
			dInvalidezVida = imssInvalidezVidaBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dInvalidezVida);
			
			imssCesantiaVejezBO = new IMSSCesantiaEdadAvanzadaVejezDeduccion(this.fechaInicioAnio, this.fechaFinAnio, diasPorPeriodo, this.sdi);
			imssCesantiaVejezBO.setTiposDeduccion(tiposDeduccion);
			imssCesantiaVejezBO.setCuotasIMSS(cuotasIMSS);
			dCesantiaVejez = imssCesantiaVejezBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dCesantiaVejez);
			
			imss = dEnfermedadMaternidad.getImporte()
					.add(dGastosMedicos.getImporte())
					.add(dEnDinero.getImporte())
					.add(dInvalidezVida.getImporte())
					.add(dCesantiaVejez.getImporte())
					;
			
			dIMSS = new DetNominaDeduccion();
			dIMSS.setKey(new DetNominaDeduccionPK(nomina, idx++));
			tdIMSS = this.getTipoDeduccion("001");
			dIMSS.setTipoDeduccion(tdIMSS);
			dIMSS.setClave("FRB-052");
			dIMSS.setNombre("I.M.S.S.");
			dIMSS.setImporte(imss);
			dIMSS.setProcesar(true);
			aportacionesIMSS.add(dIMSS);
			
			
		} catch(Exception ex) {
			log.error("Problema para obtener las aportaciones del IMSS...", ex);
		} finally {
			this.tiposDeduccion = null;
		}
		
		return aportacionesIMSS;
	}

	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}
	
	

}
