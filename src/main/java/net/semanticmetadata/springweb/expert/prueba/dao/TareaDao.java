package com.expert.prueba.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.expert.prueba.model.Tarea;


public class TareaDao implements ITareaDao{

	
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
	public Tarea getTareaById(Long id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Tarea where id=?")
		        .setParameter(0, id).list();
       return (Tarea)list.get(0);
	}

	public List<Tarea> getTareas() {
		List list = getSessionFactory().getCurrentSession().createQuery("from Tarea").list();
		return list;
	}

	public List<Tarea> getTareasPorProyecto(Long proyectoId) {
		List list = getSessionFactory().getCurrentSession().createQuery("from Tarea where proyecto.id = ?")
				.setLong(0, proyectoId).list();
		return list;
	}

	public Tarea getTareaByName(String name) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Tarea where name=?")
		        .setParameter(0, name).list();
       return (Tarea)list.get(0);
	}

}
