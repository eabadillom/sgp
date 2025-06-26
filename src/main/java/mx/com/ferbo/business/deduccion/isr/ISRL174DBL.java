package mx.com.ferbo.business.deduccion.isr;

import static mx.com.ferbo.enums.ValoresBD._30_4;
import static mx.com.ferbo.enums.ValoresBD._CERO;
import static mx.com.ferbo.enums.ValoresBD._DIAS_ANIO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class ISRL174DBL extends AbstractDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(ISRL174DBL.class);
	
	private BigDecimal         importeGravado  = null;
	private List<CatTarifaISR> tablaISRMensual = null;
	
	public ISRL174DBL(ParametrosNomina parametros, BigDecimal importeGravado) {
		this.tiposDeduccion  = parametros.getTiposDeduccion();
		this.importeGravado  = importeGravado;
		
		this.tablaISRMensual = parametros.getTablaISR().stream()
				.filter(t -> "m".equalsIgnoreCase(t.getTipo()))
				.collect(Collectors.toList())
				;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion     = null;
		CatTipoDeduccion   tipoDeduccion = null;
		BigDecimal         salarioDiario = null;
		
		//Art. 174 F. I
		BigDecimal remuneracionMensual = null;
		
		//Art. 174 F. II
		BigDecimal ingresoGravadoMensual   = null;
		BigDecimal baseISRMensualFraccion2 = null;
		
		TarifaISRBL        tarifaISRBOFraccion2 = null;
		CatTarifaISR              tarifaISRFraccion2   = null;
		ISRAntesSubsidioDBL isrFraccion2BO       = null;
		DetNominaDeduccion        dISRCausadoFraccion2 = null;
		BigDecimal                isrCausadoFraccion2  = null;
		
		
		//Art. 174 F. III
		BigDecimal isrCausadoFraccion3   = null;
		BigDecimal diferenciaISRaRetener = null;
		
		TarifaISRBL        tarifaISRBOFraccion3 = null;
		CatTarifaISR              tarifaISRFraccion3   = null;
		ISRAntesSubsidioDBL isrFraccion3BO       = null;
		DetNominaDeduccion        dISRCausadoFraccion3 = null;
		
		//Art. 174 F. IV
		BigDecimal isrCausadoFraccion4 = null;
		
		//Art. 174 F. V
		BigDecimal tasaISRFraccion5 = null;
		
		
		try {
			tipoDeduccion = this.getTipoDeduccion(TD_ISR_LEY_174);
			
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			if(this.importeGravado.compareTo(_CERO.get()) == 0)
				throw new SGPException("El importe gravado debe ser mayor a cero.");
			
			log.info("[UI] Cálculo ISR Art. 174: Remuneración gravada = {}", this.importeGravado);
			
			/*
			 * Artículo 174 Reglamento LISR, Fracción I.
			 * La remuneración de que se trate se dividirá entre 365 y el resultado se multiplicará por 30.4
			 * */
			remuneracionMensual = importeGravado
					.divide(_DIAS_ANIO.get(), 2, RoundingMode.HALF_UP)
					.multiply(_30_4.get())
					.setScale(2, RoundingMode.HALF_UP)
					;
			log.info("[UI] Fracción I - Remuneración mensual = {} / {} * {} = {}",
					importeGravado, _DIAS_ANIO.get(), _30_4.get(), remuneracionMensual);
			
			/* Artículo 174 Reglamento LISR, Fracción II.
			   A la cantidad que se obtenga conforme a la fracción anterior, se le adicionará el ingreso
			   ordinario por la prestación de un servicio personal subordinado que perciba el trabajador en
			   forma regular en el mes de que se trate y al resultado se le aplicará el procedimiento
			   establecido en el artículo 96 de la Ley;
			 * */
			ingresoGravadoMensual = salarioDiario
					.multiply(_30_4.get())
					.setScale(2, RoundingMode.HALF_UP);
			log.info("[UI] Fracción II - Ingreso gravado mensual = {} * {} = {}",
					salarioDiario, _30_4.get(),
					ingresoGravadoMensual);
			
			baseISRMensualFraccion2 = ingresoGravadoMensual
					.add(remuneracionMensual)
					.setScale(2, RoundingMode.HALF_UP);
			log.info("[UI] Fraccion II - Base ISR = {} + {} = {}",
					ingresoGravadoMensual, remuneracionMensual, baseISRMensualFraccion2);
			
			   //Cálculo mensual de ISR necesario para L174
			tarifaISRBOFraccion2 = new TarifaISRBL(this.tablaISRMensual, baseISRMensualFraccion2);
			tarifaISRFraccion2 = tarifaISRBOFraccion2.calcular();
			log.info("[UI] Fracción II - Tarifa ISR: {}", tarifaISRFraccion2);
			isrFraccion2BO = new ISRAntesSubsidioDBL(baseISRMensualFraccion2, tarifaISRFraccion2);
			dISRCausadoFraccion2 = isrFraccion2BO.calcular(nomina);
			isrCausadoFraccion2 = dISRCausadoFraccion2.getImporte();
			log.info("[UI] Fracción II - ISR: {}", isrCausadoFraccion2);
			
			/* Artículo 174 Reglamento LISR, Fracción III.
			 * El Impuesto que se obtenga conforme a la fracción anterior se disminuirá con el Impuesto que
			   correspondería al ingreso ordinario por la prestación de un servicio personal subordinado a que
			   se refiere dicha fracción, calculando este último sin considerar las demás remuneraciones
			   mencionadas en este artículo;
			 * */
			log.info("[UI] Fracción III - Base ISR: {}", ingresoGravadoMensual);
			tarifaISRBOFraccion3 = new TarifaISRBL(this.tablaISRMensual, ingresoGravadoMensual);
			tarifaISRFraccion3 = tarifaISRBOFraccion3.calcular();
			log.info("[UI] Fracción III - Tarifa ISR: {}", tarifaISRFraccion3);
			isrFraccion3BO = new ISRAntesSubsidioDBL(ingresoGravadoMensual, tarifaISRFraccion3);
			dISRCausadoFraccion3 = isrFraccion3BO.calcular(nomina);
			isrCausadoFraccion3 = dISRCausadoFraccion3.getImporte();
			log.info("[UI] Fracción III - ISR: {}", isrCausadoFraccion3);
			diferenciaISRaRetener = isrCausadoFraccion2
					.subtract(isrCausadoFraccion3)
					.setScale(2, RoundingMode.HALF_UP);
			log.info("[UI] Fraccion III - Diferencia de ISR a Retener Fraccion II vs Fraccion III = {} - {} = {}",
					isrCausadoFraccion2, isrCausadoFraccion3, diferenciaISRaRetener);
			
			/* Artículo 174 Reglamento LISR, Fraccion IV
			 * El Impuesto a retener será el que resulte de aplicar a las remuneraciones a que se refiere este
			   artículo, sin deducción alguna, la tasa a que se refiere la fracción siguiente, y
			   
			   Artículo 174 Reglamento LISR, Fraccion V
			   La tasa a que se refiere la fracción anterior, se calculará dividiendo el Impuesto que se
			   determine en términos de la fracción III de este artículo entre la cantidad que resulte conforme a
			   la fracción I de dicho artículo. El cociente se multiplicará por cien y el producto se expresará en
			   por ciento.
			   
			   Nota: Primero se aplica la fracción V y posteriormente Fraccion IV.
			 * */
			
			//Aplicando primero Fracción V:
			tasaISRFraccion5 = diferenciaISRaRetener
					.divide(remuneracionMensual, 4, RoundingMode.HALF_UP);
			log.info("[UI] Fraccion V - Tasa ISR = {} / {} = {}",
					diferenciaISRaRetener, remuneracionMensual, tasaISRFraccion5);
			
			//Aplicando después Fracción IV:
			isrCausadoFraccion4 = this.importeGravado
					.multiply(tasaISRFraccion5)
					.setScale(2, RoundingMode.HALF_UP);
			log.info("[UI] Fraccion IV - ISR = {} * {} = {}", importeGravado, tasaISRFraccion5, isrCausadoFraccion4);
			
			
		} catch(SGPException ex) {
			log.warn("Problema para generar el cálculo de ISR R. Art. 174, {}", ex.getMessage());
			isrCausadoFraccion4 = _CERO.get();
		} catch(Exception ex) {
			log.error("Problema para generar el cálculo de ISR R. Art. 174...", ex);
			isrCausadoFraccion4 = _CERO.get();
		} finally {
			deduccion = new DetNominaDeduccion.Builder()
					.nomina(nomina)
					.clave(CVE_ISR_LEY_174)
					.nombre("I.S.R. Art. 174")
					.importe(isrCausadoFraccion4)
					.tipoDeduccion(tipoDeduccion)
					.informar(true)
					.procesar(true)
					.build()
					;
		}
			
		return deduccion;
	}

}
