package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "cat_estado")
@NamedQueries({
    @NamedQuery(name = "Estados.findAll", query = "SELECT ce FROM cat_estado ce")
})
public class CatEstado implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    CatEstadoPK estadoPK;
}
