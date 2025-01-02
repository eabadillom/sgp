package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.PrestamoDeduccion;
import mx.com.ferbo.business.deduccion.imss.IMSSDeduccion;
import mx.com.ferbo.business.deduccion.isr.ISRExecutor;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.percepcion.AbstractPercepcion;
import mx.com.ferbo.business.percepcion.BonoPuntualidadPercepcion;
import mx.com.ferbo.business.percepcion.SeptimoDiaPercepcion;
import mx.com.ferbo.business.percepcion.SueldoPercepcion;
import mx.com.ferbo.business.percepcion.ValesDespensaPercepcion;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionEmpleadoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL extends NominaBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private DetEmpleado empleado = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer semanaAnio = null;
	
	//PERCEPCIONES
	private BigDecimal valesDespensa = null;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private ParametrosNomina parametros = null;
	
	private Integer anio = null;
	
	private PercepcionEmpleadoDAO percepcionEmpleadoDAO = null;
	private List<DetPercepcionEmpleado> percepcionesEmpleado = null;
	
	
	public NominaSemanalBL(DetEmpleado empleado, Date periodoInicio, Date periodoFin) {
		Integer anioActual = null;
		
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.percepcionEmpleadoDAO = new PercepcionEmpleadoDAO();
		
		anioActual = DateUtil.getAnio(periodoInicio);
		this.fechaInicioAnio = DateUtil.getDate(anioActual, DateUtil.ENERO, 1);
		DateUtil.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
		
		this.fechafinAnio = DateUtil.getDate(anioActual, DateUtil.DICIEMBRE, 31);
		DateUtil.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
		this.semanaAnio = DateUtil.getSemanaAnio(this.periodoInicio);
		
		percepcionesEmpleado = percepcionEmpleadoDAO.buscarPorEmpleado(this.empleado.getIdEmpleado());
	}
	
	public DetNomina calculoNomina() {
		BigDecimal diasTrabajados = null;
		BigDecimal salarioSemanal = null;
		BigDecimal diasPagados    = null;
		DetNomina  nomina         = null;
		
		Map<String, DetRegistro>    mapAsistencias       = null;
		List<DetNominaPercepcion>   percepciones         = null;
		List<DetNominaOtroPago>     otrosPagos           = null;
		List<DetNominaDeduccion>    deducciones          = null;
		
		try {
			log.info("#############################################################################");
			log.info("Empleado: {} {} {}, Salario diario: {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), empleado.getDatoEmpresa().getSalarioDiario());
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			nomina =  NominaBL.build(NominaBL.TP_NOMINA_ORDINARIA, this.parametros, this.empleado);
			
			percepciones = nomina.getPercepciones();
			otrosPagos = nomina.getOtrosPagos();
			deducciones = nomina.getDeducciones();
			
			mapAsistencias = NominaSemanalBL.getAsistencias(this.empleado, this.parametros);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
    		nomina.getReceptor().setSalarioDiarioIntegrado(this.calculoSDI(this.empleado));
    		
    		/*---------------------------PERCEPCIONES------------------------------*/
    		NominaSemanalBL.calcularSueldo(nomina, this.parametros, diasTrabajados);
    		NominaSemanalBL.calcularBonoPuntualidad(nomina, this.parametros, percepcionesEmpleado, mapAsistencias);
    		NominaSemanalBL.calcularValesDespensa(nomina, this.parametros, percepcionesEmpleado);
			
			/*-------------------------DEDUCCIONES-----------------------*/
    		Optional<DetNominaPercepcion> optSueldo = nomina.getPercepciones().stream().filter(p -> AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(p.getClave())).findFirst();
    		salarioSemanal = optSueldo.isPresent() ? optSueldo.get().getImporteExcento().add(optSueldo.get().getImporteGravado()) : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    		
			if(salarioSemanal.compareTo(BigDecimal.ZERO) > 0) {
				
				NominaSemanalBL.procesarISR(nomina, this.periodoInicio, this.periodoFin, this.parametros);
				NominaSemanalBL.procesarIMSS(nomina, this.parametros);
				NominaSemanalBL.procesarPrestamos(nomina, this.parametros, this.empleado);
			}
			
			/*--------------------------RESUMEN--------------------------*/
			for(DetNominaPercepcion p : percepciones) {
				log.info("Percepcion: {} - {} - {}", p.getNombre(), p.getImporteExcento(), p.getImporteGravado());
			}
			
			for(DetNominaOtroPago o : otrosPagos) {
				log.info("Otro pago: {} - {}", o.getNombre(), o.getImporte());
			}
			
			for(DetNominaDeduccion d : deducciones) {
				log.info("Deduccion: {} - {}", d.getNombre(), d.getImporte());
			}
			
			NominaSemanalBL.calcularTotales(nomina, this.parametros);
			
			diasPagados = nomina.getPercepciones().stream()
				.filter(p -> AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(p.getClave()) || AbstractPercepcion.CVE_SEPTIMO_DIA.equalsIgnoreCase(p.getClave()))
				.map(p -> p.getCantidad())
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
				;
			
			nomina.setFechaEmision(new Date());
			nomina.setClaveExportacion("01");
			nomina.setTipoComprobante("N");
			nomina.setMoneda("MXN");
			nomina.setMetodoPago(this.parametros.getMetodoPago());
			nomina.setSerie(String.format("%d", this.anio));
			nomina.setFolio(String.format("%d", this.semanaAnio));
			nomina.setLugarExpedicion(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			nomina.setEjercicio(DateUtil.getAnio(this.fechaInicioAnio));
			nomina.setDiasLaborados(diasTrabajados.intValue());
			nomina.setDiasPagados(diasPagados);
			//TODO Revisar los días no laborados.
//			nomina.setDiasNoLaborados(diasLaboralesPeriodo.subtract(diasTrabajados).intValue());
			nomina.setPeriodo(this.semanaAnio);
			nomina.setPeriodoInicio(this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setPeriodoFin(this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			
			
			DetNominaEmisor emisor = getEmisor(nomina, this.empleado.getDatoEmpresa().getEmpresa());
			log.info("Emisor: {}", emisor.getNombre());
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		} finally {
			log.info("-----------------------------------------------------------------------------");
		}
		
		return nomina;
	}
	
	/**Cálculo de sueldo semanal (6 días de trabajo + Septimo día)
	 * @param nomina
	 * @param parametros
	 * @param diasTrabajados
	 */
	public static synchronized void calcularSueldo(DetNomina nomina, ParametrosNomina parametros, BigDecimal diasTrabajados) {
		BigDecimal           diasLaboralesPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
		SueldoPercepcion     sueldoBO = null;
		SeptimoDiaPercepcion septimoDiaBO = null;
		
		sueldoBO = new SueldoPercepcion(parametros.getTiposPercepcion(), diasTrabajados);
		sueldoBO.calcular(nomina);
		
		//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
		septimoDiaBO = new SeptimoDiaPercepcion(parametros.getTiposPercepcion(), diasLaboralesPeriodo, diasTrabajados);
		septimoDiaBO.calcular(nomina);
		
		
	}
	
	private static void calcularBonoPuntualidad(DetNomina nomina, ParametrosNomina parametros, List<DetPercepcionEmpleado> percepcionesEmpleado, Map<String, DetRegistro> mapAsistencias) {
		DetNominaPercepcion       pBonoPuntualidad       = null;
		BonoPuntualidadPercepcion bonoPuntualidadBO      = null;
		BigDecimal                tasaBonoPuntualidad    = null;
		BigDecimal                salarioDiarioIntegrado = null;
		BigDecimal                diasTrabajados         = null;
		BigDecimal                proporcionalSeptimoDia = null;
		List<DetNominaPercepcion> percepciones           = null;
		
		percepciones = nomina.getPercepciones();
		salarioDiarioIntegrado = nomina.getReceptor().getSalarioDiarioIntegrado();
		
		Optional<DetPercepcionEmpleado> optPercepcion010 = percepcionesEmpleado.stream()
				.filter(p -> p.getTipoPercepcion().getClave().equalsIgnoreCase("010"))
				.findFirst()
				;
		
		if(optPercepcion010.isPresent() && optPercepcion010.get().getActivo() == false) {
			return;
		}
		
		Optional<DetNominaPercepcion> optSueldo = percepciones.stream()
				.filter(p -> AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(p.getClave()))
				.findFirst()
				;
		
		diasTrabajados = optSueldo.isPresent() ? optSueldo.get().getCantidad() : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		Optional<DetNominaPercepcion> optSeptimoDia = percepciones.stream()
				.filter(p -> AbstractPercepcion.CVE_SEPTIMO_DIA.equalsIgnoreCase(p.getClave()))
				.findFirst()
				;
		
		proporcionalSeptimoDia = optSeptimoDia.isPresent() ? optSeptimoDia.get().getCantidad() : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		tasaBonoPuntualidad = parametros.getParametrosPercepciones().getBonoPuntualidad();
		bonoPuntualidadBO = new BonoPuntualidadPercepcion(parametros.getTiposPercepcion(), tasaBonoPuntualidad, diasTrabajados, mapAsistencias, DIAS_POR_PERIODO, salarioDiarioIntegrado, proporcionalSeptimoDia);
		bonoPuntualidadBO.setPercepcionesEmpleado(percepcionesEmpleado);
		pBonoPuntualidad = bonoPuntualidadBO.calcular(nomina);
		if(pBonoPuntualidad.getImporteExcento().add(pBonoPuntualidad.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0) //Si el hay bono de puntualidad, se agrega a la lista de percepciones.
			percepciones.add(pBonoPuntualidad);
		
	}
	
	/**Preparación para el cálculo de vales de despensa. Depende de Sueldo y Septimo Día. Ambos se debe encontrar en el parámetro "nomina"
	 * @param nomina Objeto nómina, contiene la información y cálculo de nómina del empleado.
	 * @param parametros Parametros de nómina (Tipos de percepciones, deducciones, etc).
	 * @param percepcionesEmpleado Lista de percepciones que se aplicarán al empleado y las restricciones que están configuradas desde el registro de empleados.
	 */
	private static void calcularValesDespensa(DetNomina nomina, ParametrosNomina parametros, List<DetPercepcionEmpleado> percepcionesEmpleado) {
		DetNominaPercepcion pValeDespensa = null;
		ValesDespensaPercepcion   valesDespensaBO = null;
		List<DetNominaPercepcion> percepciones = null;
		BigDecimal                diasTrabajados = null;
		BigDecimal                diasPeriodo = null;
		
		diasPeriodo = new BigDecimal(DateUtil.daysDiff(parametros.getPeriodoInicio(), parametros.getPeriodoFin())).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		percepciones = nomina.getPercepciones();
		
		Optional<DetPercepcionEmpleado> optPercepcion029 = percepcionesEmpleado.stream
				().filter(p -> p.getTipoPercepcion().getClave().equalsIgnoreCase("029"))
				.findFirst()
				;
		
		if(optPercepcion029.isPresent() && optPercepcion029.get().getActivo() == false) {
			return;
		}
		
		Optional<DetNominaPercepcion> optSueldo = percepciones.stream()
				.filter(p -> AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(p.getClave()))
				.findFirst()
				;
		
		diasTrabajados = optSueldo.isPresent() ? optSueldo.get().getCantidad() : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		valesDespensaBO = new ValesDespensaPercepcion(parametros.getTiposPercepcion(), diasTrabajados, parametros.getUma().getImporteDiario(), parametros.getParametrosPercepciones().getValeDespensa(), diasPeriodo);
		valesDespensaBO.setPercepcionesEmpleado(percepcionesEmpleado);
		pValeDespensa = valesDespensaBO.calcular(nomina);
		
		if(pValeDespensa.getImporteExcento().add(pValeDespensa.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0)
			percepciones.add(pValeDespensa);
		
	}
	
	public static synchronized Boolean esUltimaSemanaMes(Date periodoInicio, Date periodoFin) {
		Boolean ultimaSemanaMes = null;
		Integer mesActualInicio = null;
		Integer mesActualFin = null;
		Date periodoSiguienteInicio = null;
		Date periodoSiguienteFin;
		Integer mesSiguienteInicio = null;
		Integer mesSiguienteFin = null;
		Integer diaInicioMes = null;
		
		try {
			//PRIMERO SE VERIFICA SI EL INICIO DE LA SEMANA COINCIDE CON EL PRIMER DIA DEL MES. 
			diaInicioMes = DateUtil.getDia(periodoInicio);
			
			if(diaInicioMes.compareTo(new Integer(1)) == 0)
				throw new SGPException("La semana en curso es la primera del mes.");
			
			//SI NO, SE VERIFICA SI EL MES DEL PRIMER DIA DE LA SEMANA EN CURSO ES DIFERENTE AL MES DEL ÚLTIMO DIA DE LA SEMANA EN CURSO.
			//SI EL PRIMER DIA DEL PERIODO ES DE UN MES DIFERENTE AL ÚLTIMO DIA DEL PERIODO, ENTONCES, LA SEMANA NO ES LA ÚLTIMA.
			mesActualInicio = DateUtil.getMes(periodoInicio);
			mesActualFin = DateUtil.getMes(periodoFin);
			
			if(mesActualInicio.compareTo(mesActualFin) != 0)
				throw new SGPException("La semana actual termina en un mes diferente al inicial.");
			
			//SI NO, CALCULAMOS LAS FECHAS DE INICIO Y FIN DE LA SIGUIENTE SEMANA Y REPETIMOS LA EVALUACIÓN ANTERIOR. 
			periodoSiguienteInicio = DateUtil.addDay(periodoInicio, 7);
			periodoSiguienteFin = DateUtil.addDay(periodoFin, 7);
			
			mesSiguienteInicio = DateUtil.getMes(periodoSiguienteInicio);
			mesSiguienteFin = DateUtil.getMes(periodoSiguienteFin);
			
			if(mesSiguienteInicio.compareTo(mesSiguienteFin) != 0) {
				ultimaSemanaMes = new Boolean(true);
				log.info("ULTIMA SEMANA DEL MES: {} - {}", periodoInicio, periodoFin);
			} else {
				ultimaSemanaMes = new Boolean(false);
			}
			
		} catch(Exception ex) {
			ultimaSemanaMes = new Boolean(false);
		}
		
		return ultimaSemanaMes;
	}
	
	public static List<DetNomina> procesaNominaDelMes(Date periodoInicio, String rfc) {
		List<DetNomina> listaNominaDelMes = null;
		Date dPeriodoInicio = new Date(periodoInicio.getTime());
		Date dPeriodoAnteriorFin = null;
		Date dPeriodoAnteriorInicio = null;
		
		Integer mesActual = null;
		Integer mesAnterior = null;
		
		Integer semanaInicio = null;
		Integer semanaFin = null;
		
		NominaDAO nominaDAO = new NominaDAO();
		
		//Obtener la fecha inicio de la primera semana del mes
		for(int i = 0; i < 6; i++) {
			dPeriodoAnteriorInicio = DateUtil.addDay(dPeriodoInicio, (-7 * i));
			
			if(DateUtil.getDia(dPeriodoAnteriorInicio) == 1)
				break;
			
			mesActual = DateUtil.getMes(dPeriodoInicio);
			mesAnterior = DateUtil.getMes(dPeriodoAnteriorInicio);
			
			if(mesAnterior.equals(mesActual) == false)
				break;
		}
		
		dPeriodoAnteriorFin = DateUtil.addDay(dPeriodoInicio, -1);
		
		try {
			log.info("Buscando pagos semanales de nómina del {} al {}", 
					DateUtil.getString(dPeriodoAnteriorInicio, DateUtil.FORMATO_DD_MM_YYYY),
					DateUtil.getString(dPeriodoAnteriorFin, DateUtil.FORMATO_DD_MM_YYYY));
		} catch (SGPException e) {
			log.warn(e.getMessage());
		}
		
		semanaInicio = DateUtil.getSemanaAnio(dPeriodoAnteriorInicio);
		semanaFin = DateUtil.getSemanaAnio(dPeriodoAnteriorFin);
		
		log.info("Búsqueda de la semana {} a {}", semanaInicio, semanaFin);
		
		listaNominaDelMes = nominaDAO.buscarPorSemanaRfc(semanaInicio, semanaFin, rfc);
		
		return listaNominaDelMes;
	}
	
	/**Con base en las asistencias del trabajador, se determina cuantos días se presentó a laborar.
	 * @param mapAsistencias Registro de asistencias del trabajador
	 * @param diasPorSemana Máximo de días por periodo que un trabajador puede laborar (según contrato).<br>
	 * Por ejemplo, un trabajador, por contrato, puede trabajar 6 días de la semana.
	 * @return
	 */
	private BigDecimal getDiasTrabajados(Map<String, DetRegistro> mapAsistencias, int diasPorSemana) {
		BigDecimal diasTrabajados = null;
		Integer diasRegistrados = null;
		DetRegistro registro = null;
		
		diasRegistrados = mapAsistencias.size();
		
		if(diasRegistrados.compareTo(diasPorSemana) == 0) {
			return new BigDecimal(diasRegistrados).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		Set<String> keySet = mapAsistencias.keySet();
		
		for(String key : keySet) {
			registro = mapAsistencias.get(key);
			log.info("Buscando día de la semana Key en el catálogo de días no laborales: {}, {}...", key, registro.getFechaEntrada());
		}
		diasTrabajados = new BigDecimal(mapAsistencias.size());
		
		return diasTrabajados;
	}
	
	public static Map<String, DetRegistro> getAsistencias(DetEmpleado empleado, ParametrosNomina parametros) {
		Map<String, DetRegistro> mapAsistencias = null;
		List<DetRegistro> listaAsistencias = null;
		RegistroDAO registroDAO = null;
		String diaSemana = null;
		Date diaNLEntrada = null;
		Date diaNLSalida = null;
		
		mapAsistencias = new HashMap<String, DetRegistro>();
		registroDAO = new RegistroDAO();
		listaAsistencias = registroDAO.buscar(empleado.getIdEmpleado(), parametros.getPeriodoInicio(), parametros.getPeriodoFin());
		for(DetRegistro registro : listaAsistencias) {
			diaSemana = DateUtil.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		for(CatDiaNoLaboral dia : parametros.getDiasNoLaborales()) {
			log.info("Dia no laboral encontrado: {}", dia);
			diaSemana = DateUtil.getDiaSemana(dia.getFecha());
			if(mapAsistencias.containsKey(diaSemana)) {
				//TODO EL TRABAJADOR TIENE ASISTENCIA EN UN DIA NO LABORABLE. SE DEBEN AGREGAR HORAS EXTRAS AL DOBLE O TRIPLE.
				continue;
			}
			
			diaNLEntrada = new Date(dia.getFecha().getTime());
			diaNLSalida = new Date(dia.getFecha().getTime());
			
			mapAsistencias.put(diaSemana, new DetRegistro(null, diaNLEntrada, diaNLSalida, -1, "DIA NO LABORAL"));
		}
		
		log.info("Mapa de asistencias: {}", mapAsistencias);
		
		return mapAsistencias;
	}

	private BigDecimal calculoSDI(DetEmpleado empleado) {
		BigDecimal sdi = null;
    	BigDecimal diasAguinaldo = null;
    	BigDecimal diasVacaciones = null;
    	BigDecimal primaVacacional = null;
    	BigDecimal diasAnio = null;
    	BigDecimal sueldoDiario = null;
    	
    	BigDecimal factorSDI = null;
    	
    	try {
    		diasAnio = new BigDecimal(DIAS_ANIO).setScale(2, BigDecimal.ROUND_HALF_UP);
    		diasAguinaldo = new BigDecimal(this.parametros.getParametrosPercepciones().getDiasAguinaldo().intValue());
    		
    		//TODO Dias de vacaciones debe ser un dato calculado, conforme a la ley federal del trabajo
    		diasVacaciones = new BigDecimal(this.parametros.getParametrosPercepciones().getDiasVacaciones().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		primaVacacional = new BigDecimal(this.parametros.getParametrosPercepciones().getPrimaVacacional().floatValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		sueldoDiario = empleado.getDatoEmpresa().getSalarioDiario();
    		
    		factorSDI = primaVacacional
    				.multiply(diasVacaciones).setScale(4, BigDecimal.ROUND_HALF_UP)
    				.add(diasAguinaldo)
    				.add(diasAnio)
    				.divide(diasAnio, 4, BigDecimal.ROUND_HALF_UP)
    				;
    		
    		sdi = sueldoDiario.multiply(factorSDI).setScale(2, BigDecimal.ROUND_HALF_UP);
    	} catch(Exception ex) {
    		sdi = BigDecimal.ZERO;
    	}
    	
        return sdi;
	}
	
	public static synchronized void procesarISR(DetNomina nomina, Date periodoInicio, Date periodoFin, ParametrosNomina parametros) {
		Boolean esUltimaSemanaMes = false;
		List<DetNomina> nominaMensual = null;
		
		esUltimaSemanaMes = NominaSemanalBL.esUltimaSemanaMes(periodoInicio, periodoFin);
		if(esUltimaSemanaMes)
			nominaMensual = NominaSemanalBL.procesaNominaDelMes(periodoInicio, nomina.getReceptor().getRfc());
		
		log.info("Procesando cálculo de ISR...");
		//Primero se debe buscar en "nomina" si ya existen registros de ISR y Subsidio al salario y eliminarlos.
		List<DetNominaDeduccion> deducciones = nomina.getDeducciones();
		boolean removedDeducciones = deducciones.removeIf(d -> AbstractDeduccion.D_ISR.equalsIgnoreCase(d.getTipoDeduccion().getClave()));
		if(removedDeducciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de ISR", AbstractDeduccion.D_ISR);
		
		List<DetNominaOtroPago> otrosPagos = nomina.getOtrosPagos();
		boolean removedOtrosPagos = otrosPagos.removeIf(o -> AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO.equalsIgnoreCase(o.getTipoOtroPago().getClave()));
		if(removedOtrosPagos)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de Subsidio al empleo.", AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO);
		
		ISRExecutor isrExecutor = new ISRExecutor(periodoInicio, periodoFin, parametros.getTiposDeduccion(), parametros.getTiposOtroPago(), parametros.getTablaISR(), nominaMensual);
		IDeducciones isrBO = isrExecutor.loadClass("ISRS", DateUtil.toLocalDate(periodoFin));
		isrBO.procesar(nomina);
	}
	
	private static void procesarIMSS(DetNomina nomina, ParametrosNomina parametros) {
		IMSSDeduccion imssBO = null;
		imssBO = new IMSSDeduccion(parametros, new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA));
		imssBO.procesar(nomina);
	}
	
	private static void procesarPrestamos(DetNomina nomina, ParametrosNomina parametros, DetEmpleado empleado) {
		PrestamoDeduccion prestamosBO = null;
		prestamosBO = new PrestamoDeduccion(empleado);
		prestamosBO.setFecha(parametros.getPeriodoFin());
		prestamosBO.procesar(nomina);
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public BigDecimal getValesDespensa() {
		return valesDespensa;
	}

	public void setValesDespensa(BigDecimal valesDespensa) {
		this.valesDespensa = valesDespensa;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public void setParametros(ParametrosNomina parametros) {
		this.parametros = parametros;
	}
}
