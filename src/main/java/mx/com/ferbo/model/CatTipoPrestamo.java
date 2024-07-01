package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.sat.CatTipoDeduccion;

@NamedQueries({
	@NamedQuery(name = "CatTipoPrestamo.findAll", query = "SELECT t FROM CatTipoPrestamo t")
})
@Entity
@Table(name = "cat_tipo_prestamo")
public class CatTipoPrestamo implements Serializable {
	
	private static final long serialVersionUID = -3814906808587120009L;

	@Id
	@Basic(optional = false)
	@Column(name = "tp_prestamo", length = 5)
	private String tipoPrestamo;
	
	@Basic(optional = false)
	@Column(name = "descripcion", length = 30)
	private String descripcion;
	
	@OneToOne
	@JoinColumn(name = "tp_deduccion")
	@Basic(optional = true)
	private CatTipoDeduccion tipoDeduccion;

	public String getTipoPrestamo() {
		return tipoPrestamo;
	}

	public void setTipoPrestamo(String tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public CatTipoDeduccion getTipoDeduccion() {
		return tipoDeduccion;
	}

	public void setTipoDeduccion(CatTipoDeduccion tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipoPrestamo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatTipoPrestamo other = (CatTipoPrestamo) obj;
		return Objects.equals(tipoPrestamo, other.tipoPrestamo);
	}

	@Override
	public String toString() {
		return "CatTipoPrestamo [tipoPrestamo=" + tipoPrestamo + ", descripcion=" + descripcion + "]";
	}
}
