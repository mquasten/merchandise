package de.mq.merchandise.customer;

import java.util.Date;


public interface LegalPerson extends Person {

	String taxId();

	TradeRegister tradeRegister();

	Date foundationDate();
	
	LegalForm legalForm();

}