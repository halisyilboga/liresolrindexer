/**
 * 
 */
package com.expert.prueba.service;

import java.util.List;

import com.expert.prueba.model.Tarea;

/**
 * @author EDGO
 *
 */
public interface ITareaService {
	
	/**
	 * Get Tarea
	 * 
	 * @param  int Tarea Id
	 */
	public Tarea getTareaById(Long tarea);
	
	/**
	 * Get Tarea List
	 * 
	 * @return List - Tarea list
	 */
	public List<Tarea> getTareas();

	public List<Tarea> getTareasPorProyecto(Long proyecto);

	public Tarea getTareaByName(String submittedValue);

}
