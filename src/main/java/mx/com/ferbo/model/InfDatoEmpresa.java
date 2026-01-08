package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import mx.com.ferbo.model.sat.CatBanco;
import mx.com.ferbo.model.sat.CatEntidadFederativa;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
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
    @Column(name = "fh_baja")
    private Date fechaBaja;

    @Basic(optional = true)
    @Column(name = "nu_nss")
    private String nss;

    @Basic(optional = true)
    @Column(name = "nb_rfc")
    private String rfc;

    @Transient
    private BigDecimal salarioDiario;

    @Basic(optional = true)
    @Column(name = "tm_entrada")
    private Date horaEntrada;

    @Basic(optional = true)
    @Column(name = "tm_salida")
    private Date horasalida;

    @Basic(optional = true)
    @Column(name = "nu_tolerancia")
    private Integer minutosTolerancia;

    @ManyToOne
    @JoinColumn(name = "cd_estado", referencedColumnName = "cd_estado")
    private CatEntidadFederativa entidadFederativa;

    @ManyToOne
    @JoinColumn(name = "cd_riesgo", referencedColumnName = "cd_riesgo")
    private CatRiesgoPuesto riesgoPuesto;

    @ManyToOne
    @JoinColumn(name = "cd_periodicidad", referencedColumnName = "periodicidad")
    private CatPeriodicidadPago periodicidadPago;

    @ManyToOne
    @JoinColumn(name = "tp_baja", referencedColumnName = "tp_baja")
    private CatTipoBajaEmpleado tipodebaja;

    @ManyToOne
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco")
    private CatBanco banco;

    @Basic(optional = true)
    @Column(name = "st_sindicalizado")
    private Boolean sindicalizado;

    @Basic(optional = true)
    @Column(name = "st_confianza")
    private Boolean confianza;

    @Basic(optional = true)
    @Column(name = "st_diat_lunes")
    private Boolean diaLunes;

    @Basic(optional = true)
    @Column(name = "st_diat_martes")
    private Boolean diaMartes;

    @Basic(optional = true)
    @Column(name = "st_diat_miercoles")
    private Boolean diaMiercoles;

    @Basic(optional = true)
    @Column(name = "st_diat_jueves")
    private Boolean diaJueves;

    @Basic(optional = true)
    @Column(name = "st_diat_viernes")
    private Boolean diaViernes;

    @Basic(optional = true)
    @Column(name = "st_diat_sabado")
    private Boolean diaSabado;

    @Basic(optional = true)
    @Column(name = "st_diat_domingo")
    private Boolean diaDomingo;

    @Basic(optional = true)
    @Column(name = "nb_baja")
    private String motivobaja;
    
    @Basic(optional = true)
    @Column(name = "nu_prima_vacacional", precision = 4, scale = 2)
    private BigDecimal primaVacacional;
    
    @Basic(optional = true)
    @Column(name = "nu_dias_aguinaldo" , precision = 4, scale = 2)
    private BigDecimal diasAguinaldo;
    
    @OneToMany(mappedBy = "datoEmpresa", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<DetSalarioDiario> salariosDiarios;
    
	private InfDatoEmpresa(Builder builder) {
		this.id = builder.id;
		this.perfil = builder.perfil;
		this.empresa = builder.empresa;
		this.planta = builder.planta;
		this.area = builder.area;
		this.puesto = builder.puesto;
		this.tipoContrato = builder.tipoContrato;
		this.tipoJornada = builder.tipoJornada;
		this.tipoRegimen = builder.tipoRegimen;
		this.fechaIngreso = builder.fechaIngreso;
		this.fechaBaja = builder.fechaBaja;
		this.nss = builder.nss;
		this.rfc = builder.rfc;
		this.salarioDiario = builder.salarioDiario;
		this.horaEntrada = builder.horaEntrada;
		this.horasalida = builder.horasalida;
		this.minutosTolerancia = builder.minutosTolerancia;
		this.entidadFederativa = builder.entidadFederativa;
		this.riesgoPuesto = builder.riesgoPuesto;
		this.periodicidadPago = builder.periodicidadPago;
		this.tipodebaja = builder.tipodebaja;
		this.banco = builder.banco;
		this.sindicalizado = builder.sindicalizado;
		this.confianza = builder.confianza;
		this.diaLunes = builder.diaLunes;
		this.diaMartes = builder.diaMartes;
		this.diaMiercoles = builder.diaMiercoles;
		this.diaJueves = builder.diaJueves;
		this.diaViernes = builder.diaViernes;
		this.diaSabado = builder.diaSabado;
		this.diaDomingo = builder.diaDomingo;
		this.motivobaja = builder.motivobaja;
		this.primaVacacional = builder.primaVacacional;
		this.diasAguinaldo = builder.diasAguinaldo;
		this.salariosDiarios = builder.salariosDiarios;
	}

    @Override
    public int hashCode() {
    	if(this.id == null)
    		return System.identityHashCode(this);
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InfDatoEmpresa other = (InfDatoEmpresa) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\"}";
    }
    
    public InfDatoEmpresa() {
    }
    
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

    public Date getHorasalida() {
        return horasalida;
    }

    public void setHorasalida(Date horasalida) {
        this.horasalida = horasalida;
    }

    public Integer getMinutosTolerancia() {
        return minutosTolerancia;
    }

    public void setMinutosTolerancia(Integer minutosTolerancia) {
        this.minutosTolerancia = minutosTolerancia;
    }

    public Boolean getDiaLunes() {
        return diaLunes;
    }

    public void setDiaLunes(Boolean diaLunes) {
        this.diaLunes = diaLunes;
    }

    public Boolean getDiaMartes() {
        return diaMartes;
    }

    public void setDiaMartes(Boolean diaMartes) {
        this.diaMartes = diaMartes;
    }

    public Boolean getDiaMiercoles() {
        return diaMiercoles;
    }

    public void setDiaMiercoles(Boolean diaMiercoles) {
        this.diaMiercoles = diaMiercoles;
    }

    public Boolean getDiaJueves() {
        return diaJueves;
    }

    public void setDiaJueves(Boolean diaJueves) {
        this.diaJueves = diaJueves;
    }

    public Boolean getDiaViernes() {
        return diaViernes;
    }

    public void setDiaViernes(Boolean diaViernes) {
        this.diaViernes = diaViernes;
    }

    public Boolean getDiaSabado() {
        return diaSabado;
    }

    public void setDiaSabado(Boolean diaSabado) {
        this.diaSabado = diaSabado;
    }

    public Boolean getDiaDomingo() {
        return diaDomingo;
    }

    public void setDiaDomingo(Boolean diaDomingo) {
        this.diaDomingo = diaDomingo;
    }

    public Boolean getConfianza() {
        return confianza;
    }

    public void setConfianza(Boolean confianza) {
        this.confianza = confianza;
    }

    public CatEntidadFederativa getEntidadFederativa() {
        return entidadFederativa;
    }

    public void setEntidadFederativa(CatEntidadFederativa entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
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

    public Boolean getSindicalizado() {
        return sindicalizado;
    }

    public void setSindicalizado(Boolean sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public CatTipoBajaEmpleado getTipodebaja() {
        return tipodebaja;
    }

    public void setTipodebaja(CatTipoBajaEmpleado tipodebaja) {
        this.tipodebaja = tipodebaja;
    }

    public String getMotivobaja() {
        return motivobaja;
    }

    public void setMotivobaja(String motivobaja) {
        this.motivobaja = motivobaja;
    }

    public CatBanco getBanco() {
        return banco;
    }

    public void setBanco(CatBanco banco) {
        this.banco = banco;
    }

	public BigDecimal getPrimaVacacional() {
		return primaVacacional;
	}

	public void setPrimaVacacional(BigDecimal primaVacacional) {
		this.primaVacacional = primaVacacional;
	}

	public BigDecimal getDiasAguinaldo() {
		return diasAguinaldo;
	}

	public void setDiasAguinaldo(BigDecimal diasAguinaldo) {
		this.diasAguinaldo = diasAguinaldo;
	}
	
	public List<DetSalarioDiario> getSalariosDiarios() {
		return salariosDiarios;
	}

	public void setSalariosDiarios(List<DetSalarioDiario> salariosDiarios) {
		this.salariosDiarios = salariosDiarios;
	}
	
	public static final class Builder {
		private Integer id;
		private CatPerfil perfil;
		private CatEmpresa empresa;
		private CatPlanta planta;
		private CatArea area;
		private CatPuesto puesto;
		private CatTipoContrato tipoContrato;
		private CatTipoJornada tipoJornada;
		private CatTipoRegimen tipoRegimen;
		private Date fechaIngreso;
		private Date fechaBaja;
		private String nss;
		private String rfc;
		private BigDecimal salarioDiario;
		private Date horaEntrada;
		private Date horasalida;
		private Integer minutosTolerancia;
		private CatEntidadFederativa entidadFederativa;
		private CatRiesgoPuesto riesgoPuesto;
		private CatPeriodicidadPago periodicidadPago;
		private CatTipoBajaEmpleado tipodebaja;
		private CatBanco banco;
		private Boolean sindicalizado;
		private Boolean confianza;
		private Boolean diaLunes;
		private Boolean diaMartes;
		private Boolean diaMiercoles;
		private Boolean diaJueves;
		private Boolean diaViernes;
		private Boolean diaSabado;
		private Boolean diaDomingo;
		private String motivobaja;
		private BigDecimal primaVacacional;
		private BigDecimal diasAguinaldo;
		private List<DetSalarioDiario> salariosDiarios;

		public Builder() {
		}

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder perfil(CatPerfil perfil) {
			this.perfil = perfil;
			return this;
		}

		public Builder empresa(CatEmpresa empresa) {
			this.empresa = empresa;
			return this;
		}

		public Builder planta(CatPlanta planta) {
			this.planta = planta;
			return this;
		}

		public Builder area(CatArea area) {
			this.area = area;
			return this;
		}

		public Builder puesto(CatPuesto puesto) {
			this.puesto = puesto;
			return this;
		}

		public Builder tipoContrato(CatTipoContrato tipoContrato) {
			this.tipoContrato = tipoContrato;
			return this;
		}

		public Builder tipoJornada(CatTipoJornada tipoJornada) {
			this.tipoJornada = tipoJornada;
			return this;
		}

		public Builder tipoRegimen(CatTipoRegimen tipoRegimen) {
			this.tipoRegimen = tipoRegimen;
			return this;
		}

		public Builder fechaIngreso(Date fechaIngreso) {
			this.fechaIngreso = fechaIngreso;
			return this;
		}

		public Builder fechaBaja(Date fechaBaja) {
			this.fechaBaja = fechaBaja;
			return this;
		}

		public Builder nss(String nss) {
			this.nss = nss;
			return this;
		}

		public Builder rfc(String rfc) {
			this.rfc = rfc;
			return this;
		}

		public Builder salarioDiario(BigDecimal salarioDiario) {
			this.salarioDiario = salarioDiario;
			return this;
		}

		public Builder horaEntrada(Date horaEntrada) {
			this.horaEntrada = horaEntrada;
			return this;
		}

		public Builder horasalida(Date horasalida) {
			this.horasalida = horasalida;
			return this;
		}

		public Builder minutosTolerancia(Integer minutosTolerancia) {
			this.minutosTolerancia = minutosTolerancia;
			return this;
		}

		public Builder entidadFederativa(CatEntidadFederativa entidadFederativa) {
			this.entidadFederativa = entidadFederativa;
			return this;
		}

		public Builder riesgoPuesto(CatRiesgoPuesto riesgoPuesto) {
			this.riesgoPuesto = riesgoPuesto;
			return this;
		}

		public Builder periodicidadPago(CatPeriodicidadPago periodicidadPago) {
			this.periodicidadPago = periodicidadPago;
			return this;
		}

		public Builder tipodebaja(CatTipoBajaEmpleado tipodebaja) {
			this.tipodebaja = tipodebaja;
			return this;
		}

		public Builder banco(CatBanco banco) {
			this.banco = banco;
			return this;
		}

		public Builder sindicalizado(Boolean sindicalizado) {
			this.sindicalizado = sindicalizado;
			return this;
		}

		public Builder confianza(Boolean confianza) {
			this.confianza = confianza;
			return this;
		}

		public Builder diaLunes(Boolean diaLunes) {
			this.diaLunes = diaLunes;
			return this;
		}

		public Builder diaMartes(Boolean diaMartes) {
			this.diaMartes = diaMartes;
			return this;
		}

		public Builder diaMiercoles(Boolean diaMiercoles) {
			this.diaMiercoles = diaMiercoles;
			return this;
		}

		public Builder diaJueves(Boolean diaJueves) {
			this.diaJueves = diaJueves;
			return this;
		}

		public Builder diaViernes(Boolean diaViernes) {
			this.diaViernes = diaViernes;
			return this;
		}

		public Builder diaSabado(Boolean diaSabado) {
			this.diaSabado = diaSabado;
			return this;
		}

		public Builder diaDomingo(Boolean diaDomingo) {
			this.diaDomingo = diaDomingo;
			return this;
		}

		public Builder motivobaja(String motivobaja) {
			this.motivobaja = motivobaja;
			return this;
		}

		public Builder primaVacacional(BigDecimal primaVacacional) {
			this.primaVacacional = primaVacacional;
			return this;
		}

		public Builder diasAguinaldo(BigDecimal diasAguinaldo) {
			this.diasAguinaldo = diasAguinaldo;
			return this;
		}
		
		public Builder salariosDiarios(List<DetSalarioDiario> salariosDiarios) {
			this.salariosDiarios = salariosDiarios;
			return this;
		}

		public InfDatoEmpresa build() {
			return new InfDatoEmpresa(this);
		}
	}
}
