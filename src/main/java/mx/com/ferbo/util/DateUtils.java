package mx.com.ferbo.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**Esta clase agrupa varios metodos para la operacion con fechas.
 * Los dos métodos principales son getFechaLiquidacion y esDiaAsueto,
 * ambos consultan a un archivo DateUtils.properties o directamente
 * a la Base de Datos de Scrittura cuáles son los días de asueto que
 * se han declarado previamente.
 * @author Esteban Antonio Badillo Martinez
 * @version 1.0
 */
public class DateUtils {
	
	private static Logger log = LogManager.getLogger(DateUtils.class);

	public static String PREF_FECHA        = "fecha"; //Para lectura del .properties
	public static String PREF_ASUETO       = "asueto"; //Para lectura del .properties
	public static String PREF_DIA_ESPECIAL = "especial"; //Para lectura del .properties
	
	public static final String FORMATO_DD_MM_YYYY          = "dd/MM/yyyy";
	public static final String FORMATO_D_MMM_YYYY          = "d MMM yyyy";
	public static final String FORMATO_YYYY_MM_DD          = "yyyy-MM-dd";
	public static final String FORMATO_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss.SSS";
	public static final String FORMATO_ISO_Z = "yyyy-MM-dd'T'hh:mm:ss.SSSZ";
	public static final String FORMATO_DD_MM_YYYY_FULL     = "dd MMMM yyyy";
	
	public static final int ENERO      = Calendar.JANUARY;
	public static final int FEBRERO    = Calendar.FEBRUARY;
	public static final int MARZO      = Calendar.MARCH;
	public static final int ABRIL      = Calendar.APRIL;
	public static final int MAYO       = Calendar.MAY;
	public static final int JUNIO      = Calendar.JUNE;
	public static final int JULIO      = Calendar.JULY;
	public static final int AGOSTO     = Calendar.AUGUST;
	public static final int SEPTIEMBRE = Calendar.SEPTEMBER;
	public static final int OCTUBRE    = Calendar.OCTOBER;
	public static final int NOVIEMBRE  = Calendar.NOVEMBER;
	public static final int DICIEMBRE  = Calendar.DECEMBER;
	
	public static final int AM         = Calendar.AM;
	public static final int PM         = Calendar.PM;
	
	public static String PROP_ENERO      = "enero"; //Para lectura del .properties
	public static String PROP_FEBRERO    = "febrero"; //Para lectura del .properties
	public static String PROP_MARZO      = "marzo"; //Para lectura del .properties
	public static String PROP_ABRIL      = "abril"; //Para lectura del .properties
	public static String PROP_MAYO       = "mayo"; //Para lectura del .properties
	public static String PROP_JUNIO      = "junio"; //Para lectura del .properties
	public static String PROP_JULIO      = "julio"; //Para lectura del .properties
	public static String PROP_AGOSTO     = "agosto"; //Para lectura del .properties
	public static String PROP_SEPTIEMBRE = "septiembre"; //Para lectura del .properties
	public static String PROP_OCTUBRE    = "octubre"; //Para lectura del .properties
	public static String PROP_NOVIEMBRE  = "noviembre"; //Para lectura del .properties
	public static String PROP_DICIEMBRE  = "diciembre"; //Para lectura del .properties
	
	public static String PROP_CD_DOMINGO   = "D";
	public static String PROP_CD_LUNES     = "L";
	public static String PROP_CD_MARTES    = "M";
	public static String PROP_CD_MIERCOLES = "MI";
	public static String PROP_CD_JUEVES    = "J";
	public static String PROP_CD_VIERNES   = "V";
	public static String PROP_CD_SABADO    = "S";
	
	public static String[] PROP_DIA_SEMANA = {PROP_CD_DOMINGO, PROP_CD_LUNES, PROP_CD_MARTES, PROP_CD_MIERCOLES, PROP_CD_JUEVES, PROP_CD_VIERNES, PROP_CD_SABADO}; 
	
