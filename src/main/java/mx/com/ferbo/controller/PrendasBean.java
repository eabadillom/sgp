package mx.com.ferbo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.com.ferbo.dao.n.PrendaDAO;
import mx.com.ferbo.model.CatPrenda;
import mx.com.ferbo.util.DataSourceManager;
import mx.com.ferbo.util.IOUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

@Named(value = "prendasBean")
@ViewScoped
public class PrendasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(PrendasBean.class);

    private CatPrenda prenda;
    private PrendaDAO prendadao;
    private List<CatPrenda> prendas;
    
    private FacesContext fc;
    private PrimeFaces pf;
    private ServletContext sc;
    
    private UploadedFile imagen;
    private String direccion;

    private String accion;

    public PrendasBean() {
        this.prendadao = new PrendaDAO();
    }
    
    @PostConstruct
    public void init(){
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
        this.setDireccion(DataSourceManager.getJndiParameter("sgp/imagenes"));
    }

    public CatPrenda getPrenda() {
        return prenda;
    }

    public void setPrenda(CatPrenda prenda) {
        this.prenda = prenda;
    }

    public List<CatPrenda> getPrendas() {
        return prendas;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public void nuevaPrenda(){
        this.prenda = new CatPrenda();
        this.setAccion("Registrar");
    }
    
    public void pasarPrenda(CatPrenda prendatmp){
        try{
            this.prenda = prendatmp;
            this.setAccion("Modificar");
        }
        catch(Exception ex){
            log.debug("Problema en asignar la prenda...", ex);
        }
    }

    private boolean isPostBack(){
        boolean respuesta = false;
        
        respuesta = fc.isPostback();
        
        return respuesta;
    }
    
    public void listar(boolean bandera){
        try{
            if(!bandera){
                if(this.isPostBack() == false){
                    this.prendas = this.prendadao.buscarTodos();
                }
            }
            else{
                this.prendas = this.prendadao.buscarTodos();
            }
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: no se cargaron los elementos", null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void registrar(){
        try{
            this.prendadao.guardar(prenda);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void actualizar(){
        try{
            this.prendadao.actualizar(prenda);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void eliminar(){
        try{
            this.prendadao.eliminar(prenda);
            this.borrarImagen();
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void operar() {
        switch (accion) {
            case "Registrar":
                this.registrar();
                this.guardarImagen();
                break;
            case "Modificar":
                this.actualizar();
                this.guardarImagen();
                break;
        }
    }
    
    public String disponibilidad(CatPrenda prendatmp) {

        return (prendatmp.getActivo() == 1) ? "Existencia" : "Sin Existencia";

    }
    
     private File buscaImagen(File directorio, String imagen) {

        String imegencompleta = imagen + ".jpg";
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    File buscado = buscaImagen(archivo, imegencompleta);
                    if (buscado != null) {
                        return buscado;
                    }
                } else if (archivo.getName().equalsIgnoreCase(imegencompleta)) {
                    return archivo;
                }
            }
        }
        return null;
    }

    public void guardarImagen() {
        log.info("Entrando a guardarImagen...");

        byte[] contenidoimagen = null;
        String nombreimagen = this.prenda.getDescripcion();

        String ruta = this.getDireccion() + "prendas/";
        File directorio = new File(ruta);
        File buscado = this.buscaImagen(directorio, nombreimagen);

        if (imagen != null) {

            if (directorio.exists() && directorio.isDirectory()) {

                if (buscado != null) {
                    buscado.delete();
                }

            } else {
                log.debug("Problema al encontrar el directorio.");
                return;
            }

            log.info("Direccion: {}", ruta);

            try {
                contenidoimagen = this.imagen.getContent();
                log.info("Longitud del archivo: {}", this.imagen.getSize());
                contenidoimagen = IOUtil.read(this.imagen.getInputStream());
            } catch (IOException ex) {
                log.debug("Hubo algun problema al momento de convertir la imagen a un arreglo de bytes", ex);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: problema al momento de guardar la imagen", null));
                pf.ajax().update("message");
            }

            ruta = this.getDireccion() + "prendas/" + nombreimagen + ".jpg";

            try (FileOutputStream fos = new FileOutputStream(ruta)) {
                fos.write(contenidoimagen);
                fos.flush();
            } catch (IOException ex) {
                log.debug("Hubo algun problema al momento de guardar la imagen en el servidor", ex);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: problema al momento de guardar la imagen", null));
                pf.ajax().update("message");
            }
        } else if (imagen == null && buscado == null) {

            String sourcePath = this.getDireccion() + "sinimagen.jpg";
            String newFileName = "prendas/" + this.prenda.getDescripcion() + ".jpg";

            File sourceFile = new File(sourcePath);
            String directory = sourceFile.getParent();
            String destinationPath = directory + File.separator + newFileName;

            try {
                Path source = Paths.get(sourcePath);
                Path destination = Paths.get(destinationPath);
                Files.copy(source, destination);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ArticulosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.listar(true);
    }

    private void borrarImagen() throws IOException {
        try {
            String nombreimagen = this.prenda.getDescripcion();

            String ruta = this.getDireccion() + "prendas/";
            File directorio = new File(ruta);
            File buscado = this.buscaImagen(directorio, nombreimagen);

            if (directorio.exists() && directorio.isDirectory()) {

                if (buscado != null) {
                    buscado.delete();
                }

            } else {
                log.debug("Problema al encontrar el directorio.");

            }
        } catch (Exception ex) {
            throw new IOException("Problema al borrar la imagen del articulo.");
        }
    }

}
