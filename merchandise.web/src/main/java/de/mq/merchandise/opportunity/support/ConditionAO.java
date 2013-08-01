package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Enum2StringConverter;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class ConditionAO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	
	@Getter(clazz=ConditionImpl.class, value = "id", converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=ConditionImpl.class, value = "id", converter=String2LongConverter.class)
	public abstract void setId(final String id);
	
	@Getter(clazz=ConditionImpl.class, value = "conditionType", converter=Enum2StringConverter.class)
	public abstract String getConditionType();

	@Setter(clazz=ConditionImpl.class, value = "conditionType" , converter=String2ConditionType.class)
	public abstract void setConditionType(final String type);
	
	@Getter(clazz=ConditionImpl.class, value = "calculation")
	public abstract String getCalculation();

	@Setter(clazz=ConditionImpl.class, value = "calculation")
	public abstract void setCalculation(final String calculation);
	
	@Getter(clazz=ConditionImpl.class, value = "validation")
	public abstract String getValidation();

	@Setter(clazz=ConditionImpl.class, value = "validation")
	public abstract void setValidation(final String validation);
	
	
	@Getter(clazz=ConditionImpl.class, value = "values")
	public abstract String getValules();
	
	@SetterDomain(clazz=ConditionImpl.class)
	public abstract void setCondition(final Condition condition) ;
	
	@GetterDomain(clazz=ConditionImpl.class)
	public abstract Condition getCondition() ;


}
