/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Julio
 */
public class DataSourceManager {
    
    private static Logger log = LogManager.getLogger(DataSourceManager.class);
    
    public static String getJndiParameter(String name) {
	    Context initContext = null;
		String parameter = null;
	    try {
            initContext = new InitialContext();
            parameter = (String) initContext.lookup(name);
            
        } catch (NamingException ex) {
            try {
            	if(initContext == null)
            		initContext = new InitialContext();
            	
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                parameter = (String) envContext.lookup(name);
            } catch(NamingException inEx) {
                log.error("Problema para obtener el valor JNDI: " + name, inEx);
            }
        }
	    
	    return parameter;
	}
}
