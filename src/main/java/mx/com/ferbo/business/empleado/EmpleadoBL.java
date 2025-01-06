package mx.com.ferbo.business.empleado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        if (empleado.getVacaciones().isEmpty()) {
            fechaaux = DateUtil.addYear(fechaaux, 1);
        } else {
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

                dias = 12;
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

    public static void actualizarDiasTomados(DetEmpleado empleado, List<Date> diassolicitados) throws SGPException {

        int[] diasporperiodo = {0, 0};

        if (empleado.getVacaciones().size() > 1) {

            for (Date dia : diassolicitados) {
                if (dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechainicio()) || dia.after(empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechainicio()) && dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechafin()) || dia.before(empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechafin())) {
                    diasporperiodo[0]++;
                }

                if (dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechainicio()) || dia.after(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechainicio()) && dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin()) || dia.before(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin())) {
                    diasporperiodo[1]++;
                }
            }

            int actual = diasporperiodo[0] + empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getDiastomados();
            int siguiente = diasporperiodo[1] + empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiastomados();
            int banderaactual = 0;
            int banderasiguiente = 0;

            if (actual > empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getDiastotales()) {
                banderaactual = 1;
            }

            if (siguiente > empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiastotales()) {
                banderasiguiente = 1;
            }

            if (banderaactual == 1 && banderasiguiente == 1) {
                throw new SGPException("No se actualizaron los dias de vacaciones, revise sus periodos de vacaciones");
            }

            if (banderaactual == 0 && banderasiguiente == 1) {
                empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).setDiastomados(actual);
                throw new SGPException("No se actualizaron los dias de vacaciones dentro del perdiodo " + empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechainicio() + " al " + empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin());
            }

            if (banderaactual == 1 && banderasiguiente == 0) {
                empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).setDiastomados(siguiente);
                throw new SGPException("No se actualizaron los dias de vacaciones dentro del perdiodo " + empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechainicio() + " al " + empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).getFechafin());
            }

            empleado.getVacaciones().get(empleado.getVacaciones().size() - 2).setDiastomados(actual);
            empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).setDiastomados(siguiente);

        } else {

            if (empleado.getVacaciones().size() == 1) {

                int nuevosdias = 0;

                List<Date> diasnopermitidos = new ArrayList<Date>();

                for (Date dia : diassolicitados) {
                    if (dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechainicio()) || dia.after(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechainicio()) && dia.equals(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin()) || dia.before(empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getFechafin())) {
                        nuevosdias++;
                    } else {
                        diasnopermitidos.add(dia);
                    }
                }
                nuevosdias = nuevosdias + empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiastomados();

                if (nuevosdias > empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).getDiastotales()) {
                    throw new SGPException("El empleado supera el numero de dias permitidos para vacaciones");
                }

                if (!diasnopermitidos.isEmpty()) {
                    empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).setDiastomados(nuevosdias);
                    throw new SGPException("Los dias: " + diasnopermitidos + " no fueron agregados por estar fuera del periodo vacional permitido.");
                }

                empleado.getVacaciones().get(empleado.getVacaciones().size() - 1).setDiastomados(nuevosdias);

            } else {

                throw new SGPException("El empleado aun no tiene derecho a vacaciones");

            }
        }

    }

    public static void actualizarPagoVacacionesConcepto(DetEmpleado empleado, Date periodoporpagar, String concepto) {

        for (int i = 0; i < empleado.getVacaciones().size(); i++) {

            Date fechainicio = empleado.getVacaciones().get(i).getFechainicio();
            Date fechafin = empleado.getVacaciones().get(i).getFechafin();

            if (periodoporpagar.after(fechainicio) && periodoporpagar.before(fechafin)) {

                if (concepto.equals("ambas")) {
                    if (empleado.getVacaciones().get(i).getDiaspendientespagados() == false && empleado.getVacaciones().get(i).getPrimapagada() == false) {
                        empleado.getVacaciones().get(i).setPrimapagada(Boolean.TRUE);
                        empleado.getVacaciones().get(i).setDiaspendientespagados(Boolean.TRUE);
                        break;
                    }
                }

                if (concepto.equals("prima")) {
                    if (empleado.getVacaciones().get(i).getPrimapagada() == false) {
                        empleado.getVacaciones().get(i).setPrimapagada(Boolean.TRUE);
                        break;
                    }
                }

                if (concepto.equals("dias pendientes")) {
                    if (empleado.getVacaciones().get(i).getDiaspendientespagados() == false) {
                        empleado.getVacaciones().get(i).setDiaspendientespagados(Boolean.TRUE);
                        break;
                    }
                }

            }

        }

    }

}
