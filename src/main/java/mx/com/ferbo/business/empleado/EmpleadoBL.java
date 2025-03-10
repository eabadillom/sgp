package mx.com.ferbo.business.empleado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatParametro;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpleadoBL {

    private static final Logger log = LogManager.getLogger(EmpleadoBL.class);

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
            int aniotmp = DateUtil.getAnio(empleado.getDatoEmpresa().getFechaIngreso());
            int mestmp = DateUtil.getMes(empleado.getDatoEmpresa().getFechaIngreso());
            int diatmp = DateUtil.getDia(empleado.getDatoEmpresa().getFechaIngreso());
            Date fechaaux = DateUtil.getDate(aniotmp, mestmp, diatmp);
            primerasVacaciones.setFechainicio(fechaaux);
            DateUtil.addYear(fechaaux, 1);
            Date finalaux = DateUtil.addDay(fechaaux, -1);
            primerasVacaciones.setFechafin(finalaux);
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
            fechaaux = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin();
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
                dias = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiastotales();
                fechatmp = empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin();
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

            ultimasvacaciones.setFechainicio(fechainicio);
            ultimasvacaciones.setFechafin(fechafin);
            ultimasvacaciones.setDiastotales(dias);
            ultimasvacaciones.setEmpleado(empleado);
            ultimasvacaciones.setDiastomados(0);
            ultimasvacaciones.setDiaspagados(0);
            ultimasvacaciones.setPrimapagada(Boolean.FALSE);
            ultimasvacaciones.setDiaspendientespagados(Boolean.FALSE);

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
}
