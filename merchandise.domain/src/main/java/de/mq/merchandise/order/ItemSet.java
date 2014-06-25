package de.mq.merchandise.order;

import java.util.Collection;
import java.util.Currency;
import java.util.Date;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;

public interface ItemSet   extends BasicEntity{

	Date submitted();

	boolean isSubmitted();

	Customer tradingPartner();

	Opportunity opportunity();

	Money amount();

	void assign(final Item item);

	void remove(final String itemId);

	Item item(final String itemId);

	Collection<Item> items();

	Date created();

	Currency currency();



	

}