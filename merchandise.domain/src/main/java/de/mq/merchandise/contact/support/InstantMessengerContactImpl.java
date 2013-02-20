package de.mq.merchandise.contact.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.util.Equals;


@Entity(name="InstantMessengerContact")
class InstantMessengerContactImpl extends AbstractContact  {
	
	private static final long serialVersionUID = 1L;
	@Enumerated(EnumType.STRING)
	@Column(length=25, name="provider")
	@Equals
	private final InstantMessenger instantMessenger;
	
	@SuppressWarnings("unused")
	private InstantMessengerContactImpl() {
		this(null, null, false);
	}
	
	InstantMessengerContactImpl(final InstantMessenger instantMessenger, final String account, final boolean isLogin) {
		super(account,isLogin);
		this.instantMessenger=instantMessenger;
	}

	@Override
	public String contactInfo() {
		return instantMessenger.name() + ": " + account;
	}
	
	

}
