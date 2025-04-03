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

public class IMSSDeduccionesBL extends AbstractIMSSDBL implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(IMSSDeduccionesBL.class);
	
	private BigDecimal       diasPeriodo   = null;
	private BigDecimal       ausencias     = null;
	private BigDecimal       incapacidades = null;
	private BigDecimal       sdi           = null;
	private ParametrosNomina parametros    = null;
	
	private IMSSRiesgoTrabajoDBL             imssRiesgoTrabajoBO   = null;
	private IMSSEnfMatEnEspecieDBL           imssEnfMatBO          = null;
	private IMSSEnfMatGastosMedicosDBL       imssEnfMatGastosMedBO = null;
	private IMSSEnfMatEnDineroDBL            imssEnfMatEnDineroBO  = null;
	private IMSSInvalidezVidaDBL                      imssInvalidezVidaBO   = null;
	private IMSSCesantiaEdadAvanzadaVejezDBL imssCesantiaVejezBO   = null;
	
	public IMSSDeduccionesBL(ParametrosNomina parametros, BigDecimal diasPeriodo, BigDecimal ausencias, BigDecimal incapacidades) {
		this.parametros      = parametros;
		this.cuotasIMSS      = parametros.getCuotasIMSS();
		this.tiposDeduccion  = parametros.getTiposDeduccion();
		this.diasPeriodo     = diasPeriodo;
		this.ausencias       = ausencias;
		this.incapacidades   = incapacidades;
	}
	
	@Override
	public void procesar(DetNomina nomina) {
		List<DetNominaDeduccion> aportacionesIMSS      = null;
		DetNominaDeduccion       dRiesgoTrabajo        = null;
		DetNominaDeduccion       dEnfermedadMaternidad = null;
		DetNominaDeduccion       dGastosMedicos        = null;
		DetNominaDeduccion       dEnDinero             = null;
		DetNominaDeduccion       dInvalidezVida        = null;
		DetNominaDeduccion       dRetiro               = null;
		DetNominaDeduccion       dCesantiaVejez        = null;
		DetNominaDeduccion       dIMSS                 = null;
		CatTipoDeduccion         tdIMSS                = null;
		BigDecimal               imss                  = null;
		Integer                  index                 = null;
		
		try {
			if(nomina.getReceptor() == null || nomina.getReceptor().getSalarioDiarioIntegrado() == null)
				throw new SGPException("Debe configurar el receptor con el salario diario integrado.");
			
			this.sdi = nomina.getReceptor().getSalarioDiarioIntegrado();
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No hay una lista de tipos de deducción establecida.");
			
			aportacionesIMSS = new ArrayList<DetNominaDeduccion>();
			
			//RIESGOS DE TRABAJO
			imssRiesgoTrabajoBO = new IMSSRiesgoTrabajoDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			index = imssRiesgoTrabajoBO.nuevoIndiceDe(nomina.getDeducciones());
			//TODO Falta integrar la cuota de riesgo de la empresa.
			dRiesgoTrabajo = imssRiesgoTrabajoBO.calcular(nomina);
			nomina.getDeducciones().add(dRiesgoTrabajo);
			aportacionesIMSS.add(dRiesgoTrabajo);
			
			//ENFERMEDADES Y MATERNIDAD (EN ESPECIE)
			imssEnfMatBO = new IMSSEnfMatEnEspecieDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			dEnfermedadMaternidad = imssEnfMatBO.calcular(nomina);
			nomina.getDeducciones().add(dEnfermedadMaternidad);
			aportacionesIMSS.add(dEnfermedadMaternidad);
			
			//ENFERMEDADES Y MATERNIDAD (GASTOS MEDICOS PARA PENSIONADOS Y BENEFICIARIOS)
			imssEnfMatGastosMedBO = new IMSSEnfMatGastosMedicosDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			dGastosMedicos = imssEnfMatGastosMedBO.calcular(nomina);
			nomina.getDeducciones().add(dGastosMedicos);
			aportacionesIMSS.add(dGastosMedicos);
			
			//ENFERMEDADES Y MATERNIDAD (EN DINERO)
			imssEnfMatEnDineroBO = new IMSSEnfMatEnDineroDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			dEnDinero = imssEnfMatEnDineroBO.calcular(nomina);
			nomina.getDeducciones().add(dEnDinero);
			aportacionesIMSS.add(dEnDinero);
			
			//INVALIDEZ Y VIDA (EN ESPECIE Y EN DINERO)
			imssInvalidezVidaBO = new IMSSInvalidezVidaDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			dInvalidezVida = imssInvalidezVidaBO.calcular(nomina);
			nomina.getDeducciones().add(dInvalidezVida);
			aportacionesIMSS.add(dInvalidezVida);
			
			//GUARDERIAS Y PRESTACIONES SOCIALES (EN ESPECIE) Fund. Art. 211 LSS
			//TODO implementar cálculo.
			//Cuota = SBC x prima (Patrón 1%, Trabajador 0%) x (Dias periodo - Ausencias - Incapacidades)
			
			
			//-----------------Aportaciones bimestrales-----------------------------------
			//RETIRO, CESANTIA EN EDAD AVANZADA Y VEJEZ (CESANTIA EN EDAD AVANZADA Y VEJEZ)
			//TODO implementar cálculo.
			dRetiro = null;
			
			//CESANTIA EN EDAD AVANZADA Y VEJEZ (RETIRO)
			imssCesantiaVejezBO = new IMSSCesantiaEdadAvanzadaVejezDBL(this.parametros, this.diasPeriodo, this.ausencias, this.incapacidades, this.sdi);
			dCesantiaVejez = imssCesantiaVejezBO.calcular(nomina);
			nomina.getDeducciones().add(dCesantiaVejez);
			aportacionesIMSS.add(dCesantiaVejez);
			
			//INFONAVIT Fund. Art. 29 Fracción II LEY DEL INSTITUTO DEL FONDO NACIONAL DE LA VIVIENDA PARA LOS TRABAJADORES.
			//Descuento de ausentismos, Art. 35 REGLAMENTO DE INSCRIPCIÓN, PAGO DE APORTACIONES Y ENTERO DE DESCUENTOS AL INSTITUTO DEL FONDO NACIONAL DE LA VIVIENDA PARA LOS TRABAJADORES
			//TODO implementar cálculo.
			//Cuota = SBC x prima (Patrón 5%, Trabajador 0%) x (Dias trabajados - Ausencias)
			
			
			
			
			
			/**************SUMA DE APORTACIONES IMSS*********************/
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
			
			tdIMSS = this.getTipoDeduccion(TD_IMSS);
			dIMSS = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, index++))
					.tipoDeduccion(tdIMSS)
					.clave(CVE_IMSS)
					.nombre("I.M.S.S.")
					.importe(imss)
					.informar(true)
					.procesar(true)
					.build();
			
			nomina.getDeducciones().add(dIMSS);
			
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
