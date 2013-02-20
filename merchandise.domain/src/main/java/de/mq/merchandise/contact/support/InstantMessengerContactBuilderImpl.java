package de.mq.merchandise.contact.support;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.InstantMessengerContactBuilder;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.util.EntityUtil;

class InstantMessengerContactBuilderImpl implements InstantMessengerContactBuilder   {
	
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private InstantMessenger provider;
	
	private String account;
	
	
	
	private boolean login=false;
	
	@Override
	public final InstantMessengerContactBuilder withProvider(final InstantMessenger provider){
		this.provider=provider;
		return this;
	}
	
	
	@Override
	public final InstantMessengerContactBuilder withAccount(final String account){
		this.account=account;
		return this;
	}
	
	@Override
	public final InstantMessengerContactBuilder withLogin(){
		this.login=true;
		return this;
	}


	@Override
	public LoginContact build() {
		EntityUtil.mandatoryGuard(account, account);
		providerExistsGuard();
		return new InstantMessengerContactImpl(provider, account, login);
	}


	private void providerExistsGuard() {
		if( provider == null){
			throw new IllegalArgumentException("Provider is mandatory");
		}
	}


	


	

}
