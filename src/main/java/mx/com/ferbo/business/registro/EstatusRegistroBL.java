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
		String aTiempo = "T";
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusATiempo = estatusDAO.buscarPorCodigo(aTiempo);
		return estatusATiempo;
	}

	public static CatEstatusRegistro estatusRetardo() {
		String retardo = "R";
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusRetardo = estatusDAO.buscarPorCodigo(retardo);
		return estatusRetardo;
	}

	public static CatEstatusRegistro estatusPermiso() {
		String permiso = "P";
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusPermiso = estatusDAO.buscarPorCodigo(permiso);
		return estatusPermiso;
	}

	public static CatEstatusRegistro estatusVacaciones() {
		String vacaciones = "V";
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusVacaciones = estatusDAO.buscarPorCodigo(vacaciones);
		return estatusVacaciones;
	}

	public static CatEstatusRegistro estatusIncapacidad() {
		String incapacidad = "I";
		EstatusRegistroDAO estatusDAO = new EstatusRegistroDAO();
		CatEstatusRegistro estatusIncapacidad = estatusDAO.buscarPorCodigo(incapacidad);
		return estatusIncapacidad;
	}

}
