package de.mq.merchandise.opportunity.support;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class ProductclassificationAO {
	
	
		
		@Getter(clazz = ProductClassificationImpl.class, value = "id")
		public abstract String getId();	
		
		@Getter(clazz = ProductClassificationImpl.class, value = "description")
		public abstract String getDescription();

		@GetterProxy(clazz=ProductClassificationImpl.class, name = "parent", proxyClass = ProductclassificationAO.class, converter=HibernateProxyConverter.class)
		public abstract ProductclassificationAO getParent();
		
		@GetterDomain(clazz=ProductClassificationImpl.class)
		public abstract ProcuctClassification getProductClassification();

	
	

}
