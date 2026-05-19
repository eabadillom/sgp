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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "det_dia_permiso")
public class DetDiaPermiso implements Serializable{
	
	private static final long serialVersionUID = 9073893533076521696L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_dia_permiso")
	private Integer id;
	
	@Basic(optional = false)
	@Column(name = "fh_permiso")
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")
	@Basic(optional = false)
	private DetSolicitudPermiso solicitudPermiso;
	
	public DetDiaPermiso() {
	}

	private DetDiaPermiso(Builder builder) {
		this.id = builder.id;
		this.fecha = builder.fecha;
		this.solicitudPermiso = builder.solicitudPermiso;
	}

	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetDiaPermiso other = (DetDiaPermiso) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",  fecha\":\"" + fecha + "}";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public DetSolicitudPermiso getSolicitudPermiso() {
		return solicitudPermiso;
	}

	public void setSolicitudPermiso(DetSolicitudPermiso solicitudPermiso) {
		this.solicitudPermiso = solicitudPermiso;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private Integer id;
		private Date fecha;
		private DetSolicitudPermiso solicitudPermiso;

		public Builder() {
		}

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder fecha(Date fecha) {
			this.fecha = fecha;
			return this;
		}

		public Builder solicitudPermiso(DetSolicitudPermiso solicitudPermiso) {
			this.solicitudPermiso = solicitudPermiso;
			return this;
		}

		public DetDiaPermiso build() {
			return new DetDiaPermiso(this);
		}
	}
}
