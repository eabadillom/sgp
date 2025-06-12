package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

/**
 * 
 */
public abstract class AbstractPBL {
	private static Logger log = LogManager.getLogger(AbstractPBL.class);
	
	protected ParametrosNomina parametros = null;
	protected DetNomina nomina = null;
	protected List<CatTipoPercepcion> tiposPercepcion = null;
	protected List<DetPercepcionEmpleado> percepcionesEmpleado = null;
	
	protected BigDecimal cantidad = null;
	protected BigDecimal baseCalculo = null;
	protected BigDecimal limiteExento = null;
	protected BigDecimal importe = null;
	protected BigDecimal importeExento = null;
	protected BigDecimal importeGravado = null;
	
	
	public static final String P_SUELDO           = "001";
	public static final String P_SEXTO_DIA        = "001";
	public static final String P_SEPTIMO_DIA      = "001";
	public static final String P_BONO_PUNTUALIDAD = "010";
	public static final String P_VALES_DESPENSA   = "029";
	
	//Con base en la tabla cat_percepcion
	public static final String CVE_SUELDO                      = "001";
	public static final String CVE_SEXTO_DIA                   = "002";
	public static final String CVE_SEPTIMO_DIA                 = "003";
	public static final String CVE_BONO_PUNTUALIDAD            = "015";
	public static final String CVE_PRIMA_VACACIONES_EN_TIEMPO  = "020";
	public static final String CVE_VACACIONES_REPORTADAS       = "021";
	public static final String CVE_PRIMA_VACACIONES_REPORTADAS = "022";
	public static final String CVE_AGUINALDO                   = "024";
	public static final String CVE_VACACIONES_EN_TIEMPO        = "029";
	public static final String CVE_VALES_DESPENSA              = "032";
	public static final String CVE_PTU                         = "100";
	
	public AbstractPBL(ParametrosNomina parametros, DetNomina nomina) {
		this.parametros = parametros;
		log.info("Parametros nomina: {}", parametros);
		this.nomina = nomina;
		log.info("Nomina: {}", nomina);
		
		this.cantidad       = _CERO.get();
		this.importe        = _CERO.get();
		this.importeExento  = _CERO.get();
		this.importeGravado = _CERO.get();
		
		this.tiposPercepcion = parametros.getTiposPercepcion();
	}
	
	/** Cada percepción debe calcularse con base en una "cantidad" multiplicada por
	 * una base de cálculo. Por ejemplo, en el sueldo semanal, la cantidad son los
	 * días laborados por el empleado, mientras que la base de cálculo es el salario
	 * diario.<br><br>
	 * Por ejemplo, el salario semanal se calcula como:<br><br>
	 * salarioDiario = cantidad x baseCalculo = 5 dias x $300.00<br><br>
	 * En otros casos, la "cantidad" lleva otros cálculos, como en el caso de el
	 * el aguinaldo, el cual depende de:<br><br>
	 * Los días laborados del empleado desde su ingreso hasta el último día del año
	 * en curso (o bien, los 365 días del año cuando su ingreso fue en años anteriores).<br><br>
	 * Las ausencias<br><br>
	 * Los días de aguinaldo que el patrón otorga al empleado.
	 * <br><br>
	 * De tal manera que la cantidad depende siempre de otros factores que dependen
	 * completamente de la percepción que se está analizando. 
	 * @param nomina
	 * @return
	 */
	protected abstract BigDecimal calcularCantidad(DetNomina nomina) throws SGPException;
	
	/**Las percepciones en general se deberían calcular a partir de una "cantidad" y una
	 * base de cargo. Por ejemplo, El salario se calcula a partir de los días trabajados
	 * (cantidad) y el salario diario (base de cargo).<br>
	 * Para otras percepciones (p. e. Aguinaldo), la cantidad se obtiene a partir de otros
	 * datos (como los días de aguinaldo, las ausencias en el año, etc), mientras que la
	 * base de cargo puede tener otro dato como origen (p. e. Para el bono de puntualidad,
	 * la base de cargo es el salario diario integrado).
	 * @param cantidad De acuerdo con el anexo 1.2 de Nómina (CFDI 4.0) se indica la cantidad
	 * a multiplicar por la base de cargo.
	 * @param baseCalculo Base de cargo a multiplicar por la cantidad.
	 * @return Importe.
	 */
	public BigDecimal calcularImporte(BigDecimal cantidad, BigDecimal baseCalculo) {
		BigDecimal importe = null;
		importe = cantidad.multiply(baseCalculo).setScale(2, RoundingMode.HALF_UP);
		log.info("[UI] Importe = ({} * {})", cantidad, baseCalculo);
		return importe;
	}
	
	/**Todas las percepciones deben tener un límite de importe exento y gravado.
	 * En el caso de las percepciones gravadas al 100% (p. e. Sueldo, Aguinaldo, etc)
	 * El limite de exento debe ser 0.00. En el caso de otras percepciones, el
	 * límite de exento varía de acuerdo a lo publicado por la LISR vigente.<br>
	 * Cada clase que herede de AbstractPBL debe implementar el cálculo del
	 * límite exento.
	 * @return
	 */
	protected abstract BigDecimal calcularLimiteExento();
	
