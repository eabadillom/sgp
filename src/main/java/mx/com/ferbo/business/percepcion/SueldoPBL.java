package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaIncidencia;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class SueldoPBL extends PercepcionBL {
	
	private static Logger log = LogManager.getLogger(SueldoPBL.class);
	
	private BigDecimal diasTrabajados = null;
	
	public SueldoPBL(ParametrosNomina parametros, DetNomina nomina, BigDecimal diasTrabajados) {
		super(parametros, nomina);
		this.baseCalculo = this.nomina.getReceptor().getSalarioDiario();
		this.diasTrabajados = diasTrabajados;
	}
	
	public SueldoPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = this.nomina.getReceptor().getSalarioDiario();
	}
	
	@Override
	public BigDecimal calcularLimiteExento() {
		log.info("[UI] Limite exento = {}", _CERO.get());
		return _CERO.get();
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		this.cantidad = this.calcularCantidad(nomina);
		percepcion = this.procesar(nomina, this.cantidad);
		
		return percepcion;
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		try {
			if(cantidad == null)
				throw new SGPException("No se indicó el valor (cantidad).");
			
			List<BigDecimal> salarios = nomina.getIncidencias().stream()
				.map(DetNominaIncidencia::getSalario)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList())
				;
			
			if(salarios.size() > 1) {
				this.importe = this.calcularSalario(nomina.getIncidencias());
			} else {
				this.cantidad = cantidad;
				this.importe = this.calcularImporte(this.cantidad, baseCalculo);
			}
			
			this.calcularExentoGravado();
		} catch(SGPException ex) {
			log.warn("[UI] {}", ex.getMessage());
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
			throw ex;
		} catch(Exception ex) {
			log.error("Problema para generar la percepción...", ex);
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_SUELDO, cantidad, this.importe, importeExento, importeGravado);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		BigDecimal cantidad = null;
		cantidad = nomina.getDiasLaborados();
		log.info("[UI] dias trabajados = {}", this.diasTrabajados);
		return cantidad;
	}
	
	private BigDecimal calcularSalario(List<DetNominaIncidencia> incidencias) {
		BigDecimal salario = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		
		for(DetNominaIncidencia incidencia : incidencias) {
			
			switch (incidencia.getClave()) {
			case "T":
			case "R":
			case "J":
			case "V":
			case "O":
			case "N":
				log.info("Agregando salario por {}: {}", incidencia.getDescripcion(), incidencia.getSalario());
				salario = salario.add(incidencia.getSalario());
				break;
			default:
				break;
			}
		}
		
		return salario;
	}
}
