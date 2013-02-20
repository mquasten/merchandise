package de.mq.merchandise.contact.support;

import javax.persistence.Entity;

import de.mq.merchandise.contact.LoginContact;




@Entity(name="EMailContact")
class EMailContactImpl extends AbstractContact implements LoginContact {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private EMailContactImpl() {
		
	}
	
	EMailContactImpl(final String mailAddress, boolean isLogin){
		super(mailAddress,isLogin);
	}
	

	@Override
	public String toString() {
		if( account == null) {
			return super.toString();
		}
		return "account=" + account;
	}

	@Override
	protected String contactInfo() {
		
		return account;
	}

	
	

}
