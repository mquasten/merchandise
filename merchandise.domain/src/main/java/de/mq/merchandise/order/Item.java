package de.mq.merchandise.order;

import java.util.Collection;
import java.util.Currency;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;


public interface Item extends BasicEntity{

	String itemId();

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

	void assign(final String itemId);

}