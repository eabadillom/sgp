package mx.com.ferbo.controller;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    private List<Pais> paises;
    private PaisDAO paisdao;

    // Variables de estado
    private CatEstado estado;
    private List<CatEstado> estados;
    private EstadoDAO estadodao;

    // Variables de municipio
    private CatMunicipio municipio;
    private List<CatMunicipio> municipios;
    private MunicipioDAO municipiodao;

    // Variables de localidad
    private CatLocalidad localidad;
    private List<CatLocalidad> localidades;
    private LocalidadDAO localidaddao;

    // Variables de asentamiento
    private CatAsentamiento asentamiento;
    private List<CatAsentamiento> asentamientos;
    private AsentamientoDAO asentamientodao;

    // Variables tipo de asentamiento
    private CatTipoAsentamiento tipoasentamiento;
    private List<CatTipoAsentamiento> tiposasentamiento;
    private TipoAsentamientoDAO tipoasentamientodao;

    //Variables codigo postal
    private CatEntidadPostal entidadpostal;
    private List<CatEntidadPostal> entidadespostales;
    private EntidadPostalDAO entidadpostaldao;

    // Codigo postal
    private String codigopostal;

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
    }

    // Implementacion de Pais
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public void nuevoPais() {
        this.pais = new Pais();
    }

    public void pasarPais(Pais paistmp) {
        try {
            this.pais = paistmp;
        } catch (Exception ex) {
            log.error("Problema en asignar el pais...", ex);
        }
    }

    // implementacion de estado
    public CatEstado getEstado() {
        return estado;
    }

    public void setEstado(CatEstado estado) {
        this.estado = estado;
    }

    public List<CatEstado> getEstados() {
        return estados;
    }

    public void nuevoEstado() {
        this.estado = new CatEstado();
    }

    public void pasarEstado(CatEstado estadotmp) {
        try {
            this.estado = estadotmp;
        } catch (Exception ex) {
            log.error("Problema en asignar el pais...", ex);
        }
    }

    // Implementacion de municipio
    public CatMunicipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(CatMunicipio municipio) {
        this.municipio = municipio;
    }

    public List<CatMunicipio> getMunicipios() {
        return municipios;
    }

    public void nuevoMunicipio() {
        this.municipio = new CatMunicipio();
    }

    public void pasarMunicipio(CatMunicipio municipiotmp) {
        try {
            this.municipio = municipiotmp;
        } catch (Exception ex) {
            log.error("Problema en asignar el municipio...", ex);
        }
    }

    // Implementacion de localidad
    public CatLocalidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) {
        this.localidad = localidad;
    }

    public List<CatLocalidad> getLocalidades() {
        return localidades;
    }

    public void nuevaLocalidad() {
        this.localidad = new CatLocalidad();
    }

    public void pasarLocalidad(CatLocalidad localidadtmp) {
        try {
            this.localidad = localidadtmp;
        } catch (Exception ex) {
            log.error("Problema en asignar la localidad...", ex);
        }
    }

    // Implementacion de asentamiento
    public CatAsentamiento getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(CatAsentamiento asentamiento) {
        this.asentamiento = asentamiento;
    }

    public List<CatAsentamiento> getAsentamientos() {
        return asentamientos;
    }

    public void nuevoAsentamiento() {
        this.asentamiento = new CatAsentamiento();
    }

    public void pasarAsentamiento(CatAsentamiento asentamientotmp) {
        try {
            this.asentamiento = asentamientotmp;
        } catch (Exception ex) {
            log.error("Problema en asignar la localidad...", ex);
        }
    }

    // Implementacion tipo asentamiento
    public CatTipoAsentamiento getTipoasentamiento() {
        return tipoasentamiento;
    }

    public void setTipoasentamiento(CatTipoAsentamiento tipoasentamiento) {
        this.tipoasentamiento = tipoasentamiento;
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
            char digito = cd.charAt(i);
            for (int j = 0; j < permitidos.length; j++) {
                if (permitidos[j] != digito) {
                    throw new SGPException("El codigo postal solo debe contener digitos de 0 a 9");
                }
            }
        }

        if (cd.length() != 4) {
            throw new SGPException("El codigo postal solo debe ser de ocho digitos");
        }

        return cd;
    }
    
    public void bucarCodigoPostal(){
        
        try {
            this.paises = null;
            this.estados = null;
            this.municipios = null;
            this.localidades = null;
            
            this.asentamientos = this.asentamientodao.buscarPorCodigoPostal(this.validarCodigoPostal());
            
            for(CatAsentamiento tmp : this.asentamientos){
                this.paises.add(tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais());
                this.estados.add(tmp.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado());
                this.municipios.add(tmp.getKey().getLocalidad().getKey().getMunicipio());
                this.localidades.add(tmp.getKey().getLocalidad());
            }
            
        }
        catch(SGPException ex){
            
        }
        catch(Exception ex){
        
        }
    }

    // generales
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

}