	/**A partir del límite de exención para la percepción, se calcula el importe exento
	 * y el importe gravado. Todos los datos se obtienen de los atributos internos
	 * "importe" y "cantidad", que debieron ser previamente calculados.
	 */
	public void calcularExentoGravado() {
		limiteExento = this.calcularLimiteExento();
		
		if(importe.compareTo(limiteExento) > 0) {
			//El importe NO está exento de ISR.
			importeGravado = importe.subtract(limiteExento);
			importeExento = limiteExento.setScale(2, RoundingMode.HALF_UP);
		} else {
			//El importe SI está exento de ISR.
			importeGravado = _CERO.get();
			importeExento = importe.setScale(2, RoundingMode.HALF_UP);
		}
	}
	
	/**Este método debe implementar todos los pasos para realizar el cálculo de una
	 * percepción:<br>
	 * 1. Obtener el calculo del concepto "Cantidad" y los sub pasos que implica dicho cálculo.<br>
	 * 2. Obtener el cálculo del concepto "Importe".<br>
	 * 3. Obtener el cálculo de los conceptos "Importe exento" e "Importe gravado".<br>
	 * 4. Generar el objeto DetNominaPercepcion.<br>
	 * <br>
	 * <br>
	 * Depende de la implmenetación de:<br>
	 * - BigDecimal calcularCantidad(DetNomina);<br>
	 * - BigDecimal calcularImporte(DetNomina);<br>
	 * - BigDecimal calcularLimiteExento();<br>
	 * - void calcularExentoGravado();<br>
	 * @param nomina
	 * @return
	 */
	public abstract DetNominaPercepcion procesar(DetNomina nomina) throws SGPException;
	
	
	/**Este método debe implementar sólo el reproceso del cálculo de una percepción, asumiento
	 * que el concepto "Cantidad" es diferente al que se obtiene de manera automática por la
	 * implementación de la percepción {@link #procesar(DetNomina)}
	 * @param nomina
	 * @param cantidad
	 * @return
	 */
	public abstract DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) throws SGPException;
	
	public DetNominaPercepcion build(DetNomina nomina, String clave) {
		DetNominaPercepcion percepcion = null;
		PercepcionDAO percepcionDAO = null;
		CatPercepcion catPercepcion = null;
		Integer index = null;
		
		percepcionDAO = new PercepcionDAO();
		catPercepcion = percepcionDAO.buscarPorId(clave);
		
		index = this.nuevoIndiceDe(nomina.getPercepciones());
		
		percepcion = new DetNominaPercepcion();
		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
		percepcion.setClave(clave);
		percepcion.setNombre(catPercepcion.getNombre());
		percepcion.setTipoPercepcion(catPercepcion.getTipoPercepcion());
		
		return percepcion;
	}
	
	public DetNominaPercepcion build(DetNomina nomina, String clave, BigDecimal cantidad, BigDecimal importeExento, BigDecimal importeGravado) {
		DetNominaPercepcion percepcion = null;
		
		percepcion = this.build(nomina, clave);
		percepcion.setCantidad(cantidad);
		percepcion.setImporteExento(importeExento);
		percepcion.setImporteGravado(importeGravado);
		
		return percepcion;
	}
	
	public CatTipoPercepcion getTipoPercepcion(String clave) {
		CatTipoPercepcion tipoPercepcion = null;
		List<CatTipoPercepcion> collect = null;
		
		try {
			collect = this.tiposPercepcion.stream()
					.filter(t -> clave.equals(t.getClave()))
					.collect(Collectors.toList())
					;
			
			if(collect.size() > 0)
				tipoPercepcion = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de percepción: " + clave, ex);
		}
		
		return tipoPercepcion;
	}
	
	
	public DetPercepcionEmpleado buscaPercepcionEmpleado(String clave) {
		DetPercepcionEmpleado percepcionEmpleado = null;
		List<DetPercepcionEmpleado> collect = null;
		
		try {
			collect = this.percepcionesEmpleado.stream()
					.filter(p -> clave.equals(p.getPercepcion().getTipoPercepcion().getClave()))
					.collect(Collectors.toList())
					;
			
			if(collect.size() > 0)
				percepcionEmpleado = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar la percepción predeterminada para el empleado: " + clave, ex);
		}
		
		return percepcionEmpleado;
	}
	
	public Integer nuevoIndiceDe(List<DetNominaPercepcion> percepciones) {
		Integer maxIndex = null;
		DetNominaPercepcion maxP = null;
		
		try {
			maxP = Collections.max(percepciones, Comparator.comparing(p -> p.getKey().getId()));
			
			if(maxP.getKey().getId() == null)
				throw new SGPException("Existen elementos de \"Percepciones\" que no tienen asignado un consecutivo");
			
			maxIndex = maxP.getKey().getId() + 1;
			
		} catch(Exception ex) {
			maxIndex = 0;
		}
		
		return maxIndex;
	}
	
	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}


	public void setPercepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
		this.percepcionesEmpleado = percepcionesEmpleado;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getLimiteExento() {
		return limiteExento;
	}

	public void setLimiteExento(BigDecimal limiteExento) {
		this.limiteExento = limiteExento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getImporteExento() {
		return importeExento;
	}

	public void setImporteExento(BigDecimal importeExento) {
		this.importeExento = importeExento;
	}

	public BigDecimal getImporteGravado() {
		return importeGravado;
	}

	public void setImporteGravado(BigDecimal importeGravado) {
		this.importeGravado = importeGravado;
	}

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}
}
