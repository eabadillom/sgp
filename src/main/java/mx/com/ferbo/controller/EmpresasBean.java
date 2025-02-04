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
import mx.com.ferbo.business.certificado.CertificadoBL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.n.CertificadoDAO;
//import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;
//import mx.com.ferbo.dto.sat.RegimenFiscalDTO;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.RiesgoPuestoDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.util.DateUtil;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@Named(value = "empresasBean")
@ViewScoped
public class EmpresasBean implements Serializable {

    private static final long serialVersionUID = -239468326187620536L;
    private static Logger log = LogManager.getLogger(EmpresasBean.class);
    private String contextPath = null;
    private List<CatEmpresa> empresas;
    private CatEmpresa empresa;
    private EmpresaDAO empresaDAO;

    private List<CatRiesgoPuesto> riesgos;
    private RiesgoPuestoDAO riesgoPuestoDAO;

    private List<CatRegimenFiscal> regimenes;
    private RegimenFiscalDAO regimenDAO;

    private UploadedFile certificadoFile;
    private UploadedFile llavePrivadaFile;
    private String nombrellavePrivada;
    private String password;
    private boolean estado;

    private Certificado certificadoConFechaMax;
    private StreamedContent fileDownloadCer;
    private StreamedContent fileDownloadKey;
    private Certificado certificado;

    private RegimenFiscalDAO regimenFiscalDAO;
    private CertificadoDAO certificadoDAO;

    //private List<RegimenFiscal> listaRegimenFiscal;
    private List<Certificado> certificados;

    public EmpresasBean() {
        empresaDAO = new EmpresaDAO();
        regimenDAO = new RegimenFiscalDAO();
        riesgoPuestoDAO = new RiesgoPuestoDAO(CatRiesgoPuesto.class);
        certificadoDAO = new CertificadoDAO();
    }

    @PostConstruct
    public void init() {
        contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        empresas = empresaDAO.buscarActivo();
        regimenes = regimenDAO.buscarActivo(DateUtil.now());
        //regimenes = regimenDAO.buscarActivo(new Date());
        riesgos = riesgoPuestoDAO.buscarTodos();
        certificados = new ArrayList<Certificado>();
    }

    public void nuevo() {
        this.empresa = new CatEmpresa();
        this.empresa.setActivo(true);
        this.empresa.setStatusPadron("A");
        this.empresa.setTipoPersona("M");
        log.info("Nueva empresa");
    }

    public void editar() {
        log.info("Editando empresa: {}", this.empresa);
    }

    public void upload() {
        if (this.certificadoFile != null) {
            FacesMessage message = new FacesMessage("Successful", this.certificadoFile.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void guardar() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar empresa";

        try {
            if (this.empresa == null) {
                throw new SGPException("No hay información de la empresa.");
            }

            if ("M".equalsIgnoreCase(this.empresa.getTipoPersona()) && (this.empresa.getRegimenCapital() == null || "".equalsIgnoreCase(this.empresa.getRegimenCapital().trim()))) {
                throw new SGPException("Debe indicar un régimen capital.");
            }

            if (this.empresa.getIdEmpresa() == null) {
                empresaDAO.guardar(empresa);
            } else {
                empresaDAO.actualizar(empresa);
            }

            empresas = empresaDAO.buscarActivo();

            mensaje = "La empresa se guardó correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().executeScript("PF('dgEmpresa').hide()");
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            mensaje = "Existe un problema para guardar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa");
        }
    }

    public List<CatEmpresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<CatEmpresa> empresas) {
        this.empresas = empresas;
    }

    public CatEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CatEmpresa empresa) {
        this.empresa = empresa;
    }

    public List<CatRegimenFiscal> getRegimenes() {
        return regimenes;
    }

    public void setRegimenes(List<CatRegimenFiscal> regimenes) {
        this.regimenes = regimenes;
    }

    public List<CatRiesgoPuesto> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(List<CatRiesgoPuesto> riesgos) {
        this.riesgos = riesgos;
    }

    public UploadedFile getCertificadoFile() {
        return certificadoFile;
    }

    public void setCertificadoFile(UploadedFile certificadoFile) {
        this.certificadoFile = certificadoFile;
    }

    public UploadedFile getLlavePrivadaFile() {
        return llavePrivadaFile;
    }

    public void setLlavePrivadaFile(UploadedFile llavePrivadaFile) {
        this.llavePrivadaFile = llavePrivadaFile;
    }

    public StreamedContent getFileDownloadCer() {
        return fileDownloadCer;
    }

    public void setFileDownloadCer(StreamedContent fileDownloadCer) {
        this.fileDownloadCer = fileDownloadCer;
    }

    public StreamedContent getFileDownloadKey() {
        return fileDownloadKey;
    }

    public void setFileDownloadKey(StreamedContent fileDownloadKey) {
        this.fileDownloadKey = fileDownloadKey;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void guardarCertificado() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar certificado";
        
        try {
            CertificadoBL.guardarCertificado(this.certificados, this.certificado, this.certificadoDAO, this.password, this.empresa, this.empresaDAO, this.certificadoFile, this.certificadoFile);
        }catch (SGPException sgpEx) {
            mensaje = sgpEx.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa"); // Cabiar la actualizacón al dialogo donde se guarda el archivo
        }
    }
    
    public void cargaDeArchivos() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Cargar archivos";
        try{
            CertificadoBL.cargaDeArchivos(this.empresa, this.certificados, this.certificadoDAO, this.certificado, this.fileDownloadCer, this.fileDownloadKey);
        }
        catch(SGPException sgpEx){
            mensaje = sgpEx.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa");// Cabiar la actualizacón al dialogo donde se carga el archivo
        }
    }

}
