package mx.com.ferbo.controller;

import com.ferbo.facturama.business.CertificadosBL;
import com.ferbo.facturama.request.Csd;
import com.ferbo.facturama.response.CsdRsp;
import com.ferbo.facturama.tools.FacturamaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
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

import mx.com.ferbo.dao.n.CertificadoDAO;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.RiesgoPuestoDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.IOUtil;
import org.primefaces.model.DefaultStreamedContent;
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
            this.empresa.setRazonSocial(this.empresa.getDescripcion());
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

    public void regimenSelect() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Tipo de persona";

        try {
            log.info("Tipo de persona: {}", this.empresa.getTipoPersona());

            if ("M".equals(this.empresa.getTipoPersona())) {
                estado = false;
                regimenes = regimenFiscalDAO.buscarPorPersonaMoral();
            } else if ("F".equals(this.empresa.getTipoPersona())) {
                estado = true;
                regimenes = regimenFiscalDAO.buscarPorPersonaFisica();
            } else {
                throw new SGPException("Tipo de persona no válido");
            }

            mensaje = "Rellene el formulario";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para cargar los regímenes fiscales...", ex);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void guardarCertificado() {
        this.certificado = new Certificado();

        byte[] contenidoCertificado = null;
        byte[] contenidollavePrivada = null;
        String nombreLPrivada = llavePrivadaFile.getFileName();
        try {
            contenidoCertificado = IOUtil.read(certificadoFile.getInputStream());
            contenidollavePrivada = IOUtil.read(llavePrivadaFile.getInputStream());
            certificado.setNombreKey(nombreLPrivada);
            certificado.setKey(contenidollavePrivada);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String nombreCertificado = certificadoFile.getFileName();
        certificado.setCer(contenidoCertificado);
        certificado.setNombreCer(nombreCertificado);
        System.out.println("Guardando certificado");
        System.out.println(certificado);
        certificado.setFechaAlta(new Date());
        certificado.setPassword(password);
        certificado.setEmpresa(empresa);
        String certi = certificadoDAO.guardar(certificado, "ok");

        CertificadosBL facturamaBo = new CertificadosBL();

        String sCertificado = new String(Base64.getEncoder().encode(certificado.getCer()));
        String sLlavePrivada = new String(Base64.getEncoder().encode(certificado.getKey()));

        Csd csd = new Csd();
        csd.setRfc(this.empresa.getRfc());
        csd.setCertificate(sCertificado);
        csd.setPrivateKey(sLlavePrivada);
        csd.setPrivateKeyPassword(certificado.getPassword());

        try {
            List<CsdRsp> certificados = facturamaBo.get();
            log.info(certificados);
            for (CsdRsp csdR : certificados) {
                if (csdR.getRfc().equals(certificado.getEmpresa().getRfc())) {
                    facturamaBo.elimina(csdR.getRfc());
                }
            }
            facturamaBo.registra(csd);
            try {
                empresaDAO.actualizar(this.empresa);
            } catch (SGPException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
                PrimeFaces.current().ajax().update("form:messages");
            }
        } catch (IOException | FacturamaException e) {
            log.error("Problema para cargar el archivo CSD...", e);
        }

        if (certi == null) {
            this.certificados.clear();
            try {
                this.certificados = certificadoDAO.findAll();
            } catch (SGPException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
                PrimeFaces.current().ajax().update("form:messages");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Certificado agregado con exito" + certificado.getIdentificador(), null));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-emisor");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error, verificar datos" + certificado.getIdentificador(), certi));
            PrimeFaces.current().ajax().update("form:messages");
        }
        this.certificado = new Certificado();
        this.password = null;
    }

    public void cargaDeArchivos() {

        try {
            this.certificados = certificadoDAO.buscarPorEmpresa(this.empresa.getIdEmpresa());
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
            PrimeFaces.current().ajax().update("form:messages");
        }

        if (this.certificados.isEmpty()) {
            this.certificado = new Certificado();
        } else {

            Integer size = this.certificados.size() - 1;
            this.certificado = this.certificados.get(size);
            Date fechaAltaMax = this.certificado.getFechaAlta();

            System.out.println("Fecha maxima" + fechaAltaMax);
            InputStream inputCertificado = new ByteArrayInputStream(this.certificado.getCer());
            this.fileDownloadCer = DefaultStreamedContent.builder().name(this.certificado.getNombreCer())
                    .contentType("aplication/octet-stream").stream(() -> inputCertificado).build();

            InputStream inputLlavePrivada = new ByteArrayInputStream(this.certificado.getKey());
            this.fileDownloadKey = DefaultStreamedContent.builder().name(this.certificado.getNombreKey())
                    .contentType("aplication/octet-stream").stream(() -> inputLlavePrivada).build();
            log.info("Archivo certificado: {}", this.fileDownloadCer);
            log.info("Archivo key: {}", this.fileDownloadKey);
            log.info("Fin de cargar datos");
        }
    }
}
