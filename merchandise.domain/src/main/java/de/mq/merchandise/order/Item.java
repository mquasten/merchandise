package de.mq.merchandise.order;

import java.util.Collection;
import java.util.Currency;
import java.util.UUID;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;


public interface Item {

	UUID itemId();

	ItemSet itemSet();

	String productId();

	Number quantity();

	String quality();

	String unit();

	Money pricePerUnit();

	Currency currency();

	Money amount();

	CommercialSubject subject();

	String detail();

	void assign(final Collection<Condition> conditions);

}