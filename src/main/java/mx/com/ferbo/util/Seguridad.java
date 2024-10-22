
package mx.com.ferbo.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Seguridad {
    
    private static final Logger log = LogManager.getLogger(Seguridad.class);
    private static BCryptPasswordEncoder passwordEncoder;    

    private BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    private void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
        
    
    public static String Cifrar(String psw) throws SGPException{
        String  cifrada = "";
        try{
        passwordEncoder = new BCryptPasswordEncoder();
        cifrada = passwordEncoder.encode(psw);
        }
        catch(Exception ex){
            log.info("Error: no se pudo cifrar la contrasenia dada.");
            throw new SGPException("La contrasenia no se pudo cifrar");
        }
        return cifrada;
    }
    
    public static boolean verificarCifrado(String psw1, String psw2){
    
        passwordEncoder = new BCryptPasswordEncoder();
        
        return passwordEncoder.matches(psw1, psw2);
        
    }
  
}
