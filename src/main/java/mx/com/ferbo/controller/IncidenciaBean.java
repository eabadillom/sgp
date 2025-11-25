package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.controller.incidencias.IncidenciasAusenciasBean;
import mx.com.ferbo.controller.incidencias.IncidenciasArticulosBean;
import mx.com.ferbo.controller.incidencias.IncidenciasPrendasBean;
import mx.com.ferbo.controller.incidencias.IncidenciasSolicitudesBean;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.SGPException;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Gabriel
 */
@Named("incidenciaBean")
@ViewScoped
public class IncidenciaBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciaBean.class);
    
    @Inject
    private IncidenciasAusenciasBean ausenciasBean;
    
    @Inject
    private IncidenciasSolicitudesBean incidenciasSolicitudesBean;
    
    @Inject
    private IncidenciasPrendasBean incidenciasPrendasBean;
    
    @Inject
    private IncidenciasArticulosBean incidenciasArticulosBean;
    
    private Date fechaInicio;
    private Date fechaFin;
    
    private int activeTabIndex;
    
    public IncidenciaBean() {
        log.info("Entrando a la sección de incidencias");
        this.fechaFin = new Date();
        this.fechaInicio = DateUtil.moverFechaSemanaAtras(this.fechaFin);
        this.activeTabIndex=0;
    }

    @PostConstruct
    public void init() {
        this.cargarTab(activeTabIndex);
    }
    
    public void onTabChange(TabChangeEvent event){
        int index = ((TabView) event.getComponent()).getActiveIndex();
        this.activeTabIndex = index;
        cargarTab(this.activeTabIndex);
    }
    
    public void onFechaChange() {
        cargarTab(this.activeTabIndex);
    }

    public void cargarTab(int index) {
        try {
            switch (index) {
                case 0:
                    this.ausenciasBean.cargar(this.fechaInicio, this.fechaFin);
                    break;
                case 1:
                    this.incidenciasSolicitudesBean.cargar(this.fechaInicio, this.fechaFin);
                    break;
                case 2:
                    this.incidenciasPrendasBean.cargar(this.fechaInicio, this.fechaFin);
                    break;
                case 3:
                    this.incidenciasArticulosBean.cargar(this.fechaInicio, this.fechaFin);
                    break;
                default:
                    throw new SGPException("Error al abrir una pestaña no configurada");
            } 
        } catch (SGPException e) {
            log.warn("Error al abrir una pestaña no configurada");
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", e.getMessage());
        } catch (Exception ex) {
            log.warn("Error al abrir una pestaña no configurada: {}", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Hubo algun error, consulte al administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }
    // </editor-fold>
    
}
