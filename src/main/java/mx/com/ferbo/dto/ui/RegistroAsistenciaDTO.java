package mx.com.ferbo.dto.ui;

import java.util.Date;

import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetRegistroVacaciones;
import mx.com.ferbo.model.DetSalarioDiario;

public class RegistroAsistenciaDTO extends DetRegistro {

	private static final long serialVersionUID = -1577569812237025319L;
	
	private DetSalarioDiario salario;

	private RegistroAsistenciaDTO(Builder builder) {
		this.idRegistro = builder.idRegistro;
		this.fecha = builder.fecha;
		this.fechaEntrada = builder.fechaEntrada;
		this.fechaSalida = builder.fechaSalida;
		this.status = builder.status;
		this.empleado = builder.empleado;
		this.registroVacaciones = builder.registroVacaciones;
		this.salario = builder.salario;
	}
	
	public RegistroAsistenciaDTO(Integer idRegistro, Date fecha, Date fechaEntrada, Date fechaSalida, CatEstatusRegistro status, DetEmpleado empleado, DetRegistroVacaciones registroVacaciones, DetSalarioDiario salario) {
		this.idRegistro = idRegistro;
		this.fecha = fecha;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.status = status;
		this.empleado = empleado;
		this.registroVacaciones = registroVacaciones;
		this.salario = salario;
	}
	
	public DetSalarioDiario getSalario() {
		return salario;
	}

	public void setSalario(DetSalarioDiario salario) {
		this.salario = salario;
	}

	public static final class Builder {
		private Integer idRegistro;
		private Date fecha;
		private Date fechaEntrada;
		private Date fechaSalida;
		private CatEstatusRegistro status;
		private DetEmpleado empleado;
		private DetRegistroVacaciones registroVacaciones;
		private DetSalarioDiario salario;

		public RegistroAsistenciaDTO.Builder idRegistro(Integer idRegistro) {
			this.idRegistro = idRegistro;
			return this;
		}

		public RegistroAsistenciaDTO.Builder fecha(Date fecha) {
			this.fecha = fecha;
			return this;
		}

		public RegistroAsistenciaDTO.Builder fechaEntrada(Date fechaEntrada) {
			this.fechaEntrada = fechaEntrada;
			return this;
		}

		public RegistroAsistenciaDTO.Builder fechaSalida(Date fechaSalida) {
			this.fechaSalida = fechaSalida;
			return this;
		}

		public RegistroAsistenciaDTO.Builder status(CatEstatusRegistro status) {
			this.status = status;
			return this;
		}

		public RegistroAsistenciaDTO.Builder empleado(DetEmpleado empleado) {
			this.empleado = empleado;
			return this;
		}

		public RegistroAsistenciaDTO.Builder registroVacaciones(DetRegistroVacaciones registroVacaciones) {
			this.registroVacaciones = registroVacaciones;
			return this;
		}

		public RegistroAsistenciaDTO.Builder salario(DetSalarioDiario salario) {
			this.salario = salario;
			return this;
		}

		public RegistroAsistenciaDTO build() {
			return new RegistroAsistenciaDTO(this);
		}
	}

}
