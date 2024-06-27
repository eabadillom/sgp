package mx.com.ferbo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DetEmpleadoDTO implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Integer idEmpleado;
    private String numEmpleado;
    private String nombre;
    private String primerAp;
    private String segundoAp;
    private Date fechaNacimiento;
    private Date fechaRegistro;
    private Date fechaModificacion;
    private String curp;
    private String rfc;
    private String correo;
    private Date fechaIngreso;//TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private String nss;//TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private short activo;
    private String fotografia;
    private CatAreaDTO catAreaDTO; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private CatEmpresaDTO catEmpresaDTO; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private CatPerfilDTO catPerfilDTO; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private CatPlantaDTO catPlantaDTO; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private CatPuestoDTO catPuestoDTO; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private BigDecimal sueldoDiario; //TODO SE DEBE CAMBIAR ESTA PROPIEDAD A LA ENTIDAD DET_EMPLEADO_EMPRESA
    private DetBiometricoDTO detBiometricoDTO;
    private DatoEmpresaDTO datoEmpresa;
    private EmpleadoFotoDTO empleadoFoto;

    public DetEmpleadoDTO() {
        catAreaDTO = new CatAreaDTO();
        catEmpresaDTO = new CatEmpresaDTO();
        catPerfilDTO = new CatPerfilDTO();
        catPlantaDTO = new CatPlantaDTO();
        catPuestoDTO = new CatPuestoDTO();
        detBiometricoDTO = new DetBiometricoDTO();
    }
    
    public DetEmpleadoDTO(Integer idEmpleado){
        this.idEmpleado = idEmpleado;
    }

    /*
     * Método que utiliza la query findByID
     */
    public DetEmpleadoDTO(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, String segundoAp,
                          Date fechaNacimiento, Date fechaRegistro, Date fechaModificacion, String curp, String rfc, String correo,
                          Date fechaIngreso, String nss, short activo, String fotografia) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.segundoAp = segundoAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.curp = curp;
        this.rfc = rfc;
        this.correo = correo;
        this.fechaIngreso = fechaIngreso;
        this.nss = nss;
        this.activo = activo;
        this.fotografia = fotografia;
    }

    /*
     * Método que utiliza la query findByActive
     */
    public DetEmpleadoDTO(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, String segundoAp,
                          Date fechaNacimiento, Date fechaRegistro, Date fechaModificacion, String curp, String rfc, String correo,
                          Date fechaIngreso, String nss, short activo, String fotografia, Integer idArea, String descripcionArea, Integer idEmpresa,
                          String descripcionEmpresa, Integer idPerfil, String descripcionPerfil, Integer idPlanta,
                          String descripcionPlanta, Integer idPuesto, String descripcionPuesto) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.segundoAp = segundoAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.curp = curp;
        this.rfc = rfc;
        this.correo = correo;
        this.fechaIngreso = fechaIngreso;
        this.nss = nss;
        this.activo = activo;
        this.fotografia = fotografia;
        this.catAreaDTO = new CatAreaDTO(idArea, descripcionArea, (short) 1);
        this.catEmpresaDTO = new CatEmpresaDTO(idEmpresa, descripcionEmpresa, true);
        this.catPerfilDTO = new CatPerfilDTO(idPerfil, descripcionPerfil, (short) 1);
        this.catPlantaDTO = new CatPlantaDTO(idPlanta, descripcionPlanta, (short) 1);
        this.catPuestoDTO = new CatPuestoDTO(idPuesto, descripcionPuesto, (short) 1);
    }
    
    /*
     * Método que utiliza la query findAll
     */
    public DetEmpleadoDTO(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, String segundoAp){
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.segundoAp = segundoAp;
    }
    
    /* Método para findByNumEmplSD */
    public DetEmpleadoDTO(Integer idEmpleado, String numEmpleado, String nombre, String primerAp, String segundoAp,
                          Date fechaNacimiento, Date fechaRegistro, Date fechaModificacion, String curp, String rfc, String correo,
                          Date fechaIngreso, String nss, short activo, String fotografia, Integer idArea, String descripcionArea, Integer idEmpresa,
                          String descripcionEmpresa, Integer idPerfil, String descripcionPerfil, Integer idPlanta,
                          String descripcionPlanta, Integer idPuesto, String descripcionPuesto, BigDecimal sueldoDiario
                          ) {
        this.idEmpleado = idEmpleado;
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.primerAp = primerAp;
        this.segundoAp = segundoAp;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.curp = curp;
        this.rfc = rfc;
        this.correo = correo;
        this.fechaIngreso = fechaIngreso;
        this.nss = nss;
        this.activo = activo;
        this.fotografia = fotografia;
        this.catAreaDTO = new CatAreaDTO(idArea, descripcionArea, (short) 1);
        this.catEmpresaDTO = new CatEmpresaDTO(idEmpresa, descripcionEmpresa, true);
        this.catPerfilDTO = new CatPerfilDTO(idPerfil, descripcionPerfil, (short) 1);
        this.catPlantaDTO = new CatPlantaDTO(idPlanta, descripcionPlanta, (short) 1);
        this.catPuestoDTO = new CatPuestoDTO(idPuesto, descripcionPuesto, (short) 1);
        this.sueldoDiario = sueldoDiario;
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
    	if(numEmpleado != null)
    		numEmpleado = numEmpleado.toUpperCase();
        this.numEmpleado = numEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
    	if(nombre != null)
    		nombre = nombre.toUpperCase();
        this.nombre = nombre;
    }

    public String getPrimerAp() {
        return primerAp;
    }

    public void setPrimerAp(String primerAp) {
    	if(primerAp != null)
    		primerAp = primerAp.toUpperCase();
        this.primerAp = primerAp;
    }

    public String getSegundoAp() {
        return segundoAp;
    }

    public void setSegundoAp(String segundoAp) {
    	if(segundoAp != null)
    		segundoAp = segundoAp.toUpperCase();
        this.segundoAp = segundoAp;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
    	if(curp != null)
    		curp = curp.toUpperCase();
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
    	if(rfc != null)
    		rfc = rfc.toUpperCase();
        this.rfc = rfc;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public CatAreaDTO getCatAreaDTO() {
        return catAreaDTO;
    }

    public void setCatAreaDTO(CatAreaDTO catAreaDTO) {
        this.catAreaDTO = catAreaDTO;
    }

    public CatEmpresaDTO getCatEmpresaDTO() {
        return catEmpresaDTO;
    }

    public void setCatEmpresaDTO(CatEmpresaDTO catEmpresaDTO) {
        this.catEmpresaDTO = catEmpresaDTO;
    }

    public CatPerfilDTO getCatPerfilDTO() {
        return catPerfilDTO;
    }

    public void setCatPerfilDTO(CatPerfilDTO catPerfilDTO) {
        this.catPerfilDTO = catPerfilDTO;
    }

    public CatPlantaDTO getCatPlantaDTO() {
        return catPlantaDTO;
    }

    public void setCatPlantaDTO(CatPlantaDTO catPlantaDTO) {
        this.catPlantaDTO = catPlantaDTO;
    }

    public CatPuestoDTO getCatPuestoDTO() {
        return catPuestoDTO;
    }

    public void setCatPuestoDTO(CatPuestoDTO catPuestoDTO) {
        this.catPuestoDTO = catPuestoDTO;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public BigDecimal getSueldoDiario() {
        return sueldoDiario;
    }

    public void setSueldoDiario(BigDecimal sueldoDiario) {
        this.sueldoDiario = sueldoDiario;
    }

    public DetBiometricoDTO getDetBiometricoDTO() {
        return detBiometricoDTO;
    }

    public void setDetBiometricoDTO(DetBiometricoDTO detBiometricoDTO) {
        this.detBiometricoDTO = detBiometricoDTO;
    }

	public DatoEmpresaDTO getDatoEmpresa() {
		return datoEmpresa;
	}

	public void setDatoEmpresa(DatoEmpresaDTO datoEmpresa) {
		this.datoEmpresa = datoEmpresa;
	}

	public EmpleadoFotoDTO getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(EmpleadoFotoDTO empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}
    
}
