package mx.com.ferbo.util;

/**
 *
 * @author alberto
 */

enum StatusClass 
{
    QUALIFIED,
    UNQUALIFIED,
    PROPOSAL,
    NEGOTIATION;
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
    
    public String getStatusClass(short uniforme) 
    {
        String mensaje = "";
        
        switch(uniforme)
        {
            case 1:
                mensaje = StatusClass.PROPOSAL.toString().toLowerCase();
                break;
            case 2:
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case 3:
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case 4:
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public String getStatusText(short uniforme) 
    {
        String mensaje = "";
        switch(uniforme)
        {
            case 1:
                mensaje = StatusText.ENVIADA.toString().toLowerCase();
                break;
            case 2:
                mensaje = StatusText.APROBADA.toString().toLowerCase();
                break;
            case 3:
                mensaje = StatusText.RECHAZADA.toString().toLowerCase();
                break;
            case 4:
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
}
