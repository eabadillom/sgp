package mx.com.ferbo.controller;

import java.io.Serializable;
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

import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "empresasBean")
@ViewScoped
public class EmpresasBean implements Serializable {

	private static final long serialVersionUID = -239468326187620536L;
	private static Logger log = LogManager.getLogger(EmpresasBean.class);
	private String contextPath = null;
	private List<CatEmpresaDTO> empresas;
	private CatEmpresaDTO empresa;
	private CatEmpresaDAO empresaDAO;
	
	private List<RegimenFiscalDTO> regimenes;
	private RegimenFiscalDAO regimenDAO;
	
	public EmpresasBean() {
		empresaDAO = new CatEmpresaDAO();
		regimenDAO = new RegimenFiscalDAO();
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		empresas = empresaDAO.buscarActivo();
		regimenes = regimenDAO.buscarActivo(new Date());
	}
	
	public void nuevo() {
		this.empresa = new CatEmpresaDTO();
		this.empresa.setActivo(true);
		this.empresa.setStatusPadron("A");
		this.empresa.setTipoPersona("M");
		log.info("Nueva empresa");
	}
	
	public void editar() {
		log.info("Editando empresa: {}", this.empresa);
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar empresa";
		
		try {
			if(this.empresa == null)
				throw new SGPException("No hay información de la empresa.");
			
			if("M".equalsIgnoreCase(this.empresa.getTipoPersona()) && (this.empresa.getRegimenCapital() == null || "".equalsIgnoreCase(this.empresa.getRegimenCapital().trim())))
				throw new SGPException("Debe indicar un régimen capital.");
			
			if(this.empresa.getIdEmpresa() == null)
				empresaDAO.guardar(empresa);
			else
				empresaDAO.actualizar(empresa);
			
			empresas = empresaDAO.buscarActivo();
			
			mensaje = "La empresa se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgEmpresa').hide()");
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Existe un problema para guardar la información.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa");
		}
	}

	public List<CatEmpresaDTO> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<CatEmpresaDTO> empresas) {
		this.empresas = empresas;
	}

	public CatEmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(CatEmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public List<RegimenFiscalDTO> getRegimenes() {
		return regimenes;
	}

	public void setRegimenes(List<RegimenFiscalDTO> regimenes) {
		this.regimenes = regimenes;
	}

}
