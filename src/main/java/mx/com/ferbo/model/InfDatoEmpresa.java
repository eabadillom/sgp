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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.model.sat.CatTipoJornada;
import mx.com.ferbo.model.sat.CatTipoRegimen;

@Entity
@Table(name = "inf_empleado_empresa")
public class InfDatoEmpresa implements Serializable {
	private static final long serialVersionUID = -7428030225407170556L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_empleado_empresa")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
	private CatPerfil perfil;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	private CatEmpresa empresa;
	
	@ManyToOne
	@JoinColumn(name = "id_planta", referencedColumnName = "id_planta")
	private CatPlanta planta;
	
	@ManyToOne
	@JoinColumn(name = "id_area", referencedColumnName = "id_area")
	private CatArea area;
	
	@ManyToOne
	@JoinColumn(name = "id_puesto", referencedColumnName = "id_puesto")
	private CatPuesto puesto;
	
	@ManyToOne
	@JoinColumn(name = "cd_contrato", referencedColumnName = "cd_contrato")
	private CatTipoContrato tipoContrato;
	
	@ManyToOne
	@JoinColumn(name = "cd_jornada", referencedColumnName = "cd_jornada")
	private CatTipoJornada tipoJornada;
	
	@ManyToOne
	@JoinColumn(name = "cd_tp_regimen", referencedColumnName = "cd_tp_regimen")
	private CatTipoRegimen tipoRegimen;
	
	@Basic(optional = false)
	@Column(name = "fh_ingreso")
	private Date fechaIngreso;
	
	@Basic(optional = true)
	@Column(name = "nu_nss")
	private String nss;
	
	@Basic(optional = true)
	@Column(name = "nb_rfc")
	private String rfc;
	
	@Basic(optional = true)
	@Column(name = "nu_salario_diario")
	private BigDecimal salarioDiario;
	
	@Basic(optional = true)
	@Column(name = "tm_entrada")
	private Date horaEntrada;
	
	@Basic(optional = true)
	@Column(name = "nu_tolerancia")
	private Integer minutosTolerancia;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CatPerfil getPerfil() {
		return perfil;
	}
	public void setPerfil(CatPerfil perfil) {
		this.perfil = perfil;
	}
	public CatEmpresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}
	public CatPlanta getPlanta() {
		return planta;
	}
	public void setPlanta(CatPlanta planta) {
		this.planta = planta;
	}
	public CatArea getArea() {
		return area;
	}
	public void setArea(CatArea area) {
		this.area = area;
	}
	public CatPuesto getPuesto() {
		return puesto;
	}
	public void setPuesto(CatPuesto puesto) {
		this.puesto = puesto;
	}
	public CatTipoContrato getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(CatTipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
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
		InfDatoEmpresa other = (InfDatoEmpresa) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"perfil\":\"" + perfil + "\", \"fechaIngreso\":\"" + fechaIngreso
				+ "\", \"nss\":\"" + nss + "\", \"rfc\":\"" + rfc + "\", \"salarioDiario\":\"" + salarioDiario
				+ "\", \"horaEntrada\":\"" + horaEntrada + "\", \"minutosTolerancia\":\"" + minutosTolerancia + "\"}";
	}
}
