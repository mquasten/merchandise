package de.mq.merchandise.customer;

import java.util.Date;


public interface TradeRegisterBuilder {

	TradeRegisterBuilder withZipCode(String zipCode);

	TradeRegisterBuilder withCity(String city);

	TradeRegisterBuilder withRegistrationDate(Date registrationDate);

	TradeRegisterBuilder withReference(String reference);

	TradeRegister build();
	

}