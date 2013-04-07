package de.mq.merchandise.customer.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.model.support.HibernateProxyConverter;


public abstract  class LoginAO  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Getter( value="user")
	@Size(min=1,  message="{mandatory_field}")
	public abstract  String getUser();

	@Setter(value="user")
	public abstract void setUser(String user);

	
	@Getter( value="password")
	@Size(min=4, max=8 , message="{wrong_password}")
	public abstract String getPassword(); 

	@Setter( value="password")
	public abstract void setPassword(String password); 
	
	@Setter(value="customers")
	public abstract void setCustomers(final List<Customer> custmers);
	
	
	@GetterProxyCollection(collectionClass=ArrayList.class, name="customers" , proxyClass = CustomerAO.class, converter=HibernateProxyConverter.class )
	public abstract List<CustomerAO> getCustomers(); 
	
	@GetterProxy( proxyClass=PersonSelector.class, name = "person", converter=HibernateProxyConverter.class  )
	public abstract Object getPerson(); 
	@Setter( value="person")
	public abstract void setPerson(Person person);
	
	
	@GetterProxy( proxyClass=CustomerAO.class, name = "customer", converter=HibernateProxyConverter.class  )
	public abstract CustomerAO getCustomer(); 
	@Setter( value="customer")
	public abstract void setCustomer(final Customer customer);
	
	@Setter( value="customer" , converter=CustomerAO2DomainConverter.class)
	public abstract void setCustomer(final CustomerAO customer); 
	
	
	
	

}
