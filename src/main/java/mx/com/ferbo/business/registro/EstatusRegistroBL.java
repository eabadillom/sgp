package mx.com.ferbo.business.registro;

import java.io.Serializable;
import java.util.List;

import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.model.CatEstatusRegistro;

public class EstatusRegistroBL implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String A_TIEMPO = "T";
	public static final String RETARDO = "R";
	public static final String AUSENCIA = "F";
	public static final String JUSTIFICADO = "J";
	public static final String VACACIONES = "V";
	public static final String ASISTENCIA_EN_DIA_NO_LABORAL = "X";
	public static final String PERMISO = "P";
	public static final String DESCANSO = "D";
	public static final String INCAPACIDAD = "I";

	public static List<CatEstatusRegistro> buscarTodos() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		List<CatEstatusRegistro> listEstatusRegistro = estatusDAO.buscarTodos();
		return listEstatusRegistro;
	}

	public static CatEstatusRegistro estatusATiempo() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusATiempo = estatusDAO.buscarPorCodigo(A_TIEMPO);
		return estatusATiempo;
	}

	public static CatEstatusRegistro estatusRetardo() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusRetardo = estatusDAO.buscarPorCodigo(RETARDO);
		return estatusRetardo;
	}

	public static CatEstatusRegistro estatusPermiso() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusPermiso = estatusDAO.buscarPorCodigo(PERMISO);
		return estatusPermiso;
	}

	public static CatEstatusRegistro estatusVacaciones() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusVacaciones = estatusDAO.buscarPorCodigo(VACACIONES);
		return estatusVacaciones;
	}

	public static CatEstatusRegistro estatusIncapacidad() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusIncapacidad = estatusDAO.buscarPorCodigo(INCAPACIDAD);
		return estatusIncapacidad;
	}
	
	public static CatEstatusRegistro estatusDescanso() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusIncapacidad = estatusDAO.buscarPorCodigo(DESCANSO);
		return estatusIncapacidad;
	}
	
	public static CatEstatusRegistro estatusAusencia() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusIncapacidad = estatusDAO.buscarPorCodigo(AUSENCIA);
		return estatusIncapacidad;
	}
	
	public static CatEstatusRegistro estatusJustificado() {
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusIncapacidad = estatusDAO.buscarPorCodigo(JUSTIFICADO);
		return estatusIncapacidad;
	}
}
