
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;

import mx.com.ferbo.model.CatEmpresa;

@Entity
@Table(name = "cat_certificado")
@NamedQueries({
    @NamedQuery(name = "Certificado.findAll", query = "SELECT cc FROM Certificado cc"),
    @NamedQuery(name = "Certificado.findByIdentificador", query = "SELECT cc FROM Certificado cc WHERE cc.identificador = :identificador"),
    @NamedQuery(name = "Certificado.findByAlta", query = "SELECT cc FROM Certificado cc WHERE cc.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Certificado.findByNombreCertificado", query = "SELECT cc FROM Certificado cc WHERE cc.nombreCer = :nombreCer"),
    @NamedQuery(name = "Certificado.findByFecha", query = "SELECT max(cc.fechaAlta), cc.cer FROM Certificado cc GROUP BY cc.cer"),
    @NamedQuery(name = "Certificado.findByEmpresa", query = "SELECT cc FROM Certificado cc where cc.empresa.idEmpresa = :empresa")
})
public class Certificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_certificado")
    private Integer identificador;

    @Column(name = "fh_alta")
    private Date fechaAlta;

    @Size(min = 1, max = 256)
    @Column(name = "nb_certificado")
    private String nombreCer;

    @Size(min = 1, max = 1638)
    @Column(name = "dt_certificado")
    private byte[] cer;

    @Size(min = 1, max = 256)
    @Column(name = "nb_llave_privada")
    private String nombreKey;

    @Size(min = 1, max = 1638)
    @Column(name = "dt_llave_privada")
    private byte[] key;

    @Size(min = 1, max = 150)
    @Column(name = "nb_pass")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private CatEmpresa empresa;
    
    public Certificado() {
    }

    public Certificado(Integer identificador) {
        this.identificador = identificador;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getNombreCer() {
        return nombreCer;
    }

    public void setNombreCer(String nombreCer) {
        this.nombreCer = nombreCer;
    }

    public byte[] getCer() {
        return cer;
    }

    public void setCer(byte[] cer) {
        this.cer = cer;
    }

    public String getNombreKey() {
        return nombreKey;
    }

    public void setNombreKey(String nombreKey) {
        this.nombreKey = nombreKey;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CatEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CatEmpresa empresa) {
        this.empresa = empresa;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.identificador);
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
        final Certificado other = (Certificado) obj;
        return Objects.equals(this.identificador, other.identificador);
    }

    @Override
    public String toString() {
        return "Certificado{" + "identificador=" + identificador + ", fechaAlta=" + fechaAlta + ", nombreCer=" + nombreCer + ", cer=" + cer + ", nombreKey=" + nombreKey + ", key=" + key + ", password=" + password + '}';
    }

}
