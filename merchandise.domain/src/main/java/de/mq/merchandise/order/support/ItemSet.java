package de.mq.merchandise.order.support;

import java.util.Collection;
import java.util.Date;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;

public interface ItemSet {

	Date submitted();

	boolean isSubmitted();

	Customer tradingPartner();

	Opportunity opportunity();

	Money amount();

	void assign(final Item item);

	void remove(final String productId);

	Item item(final String productId);

	Collection<Item> items();

	Date created();

	void remove(final Item item);

}