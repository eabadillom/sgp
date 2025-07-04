package mx.com.ferbo.util;

public class SGPSecurityException extends Exception {

	private static final long serialVersionUID = 8214136845394134991L;
	
	public SGPSecurityException() {
		super();
	}
	
	public SGPSecurityException(String message) {
		super(message);
	}
	
	public SGPSecurityException(Throwable cause) {
		super(cause);
	}
	
	public SGPSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

}
