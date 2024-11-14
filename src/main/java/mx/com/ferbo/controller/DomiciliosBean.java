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
import mx.com.ferbo.model.CatEntidadPostal;
import mx.com.ferbo.model.CatEstado;
import mx.com.ferbo.model.CatLocalidad;
import mx.com.ferbo.model.CatMunicipio;
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

    // Variables de pais
    private Pais pais;
    private Pais nuevopais;
    private Pais editablepais;
    private List<Pais> paises;
    private PaisDAO paisdao;
    private boolean paisStatus;

    // Variables de estado
    private CatEstado estado;
    private CatEstado nuevoestado;
    private CatEstado editableestado;
    private List<CatEstado> estados;
    private EstadoDAO estadodao;
    private boolean estadoestatus;

    // Variables de municipio
    private CatMunicipio municipio;
    private CatMunicipio nuevomunicipio;
    private CatMunicipio editablemunicipio;
    private List<CatMunicipio> municipios;
    private MunicipioDAO municipiodao;
    private boolean municipioestatus;

    // Variables de localidad
    private CatLocalidad localidad;
    private CatLocalidad nuevalocalidad;
    private CatLocalidad editablelocalidad;
    private List<CatLocalidad> localidades;
    private LocalidadDAO localidaddao;
    private boolean localidadestatus;

    // Variables de asentamiento
    private CatAsentamiento asentamiento;
    private CatAsentamiento nuevoasentamiento;
    private CatAsentamiento editableasentamiento;
    private List<CatAsentamiento> asentamientos;
    private AsentamientoDAO asentamientodao;
    private boolean asentamientoestatus;

    // Variables tipo de asentamiento
    private CatTipoAsentamiento tipoasentamiento;
    private CatTipoAsentamiento nuevotipoasentamiento;
    private CatTipoAsentamiento editabletipoasentamiento;
    private List<CatTipoAsentamiento> tiposasentamiento;
    private TipoAsentamientoDAO tipoasentamientodao;
    private boolean tipoasentamientoestatus;

    //Variables codigo postal
    private CatEntidadPostal entidadpostal;
    private CatEntidadPostal nuevaentidadpostal;
    private CatEntidadPostal editableentidadpostal;
    private List<CatEntidadPostal> entidadespostales;
    private EntidadPostalDAO entidadpostaldao;
    private boolean entidadpostalestatus;

    // Codigo postal
    private String codigopostal;

    private String operacion;
    private String ubicacion;
    private String seleccion;

    private FacesContext fc;
    private PrimeFaces pf;
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
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
        this.setPaisStatus(false);
        this.setEstadoestatus(false);
        this.setMunicipioestatus(false);
        this.setLocalidadestatus(false);
        this.setTipoasentamientoestatus(false);
        this.setAsentamientoestatus(false);
        this.setEntidadpostalestatus(false);
    }

    // Implementacion de Pais
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Pais getNuevopais() {
        return nuevopais;
    }

    public void setNuevopais(Pais nuevopais) {
        this.nuevopais = nuevopais;
    }

    public Pais getEditablepais() {
        return editablepais;
    }

    public void setEditablepais(Pais editablepais) {
        this.editablepais = editablepais;
    }

    public boolean isPaisStatus() {
        return paisStatus;
    }

    public void setPaisStatus(boolean paisStatus) {
        this.paisStatus = paisStatus;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    // implementacion de estado
    public CatEstado getEstado() {
        return estado;
    }

    public void setEstado(CatEstado estado) {
        this.estado = estado;
    }

    public CatEstado getNuevoestado() {
        return nuevoestado;
    }

    public void setNuevoestado(CatEstado nuevoestado) {
        this.nuevoestado = nuevoestado;
    }

    public CatEstado getEditableestado() {
        return editableestado;
    }

    public void setEditableestado(CatEstado editableestado) {
        this.editableestado = editableestado;
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

    // Implementacion de municipio
    public CatMunicipio getMunicipio() {
        return municipio;
    }

    public CatMunicipio getNuevomunicipio() {
        return nuevomunicipio;
    }

    public void setNuevomunicipio(CatMunicipio nuevomunicipio) {
        this.nuevomunicipio = nuevomunicipio;
    }

    public CatMunicipio getEditablemunicipio() {
        return editablemunicipio;
    }

    public void setEditablemunicipio(CatMunicipio editablemunicipio) {
        this.editablemunicipio = editablemunicipio;
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

    // Implementacion de localidad
    public CatLocalidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) {
        this.localidad = localidad;
    }

    public CatLocalidad getNuevalocalidad() {
        return nuevalocalidad;
    }

    public void setNuevalocalidad(CatLocalidad nuevalocalidad) {
        this.nuevalocalidad = nuevalocalidad;
    }

    public CatLocalidad getEditablelocalidad() {
        return editablelocalidad;
    }

    public void setEditablelocalidad(CatLocalidad editablelocalidad) {
        this.editablelocalidad = editablelocalidad;
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

    // Implementacion de asentamiento
    public CatAsentamiento getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(CatAsentamiento asentamiento) {
        this.asentamiento = asentamiento;
    }

    public CatAsentamiento getNuevoasentamiento() {
        return nuevoasentamiento;
    }

    public void setNuevoasentamiento(CatAsentamiento nuevoasentamiento) {
        this.nuevoasentamiento = nuevoasentamiento;
    }

    public CatAsentamiento getEditableasentamiento() {
        return editableasentamiento;
    }

    public void setEditableasentamiento(CatAsentamiento editableasentamiento) {
        this.editableasentamiento = editableasentamiento;
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

    // Implementacion tipo asentamiento
    public CatTipoAsentamiento getTipoasentamiento() {
        return tipoasentamiento;
    }

    public void setTipoasentamiento(CatTipoAsentamiento tipoasentamiento) {
        this.tipoasentamiento = tipoasentamiento;
    }

    public CatTipoAsentamiento getNuevotipoasentamiento() {
        return nuevotipoasentamiento;
    }

    public void setNuevotipoasentamiento(CatTipoAsentamiento nuevotipoasentamiento) {
        this.nuevotipoasentamiento = nuevotipoasentamiento;
    }

    public CatTipoAsentamiento getEditabletipoasentamiento() {
        return editabletipoasentamiento;
    }

    public void setEditabletipoasentamiento(CatTipoAsentamiento editabletipoasentamiento) {
        this.editabletipoasentamiento = editabletipoasentamiento;
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

    // implementacion entidad postal 
    public CatEntidadPostal getEntidadpostal() {
        return entidadpostal;
    }

    public void setEntidadpostal(CatEntidadPostal entidadpostal) {
        this.entidadpostal = entidadpostal;
    }

    public CatEntidadPostal getNuevaentidadpostal() {
        return nuevaentidadpostal;
    }

    public void setNuevaentidadpostal(CatEntidadPostal nuevaentidadpostal) {
        this.nuevaentidadpostal = nuevaentidadpostal;
    }

    public CatEntidadPostal getEditableentidadpostal() {
        return editableentidadpostal;
    }

    public void setEditableentidadpostal(CatEntidadPostal editableentidadpostal) {
        this.editableentidadpostal = editableentidadpostal;
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

    // Implementacion codigo postal
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
            this.paises = new ArrayList<Pais>();
            this.estados = new ArrayList<CatEstado>();
            this.municipios = new ArrayList<CatMunicipio>();
            this.localidades = new ArrayList<CatLocalidad>();
            this.tiposasentamiento = new ArrayList<CatTipoAsentamiento>();
            List<CatAsentamiento> asentamientostmp = new ArrayList<CatAsentamiento>();
            this.asentamientos = this.asentamientodao.buscarPorCodigoPostal(this.validarCodigoPostal());

            for (CatAsentamiento tmp : this.asentamientos) {

                if (this.paises.isEmpty()) {
                    if (!tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getNombrePais().equals("")) {
                        this.pais = tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais();
                        this.paises = this.paisdao.buscarTodos();
                    }
                }

                if (this.estados.isEmpty()) {
                    if (!tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getDescripcion().equals("")) {
                        this.estado = tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado();
                        this.obtenerEstadosPais();
                    }
                }

                if (this.municipios.isEmpty()) {
                    if (!tmp.getKey().getLocalidad().getKey().getMunicipio().getDescripcion().equals("")) {
                        this.municipio = tmp.getKey().getLocalidad().getKey().getMunicipio();
                        this.obtenerMunicipiosEstado();
                    }
                }

                if (!tmp.getKey().getLocalidad().getDescripcion().equals("")) {
                    this.localidad = tmp.getKey().getLocalidad();
                    if (!this.localidades.contains(this.localidad)) {
                        this.localidades.add(this.localidad);
                    }
                }

                if (!tmp.getTipoAsentamiento().getDescripcion().equals("")) {
                    this.tipoasentamiento = tmp.getTipoAsentamiento();
                    if (!this.tiposasentamiento.contains(this.tipoasentamiento)) {
                        this.tiposasentamiento.add(this.tipoasentamiento);
                    }
                }

                if (!tmp.getDescripcion().equals("")) {
                    if (!asentamientostmp.contains(tmp)) {
                        asentamientostmp.add(tmp);
                    }
                }

                this.asentamiento = tmp;

                this.entidadpostal = tmp.getEntidadPostal();
            }

            this.asentamientos = null;

            this.asentamientos = asentamientostmp;

        } catch (SGPException ex) {

        } catch (Exception ex) {

        }
    }

    // generales
    private String getOperacion() {
        return operacion;
    }

    private void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    private void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public void estadosDisponibles() {

        if (this.pais != null) {
            this.obtenerEstadosPais();
        } else {
            this.estados = null;
        }

    }

    private void obtenerEstadosPais() {

        try {
            this.estados = this.estadodao.obtenerTodosPorPais(pais.getId());
        } catch (SGPException ex) {
            log.info(ex);
        }
    }

    public void municipiosDisponibles() {
        if (this.estado != null) {
            this.obtenerMunicipiosEstado();
        } else {
            this.municipios = null;
        }
    }

    private void obtenerMunicipiosEstado() {
        try {
            this.municipios = this.municipiodao.obtenerTodosPorEstado(this.pais.getId(), this.estado.getKey().getId());
        } catch (SGPException ex) {
            log.info(ex);
        }
    }

    public void localidadesDisponibles() {
        if (this.municipio != null) {
            this.obtenerLocalidades();
        } else {
            this.localidades = null;
        }
    }

    private void obtenerLocalidades() {
        try {
            this.localidades = this.localidaddao.obtenerTodosPorMuncipio(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId());
        } catch (SGPException ex) {
            log.info(ex);
        }
    }

    public void tiposAsentamientoDisponibles() {
        if (this.localidad != null) {
            this.obtenerTiposAsentamiento();
        } else {
            this.tiposasentamiento = null;
        }
    }

    private void obtenerTiposAsentamiento() {
        try {
            this.tiposasentamiento = this.tipoasentamientodao.obtenerTodos();
        } catch (SGPException ex) {
            log.info(ex);
        }
    }

    public void asentamientosDisponibles() {
        if (this.tipoasentamiento != null) {
            this.obtenerAsentamientos();
        } else {
            this.asentamientos = null;
        }
    }

    private void obtenerAsentamientos() {
        try {
            this.asentamientos = this.asentamientodao.obtenerTodosPorTipo(this.pais.getId(), this.estado.getKey().getId(), this.municipio.getKey().getId(), this.localidad.getKey().getId(), this.tipoasentamiento.getId());
        } catch (SGPException ex) {
            log.info(ex);
        }
    }

    public void entidadPostalDisponible() {
        if (this.asentamiento != null) {
            this.obtenerEntidadPostal();
        } else {
            this.entidadpostal = null;
        }
    }

    private void obtenerEntidadPostal() {

        try {
            this.entidadpostal = this.entidadpostaldao.buscarPorId(this.asentamiento.getEntidadPostal().getId());
        } catch (Exception ex) {
            log.info(ex);
        }
    }

    public void editar(String ubicacion) {
        this.seleccion = new String();
        this.seleccion = "Editar " + ubicacion;

        switch (ubicacion) {
            case "Pais":
                this.editablepais = this.pais;
                this.paisStatus = true;
                break;

            case "Estado":
                this.editableestado = this.estado;
                this.estadoestatus = true;
                break;

            case "Municipio":
                this.editablemunicipio = this.municipio;
                this.municipioestatus = true;
                break;

            case "Localidad":
                this.editablelocalidad = this.localidad;
                this.localidadestatus = true;
                break;

            case "Tipo Asentamiento":
                this.editabletipoasentamiento = this.tipoasentamiento;
                this.tipoasentamientoestatus = true;
                break;

            case "Asentamiento":
                this.editableasentamiento = this.asentamiento;
                this.tipoasentamientoestatus = true;
                break;

            case "Entidad Postal":
                this.editableentidadpostal = this.entidadpostal;
                this.entidadpostalestatus = true;
                break;
        }
    }

    public void crear(String ubicacion) {
        this.seleccion = new String();
        this.seleccion = "Agregar " + ubicacion;

        switch (ubicacion) {
            case "Pais":
                this.nuevopais = new Pais();
                this.editablepais = this.nuevopais;
                this.paisStatus = true;
                break;

            case "Estado":
                this.nuevoestado = new CatEstado();
                this.editableestado = this.nuevoestado;
                this.estadoestatus = true;
                break;

            case "Municipio":
                this.nuevomunicipio = new CatMunicipio();
                this.editablemunicipio = this.nuevomunicipio;
                this.municipioestatus = true;
                break;

            case "Localidad":
                this.nuevalocalidad = new CatLocalidad();
                this.editablelocalidad = this.nuevalocalidad;
                this.localidadestatus = true;
                break;

            case "Tipo Asentamiento":
                this.nuevotipoasentamiento = new CatTipoAsentamiento();
                this.editabletipoasentamiento = this.nuevotipoasentamiento;
                this.asentamientoestatus = true;
                break;

            case "Asentamiento":
                this.nuevoasentamiento = new CatAsentamiento();
                this.editableasentamiento = this.nuevoasentamiento;
                this.asentamientoestatus = true;
                break;

            case "Entidad Postal":
                this.nuevaentidadpostal = new CatEntidadPostal();
                this.editableentidadpostal = this.nuevaentidadpostal;
                this.entidadpostalestatus = true;
                break;
        }
    }

    // falta por terminar esta funcion
    public void operar() {
        switch (this.getUbicacion()) {
            case "Pais":

                break;

            case "Estado":
                break;

            case "Municipio":
                break;

            case "Localidad":
                break;

            case "Tipo Asentamiento":
                break;

            case "Asentamiento":
                break;
        }
    }
}
