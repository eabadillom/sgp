package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.CatPercepcionesDAO;
import mx.com.ferbo.dao.DiaNoLaboralDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetNominaDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.dto.DiaNoLaboralDTO;
import mx.com.ferbo.util.DateUtils;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private DetEmpleadoDTO empleado = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	
	//PERCEPCIONES
	private BigDecimal salarioDiario = null;
	private BigDecimal salarioDiarioIntegrado = null;
	private BigDecimal proporcionalSeptimoDia = null;
	private BigDecimal septimoDia = null;
	private BigDecimal salarioSemanal = null;
	private BigDecimal bonoPuntualidad = null;
	private BigDecimal valesDespensa = null;
	private BigDecimal totalPercepciones = null;
	
	//DEDUCCIONES
	private BigDecimal baseISR = null;
	
	private BigDecimal diasPeriodo = null;
	private BigDecimal diasTrabajados = null;
	private BigDecimal diasAsueto = null;
	private BigDecimal diasAusencia = null;
	
	private Map<String, DetRegistroDTO> mapAsistencias = null;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private CatPercepcionesDTO percepcion = null;
    private CatPercepcionesDAO catPercepcionesDAO;
    private BigDecimal tasaValesDespensa = null;
	private BigDecimal uma = null;
	
	
	public NominaSemanalBL(DetEmpleadoDTO empleado, Date periodoInicio, Date periodoFin) {
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.catPercepcionesDAO = new CatPercepcionesDAO();
	}
	
	public DetNominaDTO calculoNomina() {
		DetNominaDTO nomina = null;
		
		BigDecimal diasTrabajados = null;
		
		BigDecimal diasPeriodo = null;
		
		
		try {
			this.percepcion = catPercepcionesDAO.buscarActual(this.periodoInicio);
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtils.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			this.diasTrabajados = diasTrabajados;
			this.diasPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
//			proporcionalSeptimoDia = this.getProporcionalSeptimoDia(this.empleado, this.diasTrabajados, DIAS_POR_PERIODO);
			
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
			
			
			
			this.baseISR = new BigDecimal(totalPercepciones.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			
			//percepciones
			nomina = new DetNominaDTO();
			nomina.setIdEmpleado(empleado);
			nomina.setSalarioDiario(this.salarioDiario);
			nomina.setAsistencia(diasTrabajados);
			nomina.setSalarioDiarioIntegrado(this.salarioDiarioIntegrado);
			nomina.setSueldo(this.salarioSemanal);
			nomina.setSeptimoDia(this.septimoDia);
			nomina.setHorasExtras(BigDecimal.ZERO);
			nomina.setDestajos(BigDecimal.ZERO);
			nomina.setPremiosEficiencia(BigDecimal.ZERO);
			nomina.setBonoPuntualidad(this.bonoPuntualidad);
			nomina.setDespensa(this.valesDespensa);
			nomina.setOtrasPercepciones(BigDecimal.ZERO);
			nomina.setTotalPercepciones(this.totalPercepciones);
			
			//deducciones
			
			
			
			//otros
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		}
		
		
		return nomina;
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
			
			//TODO falta implementar el catálogo de días festivos o no laborales.
		}
		//TODO falta agregar algún día no laboral.
		diasTrabajados = new BigDecimal(mapAsistencias.size());
		
		return diasTrabajados;
	}
	
	private BigDecimal calculaProporcionalSeptimoDia(DetEmpleadoDTO empleado, BigDecimal diasTrabajados, int diasPorPeriodo) {
		BigDecimal proporcionalSeptimoDia = null;
		BigDecimal bdDiasPorPeriodo = null;
		
		if(diasTrabajados == null)
			return new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		
		bdDiasPorPeriodo = new BigDecimal(diasPorPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
		proporcionalSeptimoDia = diasTrabajados.divide(bdDiasPorPeriodo, 2, BigDecimal.ROUND_HALF_UP);
		
		return proporcionalSeptimoDia;
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
    	BigDecimal propAntiguedad = null;
    	BigDecimal diasAnio = null;
    	BigDecimal sueldoDiario = null;
    	
    	BigDecimal factorSDI = null;
    	
    	try {
    		diasAnio = new BigDecimal(DIAS_ANIO).setScale(2, BigDecimal.ROUND_HALF_UP);
    		diasAguinaldo = new BigDecimal(percepcion.getDiasAguinaldo().intValue());
    		
    		//TODO Dias de vacaciones debe ser un dato calculado, conforme a la ley federal del trabajo
    		diasVacaciones = new BigDecimal(percepcion.getDiasVacaciones().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		primaVacacional = new BigDecimal(percepcion.getPrimaVacacional().floatValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		propAntiguedad = diasVacaciones.multiply(primaVacacional).setScale(4, BigDecimal.ROUND_HALF_UP);
    		sueldoDiario = empleado.getSueldoDiario();
    		
    		factorSDI = primaVacacional
    				.multiply(diasVacaciones).setScale(4, BigDecimal.ROUND_HALF_UP)
    				.add(diasAguinaldo)
    				.add(diasAnio)
    				.divide(diasAnio, 4, BigDecimal.ROUND_HALF_UP)
    				;
    		
    		sdi = sueldoDiario.multiply(factorSDI).setScale(6, BigDecimal.ROUND_HALF_UP);
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
			
//			septimoDia = empleado.getSueldoDiario().multiply(proporcionalSeptimoDia).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO;
		}
		return septimoDia;
	}
	
	private BigDecimal calculoSalarioSemanal() {
		BigDecimal salarioSemanal = null;
    	
    	try {
			salarioSemanal = this.salarioDiario.multiply(this.diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP);
			salarioSemanal = salarioSemanal.add(this.septimoDia).setScale(2, BigDecimal.ROUND_HALF_UP);
            
    	} catch(Exception ex) {
    		log.error("Problema para procesar el salario semanal...", ex);
    		salarioSemanal = BigDecimal.ZERO;
    	}
    	
    	return salarioSemanal;
	}
	
	private BigDecimal calculoBonoPuntualidad(int diasPorPeriodo, DetEmpleadoDTO empleado, Map mapAsistencia, BigDecimal salarioDiarioIntegrado, BigDecimal proporcionalSeptimoDia) {
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
    		
    		tasaBonoPuntualidad = new BigDecimal(this.percepcion.getBonoPuntualidad().floatValue());
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
    		
    		this.uma = percepcion.getUma();
    		this.tasaValesDespensa = percepcion.getValeDespensa();
    		
    		vales = uma.multiply(tasaValesDespensa).setScale(4, BigDecimal.ROUND_HALF_UP);
    		vales = vales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    	} catch(Exception ex) {
    		vales = BigDecimal.ZERO;
    	}
    	
    	return vales;
	}
	
	
	

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
