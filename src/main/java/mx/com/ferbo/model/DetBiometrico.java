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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "det_biometrico")
@NamedQueries({
    @NamedQuery(name = "DetBiometrico.findAll", query = "SELECT d FROM DetBiometrico d"),
    @NamedQuery(name = "DetBiometrico.findByNumEmpl", query = "SELECT NEW mx.com.ferbo.dto.DetBiometricoDTO("
            + " d.idBiometrico,"
            + " d.fechaCaptura,"
            + " d.activo,"
            + " d.huella,"
            + " d.huella2,"
            + " emp.idEmpleado"
            + ")"
            + " FROM DetBiometrico d"
            + " INNER JOIN d.idEmpleado emp"
            + " WHERE emp.numEmpleado = :numEmpl"),
    @NamedQuery(name = "DetBiometrico.findByNumeroEmpleado", query = "SELECT d FROM DetBiometrico d INNER JOIN d.idEmpleado e WHERE e.numEmpleado = :numeroEmpleado"),
    @NamedQuery(name = "DetBiometrico.findByIdEmpleado", query = "SELECT d FROM DetBiometrico d WHERE d.idEmpleado.idEmpleado = :idEmpleado")
})
public class DetBiometrico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_biometrico")
    private Integer idBiometrico;
    @Basic(optional = false)
    @Column(name = "fecha_captura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaptura;
    @Basic(optional = false)
    @Column(name = "activo")
    private short activo;
    @Basic(optional = false)
    @Column(name = "huella")
    private String huella; 
    @Basic(optional = false)
    @Column(name = "huella2")
    private String huella2;
    
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @OneToOne(optional = false)
    private DetEmpleado idEmpleado;

    public DetBiometrico() {
    }

    public DetBiometrico(Integer idBiometrico) {
        this.idBiometrico = idBiometrico;
    }

    public DetBiometrico(Integer idBiometrico, Date fechaCaptura, short activo, String huella, String huella2) {
        this.idBiometrico = idBiometrico;
        this.fechaCaptura = fechaCaptura;
        this.activo = activo;
        this.huella = huella;
        this.huella2 = huella2;
    }

    public Integer getIdBiometrico() {
        return idBiometrico;
    }

    public void setIdBiometrico(Integer idBiometrico) {
        this.idBiometrico = idBiometrico;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getHuella() {
        return huella;
    }

    public void setHuella(String huella) {
        this.huella = huella;
    }
    public String getHuella2() {
        return huella2;
    }

    public void setHuella2(String huella2) {
        this.huella2 = huella2;
    }

    public DetEmpleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(DetEmpleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

	@Override
	public int hashCode() {
		return Objects.hash(idBiometrico);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetBiometrico other = (DetBiometrico) obj;
		return Objects.equals(idBiometrico, other.idBiometrico);
	}

	@Override
	public String toString() {
		return "DetBiometrico [idBiometrico=" + idBiometrico + ", fechaCaptura=" + fechaCaptura + ", activo=" + activo
				+ ", huella=" + huella + ", huella2=" + huella2 + "]";
	}
    
}
