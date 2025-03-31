package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;

public class ISRL174Deduccion extends AbstractDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(ISRL174Deduccion.class);
	private ParametrosNomina parametros = null;
	private BigDecimal importeExento = null;
	private BigDecimal importeGravado = null;
	private List<CatTarifaISR> tablaISRMensual = null;
	
	public ISRL174Deduccion(ParametrosNomina parametros, BigDecimal importeGravado) {
		tablaISRMensual = parametros.getTablaISR().stream()
				.filter(p -> "m".equalsIgnoreCase(p.getTipo()))
				.collect(Collectors.toList())
				;
	}

	@Override
	public void procesar(DetNomina nomina) {
		DetNominaDeduccion deduccion = null;
		BigDecimal salarioDiario = null;
		
		//Art. 174 F. I
		BigDecimal remuneracionMensual = null;
		
		//Art. 174 F. II
		BigDecimal ingresoGravadoMensual = null;
		BigDecimal baseISRMensualTotal = null;
		TarifaISRDeduccion tarifaISRBO = null;
		CatTarifaISR tarifaISR = null;
		ISRAntesSubsidioDeduccion isrPreSubsidioMensualBO = null;
		DetNominaDeduccion dISRCausadoMensualTotal = null;
		BigDecimal isrCausadoMensualTotal = null;
		
		//Art. 174 F. III
		BigDecimal baseISRMensual = null;
		DetNominaDeduccion dISRCausadoMensual = null;
		
		
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			/*
			 * Artículo 174 Reglamento LISR, Fracción I.
			 * La remuneración de que se trate se dividirá entre 365 y el resultado se multiplicará por 30.4
			 * */
			remuneracionMensual = importeGravado
					.divide(ValoresBD._DIAS_ANIO.getValor(), 2, BigDecimal.ROUND_HALF_UP)
					.multiply(ValoresBD._30_4.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			/* Artículo 174 Reglamento LISR, Fracción II.
			   A la cantidad que se obtenga conforme a la fracción anterior, se le adicionará el ingreso
			   ordinario por la prestación de un servicio personal subordinado que perciba el trabajador en
			   forma regular en el mes de que se trate y al resultado se le aplicará el procedimiento
			   establecido en el artículo 96 de la Ley;
			 * */
			ingresoGravadoMensual = salarioDiario
					.multiply(ValoresBD._30_4.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			baseISRMensualTotal = ingresoGravadoMensual
					.add(remuneracionMensual).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			   //Cálculo mensual de ISR necesario para L174
			tarifaISRBO = new TarifaISRDeduccion(parametros.getTablaISR(), baseISRMensualTotal);
			tarifaISR = tarifaISRBO.calcular();
			isrPreSubsidioMensualBO = new ISRAntesSubsidioDeduccion(baseISRMensualTotal, tarifaISR);
			dISRCausadoMensualTotal = isrPreSubsidioMensualBO.calcular(nomina, idx++);
			isrCausadoMensualTotal = dISRCausadoMensualTotal.getImporte();
			
			/* Artículo 174 Regalmento LISR, Fracción III.
			 * El Impuesto que se obtenga conforme a la fracción anterior se disminuirá con el Impuesto que
			   correspondería al ingreso ordinario por la prestación de un servicio personal subordinado a que
			   se refiere dicha fracción, calculando este último sin considerar las demás remuneraciones
			   mencionadas en este artículo;
			 * */
			
			
		} catch(Exception ex) {
			
		}
			
		
		
		
		
		
		
		
		
	}

}
