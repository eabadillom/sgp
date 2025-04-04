package mx.com.ferbo.business.domicilio;

import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import mx.com.ferbo.model.CatEstado;
import mx.com.ferbo.model.CatEstadoPK;
import mx.com.ferbo.model.CatLocalidad;
import mx.com.ferbo.model.CatLocalidadPK;
import mx.com.ferbo.model.CatMunicipio;
import mx.com.ferbo.model.CatMunicipioPK;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.Pais;

public class DomicilioBL {
	
	public static DetDomicilioEmpleado build(DetEmpleado empleado) {
		DetDomicilioEmpleado domicilio = new DetDomicilioEmpleado();
		CatAsentamiento asentamiento = new CatAsentamiento();
        asentamiento.setKey(new CatAsentamientoPK());
        asentamiento.getKey().setLocalidad(new CatLocalidad());
        asentamiento.getKey().getLocalidad().setKey(new CatLocalidadPK());
        asentamiento.getKey().getLocalidad().getKey().setMunicipio(new CatMunicipio());
        asentamiento.getKey().getLocalidad().getKey().getMunicipio().setKey(new CatMunicipioPK());
        asentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().setEstado(new CatEstado());
        asentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().setKey(new CatEstadoPK());
        asentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().setPais(new Pais());
        
        domicilio.setAsentamiento(asentamiento);
        domicilio.setEmpleado(empleado);
        return domicilio;
	}

}
