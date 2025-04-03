package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

/**Cálculo de Cesantia en edad avanzada y vejez.<br>
 * Fund. Art. 168 Fracción II LEY DEL SEGURO SOCIAL.<br>
 * Fund. transitorio publicado en el Diario Oficial de la Federación el 16 de diciembre de 2020<br>
 * DECRETO por el que se reforman, adicionan y derogan diversas disposiciones de la
 * Ley del Seguro Social y de la Ley de los Sistemas de Ahorro para el Retiro, Art. Segundo.
 */
public class IMSSCesantiaEdadAvanzadaVejezDBL extends AbstractIMSSDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSCesantiaEdadAvanzadaVejezDBL.class);
	
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio = null;
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias = null;
	private BigDecimal incapacidades = null;
	private BigDecimal sdi = null;
	
	public IMSSCesantiaEdadAvanzadaVejezDBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades,  BigDecimal sdi) {
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.diasTrabajados = diasTrabajados;
		this.ausencias = ausencias;
		this.incapacidades = incapacidades;
		this.sdi = sdi;
	}
	
	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion = null;
		
		BigDecimal cuota = null;
		CatCuotaIMSS tarifaIMSS = null;
		CatTipoDeduccion tdIMSS = null;
		Integer index = null;
		
		try {
			if(this.cuotasIMSS == null)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.cuotasIMSS.size() <= 0)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			tdIMSS = this.getTipoDeduccion(TD_IMSS);
			//TODO La prima del IMSS para CEAV debe obtenerse con base en la tabla del transitorio art. segundo de la LSS.
			//1.0 SM 3.150% de 2023 a 2030
			//1.01 SM a 1.50 UMA ...
			//1.51 UMA A 2.00 UMA ...
			//etc...
			tarifaIMSS = this.getCuotaIMSS("O", "CEAV", this.fechaInicioAnio, this.fechaFinAnio, this.sdi);
			
			//TODO cálculo: SBC x prima (trans. art. segundo LSS) x (diasTrabajados - ausencias - incapacidades)
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					.multiply(diasTrabajados.subtract(ausencias).subtract(incapacidades))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
		} catch(Exception ex) {
			log.error("No es posible calcular la cuota por cesantía en edad avanzada y vejez...", ex);
			cuota = ValoresBD._CERO.get();
		} finally {
			index = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deduccion = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, index))
					.clave(CVE_IMSS)
					.nombre("I.M.S.S. (Cesantía en edad avanzada y vejez)")
					.tipoDeduccion(tdIMSS)
					.importe(cuota)
					.informar(false)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
}
