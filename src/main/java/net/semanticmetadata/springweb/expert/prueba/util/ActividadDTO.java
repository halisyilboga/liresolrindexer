package com.expert.prueba.util;

import java.io.Serializable;
import java.util.Date;

import com.expert.prueba.model.Actividad;

public class ActividadDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7744413358243116592L;
	
	private Long id;
	private String tarea;
	private Long tareaId;
	private String usuario;
	private String proyecto;
	private Long proyectoId;
	private Date fecha;
	private Date horaInicio;
	private Date horaFin;
	private String descripcion;
	private Long usuarioId;
	
	public ActividadDTO(Actividad actividad){
		this.id = actividad.getId();
		this.tarea = actividad.getTarea().getName();
		this.usuario = actividad.getUser().getName();
		this.fecha = actividad.getFecha();
		this.horaInicio = actividad.getHoraInicio();
		this.horaFin = actividad.getHoraFin();
		this.descripcion = actividad.getDescripcion();
		this.proyecto = actividad.getTarea().getProyecto().getName();
		this.proyectoId = actividad.getTarea().getProyecto().getId();
		this.tareaId = actividad.getTarea().getId();
		this.usuarioId = actividad.getUser().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Date horaFin) {
		this.horaFin = horaFin;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}
	public Long getTareaId() {
		return tareaId;
	}

	public void setTareaId(Long tareaId) {
		this.tareaId = tareaId;
	}

	public Long getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(Long proyectoId) {
		this.proyectoId = proyectoId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

}
