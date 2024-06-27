package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatTipoPercepcion;

@Entity
@Table(name = "det_nom_percepcion")
public class DetNominaPercepcion implements Serializable {

	private static final long serialVersionUID = -3328622010561838312L;
	
	@EmbeddedId
	private DetNominaPercepcionPK key;
	
	
	@OneToOne
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
	
	@Column(name = "nu_imp_gravado", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importeGravado;
	
	@Column(name = "nu_imp_exento", scale = 12, precision = 2)
	@Basic(optional = false)
	private BigDecimal importeExcento;

	public DetNominaPercepcionPK getKey() {
		return key;
	}

	public void setKey(DetNominaPercepcionPK key) {
		this.key = key;
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

	public BigDecimal getImporteExcento() {
		return importeExcento;
	}

	public void setImporteExcento(BigDecimal importeExcento) {
		this.importeExcento = importeExcento;
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
		DetNominaPercepcion other = (DetNominaPercepcion) obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "DetNominaPercepcion [key=" + key + ", clavePercepcion=" + clave + ", nombrePercepcion="
				+ nombre + ", importeGravado=" + importeGravado + ", importeExcento=" + importeExcento + "]";
	}
}
