package de.mq.merchandise.contact.support;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.support.PhoneContactImpl;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class PhoneContactAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter(clazz=PhoneContactImpl.class, value = "id" , converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=PhoneContactImpl.class, value = "id" , converter=String2LongConverter.class)
	public abstract void setId(final String id );
	
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=PhoneContactImpl.class, value = "internationalAreaCode" )
	public abstract String getInternationalAreaCode();
	
	
	@Setter(clazz=PhoneContactImpl.class, value = "internationalAreaCode" )
	public abstract void setInternationalAreaCode(String internationalAreaCode);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=PhoneContactImpl.class, value = "areaCode" )
	public abstract String getAreaCode();
	
	
	@Setter(clazz=PhoneContactImpl.class, value = "areaCode" )
	public abstract void setAreaCode(String areaCode);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=PhoneContactImpl.class, value = "account" )
	public abstract String getNumber();
	
	
	@Setter(clazz=PhoneContactImpl.class, value = "account" )
	public abstract void setNumber(String number);
	
	@Getter(clazz=PhoneContactImpl.class, value = "isLogin" )
	public abstract Boolean getLoginContact() ;
	
	@Setter(clazz=PhoneContactImpl.class, value = "isLogin" )
	public abstract void setLoginContact(Boolean login) ;
	
	@GetterDomain(clazz=PhoneContactImpl.class)
	public abstract Contact getContact();
	
	
	@SetterDomain(clazz=PhoneContactImpl.class)
	public abstract void setContact(Contact contact);

}