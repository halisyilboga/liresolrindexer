package net.semanticmetadata.springweb.expert.prueba.dao;


import java.util.ArrayList;
import java.util.List;

import net.semanticmetadata.springweb.expert.prueba.model.Actividad;
import net.semanticmetadata.springweb.expert.prueba.util.ActividadDTO;

import org.hibernate.SessionFactory;


public class ActividadDao implements IActividadDao{

	
	private SessionFactory sessionFactory;

	/**
	 * Get Hibernate Session Factory
	 * 
	 * @return SessionFactory - Hibernate Session Factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Set Hibernate Session Factory
	 * 
	 * @param SessionFactory - Hibernate Session Factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	public void addActividad(Actividad actividad) {
		getSessionFactory().getCurrentSession().save(actividad);
		
	}

	public void updateActividad(Actividad actividad) {
		getSessionFactory().getCurrentSession().update(actividad);
		
	}

	public void deleteActividad(Actividad actividad) {
		getSessionFactory().getCurrentSession().delete(actividad);
		
	}
    
	public ActividadDTO getActividadDTOById(Long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Actividad where id=?")
		        .setParameter(0, id).list();
		
       return new ActividadDTO((Actividad)list.get(0));
	}
	public Actividad getActividadById(Long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Actividad where id=?")
		        .setParameter(0, id).list();
		
       return (Actividad)list.get(0);
	}

	public List<ActividadDTO> getActividades() {
		List<Actividad> list = getSessionFactory().getCurrentSession().createQuery("from Actividad").list();
		List<ActividadDTO> result = new ArrayList<ActividadDTO>();
		for (Actividad actividad : list) {
			result.add(new ActividadDTO(actividad));
		}
		return result;
	}

	public List<ActividadDTO> getActividades(Long tarea, Long usuario) {
		List<Actividad> list = getSessionFactory().getCurrentSession().createQuery("from Actividad where tarea.id = ? and user.id = ?")
				.setParameter(0, tarea)
				.setParameter(1, usuario)
				.list();
		List<ActividadDTO> result = new ArrayList<ActividadDTO>();
		for (Actividad actividad : list) {
			result.add(new ActividadDTO(actividad));
		}
		return result;
	}

}
