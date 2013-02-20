package de.mq.merchandise.customer.support;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.customer.support.NativityImpl;


public abstract class NativityAO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Size(min=1,  message="{mandatory_field}")
    @Getter(clazz=NativityImpl.class, value = "birthPlace")
	public abstract String getBirthPlace(); 

    @Setter(clazz=NativityImpl.class, value = "birthPlace")
	public abstract void setBirthPlace(String birthPlace);

    @NotNull( message="{mandatory_field}")
    @Getter(clazz=NativityImpl.class, value = "birthDate")
	public abstract Date getBirthDate();

    @Setter(clazz=NativityImpl.class, value = "birthDate")
	public abstract void setBirthDate(Date birthDate); 
	
	
	
	
	

}
