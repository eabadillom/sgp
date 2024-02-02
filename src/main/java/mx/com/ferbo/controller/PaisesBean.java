package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.PaisDAO;
import mx.com.ferbo.dto.PaisDTO;

@Named
@ViewScoped
public class PaisesBean implements Serializable {

	private static final long serialVersionUID = -7294511691759768677L;
	private static Logger log = LogManager.getLogger(PaisesBean.class);
	private PaisDAO paisDAO = null;
	
	public PaisesBean() {
	}
	
	@PostConstruct
	public void init() {
		paisDAO = new PaisDAO();
		listaPaises = paisDAO.buscarTodos();
		
	}
	
	private List<PaisDTO> listaPaises = null;

	public List<PaisDTO> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<PaisDTO> listaPaises) {
		this.listaPaises = listaPaises;
	}

}
