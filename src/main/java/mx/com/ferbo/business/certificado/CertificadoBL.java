package mx.com.ferbo.business.certificado;

import com.ferbo.facturama.business.CertificadosBL;
import com.ferbo.facturama.request.Csd;
import com.ferbo.facturama.response.CsdRsp;
import com.ferbo.facturama.tools.FacturamaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import mx.com.ferbo.dao.n.CertificadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.util.IOUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

public class CertificadoBL {

    private static final Logger log = LogManager.getLogger(CertificadoBL.class);

    public static void guardarCertificado(List<Certificado> certificados, Certificado certificado, CertificadoDAO certificadoDAO, String password, CatEmpresa empresa, EmpresaDAO empresaDAO, UploadedFile cerFile, UploadedFile keyFile) throws SGPException {
        certificado = new Certificado();
        
        try {
            log.info("Inicia el proceso para guardar el certificado");
            byte[] contenidoCertificado = IOUtil.read(cerFile.getInputStream());
            byte[] contenidollavePrivada = IOUtil.read(keyFile.getInputStream());
            String nombreLPrivada = keyFile.getFileName();
            certificado.setNombreKey(nombreLPrivada);
            certificado.setKey(contenidollavePrivada);

            String nombreCertificado = cerFile.getFileName();
            certificado.setCer(contenidoCertificado);
            certificado.setNombreCer(nombreCertificado);

            certificado.setFechaAlta(new Date());
            certificado.setPassword(password);
            certificado.setEmpresa(empresa);

            certificadoDAO.guardar(certificado);
            log.info("Finaliza el proceso para guardar el certificado");
            log.info("Inicia proceso para guardar archivo csd");
            CertificadosBL facturamaBo = new CertificadosBL();

            String sCertificado = new String(Base64.getEncoder().encode(certificado.getCer()));
            String sLlavePrivada = new String(Base64.getEncoder().encode(certificado.getKey()));

            Csd csd = new Csd();
            csd.setRfc(empresa.getRfc());
            csd.setCertificate(sCertificado);
            csd.setPrivateKey(sLlavePrivada);
            csd.setPrivateKeyPassword(certificado.getPassword());

            List<CsdRsp> csds = facturamaBo.get();
            log.info(certificados);
            for (CsdRsp csdR : csds) {
                if (csdR.getRfc().equals(certificado.getEmpresa().getRfc())) {
                    facturamaBo.elimina(csdR.getRfc());
                }
            }
            facturamaBo.registra(csd);
            log.info("Finaliza proceso para guardar archivo csd");
            log.info("Se actualiza la empresa");
            empresaDAO.actualizar(empresa);
            certificados.clear();
            log.info("Se obtienen los certificados");
            certificados = certificadoDAO.findAll();
        } catch (IOException | FacturamaException e) {
            log.error("Problema para cargar el archivo CSD. " + e.getMessage());
            throw new SGPException("No se pudo cargar el achivo CSD");
        }

        certificado = new Certificado();
        password = null;
    }

    public static void cargaDeArchivos(CatEmpresa empresa, List<Certificado> certificados, CertificadoDAO certificadoDAO, Certificado certificado, StreamedContent fileDownloadCer, StreamedContent fileDownloadKey) throws SGPException {

        certificados = certificadoDAO.buscarPorEmpresa(empresa.getIdEmpresa());

        if (certificados.isEmpty()) {
            certificado = new Certificado();
        } else {

            Integer size = certificados.size() - 1;
            certificado = certificados.get(size);
            Date fechaAltaMax = certificado.getFechaAlta();

            System.out.println("Fecha maxima" + fechaAltaMax);
            InputStream inputCertificado = new ByteArrayInputStream(certificado.getCer());
            fileDownloadCer = DefaultStreamedContent.builder().name(certificado.getNombreCer())
                    .contentType("aplication/x-x509-user-cert").stream(() -> inputCertificado).build();

            InputStream inputLlavePrivada = new ByteArrayInputStream(certificado.getKey());
            fileDownloadKey = DefaultStreamedContent.builder().name(certificado.getNombreKey())
                    .contentType("aplication/x-x509-user-cert").stream(() -> inputLlavePrivada).build();

        }
    }

}
