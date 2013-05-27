package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import javax.validation.constraints.Size;


import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class CommercialSubjectAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter(clazz = CommercialSubjectImpl.class, value = "id", converter = Number2StringConverter.class)
	public abstract String getId();
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz = CommercialSubjectImpl.class, value = "name")
	public abstract String getName();

	@Setter(clazz = CommercialSubjectImpl.class, value = "name")
	public abstract void setName(final String name);

	@Getter(clazz = CommercialSubjectImpl.class, value = "description")
	public abstract String getDescription();

	@Setter(clazz = CommercialSubjectImpl.class, value = "description")
	public abstract void setDescription(final String description);
	
	@GetterDomain(clazz = CommercialSubjectImpl.class)
	public abstract CommercialSubject getCommercialSubject();
	
	@SetterDomain(clazz = CommercialSubjectImpl.class)
	public abstract void setCommercialSubject(final CommercialSubject commercialSubject);
	
	@GetterProxy(clazz=CommercialSubjectImpl.class, name = "customer", proxyClass = CustomerAO.class , converter=HibernateProxyConverter.class)
	public abstract CustomerAO getCustomer(); 
	

}
