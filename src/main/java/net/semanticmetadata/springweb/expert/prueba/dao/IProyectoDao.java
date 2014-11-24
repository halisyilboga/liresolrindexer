/**
 * 
 */
package com.expert.prueba.dao;

import java.util.List;

import com.expert.prueba.model.Proyecto;

/**
 * @author EDGO
 *
 */
public interface IProyectoDao {
	
	public Proyecto getProyectoById(int id);
	

	public List<Proyecto> getProyectos();

}
