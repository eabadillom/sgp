package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author erale
 */
@Entity
@Table(name = "cat_subsidio")
@NamedQueries({
    @NamedQuery(name = "CatSubsidio.findAll", query = "SELECT s FROM CatSubsidio s"),
    @NamedQuery(name = "CatSubsidio.findActual", query = "SELECT NEW mx.com.ferbo.dto.CatSubsidioDTO(s.idSubsidio, s.paraIngresosDe, s.hastaIngresosDe, s.cantidadSubsidio, s.fecha) FROM CatSubsidio s WHERE s.fecha LIKE :fecha")})
public class CatSubsidio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_subsidio")
    private Integer idSubsidio;
    @NotNull
    @Column(name = "para_ingresos_de")
    private Float paraIngresosDe;
    @NotNull
    @Column(name = "hasta_ingresos_de")
    private Float hastaIngresosDe;
    @NotNull
    @Column(name = "cantidad_subsidio")
    private Float cantidadSubsidio;
    @NotNull
    @Column(name = "fecha")
    private Date fecha;

    public CatSubsidio() {
    }

    public CatSubsidio(Integer idSubsidio) {
        this.idSubsidio = idSubsidio;
    }

    public CatSubsidio(Integer idSubsidio, Float paraIngresosDe, Float hastaIngresosDe, Float cantidadSubsidio, Date fecha) {
        this.idSubsidio = idSubsidio;
        this.paraIngresosDe = paraIngresosDe;
        this.hastaIngresosDe = hastaIngresosDe;
        this.cantidadSubsidio = cantidadSubsidio;
        this.fecha = fecha;
    }

    public Integer getIdSubsidio() {
        return idSubsidio;
    }

    public void setIdSubsidio(Integer idSubsidio) {
        this.idSubsidio = idSubsidio;
    }

    public Float getParaIngresosDe() {
        return paraIngresosDe;
    }

    public void setParaIngresosDe(Float paraIngresosDe) {
        this.paraIngresosDe = paraIngresosDe;
    }

    public Float getHastaIngresosDe() {
        return hastaIngresosDe;
    }

    public void setHastaIngresosDe(Float hastaIngresosDe) {
        this.hastaIngresosDe = hastaIngresosDe;
    }

    public Float getCantidadSubsidio() {
        return cantidadSubsidio;
    }

    public void setCantidadSubsidio(Float cantidadSubsidio) {
        this.cantidadSubsidio = cantidadSubsidio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
