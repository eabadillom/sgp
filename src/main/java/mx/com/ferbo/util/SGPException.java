package mx.com.ferbo.util;

public class SGPException extends Exception {

    private static final long serialVersionUID = -4246678461721944772L;
    
    public SGPException() {
    	super();
    }

    public SGPException(String message) {
        super(message);
    }
    
    public SGPException(Throwable cause) {
    	super(cause);
    }
    
    public SGPException(String message, Throwable cause) {
    	super(message, cause);
    }
}
