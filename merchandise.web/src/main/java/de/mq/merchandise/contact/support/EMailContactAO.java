package de.mq.merchandise.contact.support;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.support.EMailContactImpl;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class EMailContactAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter(clazz=EMailContactImpl.class, value = "id" , converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=EMailContactImpl.class, value = "id" , converter=String2LongConverter.class)
	public abstract void setId(final String id );
	
	
	
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=EMailContactImpl.class, value = "account" )
	public abstract String getAccount();
	
	
	@Setter(clazz=EMailContactImpl.class, value = "account" )
	public abstract void setAccount(String number);
	
	@Getter(clazz=EMailContactImpl.class, value = "isLogin" )
	public abstract Boolean getLoginContact() ;
	
	@Setter(clazz=EMailContactImpl.class, value = "isLogin" )
	public abstract void setLoginContact(Boolean login) ;
	
	@GetterDomain(clazz=EMailContactImpl.class)
	public abstract Contact getContact();
	
	
	@SetterDomain(clazz=EMailContactImpl.class)
	public abstract void setContact(Contact contact);

}