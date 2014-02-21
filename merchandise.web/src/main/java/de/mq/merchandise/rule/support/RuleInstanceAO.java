package de.mq.merchandise.rule.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.mapping.util.proxy.support.String2IntegerConverter;
import de.mq.merchandise.controller.SimpleFacesExceptionTranslatorImpl;
import de.mq.merchandise.model.support.String2LongConverter;
import de.mq.merchandise.opportunity.support.RuleInstance;
import de.mq.merchandise.opportunity.support.RuleInstanceImpl;
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
	
	@GetterProxyCollection(clazz= RuleInstanceImpl.class , collectionClass=ArrayList.class, name = "parameters", proxyClass=ParameterAO.class, converter=Map2ParameterCollectionConverter.class)
	public abstract Collection<ParameterAO> getParameter();
	
	@Getter(clazz = RuleInstanceImpl.class, value = "priority", converter = Number2StringConverter.class)
	public abstract String  getPriority(); 
	
	@Setter(clazz= RuleInstanceImpl.class ,value="priority", converter=String2IntegerConverter.class)
	public abstract void  setPriority(final String priority); 
	
	
	@MethodInvocation(actions={@ActionEvent(name="assignSelected" , params={@Parameter(originIndex=0, clazz = Long.class ), @Parameter(clazz=RuleInstanceAO.class)})}, clazz = RuleControllerImpl.class, value=@ExceptionTranslation(  action = SimpleFacesExceptionTranslatorImpl.class, source = Exception.class  , bundle="rule_instance_invalid_rule"))
	public abstract void setSelectedId(final Long id);
	
	@MethodInvocation(actions={@ActionEvent(name="selectedId" , params={@Parameter(clazz = RuleInstanceAO.class, el="#arg.rule" , elResultType=RuleAO.class,skipNotReachableOnNullElException=true )} ) }, clazz = RuleControllerImpl.class )
	@NotNull(message="{mandatory_field}")
	public abstract Long getSelectedId();

}
