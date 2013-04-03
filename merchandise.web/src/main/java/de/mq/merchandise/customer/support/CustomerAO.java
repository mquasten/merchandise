package de.mq.merchandise.customer.support;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.model.support.HibernateProxyConverter;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class CustomerAO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Pattern(regexp="[1-9]{1,1}[0-9]{0,17}",  message="{positiv_number}")
	@Getter(clazz=CustomerImpl.class, value="id", converter=Number2StringConverter.class)
	public abstract String getId() ; 
	
	@Setter(clazz=CustomerImpl.class, value="id", converter=String2LongConverter.class)
	public abstract void  setId(String id) ; 
	
	@GetterProxy(clazz=CustomerImpl.class, proxyClass=PersonSelector.class, name = "person", converter=HibernateProxyConverter.class  )
	public abstract Object getPerson();
	
	@GetterDomain(clazz=CustomerImpl.class)
	public abstract Customer getCustomer();
	
	@SetterDomain(clazz=CustomerImpl.class)
	public abstract void setCustomer(final Customer customer);

}
