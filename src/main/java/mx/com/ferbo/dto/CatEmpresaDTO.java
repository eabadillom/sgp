package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import mx.com.ferbo.dto.sat.RegimenFiscalDTO;

public class CatEmpresaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idEmpresa;
    private String descripcion;
    private Boolean activo;
    private String razonSocial;
    private String tipoPersona;
    private String regimenCapital;
    private String rfc;
    private Date fechaInicioOperacion;
    private Date fechaUltimoCambio;
    private String statusPadron;
    private RegimenFiscalDTO regimenFiscal;
    private String registroPatronal;
    private String codigoPostal;

    public CatEmpresaDTO() {
    	
    }

    public CatEmpresaDTO(Integer idEmpresa, String descripcion, Boolean activo) {
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

	public void setFechaInicioOperacion(Date fechaInicioOperacion) {
		this.fechaInicioOperacion = fechaInicioOperacion;
	}

	public Date getFechaUltimoCambio() {
		return fechaUltimoCambio;
	}

	public void setFechaUltimoCambio(Date fechaUltimoCambio) {
		this.fechaUltimoCambio = fechaUltimoCambio;
	}

	public RegimenFiscalDTO getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(RegimenFiscalDTO regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public String getStatusPadron() {
		return statusPadron;
	}

	public void setStatusPadron(String statusPadron) {
		this.statusPadron = statusPadron;
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
	public String toString() {
		return "{\"idEmpresa\":\"" + idEmpresa + "\", \"descripcion\":\"" + descripcion + "\", \"activo\":\"" + activo
				+ "\", \"razonSocial\":\"" + razonSocial + "\", \"tipoPersona\":\"" + tipoPersona
				+ "\", \"regimenCapital\":\"" + regimenCapital + "\", \"rfc\":\"" + rfc
				+ "\", \"fechaInicioOperacion\":\"" + fechaInicioOperacion + "\", \"fechaUltimoCambio\":\""
				+ fechaUltimoCambio + "\", \"statusPadron\":\"" + statusPadron + "\"}";
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
		CatEmpresaDTO other = (CatEmpresaDTO) obj;
		return Objects.equals(idEmpresa, other.idEmpresa);
	}
}
