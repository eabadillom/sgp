/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_uma")
@NamedQueries({
    @NamedQuery(name = "CatUMA.findById", query = "SELECT c FROM CatUMA c where c.anio = :anio"),
    @NamedQuery(name = "CatUMA.findAll", query = "SELECT c FROM CatUMA c")
})
public class CatUMA implements Serializable
{
    @Id
    @Basic(optional = false)
    @Column(name = "cd_anio")
    private Integer anio;

    @Basic(optional = false)
    @Column(name = "im_diario")
    private BigDecimal importeDiario;

    @Basic(optional = false)
    @Column(name = "im_mensual")
    private BigDecimal importeMensual;

    @Basic(optional = false)
    @Column(name = "im_anual")
    private BigDecimal importeAnual;

    @Basic(optional = false)
    @Column(name = "fh_publicacion")
    private LocalDate fechaPublicacion;

    @Basic(optional = false)
    @Column(name = "fh_aplicacion")
    private LocalDate fechaAplicacion;

    public CatUMA() {
    }

    public CatUMA(Integer codigoAnio) {
        this.anio = codigoAnio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getImporteDiario() {
        return importeDiario;
    }

    public void setImporteDiario(BigDecimal importeDiario) {
        this.importeDiario = importeDiario;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(BigDecimal importeMensual) {
        this.importeMensual = importeMensual;
    }

    public BigDecimal getImporteAnual() {
        return importeAnual;
    }

    public void setImporteAnual(BigDecimal importeAnual) {
        this.importeAnual = importeAnual;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.anio);
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
        final CatUMA other = (CatUMA) obj;
        return Objects.equals(this.anio, other.anio);
    }

    @Override
    public String toString() {
        return "CatUMA[" + "Codigo Año: " + anio + ", Importe Diario: " + importeDiario + ", Importe Mensual: " + importeMensual + ", "
                + "Importe Anual: " + importeAnual + ", Fecha Publicacion: " + fechaPublicacion + ", Fecha Aplicacion: " + fechaAplicacion + ']';
    }
    
}
