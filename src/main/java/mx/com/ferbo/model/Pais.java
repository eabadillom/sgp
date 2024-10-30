package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cat_pais")
@NamedQueries({
    @NamedQuery(name = "Pais.findById", query = "SELECT d FROM Pais d WHERE d.cd_pais = :cdPais"),
    @NamedQuery(name = "Pais.findByClave", query = "SELECT d FROM Pais d WHERE d.clavePais = :clavePais"),
    @NamedQuery(name = "Pais.findAll", query = "SELECT d FROM Pais d ORDER BY d.nombrePais")
})
public class Pais implements Serializable {

    private static final long serialVersionUID = 869199289584873346L;

    @Id
    @Basic(optional = false)
    @Column(name = "cd_pais")
    private Integer id;
    
    @Column(name = "nb_clave")
    private String clave;

    @Column(name = "nb_pais")
    @JoinColumn(name = "cd_estado")
    private String nombrePais;
    
    @OneToMany
    private List<CatEstado> estados;

    public Pais() {
    }

    public Pais(Integer id, String clave, String nombrePais) {
        super();
        this.id = id;
        this.clave = clave;
        this.nombrePais = nombrePais;
    }

    public Integer getCd_pais() {
        return id;
    }

    public void setCd_pais(Integer id) {
        this.id = id;
    }

    public String getClavePais() {
        return clave;
    }

    public void setClavePais(String clavePais) {
        this.clave = clavePais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clave, nombrePais);
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
        Pais other = (Pais) obj;
        return Objects.equals(clave, other.clave) && Objects.equals(nombrePais, other.nombrePais);
    }

    @Override
    public String toString() {
        return "Pais [clavePais=" + clave + ", nombrePais=" + nombrePais + "]";
    }
}
