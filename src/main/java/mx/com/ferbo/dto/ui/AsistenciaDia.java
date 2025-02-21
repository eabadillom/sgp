package mx.com.ferbo.dto.ui;

import java.time.OffsetDateTime;

public class AsistenciaDia {
	private OffsetDateTime entrada;
	private OffsetDateTime salida;
	private AsistenciaStatus status;
	
	public OffsetDateTime getEntrada() {
		return entrada;
	}
	public OffsetDateTime getSalida() {
		return salida;
	}
	public AsistenciaStatus getStatus() {
		return status;
	}
	public void setEntrada(OffsetDateTime entrada) {
		this.entrada = entrada;
	}
	public void setSalida(OffsetDateTime salida) {
		this.salida = salida;
	}
	public void setStatus(AsistenciaStatus status) {
		this.status = status;
	}
}
