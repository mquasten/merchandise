package de.mq.merchandise.opportunity.support;

import java.io.Serializable;
import java.util.Map;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.mapping.util.proxy.support.String2IntegerConverter;
import de.mq.merchandise.model.support.String2LongConverter;
import de.mq.merchandise.rule.support.RuleAO;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class RuleInstanceAO implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Getter(clazz = RuleInstanceImpl.class, value = "id", converter = Number2StringConverter.class)
	public abstract String getId();

	@Setter(clazz = RuleInstanceImpl.class, value = "id", converter =String2LongConverter.class)
	public abstract void setId(final String id);
	
	@GetterProxy(proxyClass=RuleAO.class,name="rule" , converter=HibernateProxyConverter.class, clazz=RuleInstanceImpl.class)
	public abstract RuleAO getRule();
	
	@GetterDomain(clazz= RuleInstanceImpl.class)
	public abstract RuleInstance getRuleInstance();
	
	@SetterDomain()
	public abstract  void setRuleInstance(final RuleInstance ruleInstance);
	
	@Getter(clazz = RuleInstanceImpl.class, value = "parameters")
	public abstract Map<String,String> getParameter();
	
	@Getter(clazz = RuleInstanceImpl.class, value = "priority", converter = Number2StringConverter.class)
	public abstract String  getPriority(); 
	
	@Setter(clazz= RuleInstanceImpl.class ,value="priority", converter=String2IntegerConverter.class)
	public abstract void  setPriority(final String priority); 

}
