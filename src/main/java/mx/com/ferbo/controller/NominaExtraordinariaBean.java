package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleSelectEvent;

import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.NominaExtraordinariaBL;
import mx.com.ferbo.business.nomina.NominaPeriodoBL;
import mx.com.ferbo.business.nomina.NominaSemanalBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.business.nomina.PercepcionBL;
import mx.com.ferbo.business.percepcion.AbstractPercepcion;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "nominaE")
@ViewScoped
public class NominaExtraordinariaBean implements Serializable {

	private static final long serialVersionUID = -7354838193241382936L;
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBean.class);
	
	private HttpServletRequest request;
    private FacesContext context;
    private String contextPath = null;
    private HttpSession session = null;
	
	private Integer anio = null;
	private Date fecha = null;
	private List<Integer> semanasDelAnio = null;
	private Date fechaPeriodo = null;
	private Date fechaPago = null;
	private DetNominaPeriodo nominaPeriodo = null;
	private DetNomina nomina = null;
    private DetNominaOtroPago otroPago;
    private DetNominaDeduccion deduccion;
	private NominaExtraordinariaBL nominaBO = null;
	
	private CatEmpresa empresa = null;
	private List<CatEmpresa> empresas = null;
	private EmpresaDAO empresaDAO = null;
	
	private CatPeriodicidadPago periodicidad = null;
	private PeriodicidadPagoDAO periodicidadDAO = null;
	
	private EmpleadoDAO empleadoDAO = null;
	private NominaDAO nominaDAO = null;
	private List<DetNomina> listaNomina = null;
	private List<DetNomina> listaNominaSelected = null;
	private ParametrosNomina parametros = null;
	
	private CatPercepcion percepcion = null;
	private List<CatPercepcion> percepciones = null;
	private PercepcionDAO percepcionDAO = null;
	
	private DetNominaPercepcion nomPercepcion = null;
	private List<CatTipoPercepcion> tiposPercepcion = null;
	
	private Boolean detalle = true;

    public NominaExtraordinariaBean() {
    	this.context = FacesContext.getCurrentInstance();
    	this.request = (HttpServletRequest) context.getExternalContext().getRequest();
    	this.session = request.getSession(false);
    	
    	log.info("Entrando al constructor de NominaExtraordinariaBean");
    	empresaDAO = new EmpresaDAO();
    	periodicidadDAO = new PeriodicidadPagoDAO();
    	empleadoDAO = new EmpleadoDAO();
    	nominaDAO = new NominaDAO();
    	percepcionDAO = new PercepcionDAO();
	}
    
    @PostConstruct
    public void init() {
    	
    	try {
    		
//    		if(this.redirigir())
//    			return;
    		
    		log.info("====================== Entrada a nómina extraordinaria ======================");
    		
    		this.empresas = empresaDAO.buscarActivo();
    		this.listaNomina = new ArrayList<DetNomina>();
    		this.listaNominaSelected = new ArrayList<DetNomina>();
    		//De acuerdo a la definición del complemento de nómina 1.2, la periodicidad de pago para
    		//nóminas extraordinarias es 99 (Otro).
    		this.periodicidad = periodicidadDAO.buscarPorId("99");
    		this.nomPercepcion = PercepcionBL.build();
    		this.percepciones = percepcionDAO.buscarTodos();
    	} catch(Exception ex) {
    		log.error("Problema cargar la nómina extraordinaria...", ex);
    	}
    	
    }
    
    private Boolean  redirigir() {
    	Boolean respuesta = false;
    	DetEmpleado empleadoSesion = null;
    	String path = null;
    	empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
    	contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    	ExternalContext context = null;
    	
    	try {
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() == 1) {
    			return respuesta = false;
    		}
    		
	    	path = this.contextPath + "/unauthorized.xhtml";
	    	log.info("Redirigiendo a {}", path);
	    	context = FacesContext.getCurrentInstance().getExternalContext();
	    	context.redirect(path);
	    	respuesta = true;
    	} catch (IOException e) {
    		log.error("Problema para redirigir a la página de error...", e);
    	}
    	
    	return respuesta;
    }
    
    public String statusNomina(DetNomina nomina) {
    	if(nomina == null)
    		return "";
    	
        return ManageStatus.getEstadoEmpleadoEmpresa((nomina.getId() == null ? (short) 2 : (short) 1));
    }
    
    public void calculaPeriodo() {
    	Integer anio = null;
    	try {
    		if(this.empresa == null)
    			throw new SGPException("Debe seleccionar una empresa");
    		
    		if(this.nominaPeriodo == null)
    			anio = DateUtil.getAnio(DateUtil.now());
    		else
    			anio = this.nominaPeriodo.getKey().getAnio();
    		this.nominaPeriodo = NominaPeriodoBL.build(this.empresa, NominaBL.TP_NOMINA_EXTRAORDINARIA, this.periodicidad, anio);
    		this.nominaPeriodo.setPeriodoInicio(DateUtil.setAnio(this.nominaPeriodo.getPeriodoInicio(), anio));
    		this.nominaPeriodo.setPeriodoFin(DateUtil.setAnio(this.nominaPeriodo.getPeriodoFin(), anio));
    		this.nominaPeriodo.setFechaPago(DateUtil.setAnio(this.nominaPeriodo.getFechaPago(), anio));
    		
    	} catch (SGPException ex) {
			log.error("Problema para generar el periodo...", ex);
		}
    }
    
    public void calcularNomina() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
		List<DetEmpleado> listaEmpleados = null;
		
		try {
			this.listaNomina.clear();
			listaEmpleados = this.empleadoDAO.buscarActivoEmpresaIngreso(empresa.getIdEmpresa(), DateUtil.toDate(this.nominaPeriodo.getPeriodoInicio()), DateUtil.toDate(this.nominaPeriodo.getPeriodoFin()));
			this.procesaListaEmpleados(listaEmpleados);
			mensaje = "Nomina cargada correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
    	} catch(Exception ex) {
    		log.error("Problema para procesar la nómina.");
    		mensaje = "Hay un problema para procesar la nómina.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		if(listaEmpleados != null)
    			listaEmpleados.clear();
    		message = new FacesMessage(severity, titulo, mensaje);
    		FacesContext.getCurrentInstance().addMessage(null, message);
    		PrimeFaces.current().ajax().update(":form:messages", "form:acc");
    	}
		
    }
    
    private void procesaListaEmpleados(List<DetEmpleado> listaEmpleados)
    throws SGPException {
    	DetNomina nomina = null;
    	Integer idNominaTmp = -1;
    	
		this.nominaPeriodo = NominaPeriodoBL.build(empresa, NominaBL.TP_NOMINA_EXTRAORDINARIA, periodicidad, this.nominaPeriodo.getKey().getAnio());
		this.nominaPeriodo.setPeriodoInicio(DateUtil.toLocalDate(this.fechaPeriodo));
		this.nominaPeriodo.setPeriodoFin(DateUtil.toLocalDate(this.fechaPeriodo));
		this.nominaPeriodo.setFechaPago(DateUtil.toLocalDate(this.fechaPago));
    	
    	this.parametros = new ParametrosNomina();
    	this.parametros.cargar(this.nominaPeriodo);
    	
    	this.tiposPercepcion = this.parametros.getTiposPercepcion();
    	
    	for(DetEmpleado empleado : listaEmpleados) {
    		log.info("Cargando empleado {} {} {}...", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp());
    		nomina = nominaDAO.buscar(this.empresa.getRfc(), this.nominaPeriodo.getKey().getTipoNomina(), this.nominaPeriodo.getKey().getAnio(), this.nominaPeriodo.getKey().getPeriodo(), empleado.getRfc());
    		
    		if(nomina == null) {
    			nomina = this.procesaEmpleado(empleado);
    			nomina.setId(idNominaTmp--);
    		}
    		
    		this.listaNomina.add(nomina);
    	}
    }
    
    private DetNomina procesaEmpleado(DetEmpleado empleado) {
    	DetNomina nomina = null;
		try {
			nomina = NominaBL.build(NominaBL.TP_NOMINA_EXTRAORDINARIA, this.parametros, empleado);
		} catch (SGPException ex) {
			log.error("Problema para generar el objeto nómina del empleado...", ex);
		}
		return nomina;
    }
    
    public BigDecimal nominaSubtotal() {
    	return this.listaNomina.stream().map(item -> item.getSubtotal()).reduce(ValoresBD._CERO.getValor(), BigDecimal :: add);
    }
    
    public BigDecimal nominaDescuentos() {
    	return this.listaNomina.stream().map(item -> item.getDescuento()).reduce(ValoresBD._CERO.getValor(), BigDecimal :: add);
    }
    
    public BigDecimal nominaTotal() {
    	return this.listaNomina.stream().map(item -> item.getTotal()).reduce(ValoresBD._CERO.getValor(), BigDecimal :: add);
    }
    
    
    public void setTipoPercepcion() {
    	this.nomPercepcion.setClave(this.percepcion.getClave());
    	this.nomPercepcion.setTipoPercepcion(this.percepcion.getTipoPercepcion());
    	this.nomPercepcion.setNombre(this.percepcion.getNombre());
    }
    
    public void actualizarNomina() {
    	log.info("Actualizando nomina...");
    }
    
    public void nuevaPercepcion() {
    	log.info("Agregando nueva percepcion...");
    	
    	this.nomPercepcion = PercepcionBL.build();
    	log.info("Percepcion: {}", this.nomPercepcion);
    	
    	PrimeFaces.current().ajax().update("form:dlg-percepcion-all");
    }
    
	public void agregarPercepcion() {
		log.info("Agregando percepcion...");

		try {
			for (DetNomina nomina : this.listaNominaSelected) {
				NominaBL.agregarPercepcion(nomina, new DetNominaPercepcion(nomPercepcion));
			}
			
			nomPercepcion = PercepcionBL.build();
		} catch (SGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void seleccionarTodos(ToggleSelectEvent event){
    	log.info("Elementos seleccionados: {}", this.listaNominaSelected.size());
    	log.info("Todos los elementos seleccionados: {}", event.isSelected());
    }
    
    public void calcularPercepcion() {
    	//TODO validar parametros del dialog de percepcion.
    	
    	log.info("Percepcion: {}", this.nomPercepcion);
    	if(this.nominaBO == null)
    		this.nominaBO = new NominaExtraordinariaBL();
    	
    	for(DetNomina nomina : this.listaNominaSelected) {
    		log.info("Agreagndo percepción al empleado: {}", nomina.getReceptor().getNombre());
    		this.nominaBO.calcular(nomina, parametros, this.nomPercepcion);
    	}
    }
    
    public void cargarNominaEmpleado(DetNomina nomina) {
    	this.nomina = nomina;
    	log.info("Nomina: {}", this.nomina);
    	
    	if(nomina.getVacaciones() != null) {
    		for(DetVacaciones periodo : nomina.getVacaciones()) {
    			log.info("Periodo vacacional: {}", periodo);
    		}
    	}
    }
    
    public void actualizar() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	try {
    		NominaBL.calcularTotales(nomina, parametros);
    	} catch(Exception ex) {
    		log.error("Problema para recalcular la nómina...", ex);
    		mensaje = "Hay un problema para actualizar la nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update(":form:messages", ":form:dtNomina");
    	}
    }
    
    public void actualizarPercepcion(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		List<String> listaDiasLaboralesEmpleado = null;
		BigDecimal diasLaboralesEmpleado = null;
		List<String> listaDiasNoLaboralesEmpleado = null;
		BigDecimal diasNoLaboralesEmpleado = null;
		BigDecimal diasTrabajados = null;
		DetEmpleado empleado = null;
		
		try {
			empleado = empleadoDAO.buscarPorRFC(this.nomina.getReceptor().getRfc());
			
			listaDiasLaboralesEmpleado = NominaSemanalBL.getDiasLaboralesPorSemana(empleado);
			listaDiasNoLaboralesEmpleado = NominaSemanalBL.getDiasNoLaboralesPorSemana(empleado);
			diasLaboralesEmpleado   = new BigDecimal(listaDiasLaboralesEmpleado.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
			diasNoLaboralesEmpleado = new BigDecimal(listaDiasNoLaboralesEmpleado.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
			diasTrabajados = percepcion.getCantidad();
			
			if(percepcion.getCantidad() != null && AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(percepcion.getClave())) {
				NominaSemanalBL.calcularSueldo(nomina, parametros,  diasLaboralesEmpleado, diasNoLaboralesEmpleado, diasTrabajados, null);
			}
			NominaSemanalBL.procesarISR(nomina, parametros);
			
			this.actualizar();
			
		} catch(Exception ex) {
			log.error("Problema para agregar la percepcion...", ex);
    		mensaje = "Hay un problema para agregar la percepción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void eliminarPercepcion(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		
		try {
			NominaBL.eliminarPercepcion(nomina, percepcion);
			NominaSemanalBL.procesarISR(nomina, parametros);
			this.actualizar();
			
			mensaje = "Percepción eliminada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dlgEliminaPercepcion').hide();");
		} catch(SGPException ex) {
			log.warn("Problema para eliminar la percepcion: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Hay un problema para eliminar la percepcion seleccionada.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void nuevoOtroPago() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.otroPago = NominaBL.nuevoOtroPago(this.nomina);
		} catch(SGPException ex) {
			log.warn("Problema para crear el pago: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
		}
    }
    
    public List<DetNominaOtroPago> filtraOtroPagos() {
    	return this.nomina.getOtrosPagos().stream()
    			.filter(d -> this.detalle || Boolean.TRUE.equals(d.getInformar()))
    			.collect(Collectors.toList());
    }
    
    public List<DetNominaDeduccion> filtrarDeducciones() {
    	return this.nomina.getDeducciones().stream()
                .filter(d -> this.detalle || Boolean.TRUE.equals(d.getInformar()))
                .collect(Collectors.toList());
    }
    
    public void nuevaDeduccion() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.deduccion = NominaBL.nuevaDeduccion(this.nomina);
		} catch(SGPException ex) {
			log.warn("Problema para crear la deducción: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear la deducción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
		}
    }
    
    public void eliminarDeduccion(DetNominaDeduccion deduccion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Deducción";
		
		try {
			NominaBL.eliminarDeduccion(this.nomina, deduccion);
			this.actualizar();
			
			mensaje = "Deducción eliminada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch(SGPException ex) {
			log.warn("Problema para eliminar la deducción: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Hay un problema para eliminar la deducción seleccionada.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
		}
    }
    
    /***************GETTERS Y SETTERS***************/
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Integer> getSemanasDelAnio() {
		return semanasDelAnio;
	}

	public void setSemanasDelAnio(List<Integer> semanasDelAnio) {
		this.semanasDelAnio = semanasDelAnio;
	}

	public DetNominaPeriodo getNominaPeriodo() {
		return nominaPeriodo;
	}

	public void setNominaPeriodo(DetNominaPeriodo nominaPeriodo) {
		this.nominaPeriodo = nominaPeriodo;
	}

	public CatEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}

	public List<CatEmpresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<CatEmpresa> empresas) {
		this.empresas = empresas;
	}

	public List<DetNomina> getListaNomina() {
		return listaNomina;
	}

	public void setListaNomina(List<DetNomina> listaNomina) {
		this.listaNomina = listaNomina;
	}

	public List<DetNomina> getListaNominaSelected() {
		return listaNominaSelected;
	}

	public void setListaNominaSelected(List<DetNomina> listaNominaSelected) {
		this.listaNominaSelected = listaNominaSelected;
	}

	public DetNominaPercepcion getNomPercepcion() {
		return nomPercepcion;
	}

	public void setNomPercepcion(DetNominaPercepcion percepcion) {
		this.nomPercepcion = percepcion;
	}
	
	public CatPercepcion getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(CatPercepcion percepcion) {
		this.percepcion = percepcion;
	}

	public List<CatPercepcion> getPercepciones() {
		return percepciones;
	}

	public void setPercepciones(List<CatPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
	}

	public Boolean getDetalle() {
		return detalle;
	}

	public void setDetalle(Boolean detalle) {
		this.detalle = detalle;
	}

	public Date getFechaPeriodo() {
		return fechaPeriodo;
	}

	public void setFechaPeriodo(Date fechaPeriodo) {
		this.fechaPeriodo = fechaPeriodo;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
}
