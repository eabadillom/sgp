
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "det_rol_sistema")
public class DetRolSistema {
    
    
    @Id
    @Column(name = "nb_rol")
    private String tiporol;

    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "rol")
    List<DetFpClient> clientes;

    public String getTipoRol() {
        return tiporol;
    }

    public void setTipoRol(String rol) {
        this.tiporol = rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DetFpClient> getClientes() {
        return clientes;
    }

    public void setRoles(List<DetFpClient> clientes) {
        this.clientes = clientes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetRolSistema other = (DetRolSistema) obj;
		return Objects.equals(tiporol, other.tiporol);
    }

    @Override
    public int hashCode() {
       return Objects.hash(tiporol);
    }
    
    

    @Override
    public String toString() {
        return "DetRolSistema{" + "rol=" + tiporol + ", descripcion=" + descripcion + '}';
    }
    
    
}
