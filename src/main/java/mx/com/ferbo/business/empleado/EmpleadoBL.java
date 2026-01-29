package mx.com.ferbo.business.empleado;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.domicilio.DomicilioBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatParametro;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.DetSalarioDiario;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class EmpleadoBL {

    private static final Logger log = LogManager.getLogger(EmpleadoBL.class);
    
    public static DetEmpleado build() {
    	DetEmpleado empleado = null;
    	DetDomicilioEmpleado domicilio = null;
    	
    	empleado = new DetEmpleado();
    	empleado.setActivo((short) 1);
    	empleado.setDatoEmpresa(new InfDatoEmpresa());
    	empleado.getDatoEmpresa().setSalariosDiarios(new ArrayList<DetSalarioDiario>());
    	
    	domicilio = DomicilioBL.build(empleado);
    	empleado.setDomicilio(domicilio);
    	empleado.getDomicilio().setEmpleado(empleado);
    	
    	empleado.setEmpleadoConfiguracion(new DetEmpleadoConfiguracion());
    	empleado.getEmpleadoConfiguracion().setEmpleado(empleado);
    	
    	empleado.setVacaciones(new ArrayList<DetVacaciones>());
    	
    	return empleado;
    }
    
    /**Debería usarse sólamente para cargar toda la informacíon completa del empleado, incluidos sus datos biométricos
     * que se encuentran desacoplados en otras tablas.
     * Se crea este método sólamente para su uso en el registro de empleados.
     * @param idEmpleado
     * @return
     */
    public static DetEmpleado load(Integer idEmpleado) throws SGPException {
    	DetEmpleado empleado = null;
    	EmpleadoDAO empleadoDAO = null;
    	DetSalarioDiario salarioDiario = null;
    	
    	try {
    		empleadoDAO = new EmpleadoDAO();
    		empleado = empleadoDAO.buscarPorId(idEmpleado, true);
    		
    		salarioDiario = salarioDiarioVigente(empleado.getDatoEmpresa().getSalariosDiarios(), new Date());
    		empleado.getDatoEmpresa().setSalarioDiario(salarioDiario.getImporte());
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado solicitado: id = {}", idEmpleado);
    		throw new SGPException("Ocurrió un problema al cargar la información del empleado: id = " + idEmpleado, ex);
    	}
    	
    	loadDetail(empleado);
    	
    	return empleado;
    }
    
    public static DetSalarioDiario salarioDiarioVigente(List<DetSalarioDiario> salariosDiarios, Date fecha) {
    	DetSalarioDiario salarioDiario = null;
    	
		if(salariosDiarios == null || salariosDiarios.size() == 0)
			return new DetSalarioDiario.Builder().importe(ValoresBD._CERO.get()).build();
		
		salarioDiario = salariosDiarios.stream()
				.filter(sd -> sd.getFechaRegistro() != null)
				.filter(sd -> sd.getFechaRegistro().compareTo(fecha) <= 0)
				.max(Comparator.comparing(DetSalarioDiario::getFechaRegistro))
				.orElse(new DetSalarioDiario.Builder().importe(ValoresBD._CERO.get()).build());
    	
    	return salarioDiario;
    }
    
    public static DetEmpleado load(String rfc) throws SGPException {
    	DetEmpleado empleado = null;
    	EmpleadoDAO empleadoDAO = null;
    	
    	empleadoDAO = new EmpleadoDAO();
    	
    	try {
    		empleado = empleadoDAO.buscarPorRFC(rfc, true);
    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado solicitado: RFC = {}", rfc);
    		throw new SGPException("Ocurrió un problema al cargar la información del empleado: RFC = " + rfc, ex);
    	}
    	
    	loadDetail(empleado);
    	
    	return empleado;
    }
    
    private static void loadDetail(DetEmpleado empleado) {
    	DetDomicilioEmpleado domicilio = null;
    	
    	try {
    		log.info("id dato empresa: {}", empleado.getDatoEmpresa().getId());
    	} catch(Exception ex) {
    		empleado.setDatoEmpresa(new InfDatoEmpresa());
    	}
    	
    	try {
    		log.info("Id Asentamiento: {}", empleado.getDomicilio().getAsentamiento().getKey().getId());
    		log.info("Id Localidad: {}", empleado.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getId());
            log.info("Id Municipio: {}", empleado.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
            log.info("Id Estado: {}", empleado.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
            log.info("Id Pais: {}", empleado.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
    	} catch(Exception ex) {
    		domicilio = DomicilioBL.build(empleado);
    		empleado.setDomicilio(domicilio);
    	}
    	
    	try {
    		for(DetVacaciones v : empleado.getVacaciones()) {
            	log.info("Periodo Vacacional: {}", v.getIdVacaciones());
            }
    	} catch(Exception ex) {
    		empleado.setVacaciones(new ArrayList<DetVacaciones>());
    	}
    	
    	try {
    		for(DetPercepcionEmpleado p : empleado.getPercepcionesEmpleado()) {
            	log.info("Percepción del empleado: {}", p.getId());
            }
    	} catch(Exception ex) {
    		empleado.setPercepcionesEmpleado(new ArrayList<DetPercepcionEmpleado>());
    	}
    	
    	try {
    		for(DetPrestamo p : empleado.getPrestamos()) {
            	log.info("Préstamo del empleado: {}", p.getIdPrestamo());
            }
    	} catch(Exception ex) {
    		empleado.setPrestamos(new ArrayList<DetPrestamo>());
    	}
    	
    	try {
    		log.info("Id Empleado configuración: {}", empleado.getEmpleadoConfiguracion().getIdEmpleadoConf());
    	} catch(Exception ex) {
    		log.info("El empleado no tiene configuración establecida, se creará un nuevo objeto de configuración.");
    		empleado.setEmpleadoConfiguracion(new DetEmpleadoConfiguracion());
    	}
    	
    	try {
    		log.info("Id domicilio empleado: {}", empleado.getDomicilio().getId());
    	} catch(Exception ex) {
    		empleado.setDomicilio(new DetDomicilioEmpleado());
			empleado.getDomicilio().setEmpleado(empleado);
    	}
    }
    
    public static List<DetEmpleado> buscarActivosPorEmpresa(Integer idEmpresa, Date periodoPagoInicio, Date periodoPagoFin) {
    	List<DetEmpleado> empleados = null;
    	EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    	
    	List<DetEmpleado> resultado= empleadoDAO.buscarActivoEmpresaIngreso(idEmpresa, periodoPagoInicio, periodoPagoFin);
		empleados = resultado.stream()
				.sorted(Comparator.comparing(EmpleadoBL::obtenerNombreArea).reversed())
				.collect(Collectors.toList());
    	
    	return empleados;
    }
    
    private static String obtenerNombreArea(DetEmpleado empleado) {
    	return empleado.getDatoEmpresa().getArea().getDescripcion();
    }

    public static void validarDatosEmpleado(DetEmpleado empleadoporvalidar) {

        if (empleadoporvalidar == null) {
            log.info("El empleado es nulo, se le asigna memoria");
            empleadoporvalidar = new DetEmpleado();
        }

        if (empleadoporvalidar.getDomicilio() == null) {
            log.info("El domicilio del empleado es nulo, se le asigna memoria");
            empleadoporvalidar.setDomicilio(new DetDomicilioEmpleado());
        }

        if (empleadoporvalidar.getEmpleadoConfiguracion() == null) {
            log.info("La configuracion del empleado es nula, se le asigna memoria");
            empleadoporvalidar.setEmpleadoConfiguracion(new DetEmpleadoConfiguracion());
        }

        if (empleadoporvalidar.getEmpleadoFoto() == null) {
            log.info("La foto del empleado es nula, se le asigna memoria");
            empleadoporvalidar.setEmpleadoFoto(new DetEmpleadoFoto());
        }

        if (empleadoporvalidar.getDatoEmpresa() == null) {
            log.info("El dato empresarial del empleado es nulo, se le asigna memoria");
            empleadoporvalidar.setDatoEmpresa(new InfDatoEmpresa());
        }

    }

    public static List<String> diasEmpleadoTrabaja(DetEmpleado empleado){
        List<String> diasLaboralesEmpleado = new ArrayList<String>();
        
        if (empleado.getDatoEmpresa().getDiaLunes()) {
            diasLaboralesEmpleado.add("L");
        }

        if (empleado.getDatoEmpresa().getDiaMartes()) {
            diasLaboralesEmpleado.add("M");
        }

        if (empleado.getDatoEmpresa().getDiaMiercoles()) {
            diasLaboralesEmpleado.add("X");
        }

        if(empleado.getDatoEmpresa().getDiaJueves()){
            diasLaboralesEmpleado.add("J");
        }
        
        if(empleado.getDatoEmpresa().getDiaViernes()){
            diasLaboralesEmpleado.add("V");
        }
        
        if(empleado.getDatoEmpresa().getDiaSabado()){
            diasLaboralesEmpleado.add("S");
        }
        
        if(empleado.getDatoEmpresa().getDiaDomingo()){
            diasLaboralesEmpleado.add("D");
        }
        
        return diasLaboralesEmpleado;
    }
    
    public static void recalcularVacaciones(DetEmpleado empleado) throws SGPException {

        if (empleado.getVacaciones().isEmpty()) {
            throw new SGPException("El empleado no tiene ningun periodo vacacional asignado");
        }

        if (empleado.getVacaciones().size() > 1) {
            throw new SGPException("El empleado tiene mas de un periodo vacacional");
        }

        try {
            log.info("Inicia el proceso de recalcular el primer periodo vacacional del empleado");
            DetVacaciones primerasVacaciones = empleado.getVacaciones().get(0);
            int anio= DateUtil.getAnio(empleado.getDatoEmpresa().getFechaIngreso());
            int mes= DateUtil.getMes(empleado.getDatoEmpresa().getFechaIngreso());
            int dia= DateUtil.getDia(empleado.getDatoEmpresa().getFechaIngreso());
            Date nuevaFechaInicio = DateUtil.getDate(anio, mes, dia);
            DateUtil.setTime(nuevaFechaInicio, 0, 0, 0, 0);
            Date nuevaFechaFin = DateUtil.getDate(anio, mes, dia);
            DateUtil.setTime(nuevaFechaFin, 0, 0, 0, 0);
            nuevaFechaFin = DateUtil.addYear(nuevaFechaFin, 1);
            nuevaFechaFin = DateUtil.addDay(nuevaFechaFin, -1);
            primerasVacaciones.setFechaInicio(nuevaFechaInicio);
            primerasVacaciones.setFechaFin(nuevaFechaFin);
            VacacionesDAO vacacionesDAO = new VacacionesDAO();
            vacacionesDAO.actualizar(primerasVacaciones);
            log.info("Finaliza el proceso de recalcular el primer periodo vacacional del empleado");
        } catch (Exception ex) {
            log.error("Hubo algun problema al recalcular el primer periodo vacacional. " + ex.getMessage());
            throw new SGPException("Hubo algun problema al recalcular el primer periodo vacacional.");
        }
    }

    public static void generarAnioVacaciones(DetEmpleado empleado) throws SGPException {

        if (empleado.getDatoEmpresa().getFechaIngreso() == null) {
            throw new SGPException("La fecha de ingreso del empleado está vacía.");
        }

        if (empleado.getDatoEmpresa().getFechaBaja() != null) {
            throw new SGPException("El empleado " + empleado.getNombre() + " " + empleado.getPrimerAp() + " " + empleado.getSegundoAp() + " ya no trabaja en la empresa");
        }

        int aniotmp = DateUtil.getAnio(empleado.getDatoEmpresa().getFechaIngreso());
        int mestmp = DateUtil.getMes(empleado.getDatoEmpresa().getFechaIngreso());
        int diatmp = DateUtil.getDia(empleado.getDatoEmpresa().getFechaIngreso());

        Date fechaaux = DateUtil.getDate(aniotmp, mestmp, diatmp);

        if (!empleado.getVacaciones().isEmpty()) {
            fechaaux = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechaFin();
        }

        while (fechaaux.before(DateUtil.now())) {
            Date fechainicio = null;
            Date fechafin = null;
            Date fechatmp = null;

            int dias = 0;

            if (empleado.getVacaciones().isEmpty()) {
                fechainicio = fechaaux;
                fechafin = DateUtil.addYear(fechaaux, 1);
                ParametroDAO parametroDAO = new ParametroDAO(CatParametro.class);
                CatParametro parametro = parametroDAO.buscarPorClave("DIVAC");
                String sDias = parametro.getValor();
                dias = Integer.parseInt(sDias);
            } else {
                dias = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiasTotales();
                fechatmp = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechaFin();
                fechainicio = DateUtil.addDay(fechatmp, 1);
                fechafin = DateUtil.addYear(fechainicio, 1);

                int tamanio = empleado.getVacaciones().size() + 1;

                if (tamanio >= 2 && tamanio <= 5) {
                    dias = dias + 2;
                }

                if (tamanio >= 10 && tamanio % 5 == 0) {
                    dias = dias + 2;
                }
            }

            fechafin = DateUtil.addDay(fechafin, -1);

            DetVacaciones ultimasvacaciones = new DetVacaciones();

            ultimasvacaciones.setFechaInicio(fechainicio);
            ultimasvacaciones.setFechaFin(fechafin);
            ultimasvacaciones.setDiasTotales(dias);
            ultimasvacaciones.setEmpleado(empleado);
            ultimasvacaciones.setDiasTomados(0);
            ultimasvacaciones.setDiasPagados(0);
            ultimasvacaciones.setPrimaPagada(Boolean.FALSE);
            ultimasvacaciones.setDiasPendientesPagados(Boolean.FALSE);

            if (empleado.getVacaciones().isEmpty()) {
                List<DetVacaciones> vacaciones = new ArrayList<DetVacaciones>();
                vacaciones.add(ultimasvacaciones);
                empleado.setVacaciones(vacaciones);
            } else {
                empleado.getVacaciones().add(ultimasvacaciones);
            }
            fechaaux = fechafin;
        }
    }

    public static void empleadoTieneDiasLaborales(DetEmpleado empleado) throws SGPException {

        if (empleado == null) {
            throw new SGPException("Error: El empleado no tiene informacion");
        }

        if (empleado.getDatoEmpresa() == null) {
            throw new SGPException("Erro: El empleado no tiene informacion empresarial");
        }

        if (empleado.getDatoEmpresa().getDiaLunes() == false || empleado.getDatoEmpresa().getDiaMartes() == false || empleado.getDatoEmpresa().getDiaMiercoles() == false || empleado.getDatoEmpresa().getDiaJueves() == false || empleado.getDatoEmpresa().getDiaViernes() == false) {
            throw new SGPException("Error: No tiene dias laborales asignados. Por favor contactar a RH");
        }

    }

    public static boolean empleadoAsisteEnDiaDescanso(DetEmpleado empleado) {
        
        List<CatDiaNoLaboral> diasDescanso = DiasDeDescansoObligatorioBL.diasDescansoAnual();
        List<String> diasLaboralesEmpleado = new ArrayList<String>();
        String diaLaborando = DateUtil.getDiaSemana(DateUtil.now());
        Date hoy = DateUtil.now();
        DateUtil.resetTime(hoy);

        diasLaboralesEmpleado = diasEmpleadoTrabaja(empleado);
        
        for(CatDiaNoLaboral diaDescanso : diasDescanso){
            if(diaDescanso.getFecha().compareTo(hoy) == 0 && diaDescanso.getOficial()){
                return true;
            }
        }
        
        if(!diasLaboralesEmpleado.contains(diaLaborando)){
            return true;
        }
        
        return false;
    }
    
    public static DetEmpleado reingreso(DetEmpleado e) {
    	DetEmpleado nuevoEmpleado = null;
    	InfDatoEmpresa datoEmpresa = null;
    	DetEmpleadoConfiguracion configuracion = null;
    	Date fechaIngreso = new Date();
		DateUtil.setTime(fechaIngreso, 0, 0, 0);
    	
    	try {
    		datoEmpresa = new InfDatoEmpresa.Builder()
    				.fechaIngreso(fechaIngreso)
    				.rfc(e.getDatoEmpresa().getRfc())
    				.nss(e.getDatoEmpresa().getNss())
    				.perfil(e.getDatoEmpresa().getPerfil())
    				.empresa(e.getDatoEmpresa().getEmpresa())
    				.puesto(e.getDatoEmpresa().getPuesto())
    				.area(e.getDatoEmpresa().getArea())
    				.planta(e.getDatoEmpresa().getPlanta())
    				.tipoContrato(e.getDatoEmpresa().getTipoContrato())
    				.tipoJornada(e.getDatoEmpresa().getTipoJornada())
    				.tipoRegimen(e.getDatoEmpresa().getTipoRegimen())
    				.horaEntrada(e.getDatoEmpresa().getHoraEntrada())
    				.horasalida(e.getDatoEmpresa().getHorasalida())
    				.minutosTolerancia(e.getDatoEmpresa().getMinutosTolerancia())
    				.entidadFederativa(e.getDatoEmpresa().getEntidadFederativa())
    				.riesgoPuesto(e.getDatoEmpresa().getRiesgoPuesto())
    				.periodicidadPago(e.getDatoEmpresa().getPeriodicidadPago())
    				.banco(e.getDatoEmpresa().getBanco())
    				.diasAguinaldo(e.getDatoEmpresa().getDiasAguinaldo())
    				.primaVacacional(e.getDatoEmpresa().getPrimaVacacional())
    				.sindicalizado(e.getDatoEmpresa().getSindicalizado())
    				.confianza(e.getDatoEmpresa().getConfianza())
    				.diaLunes(e.getDatoEmpresa().getDiaLunes())
    				.diaMartes(e.getDatoEmpresa().getDiaMartes())
    				.diaMiercoles(e.getDatoEmpresa().getDiaMiercoles())
    				.diaJueves(e.getDatoEmpresa().getDiaJueves())
    				.diaViernes(e.getDatoEmpresa().getDiaViernes())
    				.diaSabado(e.getDatoEmpresa().getDiaSabado())
    				.diaDomingo(e.getDatoEmpresa().getDiaDomingo())
    				.build();
    		
    		configuracion = new DetEmpleadoConfiguracion.Builder()
    				.build();
    		
    		nuevoEmpleado = new DetEmpleado.Builder()
    				.numEmpleado(e.getNumEmpleado())
    				.nombre(e.getNombre())
    				.primerApellido(e.getPrimerAp())
    				.segundoApellido(e.getSegundoAp())
    				.fechaNacimiento(e.getFechaNacimiento())
    				.fechaRegistro(new Date())
    				.fechaModificacion(new Date())
    				.curp(e.getCurp())
    				.correo(e.getCorreo())
    				.activo((short)1)
    				.datoEmpresa(datoEmpresa)
    				.percepcionesEmpleado(new ArrayList<DetPercepcionEmpleado>())
    				.prestamos(new ArrayList<DetPrestamo>())
    				.vacaciones(new ArrayList<DetVacaciones>())
    				.domicilio(e.getDomicilio().clone())
    				.empleadoConfiguracion(new DetEmpleadoConfiguracion())
    				.build();
    		
    		configuracion.setEmpleado(nuevoEmpleado);
    		nuevoEmpleado.setEmpleadoConfiguracion(configuracion);
    		
			nuevoEmpleado.setDomicilio(e.getDomicilio().clone());
		} catch (CloneNotSupportedException e1) {
			if(nuevoEmpleado == null)
				nuevoEmpleado = new DetEmpleado.Builder().domicilio(new DetDomicilioEmpleado()) .build();
		} finally {
			if(nuevoEmpleado == null)
				nuevoEmpleado = new DetEmpleado.Builder().build();
			nuevoEmpleado.getDomicilio().setEmpleado(nuevoEmpleado);
		}
    	
    	return nuevoEmpleado;
    }
    
    public static Boolean tieneReingresos(String curp) {
    	List<DetEmpleado> registrosEmpleado = null;
    	EmpleadoDAO empleadoDAO = null;
    	Boolean tieneBajas = null;
    	Boolean tieneVigente = null;
    	
    	empleadoDAO = new EmpleadoDAO();
    	
    	registrosEmpleado = empleadoDAO.buscarPorCURP(curp);
    	
		tieneBajas = registrosEmpleado.stream()
				.filter(empleado -> empleado.getDatoEmpresa().getFechaBaja() != null && empleado.getActivo() == 0)
				.collect(Collectors.toList()).size() > 0;
    	
    	tieneVigente = registrosEmpleado.stream()
    			.filter(empleado -> empleado.getDatoEmpresa().getFechaBaja() == null && empleado.getActivo() == 1)
    			.collect(Collectors.toList()).size() > 0;
		
    	if(tieneVigente && tieneBajas)
    		return true;
    	
    	return false;
    }

	public static void agregarSalarioDiario(DetEmpleado empleado, DetSalarioDiario salarioDiario)
	throws SGPException {
		DetSalarioDiario salarioDiarioVigente = null;
		
		if(empleado == null)
			throw new SGPException("Debe indicar un empleado");
		
		if(empleado.getDatoEmpresa() == null)
			throw new SGPException("No hay información empresarial del empleado.");
		
		if(empleado.getDatoEmpresa().getSalariosDiarios() == null)
			empleado.getDatoEmpresa().setSalariosDiarios(new ArrayList<DetSalarioDiario>());
		
		salarioDiario.setDatoEmpresa(empleado.getDatoEmpresa());
		
		empleado.getDatoEmpresa().getSalariosDiarios().add(salarioDiario);
		
		salarioDiarioVigente = salarioDiarioVigente(empleado.getDatoEmpresa().getSalariosDiarios(), new Date());
		
		empleado.getDatoEmpresa().setSalarioDiario(salarioDiarioVigente.getImporte());
	}
}
