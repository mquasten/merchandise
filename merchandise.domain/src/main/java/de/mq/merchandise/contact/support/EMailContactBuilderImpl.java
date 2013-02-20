package de.mq.merchandise.contact.support;

import de.mq.merchandise.contact.EMailContactBuilder;
import de.mq.merchandise.contact.LoginContact;

class EMailContactBuilderImpl implements EMailContactBuilder {
	
	private String account;
	
	private boolean login=false;
	
	
	@Override
	public final EMailContactBuilder withAccount(final String mailAddress){
		this.account=mailAddress;
		return this;
	}
	
	public final EMailContactBuilder withLogin() {
		this.login=true;
		return this;
	}
	
	
	@Override
	public final LoginContact build() {
		mailAddressExistsGuard();
		return new EMailContactImpl(account, login);
	}


	private void mailAddressExistsGuard() {
		if( account == null){
			throw new IllegalArgumentException("MailAddress is mandatory");
		}
	}

}
