package de.mq.merchandise.contact.support;

import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.PhoneContactBuilder;
import de.mq.merchandise.util.EntityUtil;

class PhoneContactBuilderImpl implements PhoneContactBuilder {
	
	private String countryCode=PhoneContactImpl.AREA_CODE_DE;
	
	
	private String areaCode="";
	
	private String subscriberNumber;
	
	private boolean login=false;
	
	
	@Override
	public final PhoneContactBuilder withCountryCode(final String countryCode) {
		this.countryCode=countryCode;
		return this;
	}
	
	public final PhoneContactBuilder withLogin() {
		this.login=true;
		return this;
	}
	
	
	@Override
	public final PhoneContactBuilder withAreaCode(final String areaCode){
		this.areaCode=areaCode;
		return this;
	}
	
	
	@Override
	public final PhoneContactBuilder withSubscriberNumber(final String subscriberNumber) {
		this.subscriberNumber=subscriberNumber;
		return this;
	}


	@Override
	public LoginContact build() {
		EntityUtil.mandatoryGuard(countryCode, "countryCode");
		EntityUtil.mandatoryGuard(subscriberNumber, "subscriberNumber");
		return new PhoneContactImpl(countryCode,  areaCode, subscriberNumber, login);
	}


	

}
