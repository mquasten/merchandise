package de.mq.merchandise.customer.support;

import java.util.Date;

import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.TradeRegisterBuilder;
import de.mq.merchandise.util.EntityUtil;



class TradeRegisterBuilderImpl implements TradeRegisterBuilder {
	
	
	private  String zipCode;
	
	
	private  String city;
	
	
	private  Date registrationDate;
	

	private  String reference;
	
	
	
	
	@Override
	public final TradeRegisterBuilder withZipCode(final String zipCode) {
		this.zipCode=zipCode;
		return this;
	}
	
	
	@Override
	public final TradeRegisterBuilder withCity(final String city) {
		this.city=city;
		return this;
	}
	
	
	@Override
	public final TradeRegisterBuilder withRegistrationDate(final Date registrationDate) {
		this.registrationDate=registrationDate;
		return this;
	}
	
	
	@Override
	public final TradeRegisterBuilder withReference(final String reference) {
		this.reference=reference;
		return this;
	}
	
	
	@Override
	public final TradeRegister build() {
		EntityUtil.mandatoryGuard(zipCode, "zipCode");
		EntityUtil.mandatoryGuard(city, "city");
		EntityUtil.mandatoryGuard(reference, "reference");
		return new TradeRegisterImpl(zipCode, city, reference);
	}
	

}
