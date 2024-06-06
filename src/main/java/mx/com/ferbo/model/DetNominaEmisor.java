package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatRegimenFiscal;

@Entity
@Table(name = "det_nom_emisor")
public class DetNominaEmisor implements Serializable {

	private static final long serialVersionUID = 8524892581709741382L;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_nomina", referencedColumnName = "id_nomina")
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

	@Override
	public int hashCode() {
		return Objects.hash(nomina);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaEmisor other = (DetNominaEmisor) obj;
		return Objects.equals(nomina, other.nomina);
	}
}
