package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.sat.CatEntidadFederativa;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.model.sat.CatTipoJornada;
import mx.com.ferbo.model.sat.CatTipoRegimen;
import mx.com.ferbo.model.sat.CatUsoCFDI;


@Entity
@Table(name = "det_nom_receptor")
public class DetNominaReceptor implements Serializable {

	private static final long serialVersionUID = -8691279351662644877L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_receptor")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_nomina")
	private DetNomina nomina = null;
	
	@Column(name = "nb_nombre")
	@Size(max = 150)
	private String nombre;
	
	@Column(name = "nb_rfc")
	@Size(max = 20)
	private String rfc;
	
	@Column(name = "nu_cp")
	@Size(max = 10)
	private String codigoPostal;
	
	@JoinColumn(name = "cd_regimen", referencedColumnName = "cd_regimen")
	@OneToOne
	private CatRegimenFiscal regimenFiscal;
	
	@JoinColumn(name = "cd_uso_cfdi")
	@OneToOne
	private CatUsoCFDI usoCfdi;
	
	@Column(name = "nb_curp")
	@Size(max = 20)
	private String curp;
	
	@Column(name = "nu_nss")
	@Size(max = 20)
	private String nss;
	
	@Column(name = "fh_ini_rec_lab")
	private Date inicioRelacionLaboral;
	
	@Column(name = "nb_antiguedad")
	@Size(max = 20)
	private String antiguedad;
	
	@Column(name = "nu_dias_vacaciones")
	private BigDecimal diasVacaciones;
	
	@Column(name = "nu_prima_vacacional")
	private BigDecimal primaVacacional;
	
	@Column(name = "nu_dias_aguinaldo")
	private BigDecimal diasAguinaldo;
	
	@JoinColumn(name = "cd_tp_contrato")
	@OneToOne
	private CatTipoContrato tipoContrato;
	
	@Column(name = "st_sind")
	private Boolean sindicalizado;
	
	@JoinColumn(name = "cd_tp_jornada")
	@OneToOne
	private CatTipoJornada tipoJornada;
	
	@JoinColumn(name = "cd_tp_regimen")
	@OneToOne
	private CatTipoRegimen tipoRegimen;
	
	@Column(name = "nu_empleado")
	@Size(max = 5)
	private String numeroEmpleado;
	
	@Column(name = "nb_depto")
	@Size(max = 150)
	private String departamento;
	
	@Column(name = "nb_puesto")
	@Size(max = 150)
	private String puesto;
	
	@JoinColumn(name = "cd_riesgo_puesto")
	@OneToOne
	private CatRiesgoPuesto riesgoPuesto;
	
	@JoinColumn(name = "cd_periodicidad")
	@OneToOne
	private CatPeriodicidadPago periodicidadPago;
	
	@Column(name = "nu_sd", precision = 12, scale = 2)
	private BigDecimal salarioDiario;
	
	@Column(name = "nu_sdi" , precision = 12, scale = 2)
	private BigDecimal salarioDiarioIntegrado;
	
	@OneToOne
	@JoinColumn(name = "cd_ent_fed")
	private CatEntidadFederativa entidadFederativa;
	
	@Override
	public int hashCode() {
		if (this.id == null) {
    		return System.identityHashCode(this);
        }
        return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals receptor...");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetNominaReceptor other = (DetNominaReceptor) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",  nombre\":\"" + nombre + "\",  rfc\":\"" + rfc + "\",  codigoPostal\":\""
				+ codigoPostal + "}";
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

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public CatRegimenFiscal getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(CatRegimenFiscal regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public CatUsoCFDI getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(CatUsoCFDI usoCfdi) {
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

	public CatTipoContrato getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(CatTipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Boolean getSindicalizado() {
		return sindicalizado;
	}

	public void setSindicalizado(Boolean sindicalizado) {
		this.sindicalizado = sindicalizado;
	}

	public CatTipoJornada getTipoJornada() {
		return tipoJornada;
	}

	public void setTipoJornada(CatTipoJornada tipoJornada) {
		this.tipoJornada = tipoJornada;
	}

	public CatTipoRegimen getTipoRegimen() {
		return tipoRegimen;
	}

	public void setTipoRegimen(CatTipoRegimen tipoRegimen) {
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

	public CatRiesgoPuesto getRiesgoPuesto() {
		return riesgoPuesto;
	}

	public void setRiesgoPuesto(CatRiesgoPuesto riesgoPuesto) {
		this.riesgoPuesto = riesgoPuesto;
	}

	public CatPeriodicidadPago getPeriodicidadPago() {
		return periodicidadPago;
	}

	public void setPeriodicidadPago(CatPeriodicidadPago periodicidadPago) {
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

	public CatEntidadFederativa getEntidadFederativa() {
		return entidadFederativa;
	}

	public void setEntidadFederativa(CatEntidadFederativa entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
	}

	public BigDecimal getDiasVacaciones() {
		return diasVacaciones;
	}

	public void setDiasVacaciones(BigDecimal diasVacaciones) {
		this.diasVacaciones = diasVacaciones;
	}

	public BigDecimal getDiasAguinaldo() {
		return diasAguinaldo;
	}

	public void setDiasAguinaldo(BigDecimal diasAguinaldo) {
		this.diasAguinaldo = diasAguinaldo;
	}

	public BigDecimal getPrimaVacacional() {
		return primaVacacional;
	}

	public void setPrimaVacacional(BigDecimal primaVacacional) {
		this.primaVacacional = primaVacacional;
	}
}
