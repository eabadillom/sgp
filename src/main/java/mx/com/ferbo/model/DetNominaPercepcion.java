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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoPercepcion;

@Entity
@Table(name = "det_nom_percepcion")
public class DetNominaPercepcion implements Serializable {

	private static final long serialVersionUID = -3328622010561838312L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
	@Column(name = "id_percepcion")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@ManyToOne
	@JoinColumn(name = "tp_percepcion", referencedColumnName = "cd_tipo_percepcion")
	@Basic(optional = false)
	private CatTipoPercepcion tipoPercepcion;
	
	
	/**GuiallenadoNomina311221.pdf (SAT)<br>
	 * Nodo: Percepcion<br>
	 * Atributo: Clave<br>
	 * <p>Se debe registrar la clave de control interno que asigna el patrón a cada
	 * percepción de nómina propia de su contabilidad, puede conformarse desde 3
	 * hasta 15 caracteres.</p>
	 * Ejemplo:<br>
	 * Clave= 00500<br>
	 * 
	 * <p>Se debe registrar la descripción de cada uno de los conceptos de percepción.
	 * Se ingresa el nombre o descripción específica que dé el patrón de cada uno de
	 * los conceptos de percepción pagado al trabajador que corresponda, esta
	 * descripción puede o no coincidir con la descripción del catálogo
	 * c_TipoPercepcion publicado en el Portal del SAT.</p>
	 * 
	 * <p>Nota: Es importante considerar que, aunque la descripción no coincida
	 * textualmente con la descripción del catálogo c_TipoPercepcion publicado en el
	 * Portal del SAT, se debe cuidar que el concepto utilizado si tenga relación y sea
	 * concordante con la descripción de dicho catálogo de la clave que corresponda.</p>
	 */
	@Column(name = "cd_percepcion")
	@Basic(optional = false)
	@Size(max = 5)
	private String clave;
	
	@Column(name = "nb_percepcion")
	@Basic(optional = false)
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nu_cantidad", precision = 6, scale = 3)
	@Basic(optional = true)
	private BigDecimal cantidad = null;
	
	@Transient
	private BigDecimal importe;
	
	@Column(name = "nu_imp_gravado", precision = 12, scale = 2)
	@Basic(optional = false)
	private BigDecimal importeGravado;
	
	@Column(name = "nu_imp_exento", precision = 12, scale = 2)
	@Basic(optional = false)
	private BigDecimal importeExento;
	
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
		DetNominaPercepcion other = (DetNominaPercepcion) obj;
		return Objects.equals(this.id, other.id);
	}

	@Override
	public String toString() {
		return "DetNominaPercepcion [clavePercepcion=" + clave + ", nombrePercepcion="
				+ nombre + ", importeGravado=" + importeGravado + ", importeExento=" + importeExento + "]";
	}
	
	public DetNominaPercepcion() {
		
	}
	
	public DetNominaPercepcion(DetNominaPercepcion percepcion) {
		this.id             = percepcion.getId();
		this.nomina         = percepcion.getNomina();
		this.tipoPercepcion = percepcion.getTipoPercepcion();
		this.clave          = percepcion.getClave();
		this.nombre         = percepcion.getNombre();
		this.cantidad       = percepcion.getCantidad();
		this.importe        = percepcion.getImporteExento()
				.add(percepcion.getImporteGravado());
		this.importeGravado = percepcion.getImporteGravado();
		this.importeExento  = percepcion.getImporteExento();
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
	
	public CatTipoPercepcion getTipoPercepcion() {
		return tipoPercepcion;
	}

	public void setTipoPercepcion(CatTipoPercepcion tipoPercepcion) {
		this.tipoPercepcion = tipoPercepcion;
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

	public void setNombre(String nombrePercepcion) {
		this.nombre = nombrePercepcion;
	}

	public BigDecimal getImporteGravado() {
		return importeGravado;
	}

	public void setImporteGravado(BigDecimal importeGravado) {
		this.importeGravado = importeGravado;
	}

	public BigDecimal getImporteExento() {
		return importeExento;
	}

	public void setImporteExento(BigDecimal importeExento) {
		this.importeExento = importeExento;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
}
