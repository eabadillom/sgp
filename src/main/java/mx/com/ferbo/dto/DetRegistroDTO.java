package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gabo
 */
public class DetRegistroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idRegistro;
    private DetEmpleadoDTO detEmpleadoDTO;
    private Date fechaEntrada;
    private Date fechaSalida;
    private CatEstatusRegistroDTO catEstatusRegistroDTO;

    public DetRegistroDTO() {
    }

    public DetRegistroDTO(Integer idRegistro, Date fechaEntrada, Date fechaSalida, Integer idEstatus, String descripcionEstatus) {
        this.idRegistro = idRegistro;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.catEstatusRegistroDTO = new CatEstatusRegistroDTO(idEstatus, descripcionEstatus);
    }

    public DetRegistroDTO(Integer idRegistro, Integer idEmpleado, Date fechaEntrada, Date fechaSalida, Integer idEstatus, String descripcionEstatus) {
        this.idRegistro = idRegistro;
        this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado);
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.catEstatusRegistroDTO = new CatEstatusRegistroDTO(idEstatus, descripcionEstatus);
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public DetEmpleadoDTO getDetEmpleadoDTO() {
        return detEmpleadoDTO;
    }

    public void setDetEmpleadoDTO(DetEmpleadoDTO detEmpleadoDTO) {
        this.detEmpleadoDTO = detEmpleadoDTO;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public CatEstatusRegistroDTO getCatEstatusRegistroDTO() {
        return catEstatusRegistroDTO;
    }

    public void setCatEstatusRegistroDTO(CatEstatusRegistroDTO catEstatusRegistroDTO) {
        this.catEstatusRegistroDTO = catEstatusRegistroDTO;
    }

}
