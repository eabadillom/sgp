
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name = "cat_tipo_baja_empleado")
public class CatTipoBajaEmpleado implements Serializable{
    
    private static final long serialVersionUID = -5531873424150004816L;

	@Id
    @Column (name = "tp_baja")
    private String tipodebaja;
    
    @Column (name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "tipodebaja")
    private List<InfDatoEmpresa> datoempresas;

    public CatTipoBajaEmpleado() {
    }

    public String getTipodebaja() {
        return tipodebaja;
    }

    public void setTipodebaja(String tipodebaja) {
        this.tipodebaja = tipodebaja;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<InfDatoEmpresa> getDatoempresas() {
        return datoempresas;
    }

    public void setDatoempresas(List<InfDatoEmpresa> datoempresas) {
        this.datoempresas = datoempresas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.tipodebaja);
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
        final CatTipoBajaEmpleado other = (CatTipoBajaEmpleado) obj;
        return Objects.equals(this.tipodebaja, other.tipodebaja);
    }

    @Override
    public String toString() {
        return "CatTipoBajaEmpleado{" + "tipodebaja=" + tipodebaja + ", descripcion=" + descripcion + '}';
    }
    
}
