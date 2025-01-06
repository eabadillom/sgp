package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSDeduccion extends AbstractIMSSDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(IMSSDeduccion.class);
	
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio = null;
	private BigDecimal diasTrabajados = null;
	private BigDecimal uma = null;
	private BigDecimal sdi = null;
	private ParametrosNomina parametros = null;
	
	private IMSSRiesgoTrabajoDeduccion imssRiesgoTrabajoBO = null;
	private IMSSEnfermedadMaternidadDeduccion imssEnfMatBO = null;
	private IMSSGastosMedicosPensionadosBeneficiariosDeduccion imssGastosMedicosBO = null;
	private IMSSEnDineroDeduccion imssEnDineroBO = null;
	private IMSSInvalidezVida imssInvalidezVidaBO = null;
	private IMSSCesantiaEdadAvanzadaVejezDeduccion imssCesantiaVejezBO = null;
	
	public IMSSDeduccion(ParametrosNomina parametros, BigDecimal diasTrabajados) {
		this.parametros      = parametros;
		this.cuotasIMSS      = parametros.getCuotasIMSS();
		this.tiposDeduccion  = parametros.getTiposDeduccion();
		this.fechaInicioAnio = parametros.getFechaInicioAnio();
		this.fechaFinAnio    = parametros.getFechaFinAnio();
		this.diasTrabajados  = diasTrabajados;
		this.uma             = parametros.getUma().getImporteDiario();
	}
	
	@Override
	public void procesar(DetNomina nomina) {
		List<DetNominaDeduccion> aportacionesIMSS = null;
		
		DetNominaDeduccion dRiesgoTrabajo = null;
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
			if(nomina.getReceptor() == null || nomina.getReceptor().getSalarioDiarioIntegrado() == null)
				throw new SGPException("Debe configurar el receptor con el salario diario integrado.");
			
			this.sdi = nomina.getReceptor().getSalarioDiarioIntegrado();
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			aportacionesIMSS = new ArrayList<>();
			
			imssRiesgoTrabajoBO = new IMSSRiesgoTrabajoDeduccion(this.parametros, this.diasTrabajados, this.sdi);
			imssRiesgoTrabajoBO.setTiposDeduccion(tiposDeduccion);
			imssRiesgoTrabajoBO.setCuotasIMSS(cuotasIMSS);
			idx = imssEnfMatBO.nuevoIndiceDe(nomina.getDeducciones());
			dRiesgoTrabajo = imssRiesgoTrabajoBO.calcular(nomina, idx);
			aportacionesIMSS.add(dRiesgoTrabajo);
			//TODO Agregar objeto Deducción de Riesgo de trabajo a la lista de deducciones.
			
			imssEnfMatBO = new IMSSEnfermedadMaternidadDeduccion(this.diasTrabajados, this.uma, this.sdi);
			imssEnfMatBO.setTiposDeduccion(tiposDeduccion);
			imssEnfMatBO.setCuotasIMSS(cuotasIMSS);
			idx = imssEnfMatBO.nuevoIndiceDe(nomina.getDeducciones());
			dEnfermedadMaternidad = imssEnfMatBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnfermedadMaternidad);
			
			imssGastosMedicosBO = new IMSSGastosMedicosPensionadosBeneficiariosDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasTrabajados, this.sdi);
			imssGastosMedicosBO.setTiposDeduccion(tiposDeduccion);
			imssGastosMedicosBO.setCuotasIMSS(cuotasIMSS);
			dGastosMedicos = imssGastosMedicosBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dGastosMedicos);
			
			imssEnDineroBO = new IMSSEnDineroDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasTrabajados, this.sdi);
			imssEnDineroBO.setTiposDeduccion(tiposDeduccion);
			imssEnDineroBO.setCuotasIMSS(cuotasIMSS);
			dEnDinero = imssEnDineroBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnDinero);
			
			imssInvalidezVidaBO = new IMSSInvalidezVida(fechaInicioAnio, fechaFinAnio, this.diasTrabajados, this.sdi);
			imssInvalidezVidaBO.setTiposDeduccion(tiposDeduccion);
			imssInvalidezVidaBO.setCuotasIMSS(cuotasIMSS);
			dInvalidezVida = imssInvalidezVidaBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dInvalidezVida);
			
			imssCesantiaVejezBO = new IMSSCesantiaEdadAvanzadaVejezDeduccion(this.fechaInicioAnio, this.fechaFinAnio, this.diasTrabajados, this.sdi);
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
			dIMSS.setInformar(true);
			dIMSS.setProcesar(true);
			aportacionesIMSS.add(dIMSS);
			
			nomina.getDeducciones().addAll(aportacionesIMSS);
		} catch(Exception ex) {
			log.error("Problema para obtener las aportaciones del IMSS...", ex);
		} finally {
			this.tiposDeduccion = null;
		}
	}

	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}
	
	

}
