package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoDeduccion;

@Entity
@Table(name = "det_nom_deduccion")
public class DetNominaDeduccion implements Serializable {

	private static final long serialVersionUID = 4306393726440622025L;
	
	@EmbeddedId
	private DetNominaDeduccionPK key;
	
	@ManyToOne
	@JoinColumn(name = "tp_deduccion", referencedColumnName = "cd_tipo_deduccion")
	@Basic(optional = false)
	private CatTipoDeduccion tipoDeduccion;
	
	@Column(name = "cd_deduccion")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_deduccion")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nu_importe", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importe;
	
	@Column(name = "st_informar")
	@Basic(optional = true)
	private Boolean informar;
	
	@Column(name = "st_procesar")
	@Basic(optional = true)
	private Boolean procesar;
	
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
		DetNominaDeduccion other = (DetNominaDeduccion) obj;
		return this.key.equals(other.key);
	}

	@Override
	public String toString() {
		return "DetNominaDeduccion [key=" + key + ", claveDeduccion=" + clave + ", nombreDeduccion="
				+ nombre + ", importe=" + importe + "]";
	}
	
	public DetNominaDeduccion() {
		
	}
	
	public DetNominaDeduccion(Builder builder) {
		this.key = builder.key;
		this.tipoDeduccion = builder.tipoDeduccion;
		this.clave = builder.clave;
		this.nombre = builder.nombre;
		this.importe = builder.importe;
		this.informar = builder.informar;
		this.procesar = builder.procesar;
	}

	public DetNominaDeduccion(DetNominaDeduccionPK key, CatTipoDeduccion tipoDeduccion, @Size(max = 5) String clave,
			@Size(max = 150) String nombre, BigDecimal importe, Boolean informar, Boolean procesar) {
		super();
		this.key = key;
		this.tipoDeduccion = tipoDeduccion;
		this.clave = clave;
		this.nombre = nombre;
		this.importe = importe;
		this.informar = informar;
		this.procesar = procesar;
	}

	public DetNominaDeduccionPK getKey() {
		return key;
	}

	public void setKey(DetNominaDeduccionPK key) {
		this.key = key;
	}

	public CatTipoDeduccion getTipoDeduccion() {
		return tipoDeduccion;
	}

	public void setTipoDeduccion(CatTipoDeduccion tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public Boolean getProcesar() {
		return procesar;
	}

	public void setProcesar(Boolean procesar) {
		this.procesar = procesar;
	}

	public Boolean getInformar() {
		return informar;
	}

	public void setInformar(Boolean informar) {
		this.informar = informar;
	}
	
	public static class Builder {
		private DetNominaDeduccionPK key;
		private CatTipoDeduccion tipoDeduccion;
		private String clave;
		private String nombre;
		private BigDecimal importe;
		private Boolean informar;
		private Boolean procesar;
		
		public DetNominaDeduccion.Builder key(DetNominaDeduccionPK key) {
			this.key = key;
			return this;
		}
		
		public DetNominaDeduccion.Builder clave(String clave) {
			this.clave = clave;
			return this;
		}
		
		public DetNominaDeduccion.Builder tipoDeduccion(CatTipoDeduccion tipoDeduccion) {
			this.tipoDeduccion = tipoDeduccion;
			return this;
		}

		public DetNominaDeduccion.Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public DetNominaDeduccion.Builder importe(BigDecimal importe) {
			this.importe = importe;
			return this;
		}

		public DetNominaDeduccion.Builder procesar(Boolean procesar) {
			this.procesar = procesar;
			return this;
		}

		public DetNominaDeduccion.Builder informar(Boolean informar) {
			this.informar = informar;
			return this;
		}
		
		public DetNominaDeduccion build() {
			return new DetNominaDeduccion(this);
		}

	}
}
