package de.mq.merchandise.rule.support;

import javax.validation.constraints.NotNull;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.util.ParameterImpl;

public abstract class ParameterAO {
	
	@Getter(clazz = ParameterImpl.class, value = "name")
	@NotNull(message="{mandatory_field}")
	public abstract String getName();
	
	@Setter(clazz = ParameterImpl.class, value = "name")
	public abstract void setName(final String name);
	
	@Getter(clazz = ParameterImpl.class, value = "value")
	@NotNull(message="{mandatory_field}")
	public abstract String getValue();
	
	@Setter(clazz = ParameterImpl.class, value = "value")
	public abstract void setValue(final String value);

}
