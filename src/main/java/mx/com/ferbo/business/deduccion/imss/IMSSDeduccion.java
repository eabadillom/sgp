package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;
import java.util.ArrayList;
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
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias = null;
	private BigDecimal incapacidades = null;
	private BigDecimal sdi = null;
	private ParametrosNomina parametros = null;
	
	private IMSSRiesgoTrabajoDeduccion imssRiesgoTrabajoBO = null;
	private IMSSEnfMatEnEspecieDeduccion imssEnfMatBO = null;
	private IMSSEnfMatGastosMedicosDeduccion imssEnfMatGastosMedBO = null;
	private IMSSEnfMatEnDineroDeduccion imssEnfMatEnDineroBO = null;
	private IMSSInvalidezVida imssInvalidezVidaBO = null;
	private IMSSCesantiaEdadAvanzadaVejezDeduccion imssCesantiaVejezBO = null;
	
	public IMSSDeduccion(ParametrosNomina parametros, BigDecimal diasTrabajados) {
		this.parametros      = parametros;
		this.cuotasIMSS      = parametros.getCuotasIMSS();
		this.tiposDeduccion  = parametros.getTiposDeduccion();
		this.diasTrabajados  = diasTrabajados;
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
			
			//RIESGOS DE TRABAJO
			imssRiesgoTrabajoBO = new IMSSRiesgoTrabajoDeduccion(this.parametros, this.diasTrabajados, this.sdi);
			idx = imssRiesgoTrabajoBO.nuevoIndiceDe(nomina.getDeducciones());
			//TODO Falta integrar la cuota de riesgo de la empresa.
			dRiesgoTrabajo = imssRiesgoTrabajoBO.calcular(nomina, idx);
			aportacionesIMSS.add(dRiesgoTrabajo);
			
			//ENFERMEDADES Y MATERNIDAD (EN ESPECIE)
			imssEnfMatBO = new IMSSEnfMatEnEspecieDeduccion(this.parametros, this.diasTrabajados, this.ausencias, this.incapacidades, this.sdi);
			dEnfermedadMaternidad = imssEnfMatBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnfermedadMaternidad);
			
			//ENFERMEDADES Y MATERNIDAD (GASTOS MEDICOS PARA PENSIONADOS Y BENEFICIARIOS)
			imssEnfMatGastosMedBO = new IMSSEnfMatGastosMedicosDeduccion(this.parametros, this.diasTrabajados, this.ausencias, this.incapacidades, this.sdi);
			dGastosMedicos = imssEnfMatGastosMedBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dGastosMedicos);
			
			//ENFERMEDADES Y MATERNIDAD (EN DINERO)
			imssEnfMatEnDineroBO = new IMSSEnfMatEnDineroDeduccion(this.parametros, this.diasTrabajados, this.ausencias, this.incapacidades, this.sdi);
			dEnDinero = imssEnfMatEnDineroBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dEnDinero);
			
			//INVALIDEZ Y VIDA (EN ESPECIE Y EN DINERO)
			imssInvalidezVidaBO = new IMSSInvalidezVida(this.parametros, new BigDecimal("7.00").setScale(2, BigDecimal.ROUND_HALF_UP), this.sdi);
			dInvalidezVida = imssInvalidezVidaBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dInvalidezVida);
			
			//CESANTIA EN EDAD AVANZADA Y VEJEZ (RETIRO)
			imssCesantiaVejezBO = new IMSSCesantiaEdadAvanzadaVejezDeduccion(this.parametros, this.diasTrabajados, this.sdi);
			dCesantiaVejez = imssCesantiaVejezBO.calcular(nomina, idx++);
			aportacionesIMSS.add(dCesantiaVejez);
			
			//RETIRO, CESANTIA EN EDAD AVANZADA Y VEJEZ (CESANTIA EN EDAD AVANZADA Y VEJEZ)
			//TODO implementar cálculo.
			
			//GUARDERIAS Y PRESTACIONES SOCIALES (EN ESPECIE)
			//TODO implementar cálculo.
			
			//INFONAVIT
			//TODO implementar cálculo.
			
			imss = dEnfermedadMaternidad.getImporte()
					.add(dGastosMedicos.getImporte())
					.add(dEnDinero.getImporte())
					.add(dInvalidezVida.getImporte())
					.add(dCesantiaVejez.getImporte())
					;
			
			imss = aportacionesIMSS.stream()
					.map(c -> c.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
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
