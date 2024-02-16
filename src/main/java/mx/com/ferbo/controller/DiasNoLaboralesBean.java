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

import mx.com.ferbo.dao.DiaNoLaboralDAO;
import mx.com.ferbo.dao.PaisDAO;
import mx.com.ferbo.dto.DiaNoLaboralDTO;
import mx.com.ferbo.dto.PaisDTO;
import mx.com.ferbo.util.DateUtils;

@Named(value = "diasNLBean")
@ViewScoped
public class DiasNoLaboralesBean implements Serializable {

	private static final long serialVersionUID = 685552148187441846L;
	private static Logger log = LogManager.getLogger(DiasNoLaboralesBean.class);
	
	private List<Integer> aniosList = null;
	private Integer anioSelected = null;
	private List<PaisDTO> paisesList = null;
	private PaisDTO paisSelected = null;
	List<DiaNoLaboralDTO> listaDiasNL = null;
	DiaNoLaboralDTO diaNLSelected = null;
	
	
	private DiaNoLaboralDAO diaNLDAO = null;
	private PaisDAO paisDAO = null;
	
	public DiasNoLaboralesBean() {
		log.info("Entrando al controller de Dias no laborales...");
	}
	
	@PostConstruct
	public void init() {
		Date fechaActual = new Date();
		Integer anioActual = null;
		
		try {
			anioActual = DateUtils.getAnio(fechaActual);
			this.aniosList = new ArrayList<Integer>();
			this.aniosList.add(anioActual);
			this.aniosList.add((anioActual + 1));
			
			this.anioSelected = anioActual;
			
			this.diaNLDAO = new DiaNoLaboralDAO();
			this.paisDAO = new PaisDAO();
			
			this.paisesList =  paisDAO.buscarTodos();
			this.paisSelected = paisDAO.buscarPorId("MX");
			
			this.muestraDiasNoLaborales();
		} catch(Exception ex) {
			log.error("Problema para cargar la pagina...", ex);
		}
		
	}
	
	public void muestraDiasNoLaborales() {
		Date fechaInicio = null;
		Date fechaFin = null;
		
		try {
			log.info("Año seleccionado: {}", this.anioSelected);
			fechaInicio = DateUtils.getDate(this.anioSelected, 0, 1);
			fechaFin = DateUtils.getDate(this.anioSelected, 11, 31);
			
			DateUtils.setTime(fechaInicio, 0, 0, 0, 0);
			DateUtils.setTime(fechaFin, 23, 59, 59, 999);
			
			log.info("Periodo de busqueda: {} al {}", fechaInicio, fechaFin);
			if(paisSelected != null)
				listaDiasNL = diaNLDAO.buscarPorPeriodo(this.paisSelected.getClavePais(), fechaInicio, fechaFin);
			
			log.info("Dias cargados: {}", this.listaDiasNL);
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de dìas no laborales...", ex);
		}
		
	}
	
	public void nuevoDiaNoLaboral() {
		log.info("Año seleccionado: {}", this.anioSelected);
		log.info("Pais seleccionado: {}", this.paisSelected);
		
		this.diaNLSelected = new DiaNoLaboralDTO(null, null, null, this.paisSelected);
		
		
	}
	
	public void guardarDiaNoLaboral() {
		String mensaje = null;
		Severity severity = null;	
		String titulo = "Guardar día no laboral";
		
		try {
			log.info("Guardando día no laboral...");
			if(this.diaNLSelected.getId() == null)
				diaNLDAO.guardar(diaNLSelected);
			else
				diaNLDAO.actualizar(diaNLSelected);
			
			this.muestraDiasNoLaborales();
			
			log.info("El dia no laboral se registro correctamente.");
			PrimeFaces.current().executeScript("PF('dlgDiaNL').hide()");
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "La fecha se registró correctamente.";
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar el día no laboral.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, mensaje));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public List<Integer> getAniosList() {
		return aniosList;
	}

	public void setAniosList(List<Integer> aniosList) {
		this.aniosList = aniosList;
	}

	public Integer getAnioSelected() {
		return anioSelected;
	}

	public void setAnioSelected(Integer anioSelected) {
		this.anioSelected = anioSelected;
	}

	public List<DiaNoLaboralDTO> getListaDiasNL() {
		return listaDiasNL;
	}

	public void setListaDiasNL(List<DiaNoLaboralDTO> listaDiasNL) {
		this.listaDiasNL = listaDiasNL;
	}

	public PaisDTO getPaisSelected() {
		return paisSelected;
	}

	public void setPaisSelected(PaisDTO paisSelected) {
		this.paisSelected = paisSelected;
	}

	public List<PaisDTO> getPaisesList() {
		return paisesList;
	}

	public void setPaisesList(List<PaisDTO> paisesList) {
		this.paisesList = paisesList;
	}

	public DiaNoLaboralDTO getDiaNLSelected() {
		return diaNLSelected;
	}

	public void setDiaNLSelected(DiaNoLaboralDTO diaNLSelected) {
		this.diaNLSelected = diaNLSelected;
	}

}
