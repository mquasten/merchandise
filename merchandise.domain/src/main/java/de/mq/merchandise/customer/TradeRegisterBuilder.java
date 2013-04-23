package de.mq.merchandise.customer;



public interface TradeRegisterBuilder {

	TradeRegisterBuilder withZipCode(String zipCode);

	TradeRegisterBuilder withCity(String city);

	TradeRegisterBuilder withReference(String reference);

	TradeRegister build();
	

}