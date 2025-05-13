package mx.com.ferbo.util;

/**
 *
 * @author alberto
 */

enum StatusClass 
{
    QUALIFIED, //Verde
    UNQUALIFIED, //Rojo
    PROPOSAL, //Melon
    NEGOTIATION,//Amarillo
    NEW, //Azul
    RENEWAL; //Morado 
}

enum StatusText 
{
    ENVIADA,
    APROBADA,
    RECHAZADA,
    CANCELADA;
}

public class ManageStatus 
{

    public ManageStatus() 
    {
    }
    
    public String getStatusClass(String uniforme) 
    {
        String mensaje = "";
        
        switch(uniforme)
        {
            case "E":
                mensaje = StatusClass.PROPOSAL.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public String getStatusText(String uniforme) 
    {
        String mensaje = "";
        switch(uniforme)
        {
            case "E":
                mensaje = StatusText.ENVIADA.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusText.APROBADA.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusText.RECHAZADA.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusText.CANCELADA.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public static String getEstadoEmpleadoEmpresa(short unstatus) 
    {
        String mensaje = "";
        
        switch(unstatus)
        {
            case 0:
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case 1:
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case 2:
                mensaje = StatusClass.NEGOTIATION.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public String getEstatusIncapacidad(String clave)
    {
        String mensaje = "";
        
        switch(clave)
        {
            case "A":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
        }
        
        return mensaje;
    }
    
    public String getEstatusRegistro(String clave)
    {
        String mensaje = "";
        
        switch(clave)
        {
            case "T":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();//A tiempo
                break;
            case "R":
                mensaje = StatusClass.NEGOTIATION.toString().toLowerCase();//Retardo
                break;
            case "J":
                mensaje = StatusClass.PROPOSAL.toString().toLowerCase();//Retardo
                break;
            case "F":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();//Falta
                break;
        }
        
        return mensaje;
    }
}
