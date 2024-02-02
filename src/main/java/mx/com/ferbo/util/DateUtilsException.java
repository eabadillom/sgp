package mx.com.ferbo.util;

public class DateUtilsException extends Exception{
	
	/**
	 *
	 */
   private static final long serialVersionUID = -8874890513230067507L;

   /**
	 *
	 */
   public DateUtilsException() {
	super();
   }

   /**
    * 
    * @param message
    *            mensaje
    */
   public DateUtilsException(String message) {
	super(message);
   }

   /**
    * 
    * @param cause
    *            causa
    */
   public DateUtilsException(Throwable cause) {
	super(cause);
   }

   /**
    * 
    * @param message
    *            mensaje
    * @param cause
    *            causa
    */
   public DateUtilsException(String message, Throwable cause) {
	super(message, cause);
   }
}