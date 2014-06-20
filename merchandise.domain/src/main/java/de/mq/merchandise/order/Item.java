package de.mq.merchandise.order;

import java.util.Collection;
import java.util.Currency;
import java.util.UUID;


public interface Item {

	UUID itemId();

	ItemSet itemSet();

	String productId();

	Integer quantity();

	String quality();

	String unit();

	void assignPricePerUnit(Money pricePerUnit);

	void assignQuantity(Integer quantity);

	Money pricePerUnit();

	Collection<Currency> currencies();

	Money amount();

	void assignProductId(String productId);

	void assignUnit(String unit);

}