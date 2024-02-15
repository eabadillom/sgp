package mx.com.ferbo.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.DetBiometricoDAO;

/**
 *
 * @author Gabriel
 */
public class DetBiometricoDTO implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private Integer idBiometrico;
    private Date fechaCaptura;
    private short activo;
    private String huella;
    private String huella2;
    private DetEmpleadoDTO detEmpleadoDTO;

    public DetBiometricoDTO() {
    }

    public DetBiometricoDTO(Integer idBiometrico, Date fechaCaptura, short activo, String huella, String huella2, Integer idEmpleado) {
        this.idBiometrico = idBiometrico;
        this.fechaCaptura = fechaCaptura;
        this.activo = activo;
        this.huella = huella;
        this.huella2 = huella2;
        this.detEmpleadoDTO = new DetEmpleadoDTO(idEmpleado);
    }

    public Integer getIdBiometrico() {
        return idBiometrico;
    }

    public void setIdBiometrico(Integer idBiometrico) {
        this.idBiometrico = idBiometrico;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getHuella() {
        return huella;
    }

    public void setHuella(String huella) {
        this.huella = huella;
    }

    public String getHuella2() {
        return huella2;
    }

    public void setHuella2(String huella2) {
        this.huella2 = huella2;
    }

    public DetEmpleadoDTO getDetEmpleadoDTO() {
        return detEmpleadoDTO;
    }

    public void setDetEmpleadoDTO(DetEmpleadoDTO detEmpleadoDTO) {
        this.detEmpleadoDTO = detEmpleadoDTO;
    }

}
