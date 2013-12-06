package de.mq.merchandise.rule.support;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.model.support.String2LongConverter;
import de.mq.merchandise.rule.Rule;

public abstract class RuleAO implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Getter(clazz = RuleImpl.class, value = "id", converter = Number2StringConverter.class)
	public abstract String getId();
	
	@Getter(clazz = RuleImpl.class, value = "id")
	public abstract Long getIdAsLong();
	
	@Setter(clazz = RuleImpl.class, value = "id", converter =String2LongConverter.class)
	public abstract void setId(final String id);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz =RuleImpl.class, value = "name")
	public abstract String getName();

	@Setter(clazz = RuleImpl.class, value = "name")
	public abstract void setName(final String name);
	
	@Getter(clazz =RuleImpl.class, value = "state")
	public abstract State getState();
	
	@GetterDomain(clazz = RuleImpl.class)
	public abstract Rule getRule();
	
	@SetterDomain(clazz = RuleImpl.class)
	public abstract void setRule(final Rule rule);
	
	
	
	

}
