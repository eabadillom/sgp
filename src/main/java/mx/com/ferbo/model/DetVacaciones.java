
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

@Entity
@Table(name = "det_vacaciones")
@NamedQueries({
	@NamedQuery(name = "DetVacaciones.buscarPeriodoPorEmpleadoFecha", query = "SELECT v FROM DetVacaciones v WHERE v.empleado.idEmpleado = :idEmpleado AND :fecha BETWEEN v.fechaInicio AND v.fechaFin")
})
public class DetVacaciones implements Serializable{

    private static final long serialVersionUID = -2632823297112045342L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id_vacaciones")
    private Integer idVacaciones;
    
    @Column(name = "fh_inicio")
    private Date fechaInicio;
    
    @Column(name = "fh_fin")
    private Date fechaFin;
    
    @Column(name = "nu_dias_totales")
    private Integer diasTotales;
    
    @Column(name = "nu_dias_tomados")
    private Integer diasTomados;
    
    @Column(name = "st_prima_pagada")
    private Boolean primaPagada;
    
    @Column(name = "st_dias_pend_pagados")
    private Boolean diasPendientesPagados;
    
    @Column(name = "nu_dias_pagados")
    private Integer diasPagados;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    private DetEmpleado empleado;

    public DetVacaciones() {
    }

    public DetVacaciones(Integer idVacaciones) {
        this.idVacaciones = idVacaciones;
    }

    public DetVacaciones(Integer idVacaciones, Date fechaInicio, Date fechaFin, Integer diasTotales, Integer diasTomados, Boolean primaPagada, Boolean diasPendientesPagados) {
        this.idVacaciones = idVacaciones;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasTotales = diasTotales;
        this.diasTomados = diasTomados;
        this.primaPagada = primaPagada;
        this.diasPendientesPagados = diasPendientesPagados;
    }
    
    public Integer getIdVacaciones() {
        return idVacaciones;
    }

    public void setIdVacaciones(Integer idVacaciones) {
        this.idVacaciones = idVacaciones;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechafin) {
        this.fechaFin = fechafin;
    }

    public Integer getDiasTotales() {
        return diasTotales;
    }

    public void setDiasTotales(Integer diastotales) {
        this.diasTotales = diastotales;
    }

    public Integer getDiasTomados() {
        return diasTomados;
    }

    public void setDiasTomados(Integer diastomados) {
        this.diasTomados = diastomados;
    }

    public Boolean getPrimaPagada() {
        return primaPagada;
    }

    public void setPrimaPagada(Boolean primapagada) {
        this.primaPagada = primapagada;
    }

    public Boolean getDiasPendientesPagados() {
        return diasPendientesPagados;
    }

    public void setDiasPendientesPagados(Boolean diasPendientesPagados) {
        this.diasPendientesPagados = diasPendientesPagados;
    }

    public Integer getDiasPagados() {
        return diasPagados;
    }

    public void setDiasPagados(Integer diasPagados) {
        this.diasPagados = diasPagados;
    }
    
    

    public DetEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) {
        this.empleado = empleado;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.idVacaciones);
        return hash;
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
        final DetVacaciones other = (DetVacaciones) obj;
        return Objects.equals(this.idVacaciones, other.idVacaciones);
    }

    @Override
    public String toString() {
        return "DetVacaciones{" + "idVacaciones=" + idVacaciones + ", fechainicio=" + fechaInicio + ", fechafin=" + fechaFin + ", diastotales=" + diasTotales + ", diastomados=" + diasTomados + ", primapagada=" + primaPagada + ", diaspendientespagados=" + diasPendientesPagados + '}';
    }
   
}
