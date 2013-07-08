package de.mq.merchandise.opportunity.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("classificationConverter")
public class ClassificationConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (!(value instanceof Classification)) {
			return "???";
			
		}
		return ((Classification)value).description();
	}

}
