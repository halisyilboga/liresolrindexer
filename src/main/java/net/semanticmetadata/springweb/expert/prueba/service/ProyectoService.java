/**
 * 
 */
package net.semanticmetadata.springweb.expert.prueba.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.semanticmetadata.springweb.expert.prueba.dao.IProyectoDao;
import net.semanticmetadata.springweb.expert.prueba.model.Proyecto;

/**
 * @author EDGO
 *
 */
@Transactional(readOnly = false)
public class ProyectoService implements IProyectoService{

	
	IProyectoDao proyectoDao;
	
	public IProyectoDao getProyectoDao() {
		return proyectoDao;
	}

	public void setProyectoDao(IProyectoDao proyectoDao) {
		this.proyectoDao = proyectoDao;
	}

	public Proyecto getProyectoById(int id) {
		return getProyectoDao().getProyectoById(id);
	}

	public List<Proyecto> getProyectos() {
		return getProyectoDao().getProyectos();
	}

}
