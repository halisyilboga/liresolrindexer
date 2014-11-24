/**
 * 
 */
package com.expert.prueba.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.expert.prueba.model.Proyecto;

/**
 * @author EDGO
 *
 */

public class ProyectoDao implements IProyectoDao {

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
	
	public Proyecto getProyectoById(int id) {
		List list = getSessionFactory().getCurrentSession()
				.createQuery("from Proyecto where id=?")
		        .setParameter(0, id).list();
       return (Proyecto)list.get(0);
	}

	public List<Proyecto> getProyectos() {
		List list = getSessionFactory().getCurrentSession().createQuery("from Proyecto").list();
		return list;
	}

}
