package de.mq.merchandise.contact.support;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.support.AddressImpl;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class AddressAO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Getter(clazz=AddressImpl.class, value = "id" , converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=AddressImpl.class, value = "id" , converter=String2LongConverter.class)
	public abstract void setId(final String id );
	
	@Getter(clazz=AddressImpl.class, value = "city" )
	@Size(min=1,  message="{mandatory_field}")
	public abstract String getCity();
	
	@Setter(clazz=AddressImpl.class, value = "city")
	public abstract void setCity(final String city);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=AddressImpl.class, value = "zipCode" )
	public abstract String getZipCode();
	
	@Setter(clazz=AddressImpl.class, value = "zipCode" )
	public abstract void setZipCode(final String zipCode);
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=AddressImpl.class, value = "country" )
	public abstract String getCountry();
	
	@Setter(clazz=AddressImpl.class, value = "country" )
	public abstract void setCountry(final String country);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=AddressImpl.class, value = "street" )
	public abstract String getStreet();
	
	@Setter(clazz=AddressImpl.class, value = "street")
	public abstract void setStreet(final String street);

	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=AddressImpl.class, value = "houseNumber" )
	public abstract String getHouseNumber();
	
	@Setter(clazz=AddressImpl.class, value = "houseNumber")
	public abstract void setHouseNumber(final String houseNUmber);
	
	
	@GetterDomain(clazz=AddressImpl.class)
	public abstract Address getAddress();
	
	@SetterDomain(clazz=AddressImpl.class)
	public abstract void setAddress(final Address address); 
	
}
