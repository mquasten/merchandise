package de.mq.merchandise.rule.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.mq.merchandise.opportunity.support.RuleInstance;
@FacesConverter("ruleInstanceToParameterConverter")
public class RuleInstanceToParameterConverter  implements Converter{

	static final String UNDEFINED = "???";
	@Override
	public Object getAsObject(final FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, final Object value) {
		if ( !(value instanceof RuleInstance)) {
			
			return UNDEFINED;
		}
		
		final RuleInstance  ruleInstance = (RuleInstance) value; 
		final StringBuffer buffer = new StringBuffer();
		for(final String name  : ruleInstance.parameterNames()) {
			if( buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(name + "=" + ruleInstance.parameter(name));
		}
		
		return buffer.toString();
		
	}

}
