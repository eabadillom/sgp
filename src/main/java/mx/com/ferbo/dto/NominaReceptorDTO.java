package mx.com.ferbo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import mx.com.ferbo.dto.sat.EntidadFederativaDTO;
import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.dto.sat.RiesgoPuestoDTO;
import mx.com.ferbo.dto.sat.TipoContratoDTO;
import mx.com.ferbo.dto.sat.TipoJornadaDTO;
import mx.com.ferbo.dto.sat.TipoRegimenDTO;
import mx.com.ferbo.dto.sat.UsoCFDIDTO;
import mx.com.ferbo.model.Nomina;

public class NominaReceptorDTO {
	
	private Nomina nomina;
	private String nombre;
	private String rfc;
	private String codigoPostal;
	private RegimenFiscalDTO regimenFiscal;
	private UsoCFDIDTO usoCfdi;
	private String curp;
	private String nss;
	private Date inicioRelacionLaboral;
	private String antiguedad;
	private TipoContratoDTO tipoContrato;
	private Boolean sindicalizado;
	private TipoJornadaDTO tipoJornada;
	private TipoRegimenDTO tipoRegimen;
	private String numeroEmpleado;
	private String departamento;
	private String puesto;
	private RiesgoPuestoDTO riesgoPuesto;
	private PeriodicidadPagoDTO periodicidadPago;
	private BigDecimal salarioDiario;
	private BigDecimal salarioDiarioIntegrado;
	private EntidadFederativaDTO entidadFederativa;
	
	public Nomina getNomina() {
		return nomina;
	}
	public void setNomina(Nomina nomina) {
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
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public RegimenFiscalDTO getRegimenFiscal() {
		return regimenFiscal;
	}
	public void setRegimenFiscal(RegimenFiscalDTO regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public UsoCFDIDTO getUsoCfdi() {
		return usoCfdi;
	}
	public void setUsoCfdi(UsoCFDIDTO usoCfdi) {
		this.usoCfdi = usoCfdi;
	}
	public String getCurp() {
		return curp;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getNss() {
		return nss;
	}
	public void setNss(String nss) {
		this.nss = nss;
	}
	public Date getInicioRelacionLaboral() {
		return inicioRelacionLaboral;
	}
	public void setInicioRelacionLaboral(Date inicioRelacionLaboral) {
		this.inicioRelacionLaboral = inicioRelacionLaboral;
	}
	public String getAntiguedad() {
		return antiguedad;
	}
	public void setAntiguedad(String antiguedad) {
		this.antiguedad = antiguedad;
	}
	public TipoContratoDTO getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(TipoContratoDTO tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public Boolean getSindicalizado() {
		return sindicalizado;
	}
	public void setSindicalizado(Boolean sindicalizado) {
		this.sindicalizado = sindicalizado;
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
	public String getNumeroEmpleado() {
		return numeroEmpleado;
	}
	public void setNumeroEmpleado(String numeroEmpleado) {
		this.numeroEmpleado = numeroEmpleado;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public RiesgoPuestoDTO getRiesgoPuesto() {
		return riesgoPuesto;
	}
	public void setRiesgoPuesto(RiesgoPuestoDTO riesgoPuesto) {
		this.riesgoPuesto = riesgoPuesto;
	}
	public PeriodicidadPagoDTO getPeriodicidadPago() {
		return periodicidadPago;
	}
	public void setPeriodicidadPago(PeriodicidadPagoDTO periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}
	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}
	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
	public BigDecimal getSalarioDiarioIntegrado() {
		return salarioDiarioIntegrado;
	}
	public void setSalarioDiarioIntegrado(BigDecimal salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}
	public EntidadFederativaDTO getEntidadFederativa() {
		return entidadFederativa;
	}
	public void setEntidadFederativa(EntidadFederativaDTO entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
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
		NominaReceptorDTO other = (NominaReceptorDTO) obj;
		return Objects.equals(nomina, other.nomina);
	}
	@Override
	public String toString() {
		return "NominaReceptorDTO [nombre=" + nombre + ", rfc=" + rfc + ", codigoPostal=" + codigoPostal + ", curp="
				+ curp + ", nss=" + nss + ", inicioRelacionLaboral=" + inicioRelacionLaboral + ", antiguedad="
				+ antiguedad + ", sindicalizado=" + sindicalizado + ", numeroEmpleado=" + numeroEmpleado
				+ ", departamento=" + departamento + ", puesto=" + puesto + ", salarioDiario=" + salarioDiario
				+ ", salarioDiarioIntegrado=" + salarioDiarioIntegrado + "]";
	}
}
