package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSDeduccion extends AbstractDeduccion implements IDeducciones {
	
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
	
	public IMSSDeduccion(List<CatTipoDeduccion> tiposDeduccion, Date fechaInicioAnio, Date fechaFinAnio, BigDecimal diasPorPeriodo, BigDecimal uma, BigDecimal sdi) {
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
		
		try {
			if(this.tiposDeduccion == null)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			aportacionesIMSS = new ArrayList<>();
			
			imssEnfMatBO = new IMSSEnfermedadMaternidadDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.uma, this.sdi);
			imssEnfMatBO.setTiposDeduccion(tiposDeduccion);
			dEnfermedadMaternidad = imssEnfMatBO.calcular(nomina, index++);
			aportacionesIMSS.add(dEnfermedadMaternidad);
			
			imssGastosMedicosBO = new IMSSGastosMedicosPensionadosBeneficiariosDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssGastosMedicosBO.setTiposDeduccion(tiposDeduccion);
			dGastosMedicos = imssGastosMedicosBO.calcular(nomina, index++);
			aportacionesIMSS.add(dGastosMedicos);
			
			imssEnDineroBO = new IMSSEnDineroDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssEnDineroBO.setTiposDeduccion(tiposDeduccion);
			dEnDinero = imssEnDineroBO.calcular(nomina, index++);
			aportacionesIMSS.add(dEnDinero);
			
			imssInvalidezVidaBO = new IMSSInvalidezVida(fechaInicioAnio, fechaFinAnio, this.diasPorPeriodo, this.sdi);
			imssInvalidezVidaBO.setTiposDeduccion(tiposDeduccion);
			dInvalidezVida = imssInvalidezVidaBO.calcular(nomina, index++);
			aportacionesIMSS.add(dInvalidezVida);
			
			imssCesantiaVejezBO = new IMSSCesantiaEdadAvanzadaVejezDeduccion(this.fechaInicioAnio, this.fechaFinAnio, diasPorPeriodo, this.sdi);
			imssCesantiaVejezBO.setTiposDeduccion(tiposDeduccion);
			dCesantiaVejez = imssCesantiaVejezBO.calcular(nomina, index++);
			aportacionesIMSS.add(dCesantiaVejez);
			
			imss = dEnfermedadMaternidad.getImporte()
					.add(dGastosMedicos.getImporte())
					.add(dEnDinero.getImporte())
					.add(dInvalidezVida.getImporte())
					.add(dCesantiaVejez.getImporte())
					;
			
			dIMSS = new DetNominaDeduccion();
			dIMSS.setKey(new DetNominaDeduccionPK(nomina, index++));
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
