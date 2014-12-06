/**
 * 
 */
package net.semanticmetadata.springweb.expert.prueba.service;

import java.util.List;

import net.semanticmetadata.springweb.expert.prueba.model.Proyecto;

/**
 * @author EDGO
 *
 */
public interface IProyectoService {

	/**
	 * Get Proyecto
	 * 
	 * @param  int Proyecto Id
	 */
	public Proyecto getProyectoById(int id);
	
	/**
	 * Get Proyecto List
	 * 
	 * @return List - Proyecto list
	 */
	public List<Proyecto> getProyectos();
}
