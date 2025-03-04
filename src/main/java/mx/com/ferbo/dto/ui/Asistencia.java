package mx.com.ferbo.dto.ui;

import java.util.Date;

public class Asistencia {
	private String diaSemana;
	private Date entrada;
	private Date salida;
	private AsistenciaStatus status;
	
	public Date getEntrada() {
		return entrada;
	}
	public Date getSalida() {
		return salida;
	}
	public AsistenciaStatus getStatus() {
		return status;
	}
	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}
	public void setSalida(Date salida) {
		this.salida = salida;
	}
	public void setStatus(AsistenciaStatus status) {
		this.status = status;
	}
	public String getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}
}
