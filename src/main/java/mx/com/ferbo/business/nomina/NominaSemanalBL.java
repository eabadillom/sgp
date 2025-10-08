package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.PrestamoDBL;
import mx.com.ferbo.business.deduccion.imss.IMSSDeduccionesBL;
import mx.com.ferbo.business.deduccion.isr.ISRExecutor;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.percepcion.AguinaldoPBL;
import mx.com.ferbo.business.percepcion.BonoPuntualidadPBL;
import mx.com.ferbo.business.percepcion.PercepcionBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalEnTiempoPBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalReportadasPBL;
import mx.com.ferbo.business.percepcion.SeptimoDiaPBL;
import mx.com.ferbo.business.percepcion.SueldoPBL;
import mx.com.ferbo.business.percepcion.VacacionesPBL;
import mx.com.ferbo.business.percepcion.VacacionesReportadasPBL;
import mx.com.ferbo.business.percepcion.ValesDespensaPBL;
import mx.com.ferbo.business.registro.EstatusRegistroBL;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionEmpleadoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaIncidencia;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL extends NominaBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_LABORALES_POR_PERIODO = 6;
    
	private Integer anio = null;
	private PercepcionEmpleadoDAO       percepcionEmpleadoDAO = null;
	private List<DetPercepcionEmpleado> percepcionesEmpleado = null;
	private List<String> listaDiasLaboralesEmpleado = null;
	private List<String> listaDiasNoLaboralesEmpleado = null;
	
	public NominaSemanalBL(DetEmpleado empleado, ParametrosNomina parametros, Map<String, DetRegistro> mapAsistencias) {
		super(empleado, parametros, mapAsistencias);
		this.percepcionEmpleadoDAO = new PercepcionEmpleadoDAO();
		
		this.periodoInicio   = parametros.getPeriodoInicio();
		this.periodoFin      = parametros.getPeriodoFin();
		this.anio            = parametros.getAnio();
		this.fechaInicioAnio = parametros.getFechaInicioAnio();
		this.fechafinAnio    = parametros.getFechaFinAnio();
		
		this.percepcionesEmpleado = this.percepcionEmpleadoDAO.buscarPorEmpleado(this.empleado.getIdEmpleado());
		
		DateUtil.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
		DateUtil.setTime(this.fechafinAnio, 23, 59, 59, 000);
	}
	
	public DetNomina calcular() {
		BigDecimal diasPeriodo             = null;
		BigDecimal diasLaboralesEmpleado   = null;
		BigDecimal diasNolaboralesEmpleado = null;
		BigDecimal diasTrabajados          = null;
		BigDecimal diasVacaciones          = null;
		BigDecimal ausencias               = null;
		BigDecimal incapacidades           = null;
		BigDecimal diasPagados             = null;
		DetNomina  nomina                  = null;
		
		List<DetNominaPercepcion>   percepciones         = null;
		List<DetNominaOtroPago>     otrosPagos           = null;
		List<DetNominaDeduccion>    deducciones          = null;
		List<DetNominaIncidencia>  incidencias          = null;
		
		try {
			//DIAS TOTALES DEL PERIODO = 7
			diasPeriodo = new BigDecimal(DIAS_LABORALES_POR_PERIODO + SEPTIMO_DIA).setScale(2, RoundingMode.HALF_UP);
			
			log.info("#############################################################################");
			log.info("Empleado: {} {} {}, Salario diario: {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), empleado.getDatoEmpresa().getSalarioDiario());
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.parametros.getPeriodo());
			nomina =  NominaSemanalBL.build(TP_NOMINA_ORDINARIA, this.parametros, this.empleado);
			percepciones = nomina.getPercepciones();
			otrosPagos = nomina.getOtrosPagos();
			deducciones = nomina.getDeducciones();
			
			//De la configuración del empleado, se obtienen los días de la semana que debe presentarse a laborar
			//y los que debe descansar.
			//Por ejemplo, una semana laboral de Lunes a Viernes, con Sábado y Domingo de descanso,
			//o bien, una semana laboral de Lunes a Sábado, con Domingo de descanso.
			listaDiasLaboralesEmpleado = NominaSemanalBL.getDiasLaboralesPorSemana(this.empleado);
			listaDiasNoLaboralesEmpleado = NominaSemanalBL.getDiasNoLaboralesPorSemana(this.empleado);
			diasLaboralesEmpleado   = new BigDecimal(listaDiasLaboralesEmpleado.size()).setScale(2, RoundingMode.HALF_UP);
			diasNolaboralesEmpleado = new BigDecimal(listaDiasNoLaboralesEmpleado.size()).setScale(2, RoundingMode.HALF_UP);
			
			nomina.setDiasLaborales(diasLaboralesEmpleado);
			nomina.setDiasNoLaborales(diasNolaboralesEmpleado);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			incidencias    = this.getIncidencias(nomina, mapAsistencias);
			diasTrabajados = this.getDiasTrabajados(listaDiasLaboralesEmpleado, mapAsistencias, this.parametros);
			diasVacaciones = this.getDiasVacaciones(listaDiasLaboralesEmpleado, mapAsistencias, this.parametros);
			ausencias      = this.getAusencias(listaDiasLaboralesEmpleado, mapAsistencias, this.parametros);
			incapacidades = this.getIncapacidades(mapAsistencias, diasLaboralesEmpleado);
			
			
			nomina.setDiasLaborados(diasTrabajados);
			nomina.setDiasVacaciones(diasVacaciones);
			nomina.setDiasNoLaborados(ausencias);
			nomina.setIncidencias(incidencias);
			nomina.setDiasIncapacidad(incapacidades);
			
    		/*---------------------------PERCEPCIONES------------------------------*/
    		NominaSemanalBL.calcularSueldo(nomina, this.parametros, diasLaboralesEmpleado, diasNolaboralesEmpleado, diasTrabajados, diasVacaciones);
    		NominaSemanalBL.calcularBonoPuntualidad(nomina, this.parametros, this.percepcionesEmpleado, this.mapAsistencias, this.empleado.getEmpleadoConfiguracion().getRetardo(), diasLaboralesEmpleado, diasNolaboralesEmpleado, diasTrabajados);
    		NominaSemanalBL.calcularValesDespensa(nomina, this.parametros, this.percepcionesEmpleado);
			NominaSemanalBL.calcularPrimaVacacionalEnTiempo(nomina, this.parametros);
			NominaSemanalBL.calcularPrimaVacacionalReportada(nomina, this.parametros);
    		
    		
    		/*-------------------------DEDUCCIONES-----------------------*/
    		BigDecimal ingresosGravados = nomina.getPercepciones().stream()
    			.map(item -> item.getImporteGravado())
    			.reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal::add)
    			;
    		
			if(ingresosGravados.compareTo(ValoresBD._CERO.get()) > 0) {
				NominaSemanalBL.procesarISR(this.parametros, nomina);
				NominaSemanalBL.procesarIMSS(nomina, this.parametros, diasPeriodo, ausencias, incapacidades);
				NominaSemanalBL.procesarPrestamos(nomina, this.parametros, this.empleado);
			}
			
			
			/*--------------------------RESUMEN--------------------------*/
			for(DetNominaPercepcion p : percepciones) {
				log.info("Percepcion: {} - {} - {}", p.getNombre(), p.getImporteExento(), p.getImporteGravado());
			}
			
			for(DetNominaOtroPago o : otrosPagos) {
				log.info("Otro pago: {} - {}", o.getNombre(), o.getImporte());
			}
			
			for(DetNominaDeduccion d : deducciones) {
				log.info("Deduccion: {} - {}", d.getNombre(), d.getImporte());
			}
			
			NominaSemanalBL.calcularTotales(nomina, this.parametros);
			
			diasPagados = nomina.getPercepciones().stream()
				.filter(p -> PercepcionBL.CVE_SUELDO.equalsIgnoreCase(p.getClave())
						|| PercepcionBL.CVE_SEXTO_DIA.equalsIgnoreCase(p.getClave())
						||  PercepcionBL.CVE_SEPTIMO_DIA.equalsIgnoreCase(p.getClave())
				)
				.map(p -> p.getCantidad())
				.reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal :: add)
				;
			
			nomina.setDiasPagados(diasPagados);
			
			log.info("Emisor: {}", nomina.getEmisor().getNombre());
			
		} catch(SGPException ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}... {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), ex.getMessage());
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado...", ex);
			log.error("", ex);
		} finally {
			log.info("-----------------------------------------------------------------------------");
		}
		
		return nomina;
	}
	
	public static PercepcionBL getPercepcionBusinessLogic(ParametrosNomina parametros, DetNomina nomina, String clavePercepcion) {
		PercepcionBL percepcionBO = null;
		
		switch (clavePercepcion) {
		case PercepcionBL.CVE_PRIMA_VACACIONES_EN_TIEMPO:
				percepcionBO = new PrimaVacacionalEnTiempoPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_VACACIONES_REPORTADAS:
				percepcionBO = new VacacionesReportadasPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_PRIMA_VACACIONES_REPORTADAS:
				percepcionBO = new PrimaVacacionalReportadasPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_VACACIONES_EN_TIEMPO:
				percepcionBO = new VacacionesPBL(parametros, nomina);
				break;
			
			case PercepcionBL.CVE_AGUINALDO:
				percepcionBO = new AguinaldoPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_SUELDO:
				percepcionBO = new SueldoPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_SEPTIMO_DIA:
				percepcionBO = new SeptimoDiaPBL(parametros, nomina);
				break;
				
			case PercepcionBL.CVE_VALES_DESPENSA:
				percepcionBO = new ValesDespensaPBL(parametros, nomina);
				break;
			
			case PercepcionBL.CVE_BONO_PUNTUALIDAD:
				percepcionBO = new BonoPuntualidadPBL(parametros, nomina);
				break;
				
			default:
				log.info("[UI] La percepción solicitada no está considerada para la nómina semanal o no está implementada: {}", clavePercepcion);
				throw new UnsupportedOperationException("La percepción no está implementada: " + clavePercepcion);
		}
		
		return percepcionBO;
	}
	
	public static List<String> getDiasLaboralesPorSemana(DetEmpleado empleado) throws SGPException {
		List<String> listaDiasLaborales = new ArrayList<String>();
		
		if(empleado.getDatoEmpresa().getDiaLunes().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_LUNES);
		}
		
		if(empleado.getDatoEmpresa().getDiaMartes().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_MARTES);
		}
		
		if(empleado.getDatoEmpresa().getDiaMiercoles().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_MIERCOLES);
		}
		
		if(empleado.getDatoEmpresa().getDiaJueves().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_JUEVES);
		}
		
		if(empleado.getDatoEmpresa().getDiaViernes().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_VIERNES);
		}
		
		if(empleado.getDatoEmpresa().getDiaSabado().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_SABADO);
		}
		
		if(empleado.getDatoEmpresa().getDiaDomingo().booleanValue()) {
			listaDiasLaborales.add(DateUtil.PROP_CD_DOMINGO);
		}

		//TODO Validar el Tipo Jornada del empleado, para determinar si debe tener asistencia sólo 6 días (o menos)
		//o debe ser 7 (caso de monitoreo  turno 24 x 24)
		
		if(listaDiasLaborales.size() == 0)
			throw new SGPException("No hay días laborales configurados para el empleado.");
		
		return listaDiasLaborales;
	}
	
	public static List<String> getDiasNoLaboralesPorSemana(DetEmpleado empleado) throws SGPException {
		List<String> listaDiasNoLaborales = new ArrayList<String>();
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaLunes()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_LUNES);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaMartes()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_MARTES);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaMiercoles()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_MIERCOLES);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaJueves()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_JUEVES);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaViernes()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_VIERNES);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaSabado()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_SABADO);
		}
		
		if(Boolean.FALSE.compareTo(empleado.getDatoEmpresa().getDiaDomingo()) == 0) {
			listaDiasNoLaborales.add(DateUtil.PROP_CD_DOMINGO);
		}
		
		if(listaDiasNoLaborales.size() == 0)
			throw new SGPException("No hay días de descanso asignados para el empleado.");
		
		return listaDiasNoLaborales;
	}
	
	public List<DetNominaIncidencia> getIncidencias(DetNomina nomina, Map<String, DetRegistro> mapAsistencias) {
		List<DetNominaIncidencia> incidencias = new ArrayList<DetNominaIncidencia>();
		
		mapAsistencias.forEach((key, registro) -> {
			DetNominaIncidencia incidencia = new DetNominaIncidencia();
			incidencia.setNomina(nomina);
			incidencia.setFechaHoraEntrada(registro.getFechaEntrada());
			incidencia.setFechaHoraSalida(registro.getFechaSalida());
			incidencia.setClave(registro.getStatus().getCodigo());
			incidencia.setDescripcion(registro.getStatus().getDescripcion());
			incidencias.add(incidencia);
		});
		
		incidencias.sort( (i1, i2) -> i1.getFechaHoraEntrada().compareTo(i2.getFechaHoraEntrada()));
		
		return incidencias;
	}

	/**Cálculo de sueldo semanal (6 días de trabajo + Septimo día)
	 * @param nomina
	 * @param parametros
	 * @param diasTrabajados
	 * @param diasVacaciones
	 */
	public static synchronized void calcularSueldo(DetNomina nomina, ParametrosNomina parametros, BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados, BigDecimal diasVacaciones)
	throws SGPException {
		DetNominaPercepcion  sueldo       = null;
		DetNominaPercepcion  vacaciones   = null;
		DetNominaPercepcion  septimoDia   = null;
		PercepcionBL          sueldoBO     = null;
		PercepcionBL          vacacionesBO = null;
		PercepcionBL          septimoDiaBO = null;
		
		log.info("Asistencia: {} días, Vacaciones: {}, Descanso: {} días", diasTrabajados, diasVacaciones, diasNoLaborales);
		
		/* * * * * * * * * * * * * * * * * SUELDO * * * * * * * * * * * * * * * * * * * */
		sueldoBO = getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_SUELDO);
		sueldo = sueldoBO.procesar(nomina);
		agregarPercepcion(nomina, sueldo);
			
		/* * * * * * * * * * * * * * * * * SEPTIMO DIA * * * * * * * * * * * * * * * * */
		septimoDiaBO = getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_SEPTIMO_DIA);
		septimoDia = septimoDiaBO.procesar(nomina);
		agregarPercepcion(nomina, septimoDia);
		
		
		/* * * * * * * * * * * * * * * * *  VACACIONES * * * * * * * * * * * * * * * * */
		vacacionesBO = getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_VACACIONES_EN_TIEMPO);
		vacaciones = vacacionesBO.procesar(nomina, diasVacaciones);
		agregarPercepcion(nomina, vacaciones);
	}
	
	private static void calcularBonoPuntualidad(DetNomina nomina, ParametrosNomina parametros, List<DetPercepcionEmpleado> percepcionesEmpleado, Map<String, DetRegistro> mapAsistencias, Boolean procesarRetardos, BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados) {
		PercepcionBL               bonoPuntualidadBO      = null;
		DetNominaPercepcion       percepcion             = null;
		
		try {
			Optional<DetPercepcionEmpleado> optPercepcion = percepcionesEmpleado.stream
					().filter(p -> p.getPercepcion().getClave().equalsIgnoreCase(PercepcionBL.CVE_BONO_PUNTUALIDAD))
					.findFirst()
					;
			
			if(optPercepcion.isPresent() == false) {
				log.info("[UI] El empleado no tiene configuración para Bono de puntualidad.");
				return;
			}
			
			if(optPercepcion.get().getActivo() == false) {
				log.info("[UI] El empleado no tiene activa la configuración para el cálculo de Bono de puntualidad.");
				return;
			}
			
			for(Map.Entry<String, DetRegistro> entry : mapAsistencias.entrySet()) {
				log.info("Entry: {}", entry);
				String claveStatusRegistro = entry.getValue().getStatus().getCodigo();
				if(procesarRetardos.booleanValue() && ("R".equalsIgnoreCase(claveStatusRegistro) || "F".equalsIgnoreCase(claveStatusRegistro)) )
					throw new SGPException("Existen dias con retardo no justificados o faltas para el empleado.");
			}
			
			bonoPuntualidadBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_BONO_PUNTUALIDAD);
			bonoPuntualidadBO.setValor(optPercepcion.get().getValor());
			bonoPuntualidadBO.setImporteMaximo(optPercepcion.get().getImporteMaximo());
			
			percepcion = bonoPuntualidadBO.procesar(nomina);
			
			agregarPercepcion(nomina, percepcion);
			
		} catch(SGPException ex) {
			log.warn("BONO DE PUNTUALIDAD: {}", ex.getMessage());
		}
	}
	
	/**Preparación para el cálculo de vales de despensa. Depende de Sueldo y Septimo Día. Ambos se debe encontrar en el parámetro "nomina"
	 * @param nomina Objeto nómina, contiene la información y cálculo de nómina del empleado.
	 * @param parametros Parametros de nómina (Tipos de percepciones, deducciones, etc).
	 * @param percepcionesEmpleado Lista de percepciones que se aplicarán al empleado y las restricciones que están configuradas desde el registro de empleados.
	 */
	private static void calcularValesDespensa(DetNomina nomina, ParametrosNomina parametros, List<DetPercepcionEmpleado> percepcionesEmpleado)
	throws SGPException {
		DetNominaPercepcion percepcion = null;
		PercepcionBL         valesDespensaBO = null;
		
		Optional<DetPercepcionEmpleado> optPercepcion = percepcionesEmpleado.stream
				().filter(p -> p.getPercepcion().getClave().equalsIgnoreCase(PercepcionBL.CVE_VALES_DESPENSA))
				.findFirst()
				;
		
		if(optPercepcion.isPresent() == false) {
			log.info("[UI] El empleado no tiene configuración para Vales de despensa.");
			return;
		}
		
		if(optPercepcion.get().getActivo() == false) {
			log.info("[UI] El empleado no tiene activa la configuración para el cálculo de Vales de despensa.");
			return;
		}
		
		valesDespensaBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_VALES_DESPENSA);
		valesDespensaBO.setPercepcionesEmpleado(percepcionesEmpleado);
		valesDespensaBO.setValor(optPercepcion.get().getValor());
		valesDespensaBO.setImporteMaximo(optPercepcion.get().getImporteMaximo());
		
		percepcion = valesDespensaBO.procesar(nomina);
		
		agregarPercepcion(nomina, percepcion);
	}
	
	public static void calcularPrimaVacacionalEnTiempo(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		PercepcionBL primaEnTiempoBO = null;
		
		try {
			primaEnTiempoBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_PRIMA_VACACIONES_EN_TIEMPO);
			percepcion = primaEnTiempoBO.procesar(nomina);
			agregarPercepcion(nomina, percepcion);
		} catch (SGPException ex) {
			log.error("[UI] Problema para obtener el cálculo de la prima vacacional en tiempo: {}", ex.getMessage());
		}
	}
	
	public static void calcularPrimaVacacionalReportada(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		PercepcionBL primaReportadaBO = null;
		
		try {
			primaReportadaBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_PRIMA_VACACIONES_REPORTADAS);
			percepcion = primaReportadaBO.procesar(nomina);
			agregarPercepcion(nomina, percepcion);
		} catch(SGPException ex) {
			log.error("[UI] Problema para obtener el cálculo de la prima vacacional reportada: {}", ex.getMessage());
		}
	}
	
	public static DetNominaPercepcion calcular(DetNomina nomina, ParametrosNomina parametros, String clavePercepcion, BigDecimal cantidad)
	throws SGPException {
		PercepcionBL percepcionBO = null;
		DetNominaPercepcion nuevaPercepcion = null;
		
		try {
			log.info("[UI] Recalculando para el empleado {}", nomina.getReceptor().getNombre());
			percepcionBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, clavePercepcion);
			nuevaPercepcion = percepcionBO.procesar(nomina, cantidad);
			agregarPercepcion(nomina, nuevaPercepcion);
		} catch(UnsupportedOperationException ex) {
			throw new SGPException("El cálculo de la percepción no está implementado para la nómina semanal");
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {}", clavePercepcion);
		} finally {
			procesarISR(parametros, nomina);
			calcularTotales(nomina, parametros);
		}
		
		return nuevaPercepcion;
	}
	
	public static DetNominaPercepcion calcular(DetNomina nomina, ParametrosNomina parametros, String clavePercepcion, List<DetPercepcionEmpleado> percepcionesEmpleado, DetPercepcionEmpleado percepcionEmpleado)
	throws SGPException {
		PercepcionBL percepcionBO = null;
		DetNominaPercepcion nuevaPercepcion = null;
		
		try {
			log.info("[UI] Recalculando para el empleado {}", nomina.getReceptor().getNombre());
			percepcionBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, clavePercepcion);
			percepcionBO.setPercepcionesEmpleado(percepcionesEmpleado);
			percepcionBO.setValor(percepcionEmpleado.getValor());
			percepcionBO.setImporteMaximo(percepcionEmpleado.getImporteMaximo());
			nuevaPercepcion = percepcionBO.procesar(nomina);
		} catch(UnsupportedOperationException ex) {
			throw new SGPException("El cálculo de la percepción no está implementado para la nómina semanal");
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {}", clavePercepcion);
		}
		
		return nuevaPercepcion;
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
			
			if(diaInicioMes.compareTo(Integer.valueOf(1)) == 0)
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
				ultimaSemanaMes = Boolean.TRUE;
				log.info("ULTIMA SEMANA DEL MES: {} - {}", periodoInicio, periodoFin);
			} else {
				ultimaSemanaMes = Boolean.FALSE;
			}
			
		} catch(Exception ex) {
			ultimaSemanaMes = Boolean.FALSE;
		}
		
		return ultimaSemanaMes;
	}
	
	public static List<DetNomina> procesaNominaDelMes(ParametrosNomina parametros, Date periodoInicio, String rfc) {
		List<DetNomina> listaNominaDelMes = new ArrayList<DetNomina>();
		List<DetNomina> listaNominaSemanal = null;
		List<DetNomina> listaNominaExtraordinaria = null;
		Date dPeriodoInicio         = new Date(parametros.getPeriodoInicio().getTime());
		Date dPeriodoAnteriorInicio = null;
		Date dPeriodoAnteriorFin    = null;
		
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
		
		listaNominaSemanal = nominaDAO.buscarNominasDelMesPorNumeroPeriodo(TP_NOMINA_ORDINARIA, parametros.getPeriodicidad().getPeriodicidad(), parametros.getAnio(), semanaInicio, semanaFin, rfc);
		listaNominaDelMes.addAll(listaNominaSemanal);
		
		listaNominaExtraordinaria = nominaDAO.buscarNominasDelMesPorFechaPeriodo(TP_NOMINA_EXTRAORDINARIA, "99", parametros.getAnio(), DateUtil.toLocalDate(dPeriodoAnteriorInicio), DateUtil.toLocalDate(dPeriodoAnteriorFin), rfc);
		listaNominaDelMes.addAll(listaNominaExtraordinaria);
		
		return listaNominaDelMes;
	}
	
	public static Map<String, DetRegistro> getAsistencias(DetEmpleado empleado, ParametrosNomina parametros) {
		Map<String, DetRegistro> mapAsistencias   = null;
		List<DetRegistro>        listaAsistencias = null;
		List<CatEstatusRegistro> statusRegistros  = null;
		
		RegistroDAO              registroDAO      = null;
		String                   diaSemana        = null;
		Date                     diaNLEntrada     = null;
		Date                     diaNLSalida      = null;
		CatEstatusRegistro       statusDescanso   = null;
		Date                     incidenciaInicio = null;
		Date                     incidenciaFin    = null;
		
		statusRegistros = parametros.getStatusRegistros();
		
		statusDescanso = statusRegistros.stream()
				.filter(s -> "D".equalsIgnoreCase(s.getCodigo()))
				.findFirst()
				.get();
		
		mapAsistencias = new HashMap<String, DetRegistro>();
		registroDAO = new RegistroDAO();
		
		if(DateUtil.isDateBetween(empleado.getDatoEmpresa().getFechaIngreso(), parametros.getIncidenciaInicio(), parametros.getIncidenciaFin()))
			incidenciaInicio = new Date(empleado.getDatoEmpresa().getFechaIngreso().getTime());
		else
			incidenciaInicio = new Date(parametros.getIncidenciaInicio().getTime());
		
		incidenciaFin = new Date(parametros.getIncidenciaFin().getTime());
		
		listaAsistencias = registroDAO.buscar(empleado.getIdEmpleado(), incidenciaInicio, incidenciaFin);
		for(DetRegistro registro : listaAsistencias) {
			diaSemana = DateUtil.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		
		//Revisión de días no laborables.
		for(CatDiaNoLaboral dia : parametros.getDiasNoLaborales()) {
			log.info("Dia no laboral encontrado: {}", dia);
			diaSemana = DateUtil.getDiaSemana(dia.getFecha());
			if(mapAsistencias.containsKey(diaSemana)) {
				//TODO EL TRABAJADOR TIENE ASISTENCIA EN UN DIA NO LABORABLE. SE DEBEN AGREGAR HORAS EXTRAS AL DOBLE O TRIPLE.
				continue;
			}
			
			diaNLEntrada = new Date(dia.getFecha().getTime());
			diaNLSalida = new Date(dia.getFecha().getTime());
			
			mapAsistencias.put(diaSemana, new DetRegistro(null, diaNLEntrada, diaNLSalida, statusDescanso));
		}
		
		log.info("Mapa de asistencias: {}", mapAsistencias);
		
		return mapAsistencias;
	}
	
	/**Con base en los días que el empleado tenga asignados para laborar (Lunes a Viernes, o Lunes a Sábado)<br>
	 * y las asistencias del trabajador, se determina cuantos días se presentó a laborar, incluyendo los días<br>
	 * de descanso obligatorios.<br>
	 * Si el empleado se presentó a trabajar en un día de descanso, ese tiempo extra se considerará en otro proceso.<br>
	 * @param listaDiasLaboralesEmpleado Máximo de días por periodo que un trabajador puede laborar (según contrato).<br>
	 * Por ejemplo, un trabajador, por contrato, puede trabajar 6 días de la semana.<br>
	 * @param mapAsistencias Registro de asistencias del trabajador<br>
	 * @param parametros Información necesaria del periodo de nómina a calcular<br>
	 * (Tipos de percepción, deducción, catálogos IMSS, INEGI, SAT, etc.)
	 * @return Los días trabajados del periodo indicado en parametros, incluyendo los días de descanso.
	 */
	private BigDecimal getDiasTrabajados(List<String> listaDiasLaboralesEmpleado, Map<String, DetRegistro> mapAsistencias, ParametrosNomina parametros) {
		BigDecimal  diasTrabajados        = null;
		Integer     iDias                 = Integer.valueOf(0);
		DetRegistro registro              = null;
		for(String sDia : listaDiasLaboralesEmpleado) {
			registro = mapAsistencias.get(sDia);
			
			if(registro == null)
				continue;
			
			if(        "T".equalsIgnoreCase(registro.getStatus().getCodigo()) == true //Asistencia "En tiempo"
					|| "R".equalsIgnoreCase(registro.getStatus().getCodigo()) == true //Asistencia con "Retardo"
					|| "J".equalsIgnoreCase(registro.getStatus().getCodigo()) == true //Asistencia con ausencia "Justificada"
					|| "D".equalsIgnoreCase(registro.getStatus().getCodigo()) == true //Día de descanso.
			)
				iDias++;
		}
		
		diasTrabajados = new BigDecimal(iDias).setScale(2, RoundingMode.HALF_UP);
		
		return diasTrabajados;
	}
	
	private BigDecimal getDiasVacaciones(List<String> listaDiasLaboralesEmpleado, Map<String, DetRegistro> mapAsistencias, ParametrosNomina parametros) {
		BigDecimal  diasVacaciones = null;
		Integer     iDias          = Integer.valueOf(0);
		DetRegistro registro       = null;
		
		for(String sDia : listaDiasLaboralesEmpleado) {
			registro = mapAsistencias.get(sDia);
			
			if(registro == null)
				continue;
			
			if("V".equalsIgnoreCase(registro.getStatus().getCodigo()) == false)
				continue;
			
			iDias++;
		}
		
		diasVacaciones = new BigDecimal(iDias).setScale(2, RoundingMode.HALF_UP);
		
		return diasVacaciones;
	}
	
	private BigDecimal getAusencias(List<String> listaDiasLaboralesEmpleado, Map<String, DetRegistro> mapAsistencias, ParametrosNomina parametros) {
		Integer     iAusencias = null;
		BigDecimal  ausencias  = null;
		DetRegistro registro   = null;
		
		//Empezamos indicando los días laborales que si debe presentarse a trabajar el empleado (Lunes a Viernes: 5 días, Luens a Sábado, 6 días).
		//A partir de este conteo, se quitarán las asistencias y los días de descanso obligatorio, para determinar si efectivamente hubo ausencias.
//		iAusencias = listaDiasLaboralesEmpleado.size();
		iAusencias = 0;
		
		for(String sDia : listaDiasLaboralesEmpleado) {
			registro = mapAsistencias.get(sDia);
			
			if(registro == null)
				continue;
			
			if(!registro.getStatus().getCodigo().equalsIgnoreCase(EstatusRegistroBL.AUSENCIA))
				continue;
			
			iAusencias++;
		}
		
		ausencias = new BigDecimal(iAusencias).setScale(2, RoundingMode.HALF_UP);
		
		return ausencias;
	}

	/**TODO Pendiente implementar el calculo de incapacidades
	 * @param mapAsistencias
	 * @param diasLaboralesPorPeriodo
	 * @return
	 */
	private BigDecimal getIncapacidades(Map<String, DetRegistro> mapAsistencias, BigDecimal diasLaboralesPorPeriodo) {
		BigDecimal incapacidades = null;
		Long iConteo = null;
		
		try {
			iConteo = mapAsistencias.values().stream()
					.filter(registro ->  EstatusRegistroBL.INCAPACIDAD.equalsIgnoreCase(registro.getStatus().getCodigo()))
					.count()
					;
			incapacidades = new BigDecimal(iConteo).setScale(2, RoundingMode.HALF_UP);
		} catch(Exception ex) {
			incapacidades = ValoresBD._CERO.get();
		}
		
		return incapacidades;
	}
	
	public static synchronized void procesarISR(ParametrosNomina parametros, DetNomina nomina) {
		Boolean esUltimaSemanaMes = false;
		List<DetNomina> nominaMensual = null;
		
		esUltimaSemanaMes = NominaSemanalBL.esUltimaSemanaMes(parametros.getPeriodoInicio(), parametros.getPeriodoFin());
		
		if(esUltimaSemanaMes)
			nominaMensual = NominaSemanalBL.procesaNominaDelMes(parametros, parametros.getPeriodoInicio(), nomina.getReceptor().getRfc());
		
		log.info("Procesando cálculo de ISR...");
		//Primero se debe buscar en "nomina" si ya existen registros de ISR y Subsidio al salario y eliminarlos.
		List<DetNominaDeduccion> deducciones = nomina.getDeducciones();
		boolean removedDeducciones = deducciones.removeIf(d -> AbstractDBL.TD_ISR.equalsIgnoreCase(d.getTipoDeduccion().getClave()));
		if(removedDeducciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de ISR", AbstractDBL.TD_ISR);
		
		removedDeducciones = deducciones.removeIf(d -> AbstractDBL.TD_AJUSTE_AL_SUBSIDIO.equalsIgnoreCase(d.getTipoDeduccion().getClave()));
		if(removedDeducciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de ISR", AbstractDBL.TD_AJUSTE_AL_SUBSIDIO);
		
		List<DetNominaOtroPago> otrosPagos = nomina.getOtrosPagos();
		boolean removedOtrosPagos = otrosPagos.removeIf(o -> AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO.equalsIgnoreCase(o.getTipoOtroPago().getClave()));
		if(removedOtrosPagos)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de Subsidio al empleo.", AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO);
		
		removedOtrosPagos = otrosPagos.removeIf(o -> AbstractOtroPago.OP_ISR_AJUSTADO_POR_SUBSIDIO.equalsIgnoreCase(o.getTipoOtroPago().getClave()));
		if(removedOtrosPagos)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de Subsidio al empleo.", AbstractOtroPago.OP_ISR_AJUSTADO_POR_SUBSIDIO);
		
		
		
		
		ISRExecutor isrExecutor = new ISRExecutor(parametros, nominaMensual);
		IDeducciones isrBO = isrExecutor.loadClass("ISRS", DateUtil.toLocalDate(parametros.getPeriodoFin()));
		isrBO.procesar(nomina);
		
	}
	
	private static void procesarIMSS(DetNomina nomina, ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades) {
		IMSSDeduccionesBL imssBO = null;
		imssBO = new IMSSDeduccionesBL(parametros, diasTrabajados, ausencias, incapacidades);
		imssBO.procesar(nomina);
	}
	
	private static void procesarPrestamos(DetNomina nomina, ParametrosNomina parametros, DetEmpleado empleado) {
		PrestamoDBL prestamosBO = null;
		prestamosBO = new PrestamoDBL(empleado);
		prestamosBO.setFecha(parametros.getPeriodoFin());
		prestamosBO.procesar(nomina);
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
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
}
