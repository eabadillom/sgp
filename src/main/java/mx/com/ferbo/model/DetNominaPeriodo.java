package mx.com.ferbo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "det_nomina_periodo")
@NamedQueries({
	@NamedQuery(name = "DetNominaPeriodo.buscarPorEmpresaTipoNominaPeriodicidadAnio", query = "SELECT p FROM DetNominaPeriodo p WHERE p.key.empresa.idEmpresa = :idEmpresa AND p.key.tipoNomina = :tipoNomina AND p.key.periodicidad.periodicidad = :periodicidad AND p.key.anio = :anio ")
})
public class DetNominaPeriodo implements Serializable {

	private static final long serialVersionUID = 564324600227136846L;
	
	@EmbeddedId
	private DetNominaPeriodoPK key;
	
	@Column(name = "fh_periodo_ini")
	@Basic(optional = false)
	private LocalDate periodoInicio;
	
	@Column(name = "fh_periodo_fin")
	@Basic(optional = false)
	private LocalDate periodoFin;
	
	@Column(name = "fh_pago")
	@Basic(optional = false)
	private LocalDate fechaPago;
	
	public DetNominaPeriodo() {
	}
	
	public DetNominaPeriodo(Builder builder) {
		this.key = builder.key;
		this.periodoInicio = builder.periodoInicio;
		this.periodoFin = builder.periodoFin;
		this.fechaPago = builder.fechaPago;
	}

	public DetNominaPeriodoPK getKey() {
		return key;
	}

	public void setKey(DetNominaPeriodoPK key) {
		this.key = key;
	}

	public LocalDate getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(LocalDate periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public LocalDate getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(LocalDate periodoFin) {
		this.periodoFin = periodoFin;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaPeriodo other = (DetNominaPeriodo) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "CatNominaPeriodo [key=" + key + ", periodoInicio=" + periodoInicio + ", periodoFin=" + periodoFin
				+ ", fechaPago=" + fechaPago + "]";
	}
	
	public static class Builder {
		private DetNominaPeriodoPK key;
		private LocalDate periodoInicio;
		private LocalDate periodoFin;
		private LocalDate fechaPago;
		
		public DetNominaPeriodo.Builder key(DetNominaPeriodoPK key) {
			this.key = key;
			return this;
		}
		
		public DetNominaPeriodo.Builder empresa(CatEmpresa empresa) {
			if(this.key == null)
				this.key = new DetNominaPeriodoPK();
			
			this.key.setEmpresa(empresa);
			return this;
		}
		
		public DetNominaPeriodo.Builder tipoNomina(String tipoNomina) {
			if(this.key == null)
				this.key = new DetNominaPeriodoPK();
			
			this.key.setTipoNomina(tipoNomina);
			return this;
		}
		
		public DetNominaPeriodo.Builder periodicidad(CatPeriodicidadPago periodicidad) {
			if(this.key == null)
				this.key = new DetNominaPeriodoPK();
			
			this.key.setPeriodicidad(periodicidad);
			return this;
		}
		
		public DetNominaPeriodo.Builder anio(Integer anio) {
			if(this.key == null)
				this.key = new DetNominaPeriodoPK();
			this.key.setAnio(anio);
			return this;
		}
		
		public DetNominaPeriodo.Builder periodo(Integer periodo) {
			if(this.key == null)
				this.key = new DetNominaPeriodoPK();
			
			this.key.setPeriodo(periodo);
			return this;
		}
		
		public DetNominaPeriodo.Builder periodoInicio(LocalDate periodoInicio) {
			this.periodoInicio = periodoInicio;
			return this;
		}
		
		public DetNominaPeriodo.Builder periodoFin(LocalDate periodoFin) {
			this.periodoFin = periodoFin;
			return this;
		}
		
		public DetNominaPeriodo.Builder fechaPago(LocalDate fechaPago) {
			this.fechaPago = fechaPago;
			return this;
		}
		
		public DetNominaPeriodo build() {
			return new DetNominaPeriodo(this);
		}
		
	}
}
