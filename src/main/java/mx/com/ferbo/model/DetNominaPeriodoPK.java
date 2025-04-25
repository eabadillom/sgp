package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class DetNominaPeriodoPK implements Serializable {

	private static final long serialVersionUID = -4217587598012942767L;
	
	@JoinColumn(name = "id_empresa")
	@ManyToOne
	@NotNull
	private CatEmpresa empresa;
	
	@Column(name = "tp_nomina")
	@Basic(optional = false)
	@Size(min = 1, max = 1)
	private String tipoNomina;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "cd_periodicidad")
	private CatPeriodicidadPago periodicidad;
	
	@Column(name = "nu_anio")
	@Basic(optional = false)
	private Integer anio;
	
	@Column(name = "nu_periodo")
	@Basic(optional = false)
	private Integer periodo;
	
	public DetNominaPeriodoPK() {
	}

	public DetNominaPeriodoPK(CatEmpresa empresa, String tipoNomina, CatPeriodicidadPago periodicidad, Integer anio, Integer periodo) {
		super();
		this.empresa = empresa;
		this.tipoNomina = tipoNomina;
		this.periodicidad = periodicidad;
		this.anio = anio;
		this.periodo = periodo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(anio, empresa, periodicidad, periodo, tipoNomina);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaPeriodoPK other = (DetNominaPeriodoPK) obj;
		return Objects.equals(anio, other.anio) && Objects.equals(empresa, other.empresa)
				&& Objects.equals(periodicidad, other.periodicidad) && Objects.equals(periodo, other.periodo)
				&& Objects.equals(tipoNomina, other.tipoNomina);
	}

	@Override
	public String toString() {
		return "CatNominaPeriodoPK [tipoNomina=" + tipoNomina + ", anio=" + anio + ", periodo=" + periodo + "]";
	}
	
	public CatEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}
	
	public String getTipoNomina() {
		return tipoNomina;
	}

	public void setTipoNomina(String tipoNomina) {
		this.tipoNomina = tipoNomina;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}


	public CatPeriodicidadPago getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(CatPeriodicidadPago periodicidad) {
		this.periodicidad = periodicidad;
	}
}
