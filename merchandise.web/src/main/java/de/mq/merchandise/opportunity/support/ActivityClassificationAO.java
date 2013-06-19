package de.mq.merchandise.opportunity.support;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class ActivityClassificationAO {
	
	@Getter(clazz = ActivityClassificationImpl.class, value = "id")
	public abstract String getId();
	
	@Setter(clazz = ActivityClassificationImpl.class, value = "id")
	public abstract void setId(final String id);
	
	
	@Getter(clazz = ActivityClassificationImpl.class, value = "description")
	public abstract String getDescription();

	@Setter(clazz = ActivityClassificationImpl.class, value = "description")
	public abstract void setDescription(final String name);
	
	
	@GetterProxy(clazz=ActivityClassificationImpl.class, name = "parent", proxyClass = ActivityClassificationAO.class, converter=HibernateProxyConverter.class)
	public abstract ActivityClassificationAO getParent();
	
	@SetterDomain(clazz=ActivityClassificationImpl.class)
	public abstract void setActivityClassification(final ActivityClassification activityClassification);
	
	@GetterDomain(clazz=ActivityClassificationImpl.class)
	public abstract ActivityClassification getActivityClassification();

}
