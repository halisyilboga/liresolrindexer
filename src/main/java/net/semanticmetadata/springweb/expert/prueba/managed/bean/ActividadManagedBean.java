package com.expert.prueba.managed.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.springframework.dao.DataAccessException;

import com.expert.prueba.model.Actividad;
import com.expert.prueba.model.Proyecto;
import com.expert.prueba.model.Tarea;
import com.expert.prueba.model.User;
import com.expert.prueba.service.IActividadService;
import com.expert.prueba.service.IProyectoService;
import com.expert.prueba.service.ITareaService;
import com.expert.prueba.service.IUserService;
import com.expert.prueba.util.ActividadDTO;

/**
 * 
 * Actividad Managed Bean
 * 
 * @author Edwin Gómez
 * @since 27/10/2012
 * @version 1.0.0
 *
 */
@ManagedBean(name="actividadMB")
@RequestScoped
public class ActividadManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4665852825150760246L;
	
	    @ManagedProperty(value="#{ProyectoService}")
	    IProyectoService proyectoService;
	    
		@ManagedProperty(value="#{TareaService}")
		ITareaService tareaService;
		
		@ManagedProperty(value="#{ActividadService}")
		IActividadService actividadService;
		
		@ManagedProperty(value="#{UserService}")
		IUserService usuarioService;
		
		List<ActividadDTO> actividades;
		List<SelectItem> proyectos;
		List<SelectItem> tareas;
		List<Tarea> listTareas;
		List<SelectItem> usuarios;
		
		@ManagedProperty("#{param.id}")
		Long paramActividad;
		
		
		Long idActividad;
	
		Long proyecto;
		private Long tarea;
		private Long usuario;
		private Date fecha;
		private Date horaInicio;
		private Date horaFin;
		Boolean disabled = true;
		
		public void guardar(){
			try{
			Actividad actividad = new Actividad();
			Tarea task = getTareaService().getTareaById(tarea);
		    User user = getUsuarioService().getUserById(usuario);
			actividad.setTarea(task);
			actividad.setUser(user);
			actividad.setFecha(fecha);
			actividad.setHoraInicio(horaInicio);
			actividad.setHoraFin(horaFin);
			actividad.setDescripcion(descripcion);
			actividad.setFechaRegistro(Calendar.getInstance().getTime());
			actividad.setFechaModifica(Calendar.getInstance().getTime());
			getActividadService().addActividad(actividad);
			getActividades();
			}catch (DataAccessException e) {
					e.printStackTrace();
				} 	
		}
		
		public void actualizar(){
			try{
			Actividad actividad = getActividadService().getActividadById(idActividad);
			Tarea task = getTareaService().getTareaById(tarea);
		    User user = getUsuarioService().getUserById(usuario);
			actividad.setTarea(task);
			actividad.setUser(user);
			actividad.setFecha(fecha);
			actividad.setHoraInicio(horaInicio);
			actividad.setHoraFin(horaFin);
			actividad.setDescripcion(descripcion);
			actividad.setFechaRegistro(Calendar.getInstance().getTime());
			actividad.setFechaModifica(Calendar.getInstance().getTime());
			getActividadService().updateActividad(actividad);
			getActividades();
			}catch (DataAccessException e) {
					e.printStackTrace();
				} 	
		}
		
		public void consultar(){
			if(tarea != null && usuario != null)
			actividades = getActividadService().getActividades(tarea, usuario);
		}
		
		/**
		 * Reset Fields
		 * 
		 */
		public void reset() {
			setProyecto(null);
			setTarea(null);
			setUsuario(null);
			setDescripcion("");
			setHoraInicio(null);
			setHoraFin(null);
			setFecha(null);
			
		}
		
		public Long getParamActividad() {
			return paramActividad;
		}

		public void setParamActividad(Long paramActividad) {
			this.paramActividad = paramActividad;
		}
		public Long getIdActividad() {
			return idActividad;
		}

		public void setIdActividad(Long idActividad) {
			this.idActividad = idActividad;
		}
		
		public IUserService getUsuarioService() {
			return usuarioService;
		}

		public void setUsuarioService(IUserService usuarioService) {
			this.usuarioService = usuarioService;
		}
		public IProyectoService getProyectoService() {
			return proyectoService;
		}

		public void setProyectoService(IProyectoService proyectoService) {
			this.proyectoService = proyectoService;
		}

		public List<SelectItem> getProyectos() {
			List<Proyecto> list = getProyectoService().getProyectos();
			proyectos = new ArrayList<SelectItem>();
			SelectItem seleccione = new SelectItem();
			seleccione.setLabel("--Seleccione un Proyecto--");
			seleccione.setValue("0");
			proyectos.add(0, seleccione);
			for (Proyecto proyecto : list) {
				SelectItem proyectoItem = new SelectItem();
				proyectoItem.setLabel(proyecto.getName());
				proyectoItem.setValue(proyecto.getId());
				proyectos.add(proyectoItem);
			}
			return proyectos;
		}

		
		public void setProyectos(List<SelectItem> proyectos) {
			this.proyectos = proyectos;
		}

		public List<SelectItem> getTareas() {
		
			listTareas = getTareaService().getTareas();
			tareas = new ArrayList<SelectItem>();
			SelectItem seleccione = new SelectItem();
			seleccione.setLabel("--Seleccione una Tarea--");
			seleccione.setValue("0");
			tareas.add(0, seleccione);
			for (Tarea tarea : listTareas) {
				SelectItem tareaItem = new SelectItem();
				tareaItem.setLabel(tarea.getName());
				tareaItem.setValue(tarea.getId());
				tareas.add(tareaItem);
			}
			
			
			return tareas;
		}
		public void getDetalle(){
		
		 ActividadDTO actividad = getActividadService().getActividadDTOById(paramActividad);
		 idActividad = actividad.getId();
		 proyecto = actividad.getProyectoId();
		 tarea = actividad.getTareaId();
		 fecha = actividad.getFecha();
		 horaInicio = actividad.getHoraInicio();
		 horaFin = actividad.getHoraFin();
		 usuario = actividad.getUsuarioId();
		 setDisabled(false);
		}

		public void setTareas(List<SelectItem> tareas) {
			this.tareas = tareas;
		}
		
		public List<SelectItem> getUsuarios() {
			List<User> list = getUsuarioService().getUsers();
			usuarios = new ArrayList<SelectItem>();
			SelectItem seleccione = new SelectItem();
			seleccione.setLabel("--Seleccione un Usuario--");
			seleccione.setValue("0");
			usuarios.add(0, seleccione);
			for (User user : list) {
				SelectItem userItem = new SelectItem();
				userItem.setLabel(user.getName());
				userItem.setValue(user.getId());
				usuarios.add(userItem);
			}
			return usuarios;
		}

		public void setUsuarios(List<SelectItem> usuarios) {
			this.usuarios = usuarios;
		}

		public Long getProyecto() {
			return proyecto;
		}

		public void setProyecto(Long proyecto) {
			this.proyecto = proyecto;
		}
		
		public ITareaService getTareaService() {
			return tareaService;
		}
		
		public void setTareaService(ITareaService tareaService) {
			this.tareaService = tareaService;
		}
		
		public IActividadService getActividadService() {
			return actividadService;
		}

		public void setActividadService(IActividadService actividadService) {
			this.actividadService = actividadService;
		}
		
		public List<ActividadDTO> getActividades() {
			actividades = getActividadService().getActividades();
			return actividades;
			
		}
		public void setActividades(List<ActividadDTO> actividades) {
			this.actividades = actividades;
		}
		public Long getTarea() {
			return tarea;
		}
		public void setTarea(Long tarea) {
			this.tarea = tarea;
		}
		public Long getUsuario() {
			return usuario;
		}
		public void setUsuario(Long usuario) {
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
		public Boolean getDisabled() {
			return disabled;
		}

		public void setDisabled(Boolean disabled) {
			this.disabled = disabled;
		}
		private String descripcion;


}
