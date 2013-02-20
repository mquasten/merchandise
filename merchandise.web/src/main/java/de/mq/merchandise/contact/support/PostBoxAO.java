package de.mq.merchandise.contact.support;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.support.PostBoxImpl;
import de.mq.merchandise.model.support.String2LongConverter;

public abstract class PostBoxAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter(clazz=PostBoxImpl.class, value = "id" , converter=Number2StringConverter.class)
	public abstract String getId();
	
	@Setter(clazz=PostBoxImpl.class, value = "id" , converter=String2LongConverter.class)
	public abstract void setId(final String id );
	
	@Getter(clazz=PostBoxImpl.class, value = "city" )
	@Size(min=1,  message="{mandatory_field}")
	public abstract String getCity();
	
	@Setter(clazz=PostBoxImpl.class, value = "city")
	public abstract void setCity(final String city);
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=PostBoxImpl.class, value = "zipCode" )
	public abstract String getZipCode();
	
	@Setter(clazz=PostBoxImpl.class, value = "zipCode" )
	public abstract void setZipCode(final String zipCode);
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=PostBoxImpl.class, value = "country" )
	public abstract String getCountry();
	
	@Setter(clazz=PostBoxImpl.class, value = "country" )
	public abstract void setCountry(final String country);
	
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=PostBoxImpl.class, value = "postBox" )
	public abstract String getPostBox();
	
	@Setter(clazz=PostBoxImpl.class, value = "postBox" )
	public abstract void setPostBox(final String postBox);
	
	
	
	@GetterDomain(clazz=PostBoxImpl.class)
	public abstract PostBox getAddress();
	
	@SetterDomain(clazz=PostBoxImpl.class)
	public abstract void setAddress(final PostBox address); 

}
