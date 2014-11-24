/**
 * 
 */
package com.expert.prueba.dao;

import java.util.List;

import com.expert.prueba.model.Tarea;

/**
 * @author EDGO
 *
 */
public interface ITareaDao {

public Tarea getTareaById(Long id);
	

	public List<Tarea> getTareas();


	public List<Tarea> getTareasPorProyecto(Long proyectoId);


	public Tarea getTareaByName(String submittedValue);
}
