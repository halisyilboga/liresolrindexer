package com.expert.prueba.service;

import java.util.List;

import com.expert.prueba.model.Actividad;
import com.expert.prueba.util.ActividadDTO;

public interface IActividadService {

	/**
	 * Add Actividad
	 * 
	 * @param  Actividad actividad
	 */
	public void addActividad(Actividad actividad);
	
	/**
	 * Update Actividad
	 * 
	 * @param  Actividad actividad
	 */
	public void updateActividad(Actividad actividad);

	/**
	 * Delete Actividad
	 * 
	 * @param  Actividad actividad
	 */
	public void deleteActividad(Actividad actividad);
	
	/**
	 * Get Actividad
	 * 
	 * @param  int Actividad Id
	 */
	public Actividad getActividadById(Long idActividad);
	
	public ActividadDTO getActividadDTOById(Long idActividad);
	
	/**
	 * Get Actividad List
	 * 
	 * @return List - Actividad list
	 */
	public List<ActividadDTO> getActividades();

	public List<ActividadDTO> getActividades(Long tarea, Long usuario);
}
