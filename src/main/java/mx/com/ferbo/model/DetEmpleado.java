package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "det_empleado")
@NamedQueries({
    @NamedQuery(name = "DetEmpleado.findByActive", query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, "
            + " e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion, em.idEmpresa, em.descripcion, "
            + "p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.activo = 1 ORDER BY e.activo, e.nombre, e.primerAp, e.segundoAp"),
    @NamedQuery(name = "DetEmpleado.findByID",
            query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion,"
            + " em.idEmpresa, em.descripcion, p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.idEmpleado = :idEmp"),
    @NamedQuery(name = "DetEmpleado.findByNumEmpl", query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion,"
            + " em.idEmpresa, em.descripcion, p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.activo = 1 AND e.numEmpleado = :numEmpl"),
    @NamedQuery(name = "DetEmpleado.findByNumEmplSD", query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion,"
            + " em.idEmpresa, em.descripcion, p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion, e.sueldoDiario"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.activo = 1 AND e.idEmpleado = :idEmp"),

    @NamedQuery(name = "DetEmpleado.findByActiveSDI",
            query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, "
            + " e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion, em.idEmpresa, em.descripcion, "
            + "p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion, e.sueldoDiario"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.activo = 1 ORDER BY e.primerAp, e.segundoAp, e.nombre"),
    @NamedQuery(name = "DetEmpleado.findByActiveAndIdEmpresa",
            query = "SELECT NEW mx.com.ferbo.dto.DetEmpleadoDTO("
            + " e.idEmpleado, e.numEmpleado, e.nombre, e.primerAp, e.segundoAp, e.fechaNacimiento,"
            + " e.fechaRegistro, e.fechaModificacion, e.curp, e.rfc, e.correo, e.fechaIngreso, e.nss, "
            + " e.activo, e.empleadoFoto.fotografia, a.idArea, a.descripcion, em.idEmpresa, em.descripcion, "
            + "p.idPerfil, p.descripcion, pl.idPlanta, pl.descripcion, pu.idPuesto, pu.descripcion, e.sueldoDiario"
            + ")"
            + " FROM DetEmpleado e"
            + " LEFT JOIN e.datoEmpresa de"
            + " LEFT JOIN de.area a"
            + " LEFT JOIN de.empresa em"
            + " LEFT JOIN de.perfil p"
            + " LEFT JOIN de.planta pl"
            + " LEFT JOIN de.puesto pu"
            + " WHERE e.activo = 1 AND de.empresa.idEmpresa = :idEmpresa ORDER BY e.primerAp, e.segundoAp, e.nombre"),

    @NamedQuery(name = "DetEmpleado.findByNumero", query = "SELECT e FROM DetEmpleado e WHERE e.numEmpleado = :numero"),
    @NamedQuery(name = "DetEmpleado.getNumEmpleado", query = "SELECT COALESCE(MAX(e.idEmpleado),0) FROM DetEmpleado e"),
    @NamedQuery(name = "DetEmpleado.getAll", query = "SELECT e FROM DetEmpleado e"),
    @NamedQuery(name = "DetEmpleado.getActive", query = "SELECT e FROM DetEmpleado e WHERE e.activo = :activo"),
    @NamedQuery(name = "DetEmpleado.findByActiveEmpresaIngreso", query = "SELECT e FROM DetEmpleado e WHERE e.activo = 1 AND e.datoEmpresa.empresa.idEmpresa = :idEmpresa AND e.fechaIngreso <= :periodoPagoInicio ")
})
public class DetEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "num_empleado")
    private String numEmpleado;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "primer_ap")
    private String primerAp;

    @Size(max = 45)
    @Column(name = "segundo_ap")
    private String segundoAp;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Size(max = 18)
    @Column(name = "curp")
    private String curp;

    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "rfc")
    private String rfc;

    @Size(max = 45)
    @Column(name = "correo")
    private String correo;

    @Basic(optional = true)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @Size(max = 45)
    @Column(name = "nss")
    private String nss;

    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private short activo;

    @Column(name = "sueldo_diario")
    private BigDecimal sueldoDiario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<BitacoraInventario> bitacoraInventarioList;

    @OneToMany(mappedBy = "idEmpleado")
    private List<BitacoraCatPerfil> bitacoraCatPerfilList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleadoSol")
    private List<DetSolicitudPermiso> detSolicitudPermisoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<DetBiometrico> detBiometricoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<DetRegistro> detRegistroList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleadoSol")
    private List<DetSolicitudArticulo> detSolicitudArticuloList;

    @OneToMany(mappedBy = "idEmpleadoRev")
    private List<DetSolicitudPrenda> detSolicitudPrendaList;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_empleado_empresa")
    private InfDatoEmpresa datoEmpresa;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado_foto")
    private DetEmpleadoFoto empleadoFoto;

    public DetEmpleado() {
    }

    public DetEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public DetEmpleado(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, Date fechaNacimiento, Date fechaRegistro, String rfc,
            Date fechaIngreso, short activo, BigDecimal sueldoDiario) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.rfc = rfc;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
        this.sueldoDiario = sueldoDiario;
    }

    public DetEmpleado(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, Date fechaNacimiento, Date fechaRegistro, String rfc,
            Date fechaIngreso, short activo) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.rfc = rfc;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;

    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerAp() {
        return primerAp;
    }

    public void setPrimerAp(String primerAp) {
        this.primerAp = primerAp;
    }

    public String getSegundoAp() {
        return segundoAp;
    }

    public void setSegundoAp(String segundoAp) {
        this.segundoAp = segundoAp;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public List<BitacoraInventario> getBitacoraInventarioList() {
        return bitacoraInventarioList;
    }

    public void setBitacoraInventarioList(List<BitacoraInventario> bitacoraInventarioList) {
        this.bitacoraInventarioList = bitacoraInventarioList;
    }

    public List<BitacoraCatPerfil> getBitacoraCatPerfilList() {
        return bitacoraCatPerfilList;
    }

    public void setBitacoraCatPerfilList(List<BitacoraCatPerfil> bitacoraCatPerfilList) {
        this.bitacoraCatPerfilList = bitacoraCatPerfilList;
    }

    public List<DetSolicitudPermiso> getDetSolicitudPermisoList() {
        return detSolicitudPermisoList;
    }

    public void setDetSolicitudPermisoList(List<DetSolicitudPermiso> detSolicitudPermisoList) {
        this.detSolicitudPermisoList = detSolicitudPermisoList;
    }

    public List<DetBiometrico> getDetBiometricoList() {
        return detBiometricoList;
    }

    public void setDetBiometricoList(List<DetBiometrico> detBiometricoList) {
        this.detBiometricoList = detBiometricoList;
    }

    public List<DetRegistro> getDetRegistroList() {
        return detRegistroList;
    }

    public void setDetRegistroList(List<DetRegistro> detRegistroList) {
        this.detRegistroList = detRegistroList;
    }

    public List<DetSolicitudArticulo> getDetSolicitudArticuloList() {
        return detSolicitudArticuloList;
    }

    public void setDetSolicitudArticuloList(List<DetSolicitudArticulo> detSolicitudArticuloList) {
        this.detSolicitudArticuloList = detSolicitudArticuloList;
    }

    public List<DetSolicitudPrenda> getDetSolicitudPrendaList() {
        return detSolicitudPrendaList;
    }

    public void setDetSolicitudPrendaList(List<DetSolicitudPrenda> detSolicitudPrendaList) {
        this.detSolicitudPrendaList = detSolicitudPrendaList;
    }

    public BigDecimal getSueldoDiario() {
        return sueldoDiario;
    }

    public void setSueldoDiario(BigDecimal sueldoDiario) {
        this.sueldoDiario = sueldoDiario;
    }

    public InfDatoEmpresa getDatoEmpresa() {
        return datoEmpresa;
    }

    public void setDatoEmpresa(InfDatoEmpresa datoEmpresa) {
        this.datoEmpresa = datoEmpresa;
    }

    public DetEmpleadoFoto getEmpleadoFoto() {
        return empleadoFoto;
    }

    public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
        this.empleadoFoto = empleadoFoto;
    }

}
