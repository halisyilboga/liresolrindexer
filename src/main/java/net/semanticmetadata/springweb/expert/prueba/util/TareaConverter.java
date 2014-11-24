package com.expert.prueba.util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.expert.prueba.model.Tarea;
import com.expert.prueba.service.ITareaService;

@FacesConverter(value="tareaConverter", forClass=Tarea.class)
public class TareaConverter implements Converter{
	
	@ManagedProperty(value="#{TareaService}")
	ITareaService tareaService;

	 public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
	        if (submittedValue == null || submittedValue.isEmpty()) {
	            return null;
	        }

	        try {
	            return getTareaService().getTareaByName(submittedValue);
	        } catch (NumberFormatException exception) {
	            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player ID"));
	        }
	    }

	    public ITareaService getTareaService() {
		return tareaService;
	}

	public void setTareaService(ITareaService tareaService) {
		this.tareaService = tareaService;
	}

		public String getAsString(FacesContext context, UIComponent component, Object value) {
			if (value != null && value instanceof Tarea) {  
	            return ((Tarea)value).getId().toString();  
	        }  
	          
	        return null;  
	    }

}
