package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import mx.com.ferbo.dto.sat.TipoContratoDTO;
import mx.com.ferbo.dto.sat.TipoJornadaDTO;
import mx.com.ferbo.dto.sat.TipoRegimenDTO;

public class DatoEmpresaDTO {
	
	private Integer id;
	private CatPerfilDTO perfil;
	private CatEmpresaDTO empresa;
	private CatPlantaDTO planta;
	private CatAreaDTO area;
	private CatPuestoDTO puesto;
	private TipoContratoDTO tipoContrato;
	private TipoJornadaDTO tipoJornada;
	private TipoRegimenDTO tipoRegimen;
	private Date fechaIngreso;
	private String nss;
	private String rfc;
	private BigDecimal salarioDiario;
	private Date horaEntrada;
	private Integer minutosTolerancia;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CatPerfilDTO getPerfil() {
		return perfil;
	}
	public void setPerfil(CatPerfilDTO perfil) {
		this.perfil = perfil;
	}
	public CatEmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(CatEmpresaDTO empresa) {
		this.empresa = empresa;
	}
	public CatPlantaDTO getPlanta() {
		return planta;
	}
	public void setPlanta(CatPlantaDTO planta) {
		this.planta = planta;
	}
	public CatAreaDTO getArea() {
		return area;
	}
	public void setArea(CatAreaDTO area) {
		this.area = area;
	}
	public CatPuestoDTO getPuesto() {
		return puesto;
	}
	public void setPuesto(CatPuestoDTO puesto) {
		this.puesto = puesto;
	}
	public TipoContratoDTO getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(TipoContratoDTO tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public TipoJornadaDTO getTipoJornada() {
		return tipoJornada;
	}
	public void setTipoJornada(TipoJornadaDTO tipoJornada) {
		this.tipoJornada = tipoJornada;
	}
	public TipoRegimenDTO getTipoRegimen() {
		return tipoRegimen;
	}
	public void setTipoRegimen(TipoRegimenDTO tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getNss() {
		return nss;
	}
	public void setNss(String nss) {
		this.nss = nss;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}
	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
	public Date getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Integer getMinutosTolerancia() {
		return minutosTolerancia;
	}
	public void setMinutosTolerancia(Integer minutosTolerancia) {
		this.minutosTolerancia = minutosTolerancia;
	}
	@Override
	public int hashCode() {
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
		DatoEmpresaDTO other = (DatoEmpresaDTO) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"perfil\":\"" + perfil + "\", \"empresa\":\"" + empresa + "\", \"planta\":\""
				+ planta + "\", \"area\":\"" + area + "\", \"puesto\":\"" + puesto + "\", \"tipoContrato\":\""
				+ tipoContrato + "\", \"tipoJornada\":\"" + tipoJornada + "\", \"tipoRegimen\":\"" + tipoRegimen
				+ "\", \"fechaIngreso\":\"" + fechaIngreso + "\", \"nss\":\"" + nss + "\", \"rfc\":\"" + rfc
				+ "\", \"salarioDiario\":\"" + salarioDiario + "\", \"horaEntrada\":\"" + horaEntrada
				+ "\", \"minutosTolerancia\":\"" + minutosTolerancia + "\"}";
	}
}
