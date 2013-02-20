package de.mq.merchandise.contact.support;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Enum2StringConverter;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.support.InstantMessengerContactImpl;
import de.mq.merchandise.model.support.String2InstantMessengerConverter;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class MessengerContactAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter(clazz=InstantMessengerContactImpl.class, value = "id" , converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=InstantMessengerContactImpl.class, value = "id" , converter=String2LongConverter.class)
	public abstract void setId(final String id );
	
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=InstantMessengerContactImpl.class, value = "instantMessenger" , converter=Enum2StringConverter.class)
	public abstract String getInstantMessenger();
	
	
	@Setter(clazz=InstantMessengerContactImpl.class, value = "instantMessenger", converter=String2InstantMessengerConverter.class )
	public abstract void setInstantMessenger(String number);
	
	
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=InstantMessengerContactImpl.class, value = "account" )
	public abstract String getAccount();
	
	
	@Setter(clazz=InstantMessengerContactImpl.class, value = "account" )
	public abstract void setAccount(String number);
	
	@Getter(clazz=InstantMessengerContactImpl.class, value = "isLogin" )
	public abstract Boolean getLoginContact() ;
	
	@Setter(clazz=InstantMessengerContactImpl.class, value = "isLogin" )
	public abstract void setLoginContact(Boolean login) ;
	
	@GetterDomain(clazz=InstantMessengerContactImpl.class)
	public abstract Contact getContact();
	
	
	@SetterDomain(clazz=InstantMessengerContactImpl.class)
	public abstract void setContact(Contact contact);

}