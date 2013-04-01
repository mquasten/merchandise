package de.mq.merchandise.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.EMailContactAO;
import de.mq.merchandise.contact.support.MessengerContactAO;
import de.mq.merchandise.contact.support.PhoneContactAO;
import de.mq.merchandise.contact.support.PostBoxAO;

@Component("contactModel")
@Scope("view")
public class ContactModelImpl implements Serializable {
	
	
	private static final long serialVersionUID = 1L;


	enum Kind {
		Address,
		PostBox,
		Phone,
		EMail,
		Messenger
	}
	
	
	private final Map<String,Object> addressMap = new HashMap<>();
	
	private String addressType= Kind.Address.name();
	
	private String contactType= Kind.Phone.name();
	

	@Autowired
	public ContactModelImpl(final AddressAO addressAO, final PostBoxAO cityAddressAO, final PhoneContactAO phoneContactAO, final EMailContactAO emailContactAO, final MessengerContactAO messengerContactAO) {	
		addressMap.put(Kind.Address.name(), addressAO);
		addressMap.put(Kind.PostBox.name(), cityAddressAO);
		addressMap.put(Kind.Phone.name(), phoneContactAO);
		addressMap.put(Kind.EMail.name(), emailContactAO);
		addressMap.put(Kind.Messenger.name(), messengerContactAO);
	}
	
	
	public final String getAddressType() {
		return addressType;
	}


	public void setAddressType(final String addressType) {
		this.addressType = addressType;
	}
	
	public String getContactType() {
		return contactType;
	}


	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	
	
	public final Object getAddress() {
		return addressMap.get(addressType);
	}

	
	public final Object getContact() {
		return addressMap.get(contactType);
	}
	

}
