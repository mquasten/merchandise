package de.mq.merchandise.opportunity.support;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conditionConverter")
public class ConditionConverterImpl implements Converter{

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof CommercialRelation) {
			hibernateInitialize(((CommercialRelation) value).ruleInstances());
			return ((CommercialRelation) value).commercialSubject().name();
		}
		
		if (value instanceof Condition) {
			hibernateInitialize( ((Condition)value).ruleInstances());
			return ((Condition)value).conditionType().name();
			
		}
		
		return value.toString();
	}
	
	private void hibernateInitialize(final List<RuleInstance> list){
		list.size();
	}

}
