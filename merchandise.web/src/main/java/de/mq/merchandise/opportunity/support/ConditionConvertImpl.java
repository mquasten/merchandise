package de.mq.merchandise.opportunity.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conditionConverter")
public class ConditionConvertImpl implements Converter{

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof CommercialSubject) {
			return ((CommercialSubject) value).name();
		}
		
		if (value instanceof Condition) {
			return ((Condition)value).conditionType().name();
			
		}
		
		return value.toString();
	}

}
