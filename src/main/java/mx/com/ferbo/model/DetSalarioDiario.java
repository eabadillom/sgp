package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_salario_diario")
public class DetSalarioDiario implements Serializable {

	private static final long serialVersionUID = -7220894982062698385L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_emp_salario")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_empleado_empresa", referencedColumnName = "id_empleado_empresa")
	private InfDatoEmpresa datoEmpresa;
	
	@Column(name = "nu_salario_diario", precision = 10, scale = 2)
	private BigDecimal importe;
	
	@Column(name = "fh_registro")
	private Date fechaRegistro;
	
	public DetSalarioDiario() {
	}

	private DetSalarioDiario(Builder builder) {
		this.id = builder.id;
		this.datoEmpresa = builder.datoEmpresa;
		this.importe = builder.importe;
		this.fechaRegistro = builder.fechaRegistro;
	}

	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetSalarioDiario other = (DetSalarioDiario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DetSalarioDiario [id=" + id + ", importe=" + importe + ", fechaRegistro=" + fechaRegistro
				+ "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InfDatoEmpresa getDatoEmpresa() {
		return datoEmpresa;
	}

	public void setDatoEmpresa(InfDatoEmpresa datoEmpresa) {
		this.datoEmpresa = datoEmpresa;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	/* Elementos del patrón Builder */
	public static final class Builder {
		private Integer id;
		private InfDatoEmpresa datoEmpresa;
		private BigDecimal importe;
		private Date fechaRegistro;

		public Builder() {
		}

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder datoEmpresa(InfDatoEmpresa datoEmpresa) {
			this.datoEmpresa = datoEmpresa;
			return this;
		}

		public Builder importe(BigDecimal importe) {
			this.importe = importe;
			return this;
		}

		public Builder fechaRegistro(Date fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
			return this;
		}

		public DetSalarioDiario build() {
			return new DetSalarioDiario(this);
		}
	}

}
