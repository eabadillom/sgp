package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
@Named(value = "cuotasImssBean")
@ViewScoped
public class CuotasImssBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(CuotasImssBean.class); 
    
    private CatCuotaIMSS cuotaIMSSNueva;
    private final CuotaIMSSDAO cuotaIMSSDAO;
    private List<CatCuotaIMSS> cuotasIMSS;
    private List<Integer> anios = null;
	private Integer anio = null;

	public CuotasImssBean() {
		this.cuotaIMSSDAO = new CuotaIMSSDAO();
	}
    
	@PostConstruct
	public void init() {
		Integer anio = null;
		Date fecha = new Date();

		this.anios = new ArrayList<Integer>();
		anio = DateUtil.getAnio(fecha) + 1;
		this.anios.add(anio--);
		this.anios.add(anio--);
		this.anios.add(anio--);
		this.anios.add(anio--);

		this.anio = DateUtil.getAnio(fecha);
		
		this.filtrar();
	}
	
	public void filtrar() {
		String mensaje = null;
		Severity severity = null;	
		String titulo = "Cuotas IMSS";

		try {
			if(this.anio == null)
				throw new SGPException("Debe indicar un año para la consulta");
			
			this.cuotasIMSS = this.cuotaIMSSDAO.buscarPorAnio(this.anio);
			mensaje = String.format("Cuotas IMSS del %d", this.anio);
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Ocurrió un problema al consultar las cuotas del IMSS...", ex);
			mensaje = "Ocurrió un problema al consultar las cuotas del IMSS.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, mensaje));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public void actualizarListaCuotas() {
		this.cuotasIMSS = this.cuotaIMSSDAO.obtenerLista();
	}
    
    public void crearCuotaIMSSNueva() {
        try {
            if (this.cuotaIMSSNueva.getNumero() == null) {
                Integer numeroClave = this.obtenerNoClaveMax();
                this.cuotaIMSSNueva.setNumero(numeroClave);
                this.cuotaIMSSDAO.guardar(this.cuotaIMSSNueva);
                this.actualizarListaCuotas();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-imss");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cuota Añadida"));
            }else{
                this.cuotaIMSSDAO.actualizar(this.cuotaIMSSNueva);
                PrimeFaces.current().ajax().update("form:messages", "form:dt-imss");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cuota Actualizada"));
            }
            PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
        } catch (SGPException ex) {
            log.warn("EX-0037: " + ex.getMessage() + ". Error al guardar las cuotas del IMSS. " + cuotaIMSSNueva.getCuota() != null ? cuotaIMSSNueva.getCuota() : null); 
        }
    }
    
	public Integer obtenerNoClaveMax() {
		Integer maxNumero = 0;
		Integer iterador = 0;
		String clave = this.cuotaIMSSNueva.getClave();

		for (CatCuotaIMSS auxCatCuotaIMSS : this.cuotasIMSS) {
			if (auxCatCuotaIMSS.getClave().equals(clave)) {
				iterador++;
			}
		}

		maxNumero = iterador;
		return maxNumero;
	}
    
    public void openNew() {
        this.cuotaIMSSNueva = new CatCuotaIMSS();
    }
    
    public CatCuotaIMSS getCuotaIMSSNueva() {
        return cuotaIMSSNueva;
    }
    
    public void setCuotaIMSSNueva(CatCuotaIMSS cuotaImssNueva) {
        this.cuotaIMSSNueva = cuotaImssNueva;
    }

    public List<CatCuotaIMSS> getCuotasIMSS() {
        return cuotasIMSS;
    }

    public void setCuotasIMSS(List<CatCuotaIMSS> cuotasIMSS) {
        this.cuotasIMSS = cuotasIMSS;
    }

	public List<Integer> getAnios() {
		return anios;
	}

	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
}
