/**
 * 
 */
package com.expert.prueba.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.expert.prueba.dao.IActividadDao;
import com.expert.prueba.model.Actividad;
import com.expert.prueba.util.ActividadDTO;

/**
 * @author EDGO
 *
 */
@Transactional(readOnly = true)
public class ActividadService implements IActividadService {
	
	IActividadDao actividadDao;

	@Transactional(readOnly = false)
	public void addActividad(Actividad actividad) {
		getActividadDao().addActividad(actividad);
		
	}

	public IActividadDao getActividadDao() {
		return actividadDao;
	}

	public void setActividadDao(IActividadDao actividadDao) {
		this.actividadDao = actividadDao;
	}

	@Transactional(readOnly = false)
	public void updateActividad(Actividad actividad) {
		getActividadDao().updateActividad(actividad);
		
	}

	@Transactional(readOnly = false)
	public void deleteActividad(Actividad actividad) {
		getActividadDao().deleteActividad(actividad);
		
	}

	public Actividad getActividadById(Long id) {
		return getActividadDao().getActividadById(id);
	}
	
	public ActividadDTO getActividadDTOById(Long id) {
		return getActividadDao().getActividadDTOById(id);
	}

	public List<ActividadDTO> getActividades() {
		return getActividadDao().getActividades();
	}

	public List<ActividadDTO> getActividades(Long tarea, Long usuario) {
		return getActividadDao().getActividades(tarea, usuario);
	}

}
