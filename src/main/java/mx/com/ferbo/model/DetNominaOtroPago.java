package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoOtroPago;

@Entity
@Table(name = "det_nom_otro_pago")
public class DetNominaOtroPago implements Serializable {

	private static final long serialVersionUID = -3073795346585740482L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
	@Column(name = "id_otro_pago")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@ManyToOne
	@JoinColumn(name = "tp_otro_pago", referencedColumnName = "cd_tipo_otro_pago")
	@Basic(optional = false)
	private CatTipoOtroPago tipoOtroPago;
	
	@Column(name = "cd_otro_pago")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_otro_pago")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nu_importe", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importe;
	
	@Column(name = "st_informar")
	@Basic(optional = false)
	private Boolean informar;
	
	@Column(name = "st_procesar")
	@Basic(optional = false)
	private Boolean procesar;

	@Override
	public int hashCode() {
		if(this.id == null)
			System.identityHashCode(this);
		return Objects.hash(this.id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaOtroPago other = (DetNominaOtroPago) obj;
		return Objects.equals(this.id, other.id);
	}
	
	@Override
	public String toString() {
		return "DetNominaOtroPago [clave=" + clave + ", nombre=" + nombre + ", importe=" + importe + "]";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
	}
	
	public CatTipoOtroPago getTipoOtroPago() {
		return tipoOtroPago;
	}

	public void setTipoOtroPago(CatTipoOtroPago otroPago) {
		this.tipoOtroPago = otroPago;
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
}
