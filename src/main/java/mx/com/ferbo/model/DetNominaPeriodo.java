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
}
