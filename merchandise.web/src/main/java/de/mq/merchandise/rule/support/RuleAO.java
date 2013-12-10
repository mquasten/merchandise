package de.mq.merchandise.rule.support;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Size;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.controller.SimpleFacesExceptionTranslatorImpl;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.model.support.FacesContextFactory;
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
	
	@Setter( value = "parentState")
	public abstract void setParentState(final String state);
	
	@Getter(value = "parentState")
	public abstract String getParentState();
	
	
	@GetterDomain(clazz = RuleImpl.class)
	public abstract Rule getRule();
	
	@SetterDomain(clazz = RuleImpl.class)
	public abstract void setRule(final Rule rule);
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = RuleAO.class, proxy=true)  , @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['ruleId']" , elResultType=Long.class , converter=String2LongConverter.class), @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['state']" , elResultType=String.class ) })  , },clazz=RuleControllerImpl.class, value={ @ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class, bundle="rule_not_found"), @ExceptionTranslation(action = SimpleFacesExceptionTranslatorImpl.class, source = InvalidDataAccessApiUsageException.class, bundle="rule_not_found")})
	@PostConstruct()
	public abstract void initRuleAO();

	
	

}
