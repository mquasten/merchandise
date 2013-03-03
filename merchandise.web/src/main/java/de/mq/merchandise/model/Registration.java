package de.mq.merchandise.model;

import de.mq.merchandise.customer.Customer;

public interface Registration {
	public enum Kind  {
		NaturalPerson,
		LegalPerson,
		User;
	}

    Kind kind();

	Customer customer();

	void assign(final Customer customer);

}