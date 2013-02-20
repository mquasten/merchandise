package de.mq.merchandise.customer.support;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.customer.support.TradeRegisterImpl;


public abstract class TradeRegisterAO implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=TradeRegisterImpl.class , value="reference")
	public abstract String getReference(); 
	
	@Setter(clazz=TradeRegisterImpl.class , value="reference")
	public abstract void setReference(String reference);

	@Pattern(regexp="[0-9]{5,5}" , message="{plz_field}")
	@Getter(clazz=TradeRegisterImpl.class , value="zipCode")
	public abstract String getZipCode() ;

	@Setter(clazz=TradeRegisterImpl.class , value="zipCode")
	public abstract void setZipCode(String zipCode);

	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=TradeRegisterImpl.class , value="city")
	public abstract String getCity() ;

	@Setter(clazz=TradeRegisterImpl.class , value="city")
	public abstract void setCity(String city);

	@NotNull(message="{mandatory_field}")
	@Getter(clazz=TradeRegisterImpl.class , value="registrationDate")
	public abstract Date getRegistrationDate();

	@Setter(clazz=TradeRegisterImpl.class , value="registrationDate")
	public abstract  void setRegistrationDate(Date registrationDate); 
	
	

}
