package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.ArticuloDAO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import java.io.InputStream;
import javax.servlet.ServletContext;
import mx.com.ferbo.util.IOUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import mx.com.ferbo.util.DataSourceManager;
import static org.omnifaces.util.Faces.getServletContext;

@Named(value = "articulosBean")
@ViewScoped
public class ArticulosBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ArticulosBean.class);

    private List<CatArticulo> articulos;
    private CatArticulo articulo;
    private ArticuloDAO articulodao;

    private FacesContext fc;
    private PrimeFaces pf;
    private ServletContext sc;

    private UploadedFile imagen;
    private File sinimagen;
    private String direccion;

    private String accion;

    public ArticulosBean() {
        this.articulodao = new ArticuloDAO();
    }

    @PostConstruct
    public void init() {
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
        this.setDireccion(DataSourceManager.getJndiParameter("sgp/imagenes"));
    }

    public CatArticulo getArticulo() {
        return articulo;
    }

    public void setArticulo(CatArticulo articulo) {
        this.articulo = articulo;
    }

    public List<CatArticulo> getArticulos() {
        return articulos;
    }

    public String getAccion() {
        return accion;
    }

    private void setAccion(String accion) {
        this.accion = accion;
    }

    private String getDireccion() {
        return direccion;
    }

    private void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }

    public void nuevoArticulo() {
        this.articulo = new CatArticulo();
        this.setAccion("Registrar");
    }

    public void pasarArticulo(CatArticulo articulotmp) {
        try {
            this.articulo = articulotmp;
            this.setAccion("Modificar");
        } catch (Exception ex) {
            log.debug("Problema en asignar el articulo...", ex);
        }
    }

    public void listar(boolean bandera) {
        try {
            if (!bandera) {
                if (this.isPostBack() == false) {
                    this.articulos = this.articulodao.buscarTodos();
                }
            } else {
                this.articulos = this.articulodao.buscarTodos();
            }
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: no se cargaron los elementos", null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }

    private boolean isPostBack() {
        boolean respuesta = false;

        respuesta = FacesContext.getCurrentInstance().isPostback();

        return respuesta;

    }

    public void registrar() {
        try {
            this.articulodao.guardar(articulo);
        } catch (SGPException ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }

    public void actualizar() {
        try {
            this.articulodao.actualizar(articulo);
        } catch (SGPException ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }

    public void eliminar() {
        try {
            this.articulodao.eliminar(articulo);
            this.borrarImagen();
            this.listar(true);
        } catch (SGPException ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }

    public void operar() {
        switch (this.getAccion()) {
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

    public String disponibilidad(CatArticulo articulotmp) {

        return (articulotmp.getActivo() == 1) ? "Existencia" : "Sin Existencia";

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
        String nombreimagen = this.articulo.getDescripcion();

        String ruta = this.getDireccion() + "articulos/";
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

            ruta = this.getDireccion() + "articulos/" + nombreimagen + ".jpg";

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
            String newFileName = "articulos/" + this.articulo.getDescripcion() + ".jpg";

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
            String nombreimagen = this.articulo.getDescripcion();

            String ruta = this.getDireccion() + "articulos/";
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
