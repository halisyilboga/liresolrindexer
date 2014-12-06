package net.semanticmetadata.springweb.expert.prueba.dao;


import java.util.List;

import net.semanticmetadata.springweb.expert.prueba.model.Actividad;
import net.semanticmetadata.springweb.expert.prueba.util.ActividadDTO;

public interface IActividadDao {

	
	public void addActividad(Actividad actividad);
	
	
	public void updateActividad(Actividad actividad);
	

	public void deleteActividad(Actividad actividad);
	

	public Actividad getActividadById(Long id);
	
	public ActividadDTO getActividadDTOById(Long id);

	public List<ActividadDTO> getActividades();


	public List<ActividadDTO> getActividades(Long tarea, Long usuario);
}
