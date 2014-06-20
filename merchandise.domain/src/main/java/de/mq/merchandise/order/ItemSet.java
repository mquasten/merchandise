package de.mq.merchandise.order;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;

public interface ItemSet {

	Date submitted();

	boolean isSubmitted();

	Customer tradingPartner();

	Opportunity opportunity();

	Money amount();

	void assign(final Item item);

	void remove(final UUID itemId);

	Item item(final UUID itemId);

	Collection<Item> items();

	Date created();

	

}