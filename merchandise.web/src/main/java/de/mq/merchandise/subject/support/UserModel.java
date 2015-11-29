package de.mq.merchandise.subject.support;

import java.util.Locale;

import de.mq.merchandise.customer.Customer;
import de.mq.util.event.Observable;


public interface UserModel extends Observable<UserModel.EventType> {
	
	enum EventType {
		LocaleChanged;
	}

	 Customer getCustomer();

	Locale getLocale();

	void setLocale(Locale locale);

}