	public static final String KEY_SEPARADOR_DIA = " ";//Para lectura del .properties
	
	public static Date now() {
		Date resultado = null;
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT-06:00"), Locale.getDefault());
		c.setTimeZone(TimeZone.getTimeZone("GMT-06:00"));
		resultado = new Date(c.getTimeInMillis());
		return resultado;
	}
	
	/**Metodo para agregar dias a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar dias.
	 * @param dias Numero de dias a agregar.
	 * @return Nuevo objeto fecha con los dias agregados.
	 */
	public static Date addDay(Date fecha, int dias){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT-06:00"), Locale.getDefault());
		c.setTime(fecha);
		c.add(Calendar.DATE, dias);
		
		resultado = new Date(c.getTimeInMillis()); 
		
		return resultado;
	}
	
	/**Metodo para agregar meses a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar meses.
	 * @param meses Numero de meses a agregar.
	 * @return Nuevo objeto fecha con los meses agregados.
	 */
	public static Date addMonth(Date fecha, int meses){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.MONTH, meses);
		
		resultado = new Date(c.getTimeInMillis());
		
		return resultado;
	}
	
	/**Metodo para agregar años a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar años.
	 * @param anios Numero de años a agregar.
	 * @return Nuevo objeto fecha con los años agregados.
	 */
	public static Date addYear(Date fecha, int anios){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.YEAR, anios);
		
		resultado = new Date(c.getTimeInMillis());
		
		return resultado;
	}
	
	
	
	
	
	
	
	
	public static Date fechaVencimiento(Date fecha, int diasVencimiento, boolean esVigenciaNatural){
		Date vencimiento = null;
		Date fechaAux = null;
		
		vencimiento = new Date(fecha.getTime());
		fechaAux = new Date(fecha.getTime());
		
		int dia = -1;
		int mes = -1;
		int anio = -1;
		
		GregorianCalendar c = new GregorianCalendar();
		
		if(diasVencimiento == 30 && (!esVigenciaNatural) ) {
			
			vencimiento = DateUtils.addMonth(vencimiento, 1);
			dia = DateUtils.getDia(fechaAux);
			mes = DateUtils.getMes(fechaAux);
			anio = DateUtils.getAnio(fechaAux);
			
			if(c.isLeapYear(anio)) {
				if( !(mes == DateUtils.ENERO && dia > 29) ){
					vencimiento = DateUtils.addDay(vencimiento, -1);
				} 
			} else {
				if( !(mes == DateUtils.ENERO && dia > 28)) {
					vencimiento = DateUtils.addDay(vencimiento, -1);
				}
			}
		} else {
			vencimiento = DateUtils.addDay(vencimiento, diasVencimiento);
		}
		
		return vencimiento;
	}
	
	
	
	/**Devuelve el año en formato numérico del objeto {@link Date} dado.
	 * @param fecha {@link Date} con la fecha.
	 * @return Representación numérica del año.
	 */
	public static int getAnio(Date fecha){
		int anio = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		anio = cal.get(Calendar.YEAR);
		
		return anio;
	}
	
	/**Devuelve la representación del mes en un formato String a partir de su
	 * representación Numérica. Vea las constantes de mes para la clase {@link DateUtils}
	 * @param mes Representación Numérica del mes [0-11],<br>
	 * Donde:<br>
	 * 	<li>0 = "Enero"</li>
	 * 	<li>1 = "Febrero"</li>
	 * 	<li>2 = "Marzo"</li>
	 * 	<li>3 = "Abril"</li>
	 *	<li>etc...</li><br>
	 * Vea también las <i>constantes de mes</i> de la clase {@link DateUtils}
	 * @return Representación en un objeto {@link String} del mes proporcionado
	 * en el parámetro <strong>int mes</strong>.
	 * @throws DateUtilsException Se devuelve cuando se introduce un valor
	 * incorrecto para el parámetro mes.
	 */
	public static String getMes(int mes)
	throws DateUtilsException{
		String strMes = null;
		switch(mes){
		case ENERO: strMes = PROP_ENERO; break;
		case FEBRERO: strMes = PROP_FEBRERO; break;
		case MARZO: strMes = PROP_MARZO; break;
		case ABRIL: strMes = PROP_ABRIL; break;
		case MAYO: strMes = PROP_MAYO; break;
		case JUNIO: strMes = PROP_JUNIO; break;
		case JULIO: strMes = PROP_JULIO; break;
		case AGOSTO: strMes = PROP_AGOSTO; break;
		case SEPTIEMBRE: strMes = PROP_SEPTIEMBRE; break;
		case OCTUBRE: strMes = PROP_OCTUBRE; break;
		case NOVIEMBRE: strMes = PROP_NOVIEMBRE; break;
		case DICIEMBRE: strMes = PROP_DICIEMBRE; break;
		default:
			throw new DateUtilsException("El mes no es válido: " + mes);
		}
		return strMes;
	}
	
	/**Devuelve el mes en formato numérico [0-11] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Mes en formato numérico [0-11] del objeto. Vea también las <i>constantes
	 * de mes</i> de la clase {@link DateUtils}
	 */
	public static int getMes(Date fecha){
		int mes = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		mes = cal.get(Calendar.MONTH);
		
		return mes;
	}
	
	/**Devuelve el dia, en formato numérico [1-31] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Dia en formato numérico [1-31]. El rango puede variar, según sea el mes
	 * que se haya establecido en el parámetro de entrada.
	 */
	public static int getDia(Date fecha){
		int dia = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		dia = cal.get(Calendar.DATE);
		
		return dia;
	}
	
	public static String getDiaSemana(Date fecha) {
		String sDia = null;
		int dia = -1;
		Calendar cal = null;
		
		try {
			cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
			cal.setTime(fecha);
			dia = cal.get(Calendar.DAY_OF_WEEK);
			sDia = PROP_DIA_SEMANA[dia - 1];
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return sDia;
	}

	/**Devuelve la hora, en formato numérico [0-23] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Hora en formato numérico [0-23].
	 */
	public static int getHora(Date fecha) {
		int hora = -1;		
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		hora = cal.get(Calendar.HOUR_OF_DAY);
		
		return hora;
	}
	
	/**Devuelve los minutos, en formato numérico [0-59] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Minutos en formato numérico [0-59].
	 */
	public static int getMinuto(Date fecha){
		int minuto = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		minuto = cal.get(Calendar.MINUTE);
		
		return minuto;
	}
	
	/**Devuelve los segundos, en formato numérico [0-59] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Segundos en formato numérico [0-59].
	 */
	public static int getSegundo(Date fecha){
		int segundo = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		segundo = cal.get(Calendar.SECOND);
		
		return segundo;
	}
	
	/**Devuelve los milisegundos en formato numérico [0-999] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Milisegundos en formato numérico [0-999].
	 */
	public static int getMilisegundo(Date fecha) {
		int milisegundo = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		milisegundo = cal.get(Calendar.MILLISECOND);
		
		return milisegundo;
	}
	
	/**Establece valor del año para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el año.
	 * @param anio El nuevo valor para el Año que se establecerá en el parametro {@link Date}.
	 */
	public static void setAnio(Date fecha, int anio){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.YEAR, anio);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor del mes para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el mes.
	 * @param mes El nuevo valor para el mes que se establecerá en el parámetro {@link Date}.
	 * Vea las constantes de la clase {@link DateUtils} para establecer correctamente el mes.
	 */
	public static void setMes(Date fecha, int mes){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MONTH, mes);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor pdel día para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el dia.
	 * @param dia El nuevo valor para el día que se establecerá en el parámetro {@link Date}.
	 */
	public static void setDia(Date fecha, int dia){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.DAY_OF_MONTH, dia);
		
		fecha.setTime(cal.getTimeInMillis());
		
	}
	
	/**Establece el valor de la hora para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para la hora.
	 * @param hora El nuevo valor para la hora que se establecerá en el parámetro {@link Date}.
	 */
	public static void setHora(Date fecha, int hora){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los minutos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param minuto El nuevo valor para los minutos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setMinuto(Date fecha, int minuto){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MINUTE, minuto);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los segundos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param segundo El nuevo valor para los segundos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setSegundo(Date fecha, int segundo){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.SECOND, segundo);

		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los milisegundos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param milisegundo El nuevo valor para los milisegundos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setMilisegundo(Date fecha, int milisegundo){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MILLISECOND, milisegundo);

		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Devuelve un objeto {@link Date} con el día, mes y año que se especifiquen.
	 * @param year Año en formato numérico (int).
	 * @param month Mes en formato numérico (int) [0-11]. Vea también las constantes de mes
	 * para la clase {@link DateUtils}.
	 * @param date Dia en formato numérico (int) [1-31].
	 * @return Objeto {@link Date} con la fecha establecida en los parámetros.
	 */
	public static Date getDate(int year, int month, int date){
		Date     fecha = null;
		Calendar cal = null;
		log.debug("Default Time Zone: {}", TimeZone.getDefault());
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.set(year, month, date);
		fecha = cal.getTime();
		
		return fecha;
	}
	
	public static Date getDate(int year, int month, int date, int hour, int minute, int second){
		Date     fecha = null;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.set(year, month, date, hour, minute, second);
		fecha = cal.getTime();
		
		return fecha;
	}
	
	public static void setTime(Date fecha, int hour, int minute, int second){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	public static void setTime(Date fecha, int hour, int minute, int second, int millisecond){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-06:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	public static void setTime(Date fecha, int hour, int AM_PM, int minute, int second, int millisecond) {
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.AM_PM, AM_PM);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Metodo que establece la hora en 00:00:00.00 de una fecha dada. 
	 * @param fecha
	 */
	public static void resetTime(Date fecha){
		setTime(fecha, 0, 0, 0, 0);
	}
	
	/**Método para obtener un {@link String} en un {@link Date}. La cadena de entrada
	 * debe contener una fecha con el formato dd/MM/yyyy<br>
	 * Donde:
	 * <li>dd  : Dia (En formato numérico [0-9])</li>
	 * <li>MM  : Mes (En formato numérico [01-12], donde 01 = Enero, 02 = Febrero, ...)</li>
	 * <li>yyyy: Año (En formato numérico de cuatro dígitos).</li><br><br>
	 * @param strFecha {@link String} con la cadena que representa la fecha.
	 * @return Objeto {@link Date} con la representación de la cadena.
	 */
	public static Date getDate(String strFecha, String formato)
	//throws DateUtilsException
	{
		Date             fecha = null;
		//String           formato = null;
		SimpleDateFormat dateFormat = null;
		
		//formato = "dd/MM/yyyy";
		dateFormat = new SimpleDateFormat(formato);
		
		try{
			fecha = dateFormat.parse(strFecha);
		} catch( ParseException ex) {
			//TODO Por establecer que acción procede en este caso.
		}
		
		return fecha;
	}
	
	/**Metodo para respresentar un objeto {@link Date} en un {@link String} con
	 * el formato dd/MM/yyyy (o en cualquier otro orden).<br>
	 * Donde:<br>
	 * <ul>
	 * <li>   dd  : Dia (En formato numérico [0-9])</li>
	 * <li>   MM  : Mes (En formato numérico [01-12], donde 01 = Enero, 02 = Febrero, ...)</li>
	 * <li>   yyyy: Año (En formato numérico de cuatro dígitos)</li><br><br>
	 * </ul>
	 * @param fecha Objeto {@link Date}
	 * @return Objeto {@link String} con la representación en texto del objeto {@link Date}
	 */
	public static String getString(Date fecha, String formato)
	throws DateUtilsException {
		String           strFecha   = null;
		SimpleDateFormat dateFormat = null;
		
		dateFormat = new SimpleDateFormat(formato);
		if(fecha == null)
			throw new DateUtilsException("El parámetro fecha no debe ser nulo");
		strFecha = dateFormat.format(fecha);
		
		return strFecha;
	}
	
	/**Devuelve en base a la fecha proporcionada, el último día del mes.
	 * @param fecha
	 * @return
	 */
	public static Date getLastDayOfMonth(Date fecha){
		Date lastDayOfMonth = null;
		GregorianCalendar cal = null;
		int mes = -1;
		int lastDay = -1;
		
		cal = new GregorianCalendar();
		cal.setTime(fecha);
		mes = cal.get(Calendar.MONTH);
		
		switch(mes){
		case ENERO: lastDay = 31; break;
		case FEBRERO:
			if(cal.isLeapYear(cal.get(GregorianCalendar.YEAR)))
				lastDay = 29;
			else
				lastDay = 28;
			break;
		case MARZO: lastDay = 31; break;
		case ABRIL: lastDay = 30; break;
		case MAYO: lastDay = 31; break;
		case JUNIO: lastDay = 30; break;
		case JULIO: lastDay = 31; break;
		case AGOSTO: lastDay = 31; break;
		case SEPTIEMBRE: lastDay = 30; break;
		case OCTUBRE: lastDay = 31; break;
		case NOVIEMBRE: lastDay = 30; break;
		case DICIEMBRE: lastDay = 31; break;
		}
		
		cal.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
		
		lastDayOfMonth = new Date(cal.getTimeInMillis());
		
		return lastDayOfMonth;
	}
	
	public static Date getFirstDayOfMonth(Date fecha){
		Date firstDayOfMonth = null;
		GregorianCalendar cal = null;
		int firstDay = 1;
		
		cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(GregorianCalendar.DAY_OF_MONTH, firstDay);
		firstDayOfMonth = new Date(cal.getTimeInMillis());
		
		return firstDayOfMonth;
	}
	
	/**Formatea un valor de tiempo en Milisegundos
	   * @param elapsedTimeMillis Tiempo a formatear
	   * @return Tiempo formateado
	   */
	  public static String formatElapsedTime(long elapsedTimeMillis) {

	    SimpleDateFormat dateFormat = new SimpleDateFormat("H 'hrs' m 'min' s 'seg' SSS 'ms'");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return dateFormat.format(new Date(elapsedTimeMillis));
	  }
	  
	/**Devuelve la diferencia en dias de dos fechas. De preferencia, el primer parametro (fechaIni)
	 * debera ser la fecha menor, mientras que el segundo parametro debera ser la fecha mayor.
	 * @param fechaIni
	 * @param fechaFin
	 * @return dias de diferencia.
	 */
	public static int daysDiff(Date fechaIni, Date fechaFin) {
		int days = 0;
        long dayIni = 0L;
        long dayFin = 0L;
        double daysD = 0.0D;
        BigDecimal bdDias = null;
        dayIni = fechaIni.getTime();
        dayFin = fechaFin.getTime();
        daysD = (double)(dayFin - dayIni) / (double)(24 * 60 * 60 * 1000);
        
        bdDias = new BigDecimal(daysD);
        bdDias = bdDias.setScale(1, BigDecimal.ROUND_HALF_UP);
        days = bdDias.intValue();
        days++;
        
        return days;
		
	}
	
	public static boolean isDateBetween(Date fecha, Date fechaIni, Date fechaFin){
		boolean resultado = false;
		
		long lFecha = fecha.getTime();
		long lFechaIni = fechaIni.getTime();
		long lFechaFin = fechaFin.getTime();
		
		if(lFecha >= lFechaIni && lFecha <= lFechaFin)
			resultado = true;
		
		return resultado;
	}
}