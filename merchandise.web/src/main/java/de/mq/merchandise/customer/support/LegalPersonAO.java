package de.mq.merchandise.customer.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Enum2StringConverter;
import de.mq.merchandise.contact.support.AddressSelector;
import de.mq.merchandise.contact.support.ContactSelector;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.model.support.HibernateProxyConverter;
import de.mq.merchandise.model.support.String2LegalFormConverter;


public  abstract class   LegalPersonAO   implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@GetterProxy(clazz=LegalPersonImpl.class, name = "tradeRegister", proxyClass = TradeRegisterAO.class)
	public abstract TradeRegisterAO getTradeRegister(); 
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=LegalPersonImpl.class, value="foundationDate")
	public abstract Date getFoundationDate(); 

	@Setter(clazz=LegalPersonImpl.class, value="foundationDate")
	public abstract void setFoundationDate(final Date foundationDate);

	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=LegalPersonImpl.class, value="taxId")
	public abstract String getTaxId();
	
	
	@Setter(clazz=LegalPersonImpl.class, value="taxId")
	public abstract void setTaxId(final String taxId); 
	
	@Size(min=1,  message="{mandatory_field}")
	@Getter(clazz=LegalPersonImpl.class, value="name")
	public abstract String getName();
	
	@Setter(clazz=LegalPersonImpl.class, value="name")
	public  abstract void setName(final String name);

	@NotNull( message="{mandatory_field}")
	@Getter(clazz=LegalPersonImpl.class, value="legalForm", converter=Enum2StringConverter.class)
	public abstract  String getLegalForm();

	@Setter(clazz=LegalPersonImpl.class, value="legalForm" , converter=String2LegalFormConverter.class)
	public abstract void setLegalForm(String legalForm);

	@Size(min=4, max=8 , message="{wrong_password}")
	@Getter(clazz=LegalPersonImpl.class, value="password")
	public abstract String getPassword(); 

	@Setter(clazz=LegalPersonImpl.class, value="password")
	public abstract  void setPassword(String password);
	
	
	@Size(min=4, max=8 , message="{wrong_password}")
	@Getter(value="confirmedPassword")
	public abstract String getConfirmedPassword();
	
	@Setter(value="confirmedPassword")
	public abstract void setConfirmedPassword(final String password);
	
	@NotNull( message="{mandatory_field}")
	@Getter(clazz=LegalPersonImpl.class, value="country")
	public abstract String getCountry();
	
	@Setter(clazz=LegalPersonImpl.class, value="country")
	public abstract void setCountry(final String country);
	
	
	@Getter(clazz=LegalPersonImpl.class, value="language")
	public abstract String getLanguage();
	
	@Setter(clazz=LegalPersonImpl.class, value="language")
	public abstract void setLanguage(final String languague);
	

	@GetterDomain(clazz=LegalPersonImpl.class )
	public abstract LegalPerson getPerson();
	
	@SetterDomain(clazz=LegalPersonImpl.class)
	public abstract void setPerson(final LegalPerson domain);
	
	@GetterProxyCollection(clazz=LegalPersonImpl.class,collectionClass=ArrayList.class, name="addresses" , proxyClass = AddressSelector.class, converter=HibernateProxyConverter.class )
	public abstract List<Object> getAddresses();
	
	@GetterProxyCollection(clazz=LegalPersonImpl.class,collectionClass=ArrayList.class, name="contacts" , proxyClass = ContactSelector.class, converter=HibernateProxyConverter.class )
	public abstract List<Object> getContacts();

}
