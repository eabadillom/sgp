package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatRegimenFiscal;

@Entity
@Table(name = "det_nom_emisor")
@NamedQueries({
	@NamedQuery(name = "DetNominaEmisor.buscarPorNomina", query = "SELECT e FROM DetNominaEmisor e WHERE e.nomina.id = :idNomina")
})
public class DetNominaEmisor implements Serializable {

	private static final long serialVersionUID = 8524892581709741382L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_emisor")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina;
	
	@Basic(optional = false)
	@Column(name = "nb_nombre")
	@Size(max = 150)
	private String nombre;
	
	@Basic(optional = false)
	@Column(name = "nb_rfc")
	@Size(max = 20)
	private String rfc;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "cd_regimen", referencedColumnName = "cd_regimen")
	private CatRegimenFiscal regimenFiscal;
	
	@Basic(optional = false)
	@Column(name = "nu_cp")
	@Size(max = 10)
	private String codigoPostal;
	
	@Basic(optional = false)
	@Column(name = "nu_reg_pat")
	@Size(max = 20)
	private String registroPatronal;
	
	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals emisor...");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaEmisor other = (DetNominaEmisor) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",  nombre\":\"" + nombre + "\",  rfc\":\"" + rfc + "\",  codigoPostal\":\""
				+ codigoPostal + "\",  registroPatronal\":\"" + registroPatronal + "}";
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public CatRegimenFiscal getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(CatRegimenFiscal regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getRegistroPatronal() {
		return registroPatronal;
	}

	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}
}
