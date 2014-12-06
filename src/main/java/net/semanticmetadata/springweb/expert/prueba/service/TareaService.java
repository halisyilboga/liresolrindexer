/**
 * 
 */
package net.semanticmetadata.springweb.expert.prueba.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.semanticmetadata.springweb.expert.prueba.dao.ITareaDao;
import net.semanticmetadata.springweb.expert.prueba.model.Tarea;

/**
 * @author EDGO
 *
 */
@Transactional(readOnly= true)
public class TareaService implements ITareaService{
	
	
	ITareaDao tareaDao;

	public ITareaDao getTareaDao() {
		return tareaDao;
	}

	public void setTareaDao(ITareaDao tareaDao) {
		this.tareaDao = tareaDao;
	}

	public Tarea getTareaById(Long id) {
		
		return getTareaDao().getTareaById(id);
	}

	public List<Tarea> getTareas() {
		return getTareaDao().getTareas();
	}

	public List<Tarea> getTareasPorProyecto(Long proyectoId) {
		return getTareaDao().getTareasPorProyecto(proyectoId);
	}

	public Tarea getTareaByName(String submittedValue) {
		
		return getTareaDao().getTareaByName(submittedValue);
	}

}
