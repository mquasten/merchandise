package de.mq.merchandise.opportunity.support;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class ActivityClassificationAO  {
	
	@Getter(clazz = ActivityClassificationImpl.class, value = "id")
	public abstract String getId();	
	
	@Getter(clazz = ActivityClassificationImpl.class, value = "description")
	public abstract String getDescription();

	@GetterProxy(clazz=ActivityClassificationImpl.class, name = "parent", proxyClass = ActivityClassificationAO.class, converter=HibernateProxyConverter.class)
	public abstract ActivityClassificationAO getParent();
	
	@GetterDomain(clazz=ActivityClassificationImpl.class)
	public abstract ActivityClassification getActivityClassification();

}
