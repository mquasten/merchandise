package de.mq.merchandise.customer.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.merchandise.contact.support.AddressSelector;
import de.mq.merchandise.contact.support.CheckAddressWithCoordinatesAware;
import de.mq.merchandise.contact.support.CheckLoginAware;
import de.mq.merchandise.contact.support.ContactSelector;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public abstract class NaturalPersonAO implements Serializable  {


	private static final long serialVersionUID = 1L;
	

	
	@Size(min=1,  message="{mandatory_field}")
	@Getter( clazz=NaturalPersonImpl.class, value = "firstname")
	public abstract String getFirstName();

	@Setter( clazz=NaturalPersonImpl.class, value = "firstname")
	public abstract  void setFirstName(String firstName);

	@Size(min=1,  message="{mandatory_field}")
	@Getter( clazz=NaturalPersonImpl.class, value = "name")
	public abstract String getLastName();

	@Setter( clazz=NaturalPersonImpl.class, value = "name")
	public abstract void setLastName(String lastName);
	
	
	@GetterProxy(clazz=NaturalPersonImpl.class ,proxyClass=DigestAO.class, name="digest" )
	public abstract DigestAO getDigest();
	
	

	@GetterProxy(clazz=NaturalPersonImpl.class ,proxyClass=NativityAO.class, name="nativity")
	public abstract NativityAO getNativity();
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=NaturalPersonImpl.class, value="country")
	public abstract String getCountry();
	
	@Setter(clazz=NaturalPersonImpl.class, value="country")
	public abstract void setCountry(final String country);
	
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=NaturalPersonImpl.class, value="language")
	public abstract String getLanguage();
	
	
	@Setter(clazz=NaturalPersonImpl.class, value="language")
	public abstract void setLanguage(final String password);
	
	
	

	@GetterProxyCollection(clazz=NaturalPersonImpl.class,collectionClass=ArrayList.class, name="addresses" , proxyClass = AddressSelector.class, converter=HibernateProxyConverter.class )
	@CheckAddressWithCoordinatesAware(message="{missing_address_coordinates}")
	public abstract List<Object> getAddresses();
	
	
	@GetterProxyCollection(clazz=NaturalPersonImpl.class,collectionClass=ArrayList.class, name="contacts" , proxyClass = ContactSelector.class, converter=HibernateProxyConverter.class )
	@CheckLoginAware(message="{missing_login_contact}")
	public abstract List<Object> getContacts();
	
	
	@GetterDomain(clazz=NaturalPersonImpl.class)
	public abstract NaturalPerson getPerson(); 
	
	
	@SetterDomain(clazz=NaturalPersonImpl.class)
	public abstract void setPerson(final NaturalPerson naturalPerson); 

	
}
