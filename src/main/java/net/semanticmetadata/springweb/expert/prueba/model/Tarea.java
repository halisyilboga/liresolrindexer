package com.expert.prueba.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * Tarea Entity
 * 
 * @author Edwin Gómez
 * @since 26/10/2014
 * @version 1.0.0
 *
 */
@Entity
@Table(name="TAREA")
public class Tarea {

	
	private Long id;
	private String name;
	private Proyecto proyecto;
	private int estado;
	private Date fechaRegistro;
	private Date fechaModifica;
	
	@Id
	@Column(name="ID", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyecto_id", nullable = false)
	public Proyecto getProyecto() {
		return proyecto;
	}
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}
	
	@Column(name="FECHA_REGISTRO")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Column(name="FECHA_MODIFICA")
	public Date getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	
	 
	 @Override
	 public boolean equals(Object other) {
	     return (other instanceof Tarea) && (id != null)
	         ? id.equals(((Tarea) other).id)
	         : (other == this);
	 }

	 @Override
	 public int hashCode() {
	     return (id != null)
	         ? (this.getClass().hashCode() + id.hashCode())
	         : super.hashCode();
	 }
	
}
