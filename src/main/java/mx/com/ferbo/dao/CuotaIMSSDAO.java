package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.dto.CuotaIMSSDTO;
import mx.com.ferbo.model.CatCuotaIMSSPK;
import mx.com.ferbo.util.SGPException;

@Deprecated
public class CuotaIMSSDAO extends IBaseDAO<CuotaIMSSDTO, CatCuotaIMSSPK > {

	@Override
	public CuotaIMSSDTO buscarPorId(CatCuotaIMSSPK id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public CuotaIMSSDTO buscar(String tipoCuota, String clave, Date fechaInicio, Date fechaFin, BigDecimal cuota) {
		CuotaIMSSDTO tarifa = null;
		tarifa = emSGP.createNamedQuery("CatCuotaIMSS.findByClavePeriodoCuota", CuotaIMSSDTO.class)
				.setParameter("clave", clave)
				.setParameter("fechaInicio", fechaInicio)
				.setParameter("fechaFin", fechaFin)
				.setParameter("cuota", cuota)
				.setParameter("tipoCuota", tipoCuota)
				.getSingleResult()
				;
		return tarifa;
	}

	@Override
	public List<CuotaIMSSDTO> buscarTodos() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<CuotaIMSSDTO> buscarActivo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<CuotaIMSSDTO> buscarPorCriterios(CuotaIMSSDTO e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void actualizar(CuotaIMSSDTO e) throws SGPException {
		// TODO Auto-generated method stub
	}

	@Override
	public void eliminar(CuotaIMSSDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(CuotaIMSSDTO e) throws SGPException {
		// TODO Auto-generated method stub
		
	}

}
