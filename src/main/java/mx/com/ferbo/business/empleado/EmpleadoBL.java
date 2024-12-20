
package mx.com.ferbo.business.empleado;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.InfDatoEmpresa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

public class EmpleadoBL {
    private static final Logger log = LogManager.getLogger(EmpleadoBL.class);
    
    public static void validarDatosEmpleado(DetEmpleado empleadoporvalidar){
        
        if(empleadoporvalidar== null){
            log.info("El empleado es nulo, se le asigna memoria");
            empleadoporvalidar = new DetEmpleado();
        }
        
        if(empleadoporvalidar.getDomicilio() == null){
            log.info("El domicilio del empleado es nulo, se le asigna memoria");
            empleadoporvalidar.setDomicilio(new DetDomicilioEmpleado());
        }
        
        if(empleadoporvalidar.getEmpleadoConfiguracion() == null){
            log.info("La configuracion del empleado es nula, se le asigna memoria");
            empleadoporvalidar.setEmpleadoConfiguracion(new DetEmpleadoConfiguracion()); 
        }
        
        if(empleadoporvalidar.getEmpleadoFoto() == null){
            log.info("La foto del empleado es nula, se le asigna memoria");
            empleadoporvalidar.setEmpleadoFoto(new DetEmpleadoFoto());
        }
        
        if(empleadoporvalidar.getDatoEmpresa() == null){
            log.info("El dato empresarial del empleado es nulo, se le asigna memoria");
            empleadoporvalidar.setDatoEmpresa(new InfDatoEmpresa());
        }
        
    }
}
