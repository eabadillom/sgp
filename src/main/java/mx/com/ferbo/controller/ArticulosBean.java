package mx.com.ferbo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import mx.com.ferbo.dao.n.ArticuloDAO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.util.DataSourceManager;
import mx.com.ferbo.util.IOUtil;
import mx.com.ferbo.util.SGPException;

@Named(value = "articulosBean")
@ViewScoped
public class ArticulosBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ArticulosBean.class);

    private List<CatArticulo> articulos;
    private CatArticulo articulo;
    private ArticuloDAO articulodao;

    private UploadedFile imagen;
    private String direccion;

    private String accion;

    public ArticulosBean() {
        this.articulodao = new ArticuloDAO();
    }

    @PostConstruct
    public void init() {
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
            log.error("Problema en asignar el articulo...", ex);
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: no se cargaron los elementos", null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
        }
    }

    public void actualizar() {
        try {
            this.articulodao.actualizar(articulo);
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
        }
    }

    public void eliminar() {
        try {
            this.articulodao.eliminar(articulo);
            this.borrarImagen();
            this.listar(true);
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            PrimeFaces.current().ajax().update("frm:message");
            log.error(ex);
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

        String ruta = this.getDireccion() + File.separator + "articulos";
        File directorio = new File(ruta);
        File buscado = this.buscaImagen(directorio, nombreimagen);

        if (imagen != null) {

            if (directorio.exists() && directorio.isDirectory()) {

                if (buscado != null) {
                    buscado.delete();
                }

            } else {
                log.error("Problema al encontrar el directorio.");
                return;
            }

            try {
                log.info("Longitud del archivo: {}", this.imagen.getSize());
                contenidoimagen = this.imagen.getContent();
                contenidoimagen = IOUtil.read(this.imagen.getInputStream());
            } catch (IOException ex) {
                log.error("Hubo algun problema al momento de convertir la imagen a un arreglo de bytes", ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                PrimeFaces.current().ajax().update("frm:message");
            }

            ruta = this.getDireccion() + File.separator + "articulos" + File.separator + nombreimagen + ".jpg";

            try (FileOutputStream fos = new FileOutputStream(ruta)) {
                fos.write(contenidoimagen);
                fos.flush();
            } catch (IOException ex) {
                log.error("Hubo algun problema al momento de guardar la imagen en el servidor", ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                PrimeFaces.current().ajax().update("frm:message");
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
                log.error("Problema al guardar la imagen por defecto en el servidor", ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                PrimeFaces.current().ajax().update("frm:message");
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
                log.error("Problema al encontrar el directorio.");

            }
        } catch (Exception ex) {
            throw new IOException("Problema al borrar la imagen del articulo.");
        }
    }

}
