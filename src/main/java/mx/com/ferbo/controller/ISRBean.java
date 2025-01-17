/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

import mx.com.ferbo.dao.n.TarifaISRDAO;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
@Named(value = "isrBean")
@ViewScoped
public class ISRBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(UMABean.class);

	private CatTarifaISR tarifaISR = null;
	private TarifaISRDAO tarifaISRDAO = null;
	private List<CatTarifaISR> listTarifaISR = null;
	private List<CatTarifaISR> listTarifaISRSelected = null;
	private List<Integer> anios = null;
	private Integer anio = null;
	private String periodo = null;

	public ISRBean() {
		this.listTarifaISRSelected = new ArrayList<>();
		this.tarifaISRDAO = new TarifaISRDAO();
	}

	@PostConstruct
	public void init() {
		Integer anio = null;
		Date fecha = DateUtil.now();
		
		this.anios = new ArrayList<Integer>();
		anio = DateUtil.getAnio(fecha) + 1;
		this.anios.add(anio--);
		this.anios.add(anio--);
		this.anios.add(anio--);
		this.anios.add(anio--);
		
		this.anio = DateUtil.getAnio(fecha);
	}

	public void filtrar() {
		String mensaje = null;
		Severity severity = null;	
		String titulo = "Tarifas ISR";

		Date fechaInicio = null;
		Date fechaFin = null;
		
		try {
			fechaInicio = DateUtil.getDateTime(this.anio, DateUtil.ENERO, 1, 0, 0, 0, 0);
			fechaFin = DateUtil.getDateTime(this.anio, DateUtil.DICIEMBRE, 31, 23, 59, 59, 999);
			
			this.listTarifaISR = this.tarifaISRDAO.buscar(fechaInicio, fechaFin, periodo);
			
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema consultar las tarias de ISR.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, mensaje));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
		}
	}
	
	public void openNew() {
		Date fechaInicioAnio = null;
		fechaInicioAnio = DateUtil.getDateTime(this.anio, DateUtil.ENERO, 1, 0, 0, 0, 0);
		
		this.tarifaISR = new CatTarifaISR();
		this.tarifaISR.setFecha(fechaInicioAnio);
		
		this.tarifaISR.setTipo(this.periodo);
	}

	public void crearISRNuevo() {
		String mensaje = null;
		Severity severity = null;	
		String titulo = "Tarifas ISR";
		
		try {
			if (this.tarifaISR.getIdIsr() == null) {
				this.tarifaISRDAO.guardar(this.tarifaISR);
				mensaje = "ISR Añadida";
			} else {
				this.tarifaISRDAO.actualizar(this.tarifaISR);
				mensaje = "ISR Actualizada";
			}
			this.filtrar();
			
			mensaje = String.format("Tarifas del %d", this.anio);
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('manageISRDialog').hide()");
		} catch (SGPException ex) {
			log.error("Ocurrió un problema al guardar la cuota de ISR.", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la cuota de ISR.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, mensaje));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
		}
	}
	
	public void borrarISRRegistro() {
		String mensaje = null;
		Severity severity = null;	
		String titulo = "Tarifas ISR";
		
		try {
			if (this.tarifaISR == null) {
				throw new SGPException("Debe seleccionar una cuota de ISR.");
			}
			
			this.tarifaISRDAO.eliminar(this.tarifaISR);
			this.tarifaISR = null;
			this.filtrar();
			
			mensaje = String.format("Tarifas del %d", this.anio);
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('borrarISRDialog').hide()");
		} catch (SGPException ex) {
			log.error("Error al eliminar una ISR.." + ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Problema para eliminar la cuota.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, mensaje));
		}
	}

	public CatTarifaISR getTarifaISR() {
		return tarifaISR;
	}

	public void setTarifaISR(CatTarifaISR tarifaISR) {
		this.tarifaISR = tarifaISR;
	}

	public List<CatTarifaISR> getListTarifaISR() {
		return listTarifaISR;
	}

	public void setListTarifaISR(List<CatTarifaISR> listTarifaISR) {
		this.listTarifaISR = listTarifaISR;
	}

	public List<CatTarifaISR> getListTarifaISRSelected() {
		return listTarifaISRSelected;
	}

	public void setListTarifaISRSelected(List<CatTarifaISR> listTarifaISRSelected) {
		this.listTarifaISRSelected = listTarifaISRSelected;
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

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

}
