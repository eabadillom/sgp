package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.CatPercepcionesDAO;
import mx.com.ferbo.dao.CatSubsidioDAO;
import mx.com.ferbo.dao.CatTarifaIsrDAO;
import mx.com.ferbo.dao.CuotaIMSSDAO;
import mx.com.ferbo.dao.DiaNoLaboralDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dao.TipoPercepcionDAO;
import mx.com.ferbo.dao.sat.TipoDeduccionDAO;
import mx.com.ferbo.dao.sat.TipoOtroPagoDAO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.dto.CatSubsidioDTO;
import mx.com.ferbo.dto.CatTarifaIsrDTO;
import mx.com.ferbo.dto.CuotaIMSSDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.dto.DiaNoLaboralDTO;
import mx.com.ferbo.dto.NominaConceptoDTO;
import mx.com.ferbo.dto.NominaDTO;
import mx.com.ferbo.dto.NominaDeduccionDTO;
import mx.com.ferbo.dto.NominaDeduccionDTOPK;
import mx.com.ferbo.dto.NominaEmisorDTO;
import mx.com.ferbo.dto.NominaOtroPagoDTO;
import mx.com.ferbo.dto.NominaOtroPagoPK;
import mx.com.ferbo.dto.NominaPercepcionDTO;
import mx.com.ferbo.dto.NominaPercepcionDTOPK;
import mx.com.ferbo.dto.NominaReceptorDTO;
import mx.com.ferbo.dto.sat.TipoDeduccionDTO;
import mx.com.ferbo.dto.sat.TipoOtroPagoDTO;
import mx.com.ferbo.dto.sat.TipoPercepcionDTO;
import mx.com.ferbo.util.DateUtils;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private DetEmpleadoDTO empleado = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer semanaAnio = null;
	
	private BigDecimal diasPeriodo = null;
	private BigDecimal diasTrabajados = null;
	private BigDecimal diasAsueto = null;
	private BigDecimal diasAusencia = null;
	private Map<String, DetRegistroDTO> mapAsistencias = null;
	
	//PERCEPCIONES
	private BigDecimal salarioDiario = null;
	private BigDecimal salarioDiarioIntegrado = null;
	private BigDecimal proporcionalSeptimoDia = null;
	private BigDecimal septimoDia = null;
	private BigDecimal salarioSemanal = null;
	private BigDecimal bonoPuntualidad = null;
	private BigDecimal valesDespensa = null;
	private BigDecimal totalPercepciones = null;
	
	//DEDUCCIONES ISR
	private BigDecimal baseISR = null;
	private BigDecimal excedente = null;
	private BigDecimal isrPrevio = null;
	private BigDecimal impuestoAntesSubsidio = null;
	private BigDecimal subsidioEmpleo = null;
	private BigDecimal isr = null;
	//CUOTAS DEL IMSS
	private BigDecimal enfermedadYmaternidad = null;
	private BigDecimal gastosMedicosPensionadosBeneficiarios = null;
	private BigDecimal enDinero = null;
	private BigDecimal invalidezVida = null;
	private BigDecimal cesantiaEdadAvanzadaVejez = null;
	private BigDecimal imss = null;
	//TOTAL DEDUCCIONES
	private BigDecimal totalDeducciones;
	
	//NETO A PAGAR
	private BigDecimal neto;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private CatPercepcionesDTO parametrosPercepciones = null;
    private CatPercepcionesDAO catPercepcionesDAO;
    private BigDecimal tasaValesDespensa = null;
	private BigDecimal uma = null;
	
	private CatTarifaIsrDAO tarifaISRDAO = null;
	private CatSubsidioDAO subsidioDAO = null;
	private CuotaIMSSDAO tarifaIMSSDAO = null;
	
	private TipoPercepcionDAO tipoPercepcionDAO = null;
	private TipoOtroPagoDAO tipoOtroPagoDAO = null;
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	
	//OBJETOS RELACIONADOS A LA NOMINA Y CFDI
	public NominaSemanalBL(DetEmpleadoDTO empleado, Date periodoInicio, Date periodoFin) {
		Integer anioActual = null;
		
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.catPercepcionesDAO = new CatPercepcionesDAO();
		this.tarifaISRDAO = new CatTarifaIsrDAO();
		this.subsidioDAO = new CatSubsidioDAO();
		this.tarifaIMSSDAO = new CuotaIMSSDAO();
		this.tipoPercepcionDAO = new TipoPercepcionDAO();
		this.tipoOtroPagoDAO = new TipoOtroPagoDAO();
		this.tipoDeduccionDAO = new TipoDeduccionDAO();
		
		anioActual = DateUtils.getAnio(periodoInicio);
		this.fechaInicioAnio = DateUtils.getDate(anioActual, DateUtils.ENERO, 1);
		DateUtils.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
		
		this.fechafinAnio = DateUtils.getDate(anioActual, DateUtils.DICIEMBRE, 31);
		DateUtils.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
		this.semanaAnio = DateUtils.getSemanaAnio(this.periodoInicio);
	}
	
	private NominaDTO newNomina() {
		NominaDTO nomina = null;
		
		NominaEmisorDTO emisor = null;
		NominaReceptorDTO receptor = null;
		
		List<NominaConceptoDTO> conceptos = null;
		NominaConceptoDTO concepto = null;
		
		List<NominaPercepcionDTO> percepciones = null;
		List<NominaOtroPagoDTO> otrosPagos = null;
		List<NominaDeduccionDTO> deducciones = null;
		
		try {
			nomina = new NominaDTO();
			
			conceptos = new ArrayList<>();
			percepciones = new ArrayList<>();
			otrosPagos = new ArrayList<>();
			deducciones = new ArrayList<>();
			
			emisor = new NominaEmisorDTO();
			receptor = new NominaReceptorDTO();
			concepto = new NominaConceptoDTO();
			conceptos.add(concepto);
			
			nomina.setEmisor(emisor);
			nomina.setReceptor(receptor);
			nomina.setConceptos(conceptos);
			nomina.setPercepciones(percepciones);
			nomina.setOtrosPagos(otrosPagos);
			nomina.setDeducciones(deducciones);
			
		} catch(Exception ex) {
			log.error("Problema para generar la estructura básica de la nómina del empleado.");
		}
		
		return nomina;
	}
	
	public NominaDTO calculoNomina() {
//		DetNominaDTO nomina = null;
		BigDecimal diasTrabajados = null;
		BigDecimal diasPeriodo = null;
		CatTarifaIsrDTO tarifaISR = null;
		CatSubsidioDTO tarifaSubsidio = null;
		
		NominaDTO nominaNew = null;
		NominaPercepcionDTO pSueldo = null;
		NominaPercepcionDTO pSeptimoDia = null;
		NominaPercepcionDTO pBonoPuntualidad = null;
		NominaPercepcionDTO pValeDespensa = null;
		
		NominaOtroPagoDTO opAjusteAlNeto = null;
		NominaOtroPagoDTO opSubsidioEmpleo = null;
		
		NominaDeduccionDTO dISRAntesSubsidio = null;
		NominaDeduccionDTO dISR = null;
		NominaDeduccionDTO dIMSS = null;
		
		List<NominaPercepcionDTO> percepciones = null;
		List<NominaOtroPagoDTO> otrosPagos = null;
		List<NominaDeduccionDTO> deducciones = null;
		
		try {
//			nomina = new DetNominaDTO();
			nominaNew = this.newNomina();
			
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			
			log.debug("Buscando información empresarial del empleado.");
			this.empleado = new EmpleadoDAO().buscarPorId(this.empleado.getIdEmpleado(), true);
			
			this.parametrosPercepciones = catPercepcionesDAO.buscarActual(this.periodoInicio);
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtils.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			this.diasTrabajados = diasTrabajados;
			this.diasPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
    		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
    		
			
    		this.salarioDiario = empleado.getSueldoDiario();
    		this.salarioDiarioIntegrado = this.calculoSDI(this.empleado);
    		
    		this.salarioSemanal = this.salarioDiario.multiply(diasTrabajados);
			this.septimoDia = this.calculoSeptimoDia(this.empleado);
			this.bonoPuntualidad = this.calculoBonoPuntualidad(DIAS_POR_PERIODO, empleado, mapAsistencias, this.salarioDiarioIntegrado, proporcionalSeptimoDia);
			this.valesDespensa = this.calculoValesDespensa(diasPeriodo);
			
			this.totalPercepciones = this.salarioSemanal
					.add(this.septimoDia)
					.add(this.bonoPuntualidad)
					.add(this.valesDespensa)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//percepciones
//			nomina.setIdEmpleado(empleado);
//			nomina.setSalarioDiario(this.salarioDiario);
//			nomina.setAsistencia(diasTrabajados);
//			nomina.setSalarioDiarioIntegrado(this.salarioDiarioIntegrado);
//			nomina.setSueldo(this.salarioSemanal);
//			nomina.setSeptimoDia(this.septimoDia);
//			nomina.setHorasExtras(BigDecimal.ZERO);
//			nomina.setDestajos(BigDecimal.ZERO);
//			nomina.setPremiosEficiencia(BigDecimal.ZERO);
//			nomina.setBonoPuntualidad(this.bonoPuntualidad);
//			nomina.setDespensa(this.valesDespensa);
//			nomina.setOtrasPercepciones(BigDecimal.ZERO);
//			nomina.setTotalPercepciones(this.totalPercepciones);
			
			//deducciones
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones 
			//que gravan para ISR, en este caso son:
			this.baseISR = this.salarioSemanal
					.add(this.septimoDia)
					.add(this.bonoPuntualidad)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			tarifaISR      = tarifaISRDAO.buscar(fechaInicioAnio, fechafinAnio, "s", this.baseISR);
			tarifaSubsidio = subsidioDAO.buscar(fechaInicioAnio, fechafinAnio, "s", this.baseISR);
			
			this.excedente = calculoExcedente(tarifaISR);
			this.isrPrevio = calculoISRPrevio(tarifaISR);
			this.impuestoAntesSubsidio = calculoImpuestoAntesSubsidio(tarifaISR);
			this.subsidioEmpleo = calculoSubsidioEmpleo(tarifaSubsidio);
			this.isr = impuestoAntesSubsidio.subtract(subsidioEmpleo);
			
			
//			nomina.setSubsAlEmpleoAcreditado(this.subsidioEmpleo);
//			nomina.setIsrAntesDeSubsAlEmpleo(impuestoAntesSubsidio);
//			nomina.setIsr(this.isr);
			
			//Cálculo del IMSS
			this.enfermedadYmaternidad = this.calculoEnfermedadMaternidad();
			this.gastosMedicosPensionadosBeneficiarios = this.calculoGastosMedicosPensionadosBeneficiarios();
			this.enDinero = this.calculoEnDinero();
			this.invalidezVida = this.calculoInvalidezVida();
			this.cesantiaEdadAvanzadaVejez = this.calculoCesantiaEdadAvanzadaVejez();
			this.imss = enfermedadYmaternidad
					.add(this.gastosMedicosPensionadosBeneficiarios)
					.add(this.enDinero)
					.add(this.invalidezVida)
					.add(this.cesantiaEdadAvanzadaVejez)
					;
			
//			nomina.setImss(this.imss);
			
			this.totalDeducciones = this.isr.add(this.imss);
//			nomina.setTotalDeducciones(this.totalDeducciones);
			
			this.neto = totalPercepciones.subtract(totalDeducciones);
//			nomina.setNeto(neto);
			
			
			
			
			
			
			
			
			
			
			
			//NUEVA NOMINA...........................................
			nominaNew.setEjercicio(DateUtils.getAnio(this.fechaInicioAnio));
			nominaNew.setDiasLaborados(this.diasTrabajados.intValue());
			nominaNew.setDiasNoLaborados(this.diasPeriodo.subtract(diasTrabajados).intValue());
			nominaNew.setPeriodo(DateUtils.getSemanaAnio(this.fechaInicioAnio));
			nominaNew.setSubtotal(this.totalPercepciones);
			nominaNew.setDescuento(this.totalDeducciones);
			nominaNew.setTotal(this.neto);
			
			NominaEmisorDTO emisor = nominaNew.getEmisor();
			emisor.setNomina(nominaNew);
			emisor.setNombre(this.empleado.getDatoEmpresa().getEmpresa().getRazonSocial());
			emisor.setRfc(this.empleado.getDatoEmpresa().getEmpresa().getRfc());
			emisor.setRegimenFiscal(this.empleado.getDatoEmpresa().getEmpresa().getRegimenFiscal());
			emisor.setCodigoPostal(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			emisor.setRegistroPatronal(this.empleado.getDatoEmpresa().getEmpresa().getRegistroPatronal());
			
			
			NominaReceptorDTO receptor = nominaNew.getReceptor();
			receptor.setNomina(nominaNew);
			receptor.setNombre(String.format("%s %s %s", this.empleado.getNombre(), this.empleado.getPrimerAp(), this.empleado.getSegundoAp()));
			receptor.setRfc(this.empleado.getRfc());
			receptor.setCurp(this.empleado.getCurp());
			receptor.setTipoJornada(this.empleado.getDatoEmpresa().getTipoJornada());
			receptor.setNss(this.empleado.getDatoEmpresa().getNss());
			receptor.setInicioRelacionLaboral(this.empleado.getDatoEmpresa().getFechaIngreso());
			receptor.setSalarioDiario(this.salarioDiario);
			receptor.setSalarioDiarioIntegrado(this.salarioDiarioIntegrado);
			receptor.setNumeroEmpleado(this.empleado.getNumEmpleado());
			
			percepciones = nominaNew.getPercepciones();
			otrosPagos = nominaNew.getOtrosPagos();
			deducciones = nominaNew.getDeducciones();
			
			
			int idxP = 0;
			int idxOP = 0;
			int idxD = 0;
			
			//PERCEPCIONES...
			pSueldo = new NominaPercepcionDTO();
			pSeptimoDia = new NominaPercepcionDTO();
			pBonoPuntualidad = new NominaPercepcionDTO();
			pValeDespensa = new NominaPercepcionDTO();
			if(this.isr.compareTo(BigDecimal.ZERO) > 0) {
				pSueldo.setKey(new NominaPercepcionDTOPK(idxP++, nominaNew));
				pSueldo.setClave("001");
				pSueldo.setNombre("Sueldo");
				TipoPercepcionDTO tpSueldo = tipoPercepcionDAO.buscarPorId("001");
				pSueldo.setTipoPercepcion(tpSueldo);
				pSueldo.setImporteGravado(this.salarioSemanal);
				percepciones.add(pSueldo);
				
				pSeptimoDia.setKey(new NominaPercepcionDTOPK(idxP++, nominaNew));
				pSeptimoDia.setClave("003");
				pSeptimoDia.setNombre("Séptimo día");
				TipoPercepcionDTO tpSeptimoDia = tipoPercepcionDAO.buscarPorId("001");
				pSeptimoDia.setTipoPercepcion(tpSeptimoDia);
				pSeptimoDia.setImporteGravado(this.septimoDia);
				percepciones.add(pSeptimoDia);
				
				pBonoPuntualidad.setKey(new NominaPercepcionDTOPK(idxP++, nominaNew));
				pBonoPuntualidad.setClave("015");
				pBonoPuntualidad.setNombre("Bono puntualidad");
				TipoPercepcionDTO tpBonoPuntualidad = tipoPercepcionDAO.buscarPorId("010");
				pBonoPuntualidad.setTipoPercepcion(tpBonoPuntualidad);
				pBonoPuntualidad.setImporteGravado(this.bonoPuntualidad);
				percepciones.add(pBonoPuntualidad);
			}
			
			pValeDespensa.setKey(new NominaPercepcionDTOPK(idxP++, nominaNew));
			pValeDespensa.setClave("032");
			pValeDespensa.setNombre("Despensa");
			TipoPercepcionDTO tpValeDespensa = tipoPercepcionDAO.buscarPorId("029");
			pValeDespensa.setTipoPercepcion(tpValeDespensa);
			pValeDespensa.setImporteExcento(this.valesDespensa);
			percepciones.add(pValeDespensa);
			
			
			//OTROS PAGOS...
			opAjusteAlNeto = new NominaOtroPagoDTO();
			opAjusteAlNeto.setKey(new NominaOtroPagoPK(nominaNew, idxOP++));
			TipoOtroPagoDTO tpAjusteAlNeto = this.tipoOtroPagoDAO.buscarPorId("999");
			opAjusteAlNeto.setTipoOtroPago(tpAjusteAlNeto);
			opAjusteAlNeto.setClave("099");
			opAjusteAlNeto.setNombre("Ajuste al neto");
			opAjusteAlNeto.setImporte(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			otrosPagos.add(opAjusteAlNeto);
			
			opSubsidioEmpleo = new NominaOtroPagoDTO();
			opSubsidioEmpleo.setKey(new NominaOtroPagoPK(nominaNew, idxOP++));
			TipoOtroPagoDTO topSubsidioEmpleo = this.tipoOtroPagoDAO.buscarPorId("002");
			opSubsidioEmpleo.setTipoOtroPago(topSubsidioEmpleo);
			opSubsidioEmpleo.setClave("035");
			opSubsidioEmpleo.setNombre("Subs. al empleo mes");
			opSubsidioEmpleo.setImporte(this.subsidioEmpleo);
			otrosPagos.add(opSubsidioEmpleo);
			
			//DEDUCCIONES...
//			dISRAntesSubsidio = new NominaDeduccionDTO();
//			dISRAntesSubsidio.setKey(new NominaDeduccionDTOPK(idxD++, nominaNew));
//			TipoDeduccionDTO tdISRAntesSubsidio = this.tipoDeduccionDAO.buscarPorId("002");
//			dISRAntesSubsidio.setTipoDeduccion(tdISRAntesSubsidio);
//			dISRAntesSubsidio.setClave("045");
//			dISRAntesSubsidio.setNombre("I.S.R. antes subs. al empleo");
//			dISRAntesSubsidio.setImporte(this.impuestoAntesSubsidio);
//			deducciones.add(dISRAntesSubsidio);
			
			dISR = new NominaDeduccionDTO();
			dISR.setKey(new NominaDeduccionDTOPK(idxD++, nominaNew));
			TipoDeduccionDTO tdISR = this.tipoDeduccionDAO.buscarPorId("002");
			dISR.setTipoDeduccion(tdISR);
			dISR.setClave("045");
			dISR.setNombre("I.S.R.");
			dISR.setImporte(this.isr);
			deducciones.add(dISR);
			
			dIMSS = new NominaDeduccionDTO();
			dIMSS.setKey(new NominaDeduccionDTOPK(idxD++, nominaNew));
			TipoDeduccionDTO tdIMSS = this.tipoDeduccionDAO.buscarPorId("001");
			dIMSS.setTipoDeduccion(tdIMSS);
			dIMSS.setClave("052");
			dIMSS.setNombre("I.M.S.S.");
			dIMSS.setImporte(this.imss);
			deducciones.add(dIMSS);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		}
		
		
		return nominaNew;
	}
	
	/**Con base en las asistencias del trabajador, se determina cuantos días se presentó a laborar.
	 * @param mapAsistencias Registro de asistencias del trabajador
	 * @param diasPorSemana Máximo de días por periodo que un trabajador puede laborar (según contrato).<br>
	 * Por ejemplo, un trabajador, por contrato, puede trabajar 6 días de la semana.
	 * @return
	 */
	private BigDecimal getDiasTrabajados(Map<String, DetRegistroDTO> mapAsistencias, int diasPorSemana) {
		BigDecimal diasTrabajados = null;
		Integer diasRegistrados = null;
		DetRegistroDTO registro = null;
		
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
	
	private Map<String, DetRegistroDTO> getAsistencias(DetEmpleadoDTO empleado, Date periodoInicio, Date periodoFin) {
		Map<String, DetRegistroDTO> mapAsistencias = null;
		List<DetRegistroDTO> listaAsistencias = null;
		List<DiaNoLaboralDTO> listaDiasDescanso = null;
		RegistroDAO registroDAO = null;
		String diaSemana = null;
		DiaNoLaboralDAO diaNLDAO = null;
		Date diaNLEntrada = null;
		Date diaNLSalida = null;
		
		mapAsistencias = new HashMap<String, DetRegistroDTO>();
		registroDAO = new RegistroDAO();
		listaAsistencias = registroDAO.buscar(empleado.getIdEmpleado(), periodoInicio, periodoFin);
		for(DetRegistroDTO registro : listaAsistencias) {
			diaSemana = DateUtils.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		diaNLDAO = new DiaNoLaboralDAO();
		listaDiasDescanso = diaNLDAO.buscarPorPeriodo("MX", periodoInicio, periodoFin);
		
		for(DiaNoLaboralDTO dia : listaDiasDescanso) {
			diaSemana = DateUtils.getDiaSemana(dia.getFecha());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			
			diaNLEntrada = new Date(dia.getFecha().getTime());
			diaNLSalida = new Date(dia.getFecha().getTime());
			
			mapAsistencias.put(diaSemana, new DetRegistroDTO(null, diaNLEntrada, diaNLSalida, -1, "DIA NO LABORAL"));
		}
		
		this.proporcionalSeptimoDia = new BigDecimal(mapAsistencias.size())
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				.divide(new BigDecimal( DIAS_POR_PERIODO ).setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP)
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				;
		
		return mapAsistencias;
	}

	private BigDecimal calculoSDI(DetEmpleadoDTO empleado) {
		BigDecimal sdi = null;
    	BigDecimal diasAguinaldo = null;
    	BigDecimal diasVacaciones = null;
    	BigDecimal primaVacacional = null;
    	BigDecimal diasAnio = null;
    	BigDecimal sueldoDiario = null;
    	
    	BigDecimal factorSDI = null;
    	
    	try {
    		diasAnio = new BigDecimal(DIAS_ANIO).setScale(2, BigDecimal.ROUND_HALF_UP);
    		diasAguinaldo = new BigDecimal(parametrosPercepciones.getDiasAguinaldo().intValue());
    		
    		//TODO Dias de vacaciones debe ser un dato calculado, conforme a la ley federal del trabajo
    		diasVacaciones = new BigDecimal(parametrosPercepciones.getDiasVacaciones().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		primaVacacional = new BigDecimal(parametrosPercepciones.getPrimaVacacional().floatValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		sueldoDiario = empleado.getSueldoDiario();
    		
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
	
	private BigDecimal calculoSeptimoDia(DetEmpleadoDTO empleado) {
		BigDecimal septimoDia = null;
		
		try {
			septimoDia = this.salarioDiario
					.divide(this.diasPeriodo, 4, BigDecimal.ROUND_HALF_UP)
					.multiply(this.diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO;
		}
		return septimoDia;
	}
	
	private BigDecimal calculoBonoPuntualidad(int diasPorPeriodo, DetEmpleadoDTO empleado, Map<String, DetRegistroDTO> mapAsistencia, BigDecimal salarioDiarioIntegrado, BigDecimal proporcionalSeptimoDia) {
		BigDecimal bono = null;
    	BigDecimal tasaBonoPuntualidad = null;
    	BigDecimal diasPeriodo = null;
    	
    	try {
    		//TODO VALIDAR PRIMERO SI NO HAY RETARDOS.
    		//En caso de existir retardos en el periodo de calculo, el bono de puntualidad es CERO.
    		if(this.diasTrabajados.intValue() < diasPorPeriodo)
    			return BigDecimal.ZERO;
    		
    		for(Map.Entry<String, DetRegistroDTO> entry :  this.mapAsistencias.entrySet()) {
    			String codigo = entry.getValue().getCatEstatusRegistroDTO().getCodigo();
    			if("R".equalsIgnoreCase(codigo) || "F".equalsIgnoreCase(codigo))
    				return BigDecimal.ZERO;
    		}
    		
    		tasaBonoPuntualidad = new BigDecimal(this.parametrosPercepciones.getBonoPuntualidad().floatValue());
    		diasPeriodo = this.diasTrabajados.add(proporcionalSeptimoDia).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		bono = salarioDiarioIntegrado.multiply(tasaBonoPuntualidad).setScale(2, BigDecimal.ROUND_HALF_UP);
    		bono = bono.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    	} catch(Exception ex) {
    		bono = BigDecimal.ZERO;
    	}
    	
        return bono;
	}
	
	private BigDecimal calculoValesDespensa(BigDecimal diasPeriodo) {
		BigDecimal vales = null;
		
    	try {
    		
    		if(this.diasTrabajados.compareTo(BigDecimal.ZERO) == 0)
    			throw new SGPException("No es posible asignar bono de puntualidad.");
    		
    		this.uma = parametrosPercepciones.getUma();
    		this.tasaValesDespensa = parametrosPercepciones.getValeDespensa();
    		
    		vales = uma.multiply(tasaValesDespensa).setScale(4, BigDecimal.ROUND_HALF_UP);
    		vales = vales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    	} catch(Exception ex) {
    		vales = BigDecimal.ZERO;
    	}
    	
    	return vales;
	}
	/********************TERMINAN PERCEPCIONES****************/
	
	
	/********************EMPIEZAN DEDUCCIONES****************/
	
	private BigDecimal calculoExcedente(CatTarifaIsrDTO tarifaISR) {
		BigDecimal excedente = null;
		excedente = baseISR.subtract(tarifaISR.getLimiteInferior()) ;
		return excedente;
	}
	
	private BigDecimal calculoISRPrevio(CatTarifaIsrDTO tarifaISR) {
		BigDecimal isrPrevio = null;
		BigDecimal porcentajeExcedente = null;
		BigDecimal cien = null;
		BigDecimal porcentaje = null;
		
		try {
			porcentajeExcedente = tarifaISR.getPorcAplExceLimInf();
			
			cien = new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			if(porcentajeExcedente.compareTo(cien) >= 0)
				porcentaje = porcentajeExcedente;
			else
				porcentaje = porcentajeExcedente.divide(cien, 4, BigDecimal.ROUND_HALF_UP);
			
			isrPrevio = this.excedente.multiply(porcentaje).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR previo");
			isrPrevio = BigDecimal.ZERO;
		}
		
		return isrPrevio;
	}
	
	private BigDecimal calculoImpuestoAntesSubsidio(CatTarifaIsrDTO tarifaISR) {
		BigDecimal impuesto = null;
		impuesto = this.isrPrevio.add(tarifaISR.getCuotaFija());
		return impuesto;
	}
	
	private BigDecimal calculoSubsidioEmpleo(CatSubsidioDTO tarifaSubsidio) {
		BigDecimal subsidioEmpleo = null;
		subsidioEmpleo = tarifaSubsidio.getCantidadSubsidio();
		return subsidioEmpleo;
	}
	
	//IMSS
	private BigDecimal calculoEnfermedadMaternidad() {
		BigDecimal cuota = null;
		BigDecimal cero = null;
		BigDecimal tres = null;
		BigDecimal limiteUMAs = null;
		BigDecimal excedente = null;
		BigDecimal tarifa = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			//Constantes necesarias para el cálculo de Enfermedad y Maternidad.
			cero = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tres = new BigDecimal("3.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			cuota = cero;
			
			limiteUMAs = this.uma.multiply(tres).setScale(2, BigDecimal.ROUND_HALF_UP);
			excedente = this.salarioDiarioIntegrado.subtract(limiteUMAs);
			
			if(salarioDiarioIntegrado.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM1", this.fechaInicioAnio, this.fechafinAnio, tres);
				tarifa = tarifaIMSS.getCuota();
				cuota = excedente
						.multiply(tarifa).setScale(2, BigDecimal.ROUND_HALF_UP)
						.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoGastosMedicosPensionadosBeneficiarios() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM2", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoEnDinero() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM3", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoInvalidezVida() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "IV", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoCesantiaEdadAvanzadaVejez() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "CEAV", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	/********************TERMINAN DEDUCCIONES****************/

	public DetEmpleadoDTO getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleadoDTO empleado) {
		this.empleado = empleado;
	}

	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}

	public BigDecimal getSalarioDiarioIntegrado() {
		return salarioDiarioIntegrado;
	}

	public void setSalarioDiarioIntegrado(BigDecimal salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}

	public BigDecimal getSeptimoDia() {
		return septimoDia;
	}

	public void setSeptimoDia(BigDecimal septimoDia) {
		this.septimoDia = septimoDia;
	}

	public BigDecimal getSalarioSemanal() {
		return salarioSemanal;
	}

	public void setSalarioSemanal(BigDecimal salarioSemanal) {
		this.salarioSemanal = salarioSemanal;
	}

	public BigDecimal getBonoPuntualidad() {
		return bonoPuntualidad;
	}

	public void setBonoPuntualidad(BigDecimal bonoPuntualidad) {
		this.bonoPuntualidad = bonoPuntualidad;
	}

	public BigDecimal getValesDespensa() {
		return valesDespensa;
	}

	public void setValesDespensa(BigDecimal valesDespensa) {
		this.valesDespensa = valesDespensa;
	}

	public BigDecimal getTotalPercepciones() {
		return totalPercepciones;
	}

	public void setTotalPercepciones(BigDecimal totalPercepciones) {
		this.totalPercepciones = totalPercepciones;
	}

	public BigDecimal getDiasPeriodo() {
		return diasPeriodo;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
	}

	public BigDecimal getDiasTrabajados() {
		return diasTrabajados;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}

	public BigDecimal getDiasAsueto() {
		return diasAsueto;
	}

	public void setDiasAsueto(BigDecimal diasAsueto) {
		this.diasAsueto = diasAsueto;
	}

	public BigDecimal getDiasAusencia() {
		return diasAusencia;
	}

	public void setDiasAusencia(BigDecimal diasAusencia) {
		this.diasAusencia = diasAusencia;
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

	public BigDecimal getProporcionalSeptimoDia() {
		return proporcionalSeptimoDia;
	}

	public void setProporcionalSeptimoDia(BigDecimal proporcionalSeptimoDia) {
		this.proporcionalSeptimoDia = proporcionalSeptimoDia;
	}

	public Map<String, DetRegistroDTO> getMapAsistencias() {
		return mapAsistencias;
	}

	public void setMapAsistencias(Map<String, DetRegistroDTO> mapAsistencias) {
		this.mapAsistencias = mapAsistencias;
	}
	
	
	

}
