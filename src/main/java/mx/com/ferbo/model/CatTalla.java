package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "cat_talla")
@NamedQueries({
    @NamedQuery(name = "CatTalla.findAll", query = "SELECT c FROM CatTalla c"),
    @NamedQuery(name = "CatTalla.findAllActive", query = "SELECT NEW mx.com.ferbo.dto.CatTallaDTO(c.idTalla, c.descripcion, c.activo) FROM CatTalla c WHERE c.activo = 1"),
    @NamedQuery(name = "CatTalla.findForId", query = "SELECT NEW mx.com.ferbo.dto.CatTallaDTO(c.idTalla, c.descripcion, c.activo) FROM CatTalla c WHERE c.activo = 1 AND c.idTalla = :idTalla")
})
public class CatTalla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_talla")
    private Integer idTalla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private short activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTalla")
    private List<DetSolicitudPrenda> detSolicitudPrendaList;

    public CatTalla() {
    }

    public CatTalla(Integer idTalla) {
        this.idTalla = idTalla;
    }

    public CatTalla(Integer idTalla, String descripcion, short activo) {
        this.idTalla = idTalla;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Integer idTalla) {
        this.idTalla = idTalla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public List<DetSolicitudPrenda> getDetSolicitudPrendaList() {
        return detSolicitudPrendaList;
    }

    public void setDetSolicitudPrendaList(List<DetSolicitudPrenda> detSolicitudPrendaList) {
        this.detSolicitudPrendaList = detSolicitudPrendaList;
    }
    
}
