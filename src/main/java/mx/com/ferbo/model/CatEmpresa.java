package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatRegimenFiscal;

@Entity
@Table(name = "cat_empresa")
@NamedQueries({
	@NamedQuery(name = "CatEmpresa.findActive", query = "SELECT c FROM CatEmpresa c WHERE c.activo = true"),
	
    @NamedQuery(name = "CatEmpresa.findAll", query = "SELECT c "
            + "FROM CatEmpresa c"),
    @NamedQuery(name = "CatEmpresa.findByActive", query = "SELECT c "
            + " FROM CatEmpresa c"
            + " WHERE c.activo = true"),
    @NamedQuery(name = "CatEmpresa.findById", query = "SELECT c "
            + " FROM CatEmpresa c"
            + " WHERE c.activo = true AND c.idEmpresa = :idEmpresa")})
public class CatEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Integer idEmpresa;
    
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "activo")
    private Boolean activo;
    
    @Basic(optional = false)
    @Column(name = "nb_razon_social")
    private String razonSocial;
    
    @Basic(optional = false)
    @Column(name = "tp_persona")
    private String tipoPersona;
    
    @Basic(optional = true)
    @Column(name = "nb_regimen_capital")
    private String regimenCapital;
    
    @Basic(optional = false)
    @Column(name = "nb_rfc")
    private String rfc;
    
    @Basic(optional = false)
    @Column(name = "fh_inicio_op")
    private Date fechaInicioOperacion;
    
    @Basic(optional = true)
    @Column(name = "fh_ult_cambio")
    private Date fechaUltimoCambio;
    
    @Basic(optional = false)
    @Column(name = "st_padron")
    private String statusPadron;
    
    @JoinColumn(name = "cd_regimen", referencedColumnName = "cd_regimen")
    @ManyToOne(optional = true)
    private CatRegimenFiscal regimenFiscal;
    
    @Column(name = "nu_reg_pat")
    @Basic(optional = true)
    @Size(max = 20)
    private String registroPatronal;
    
    @Column(name = "nu_cp")
    @Basic(optional = true)
    @Size(max = 5)
    private String codigoPostal;

    public CatEmpresa() {
    }

    public CatEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public CatEmpresa(Integer idEmpresa, String descripcion, Boolean activo) {
        this.idEmpresa = idEmpresa;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getRegimenCapital() {
		return regimenCapital;
	}

	public void setRegimenCapital(String regimenCapital) {
		this.regimenCapital = regimenCapital;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Date getFechaInicioOperacion() {
		return fechaInicioOperacion;
	}

	public void setFechaInicioOperacion(Date fechaInicio) {
		this.fechaInicioOperacion = fechaInicio;
	}

	public Date getFechaUltimoCambio() {
		return fechaUltimoCambio;
	}

	public void setFechaUltimoCambio(Date fechaUltimoCambio) {
		this.fechaUltimoCambio = fechaUltimoCambio;
	}

	public String getStatusPadron() {
		return statusPadron;
	}

	public void setStatusPadron(String statusPadron) {
		this.statusPadron = statusPadron;
	}

	public CatRegimenFiscal getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(CatRegimenFiscal regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public String getRegistroPatronal() {
		return registroPatronal;
	}

	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idEmpresa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatEmpresa other = (CatEmpresa) obj;
		return Objects.equals(idEmpresa, other.idEmpresa);
	}

	@Override
	public String toString() {
		return "CatEmpresa [idEmpresa=" + idEmpresa + ", descripcion=" + descripcion + ", activo=" + activo
				+ ", razonSocial=" + razonSocial + ", tipoPersona=" + tipoPersona + ", regimenCapital=" + regimenCapital
				+ ", rfc=" + rfc + ", fechaInicioOperacion=" + fechaInicioOperacion + ", fechaUltimoCambio="
				+ fechaUltimoCambio + ", statusPadron=" + statusPadron + ", registroPatronal=" + registroPatronal
				+ ", codigoPostal=" + codigoPostal + "]";
	}
}
