package mx.com.ferbo.controller;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.com.ferbo.dao.n.AsentamientoDAO;
import mx.com.ferbo.dao.n.EntidadPostalDAO;
import mx.com.ferbo.dao.n.EstadoDAO;
import mx.com.ferbo.dao.n.LocalidadDAO;
import mx.com.ferbo.dao.n.MunicipioDAO;
import mx.com.ferbo.dao.n.PaisDAO;
import mx.com.ferbo.dao.n.TipoAsentamientoDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import mx.com.ferbo.model.CatEntidadPostal;
import mx.com.ferbo.model.CatEstado;
import mx.com.ferbo.model.CatEstadoPK;
import mx.com.ferbo.model.CatLocalidad;
import mx.com.ferbo.model.CatLocalidadPK;
import mx.com.ferbo.model.CatMunicipio;
import mx.com.ferbo.model.CatMunicipioPK;
import mx.com.ferbo.model.CatTipoAsentamiento;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named(value = "domiciliosBean")
@ViewScoped
public class DomiciliosBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(DomiciliosBean.class);
    
    private Pais pais;
    private List<Pais> paises;
    private PaisDAO paisdao;
    private boolean paisstatus;
    
    private CatEstado estado;
    private List<CatEstado> estados;
    private EstadoDAO estadodao;
    private boolean estadoestatus;
    
    private CatMunicipio municipio;
    private List<CatMunicipio> municipios;
    private MunicipioDAO municipiodao;
    private boolean municipioestatus;
    
    private CatLocalidad localidad;
    private List<CatLocalidad> localidades;
    private LocalidadDAO localidaddao;
    private boolean localidadestatus;
    
    private CatAsentamiento asentamiento;
    private List<CatAsentamiento> asentamientos;
    private AsentamientoDAO asentamientodao;
    private boolean asentamientoestatus;
    
    private CatTipoAsentamiento tipoasentamiento;
    private CatTipoAsentamiento tipotemporal;
    private List<CatTipoAsentamiento> tiposasentamiento;
    private TipoAsentamientoDAO tipoasentamientodao;
    private boolean tipoasentamientoestatus;
    
    private CatEntidadPostal entidadpostal;
    private CatEntidadPostal entidadtemporal;
    private List<CatEntidadPostal> entidadespostales;
    private EntidadPostalDAO entidadpostaldao;
    private boolean entidadpostalestatus;
    
    private String codigopostal;
    
    private String operacion;
    
    private Integer iid;
    private short sid;
    private String clave;
    private String descripcion;
    private boolean statusid;
    private boolean statuspostal;
    
    private ServletContext sc;
    
    public DomiciliosBean() {
        this.paisdao = new PaisDAO();
        this.estadodao = new EstadoDAO();
        this.municipiodao = new MunicipioDAO();
        this.localidaddao = new LocalidadDAO();
        this.asentamientodao = new AsentamientoDAO();
        this.tipoasentamientodao = new TipoAsentamientoDAO();
        this.entidadpostaldao = new EntidadPostalDAO();
        
    }
    
    @PostConstruct
    public void init() {
        this.paises = this.paisdao.buscarTodos();
        this.paisstatus = false;
        this.estadoestatus = false;
        this.municipioestatus = false;
        this.localidadestatus = false;
        this.tipoasentamientoestatus = false;
        this.asentamientoestatus = false;
        this.entidadpostalestatus = false;
        this.statusid = false;
        this.statuspostal = false;
    }
    
    public Pais getPais() {
        return pais;
    }
    
    public void setPais(Pais pais) {
        this.pais = pais;
    }
    
    public boolean isPaisstatus() {
        return paisstatus;
    }
    
    public void setPaisstatus(boolean paisstatus) {
        this.paisstatus = paisstatus;
    }
    
    public List<Pais> getPaises() {
        return paises;
    }
    
    public CatEstado getEstado() {
        return estado;
    }
    
    public void setEstado(CatEstado estado) {
        this.estado = estado;
    }
    
    public boolean isEstadoestatus() {
        return estadoestatus;
    }
    
    public void setEstadoestatus(boolean estadoestatus) {
        this.estadoestatus = estadoestatus;
    }
    
    public List<CatEstado> getEstados() {
        return estados;
    }
    
    public CatMunicipio getMunicipio() {
        return municipio;
    }
    
    public void setMunicipio(CatMunicipio municipio) {
        this.municipio = municipio;
    }
    
    public boolean isMunicipioestatus() {
        return municipioestatus;
    }
    
    public void setMunicipioestatus(boolean municipioestatus) {
        this.municipioestatus = municipioestatus;
    }
    
    public List<CatMunicipio> getMunicipios() {
        return municipios;
    }
    
    public CatLocalidad getLocalidad() {
        return localidad;
    }
    
    public void setLocalidad(CatLocalidad localidad) {
        this.localidad = localidad;
    }
    
    public boolean isLocalidadestatus() {
        return localidadestatus;
    }
    
    public void setLocalidadestatus(boolean localidadestatus) {
        this.localidadestatus = localidadestatus;
    }
    
    public List<CatLocalidad> getLocalidades() {
        return localidades;
    }
    
    public CatAsentamiento getAsentamiento() {
        return asentamiento;
    }
    
    public void setAsentamiento(CatAsentamiento asentamiento) {
        this.asentamiento = asentamiento;
    }
    
    public boolean isAsentamientoestatus() {
        return asentamientoestatus;
    }
    
    public void setAsentamientoestatus(boolean asentamientoestatus) {
        this.asentamientoestatus = asentamientoestatus;
    }
    
    public List<CatAsentamiento> getAsentamientos() {
        return asentamientos;
    }
    
    public CatTipoAsentamiento getTipoasentamiento() {
        return tipoasentamiento;
    }
    
    public void setTipoasentamiento(CatTipoAsentamiento tipoasentamiento) {
        this.tipoasentamiento = tipoasentamiento;
    }
    
    public boolean isTipoasentamientoestatus() {
        return tipoasentamientoestatus;
    }
    
    public void setTipoasentamientoestatus(boolean tipoasentamientoestatus) {
        this.tipoasentamientoestatus = tipoasentamientoestatus;
    }
    
    public List<CatTipoAsentamiento> getTiposasentamiento() {
        return tiposasentamiento;
    }
    
    public CatEntidadPostal getEntidadpostal() {
        return entidadpostal;
    }
    
    public void setEntidadpostal(CatEntidadPostal entidadpostal) {
        this.entidadpostal = entidadpostal;
    }
    
    public boolean isEntidadpostalestatus() {
        return entidadpostalestatus;
    }
    
    public void setEntidadpostalestatus(boolean entidadpostalestatus) {
        this.entidadpostalestatus = entidadpostalestatus;
    }
    
    public List<CatEntidadPostal> getEntidadespostales() {
        return entidadespostales;
    }
    
    private boolean isStatuspostal() {
        return statuspostal;
    }
    
    private void setStatuspostal(boolean statuspostal) {
        this.statuspostal = statuspostal;
    }
    
    public String getCodigopostal() {
        return codigopostal;
    }
    
    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }
    
    private String validarCodigoPostal() throws SGPException {
        
        char[] permitidos = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String cd = this.codigopostal;
        cd = cd.trim();
        
        if (cd == null) {
            throw new SGPException("El codigo postal es nulo");
        }
        
        if (cd.equals("")) {
            throw new SGPException("El codigo postal esta vacio");
        }
        
        for (int i = 0; i < cd.length(); i++) {
            boolean bandera = false;
            char digito = cd.charAt(i);
            for (int j = 0; j < permitidos.length; j++) {
                if (permitidos[j] == digito) {
                    bandera = true;
                    break;
                }
            }
            if (bandera == false) {
                throw new SGPException("El codigo postal solo debe contener digitos de 0 a 9");
            }
        }
        
        if (cd.length() != 5) {
            throw new SGPException("El codigo postal solo debe ser de ocho digitos");
        }
        
        return cd;
    }
    
    public void buscarCodigoPostal() {
        
        try {
            String cp = this.validarCodigoPostal();
            this.statuspostal = true;
            this.localidades = new ArrayList<CatLocalidad>();
            this.asentamientos = new ArrayList<CatAsentamiento>();
            this.estados = new ArrayList<CatEstado>();
            this.municipios = new ArrayList<CatMunicipio>();
            List<CatAsentamiento> asentamientostmp = this.asentamientodao.obtenerPorCodigo(cp);
            
            if(asentamientostmp == null || asentamientostmp.isEmpty()){
                throw new SGPException("el codigo postal buscado, no tiene direcciones asociadas.");
            }
            
            Pais pais = null;
            CatEstado estado = null;
            CatMunicipio municipio = null;
            CatLocalidad localidad = null;
            
            for (CatAsentamiento tmp : asentamientostmp) {
                
                if (tmp != null && !this.asentamientos.contains(tmp)) {
                    this.asentamiento = tmp;
                    this.asentamientos.add(tmp);
                }
                
                pais = tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais();
                if (pais != null) {
                    this.pais = pais;
                }
                
                estado = tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado();
                if (estado != null) {
                    this.estado = estado;
                    if (!this.estados.contains(estado)) {
                        this.estados.add(estado);
                    }
                }
                
                municipio = tmp.getKey().getLocalidad().getKey().getMunicipio();
                if (municipio != null) {
                    this.municipio = municipio;
                    if (!municipios.contains(municipio)) {
                        this.municipios.add(municipio);
                    }
                }
                
                localidad = tmp.getKey().getLocalidad();
                if (!this.localidades.contains(localidad)) {
                    this.localidad = localidad;
                    this.localidades.add(localidad);
                }
                
            }
            this.tipoasentamiento = this.asentamiento.getTipoAsentamiento();
            this.entidadpostal = this.asentamiento.getEntidadPostal();
            this.tiposasentamiento = this.tipoasentamientodao.obtenerPorAsentamiento(this.asentamiento.getTipoAsentamiento().getId());
            this.entidadespostales = this.entidadpostaldao.obtenerPorAsentamiento(this.asentamiento.getEntidadPostal().getId());
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se encontraron direcciones con el codigo postal buscado.", null)); 
            PrimeFaces.current().ajax().update("message");
            
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: " + ex.getMessage(), null)); 
            PrimeFaces.current().ajax().update("message");
        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema desconocido, notifique al administrador de sistemas." , null));
            PrimeFaces.current().ajax().update("message");
            
        }
    }
    
    public CatTipoAsentamiento getTipotemporal() {
        return tipotemporal;
    }
    
    public void setTipotemporal(CatTipoAsentamiento tipotemporal) {
        this.tipotemporal = tipotemporal;
    }
    
    public CatEntidadPostal getEntidadtemporal() {
        return entidadtemporal;
    }
    
    public void setEntidadtemporal(CatEntidadPostal entidadtemporal) {
        this.entidadtemporal = entidadtemporal;
    }
    
    public Integer getIid() {
        return iid;
    }
    
    public void setIid(Integer iid) {
        this.iid = iid;
    }
    
    public short getSid() {
        return sid;
    }
    
    public void setSid(short sid) {
        this.sid = sid;
    }
    
    public String getClave() {
        return clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getOperacion() {
        return operacion;
    }
    
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
    
    public boolean isStatusid() {
        return statusid;
    }
    
    public void setStatusid(boolean statusid) {
        this.statusid = statusid;
    }
    
    public void limpiarEstatus() {
        this.paisstatus = false;
        this.estadoestatus = false;
        this.municipioestatus = false;
        this.localidadestatus = false;
        this.tipoasentamientoestatus = false;
        this.asentamientoestatus = false;
        this.entidadpostalestatus = false;
        this.statusid = false;
    }
    
    public void limpiarTodo() {
        this.pais = null;
        this.estado = null;
        this.municipio = null;
        this.localidad = null;
        this.asentamiento = null;
        this.tipoasentamiento = null;
        this.entidadpostal = null;
        
        this.estados = null;
        this.municipios = null;
        this.localidades = null;
        this.asentamientos = null;
        this.tiposasentamiento = null;
        this.entidadespostales = null;
        
        this.iid = 0;
        this.sid = 0;
        this.clave = "";
        this.descripcion = "";
        this.codigopostal = "";
        
        this.statuspostal = false;
        
        this.limpiarEstatus();
    }
    
    public void obtenerLista(String ubicacion) {
        try {
            if (this.statuspostal == false) {
                switch (ubicacion) {
                    
                    case "Pais":
                        this.paises = null;
                        this.paises = this.paisdao.buscarTodos();
                        break;
                    
                    case "Estado":
                        if (this.pais != null && this.estados == null) {
                            this.estados = this.estadodao.obtenerTodosPorPais(pais.getId());
                        }
                        
                        if (this.pais == null && this.estados != null) {
                            this.estados = null;
                            this.municipios = null;
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.estado = null;
                            this.municipio = null;
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                        }
                        
                        if (this.pais != null && this.estados != null) {
                            this.estados = null;
                            this.municipios = null;
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.estado = null;
                            this.municipio = null;
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                            this.estados = this.estadodao.obtenerTodosPorPais(pais.getId());
                        }
                        
                        break;
                    
                    case "Municipio":
                        if (this.estado != null && this.municipios == null) {
                            this.municipios = this.municipiodao.obtenerTodosPorEstado(this.pais.getId(), this.estado.getKey().getId());
                        }
                        
                        if (this.estado == null && this.municipios != null) {
                            this.municipios = null;
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.municipio = null;
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                        }
                        
                        if (this.estado != null && this.municipios != null) {
                            this.municipios = null;
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.municipio = null;
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                            this.municipios = this.municipiodao.obtenerTodosPorEstado(this.pais.getId(), this.estado.getKey().getId());
                            
                        }
                        break;
                    
                    case "Localidad":
                        if (this.municipio != null && this.localidades == null) {
                            this.localidades = this.localidaddao.obtenerTodosPorMuncipio(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId());
                        }
                        
                        if (this.municipio == null && this.localidades != null) {
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                        }
                        
                        if (this.municipio != null && this.localidades != null) {
                            
                            this.localidades = null;
                            this.tiposasentamiento = null;
                            this.asentamientos = null;
                            this.entidadespostales = null;
                            
                            this.localidad = null;
                            this.tipoasentamiento = null;
                            this.asentamiento = null;
                            this.entidadpostal = null;
                            
                            this.localidades = this.localidaddao.obtenerTodosPorMuncipio(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId());
                            
                        }
                        break;
                    
                    case "Asentamiento":
                        if (this.localidad != null) {
                            this.asentamientos = this.asentamientodao.obtenerTodosPorLocalidad(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId());
                            this.tiposasentamiento = this.tipoasentamientodao.obtenerTodos();
                            this.entidadespostales = this.entidadpostaldao.obtenerTodos();
                        }
                        
                        if (this.localidad == null) {
                            this.asentamientos = null;
                            this.tiposasentamiento = null;
                            this.entidadespostales = null;
                            
                            this.asentamiento = null;
                            this.tipoasentamiento = null;
                            this.entidadpostal = null;
                        }
                        break;
                    
                    case "Tipo Asentamiento":
                        this.tiposasentamiento = this.tipoasentamientodao.obtenerTodos();
                        break;
                    
                    case "Entidad Postal":
                        this.entidadespostales = this.entidadpostaldao.obtenerTodos();
                        break;
                    
                    case "Filtrar Asentamiento Tipo":
                        
                        this.asentamiento = null;
                        
                        if (this.entidadpostal != null) {
                            this.entidadpostal = null;
                            this.asentamientos = null;
                        }
                        
                        if (this.tipoasentamiento != null) {
                            
                            this.asentamientos = this.asentamientodao.obtenerPorTipoAsentamiento(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId(), this.tipoasentamiento.getId());
                        }
                        
                        if (this.tipoasentamiento == null) {
                            
                            this.asentamientos = this.asentamientodao.buscarTodos();
                            
                        }
                        
                        break;
                    
                    case "Filtrar Asentamiento Entidad":
                        
                        this.asentamiento = null;
                        
                        if (this.tipoasentamiento != null) {
                            this.tipoasentamiento = null;
                            this.asentamientos = null;
                        }
                        
                        if (this.entidadpostal != null) {
                            this.asentamientos = this.asentamientodao.obtenerPorEntidadPostal(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId(), this.entidadpostal.getId());
                        }
                        
                        if (this.entidadpostal == null) {
                            this.asentamientos = this.asentamientodao.buscarTodos();
                        }
                        
                        break;
                }
                
            }
        } catch (SGPException ex) {
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("message");
            
        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: hubo algun problema desconocido, notifique al administrador de sistemas.", null));
            PrimeFaces.current().ajax().update("message");
            
        }
        
    }
    
    public void crear(String ubicacion) {
        this.operacion = new String();
        this.operacion = "Agregar " + ubicacion;
        
        this.iid = 0;
        this.clave = "";
        this.descripcion = "";
        this.sid = 0;
        this.codigopostal = "";
        
        switch (ubicacion) {
            case "Pais":
                this.paisstatus = true;
                break;
            
            case "Estado":
                this.estadoestatus = true;
                break;
            
            case "Municipio":
                this.municipioestatus = true;
                break;
            
            case "Localidad":
                this.localidadestatus = true;
                break;
            
            case "Tipo Asentamiento":
                this.tipoasentamientoestatus = true;
                break;
            
            case "Asentamiento":
                this.asentamiento = new CatAsentamiento();
                this.entidadtemporal = new CatEntidadPostal();
                this.tipotemporal = new CatTipoAsentamiento();
                this.asentamientoestatus = true;
                break;
            
            case "Entidad Postal":
                this.entidadpostalestatus = true;
                break;
        }
    }
    
    public void editar(String ubicacion) throws SGPException {
        this.operacion = new String();
        this.operacion = "Editar " + ubicacion;
        this.statusid = true;
        
        switch (ubicacion) {
            case "Pais":
                this.iid = this.pais.getId();
                this.clave = this.pais.getClave();
                this.descripcion = this.pais.getNombre();
                this.paisstatus = true;
                break;
            
            case "Estado":
                this.iid = this.estado.getKey().getId();
                this.clave = this.estado.getClave();
                this.descripcion = this.estado.getDescripcion();
                this.estadoestatus = true;
                break;
            
            case "Municipio":
                this.iid = this.municipio.getKey().getId();
                this.descripcion = this.municipio.getDescripcion();
                this.municipioestatus = true;
                break;
            
            case "Localidad":
                this.iid = this.localidad.getKey().getId();
                this.descripcion = this.localidad.getDescripcion();
                this.localidadestatus = true;
                break;
            
            case "Asentamiento":
                this.tipotemporal = this.asentamiento.getTipoAsentamiento();
                this.entidadtemporal = this.asentamiento.getEntidadPostal();
                this.iid = this.asentamiento.getKey().getId();
                this.codigopostal = this.asentamiento.getCp();
                this.descripcion = this.asentamiento.getDescripcion();
                this.asentamientoestatus = true;
                break;
            
            case "Tipo Asentamiento":
                this.sid = this.tipoasentamiento.getId();
                this.clave = this.tipoasentamiento.getClave();
                this.descripcion = this.tipoasentamiento.getDescripcion();
                this.tipoasentamientoestatus = true;
                break;
            
            case "Entidad Postal":
                this.iid = this.entidadpostal.getId();
                this.descripcion = this.entidadpostal.getDescripcion();
                this.entidadpostalestatus = true;
                break;
        }
    }
    
    public void eliminar(String ubicacion) {
        
        this.operacion = new String();
        
        this.operacion = "Eliminar " + ubicacion;
        
    }
    
    public void operar() {
        try {
            
            switch (this.operacion) {
                
                case "Agregar Pais":
                    this.validarDatos(1, true);
                    this.pais = new Pais();
                    this.pais.setId(this.iid);
                    this.pais.setClave(this.clave);
                    this.pais.setNombre(this.descripcion);
                    
                    this.paisdao.guardar(this.pais);
                    
                    break;
                
                case "Editar Pais":
                    
                    this.validarDatos(1, true);
                    
                    this.pais.setClave(this.clave);
                    this.pais.setNombre(this.descripcion);
                    
                    this.paisdao.actualizar(this.pais);
                    
                    break;
                
                case "Eliminar Pais":
                    
                    this.paisdao.eliminar(this.pais);
                    
                    break;
                
                case "Agregar Estado":
                    this.validarDatos(1, true);
                    this.estado = new CatEstado();
                    
                    this.estado.setKey(new CatEstadoPK());
                    this.estado.getKey().setPais(this.pais);
                    this.estado.getKey().setId(this.iid);
                    this.estado.setClave(this.clave);
                    this.estado.setDescripcion(this.descripcion);
                    
                    this.estadodao.guardar(this.estado);
                    
                    break;
                
                case "Editar Estado":                    
                    this.validarDatos(1, true);
                    
                    this.estado.setClave(this.clave);
                    this.estado.setDescripcion(this.descripcion);
                    
                    this.estadodao.actualizar(this.estado);
                    
                    break;
                
                case "Eliminar Estado":
                    
                    this.estadodao.eliminar(this.estado);
                    
                    break;
                
                case "Agregar Municipio":
                    this.validarDatos(1, false);
                    this.municipio = new CatMunicipio();
                    
                    this.municipio.setKey(new CatMunicipioPK());
                    this.municipio.getKey().setEstado(this.estado);
                    this.municipio.getKey().setId(this.iid);
                    this.municipio.setDescripcion(this.descripcion);
                    
                    this.municipiodao.guardar(this.municipio);
                    
                    break;
                
                case "Editar Municipio":
                    this.validarDatos(1, false);
                    
                    this.municipio.setDescripcion(this.descripcion);
                    
                    this.municipiodao.actualizar(this.municipio);
                    
                    break;
                
                case "Eliminar Municipio":
                    
                    this.municipiodao.eliminar(this.municipio);
                    
                    break;
                
                case "Agregar Localidad":
                    this.validarDatos(1, false);
                    this.localidad = new CatLocalidad();
                    
                    this.localidad.setKey(new CatLocalidadPK());
                    this.localidad.getKey().setMunicipio(this.municipio);
                    this.localidad.getKey().setId(this.iid);
                    this.localidad.setDescripcion(this.descripcion);
                    
                    this.localidaddao.guardar(this.localidad);
                    
                    break;
                
                case "Editar Localidad":                    
                    this.validarDatos(1, false);
                    
                    this.localidad.setDescripcion(this.descripcion);
                    
                    this.localidaddao.actualizar(this.localidad);
                    
                    break;
                
                case "Eliminar Localidad":
                    
                    this.localidaddao.eliminar(this.localidad);
                    
                    break;
                
                case "Agregar Asentamiento":
                    this.validarDatos(1, false);
                    this.codigopostal = this.validarCodigoPostal();
                    this.asentamiento.setKey(new CatAsentamientoPK());
                    this.asentamiento.getKey().setLocalidad(this.localidad);
                    this.asentamiento.getKey().setId(this.iid);
                    this.asentamiento.setTipoAsentamiento(this.tipotemporal);
                    this.asentamiento.setEntidadPostal(this.entidadtemporal);
                    this.asentamiento.setCp(this.codigopostal);
                    this.asentamiento.setDescripcion(this.descripcion);
                    
                    this.asentamientodao.guardar(this.asentamiento);
                    
                    break;
                
                case "Editar Asentamiento":
                    this.validarDatos(1, false);
                    this.clave = this.validarCodigoPostal();
                    
                    this.asentamientodao.actualizar(this.descripcion, this.codigopostal, this.entidadtemporal, this.tipotemporal, this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId(), this.asentamiento.getKey().getId());
                    
                    break;
                
                case "Eliminar Asentamiento":
                    
                    this.asentamientodao.eliminar(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId(), this.asentamiento.getKey().getId(), this.asentamiento.getTipoAsentamiento().getId(), this.asentamiento.getEntidadPostal().getId());
                    
                    break;
                
                case "Agregar Tipo Asentamiento":
                    this.validarDatos(2, true);
                    this.tipoasentamiento = new CatTipoAsentamiento();
                    
                    this.tipoasentamiento.setId(this.sid);
                    this.tipoasentamiento.setClave(this.clave);
                    this.tipoasentamiento.setDescripcion(this.descripcion);
                    
                    this.tipoasentamientodao.guardar(this.tipoasentamiento);
                    
                    break;
                
                case "Editar Tipo Asentamiento":
                    this.validarDatos(2, true);
                    this.tipoasentamiento.setClave(this.clave);
                    this.tipoasentamiento.setDescripcion(this.descripcion);
                    
                    this.tipoasentamientodao.actualizar(this.tipoasentamiento);
                    
                    break;
                
                case "Eliminar Tipo Asentamiento":
                    
                    this.tipoasentamientodao.eliminar(this.tipoasentamiento);
                    
                    break;
                
                case "Agregar Entidad Postal":
                    this.validarDatos(1, false);
                    this.entidadpostal = new CatEntidadPostal();
                    
                    this.entidadpostal.setId(this.iid);
                    this.entidadpostal.setDescripcion(this.descripcion);
                    
                    this.entidadpostaldao.guardar(this.entidadpostal);
                    
                    break;
                
                case "Editar Entidad Postal":
                    this.validarDatos(1, false);
                    this.entidadpostal.setDescripcion(this.descripcion);
                    
                    this.entidadpostaldao.actualizar(this.entidadpostal);
                    
                    break;
                
                case "Eliminar Entidad Postal":
                    
                    this.entidadpostaldao.eliminar(this.entidadpostal);
                    
                    break;
            }
            
            this.iid = 0;
            this.sid = 0;
            this.clave = "";
            this.descripcion = "";
            this.codigopostal = "";
            String fin = "";
            
            if (this.operacion.contains("Agregar ")) {
                fin = this.operacion.replace("Agregar ", "");
            }
            
            if (this.operacion.contains("Editar ")) {
                fin = this.operacion.replace("Editar ", "");
            }
            
            if (this.operacion.contains("Eliminar ")) {
                fin = this.operacion.replace("Eliminar ", "");
            }
            
            this.obtenerLista(fin);
            this.limpiarObjeto(fin);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion realizada exitosamente.", null));
            PrimeFaces.current().ajax().update("message");
            
        } catch (SGPException ex) {
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("message");
            
        } catch (Exception ex) {
            
            log.error("Error: " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema desconocido, notifique al administrador de sistemas.", null));
            PrimeFaces.current().ajax().update("message");
            
        } finally {
            this.limpiarEstatus();
        }
        
    }
    
    private void limpiarObjeto(String ubicacion) {
        
        switch (ubicacion) {
            case "Pais":
                this.pais = null;
                break;
            
            case "Estado":
                this.estado = null;
                break;
            
            case "Municipio":
                this.municipio = null;
                break;
            
            case "Localidad":
                this.localidad = null;
                break;
            
            case "Asentamiento":
                this.asentamiento = null;
                break;
            
            case "Tipo Asentamiento":
                this.tipoasentamiento = null;
                break;
            
            case "Entidad Postal":
                this.entidadpostal = null;
                break;
        }
    }
    
    private String validarCadena(String cadena, Integer tamaniominimo, Integer tamaniomaximo) throws Exception {
        
        cadena = cadena.trim();
        char[] permitidos = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z',
            'Á', 'É', 'Í', 'Ó', 'Ú',
            'á', 'é', 'í', 'ó', 'ú',
            ' ', 'Ü', 'ü', 'Ñ', 'ñ',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        
        if (cadena == null) {
            throw new Exception("El dato introducido no puede ser nulo");
        }
        
        if (cadena.equals("")) {
            throw new Exception("El dato introducido no puede ser vacio");
        }
        
        for (int i = 0; i < cadena.length(); i++) {
            boolean bandera = false;
            char letra = cadena.charAt(i);
            for (int j = 0; j < permitidos.length; j++) {
                if (permitidos[j] == letra) {
                    bandera = true;
                    break;
                }
            }
            if (bandera == false) {
                throw new Exception("La cadena introducida tiene un caracter no permitido");
            }
        }
        
        if (cadena.length() < tamaniominimo || cadena.length() > tamaniomaximo) {
            throw new Exception("La cadena introducida no tiene el numero de catacteres permitidos");
        }
        return cadena;
    }
    
    private void validarDatos(Integer tipoentero, boolean tieneclave) throws Exception {
        
        this.descripcion = this.validarCadena(this.descripcion, 1, 150);
        
        if (tieneclave) {
            this.clave = this.validarCadena(this.clave, 2, 3);
        }
        
        switch (tipoentero) {
            case 1:
                if (this.iid < 1) {
                    throw new SGPException("El ID no puede ser menor a uno");
                }
                break;
            
            case 2:
                if (this.sid < 1) {
                    throw new SGPException("El ID no puede ser menor a uno");
                }
                break;
        }
    }
}
