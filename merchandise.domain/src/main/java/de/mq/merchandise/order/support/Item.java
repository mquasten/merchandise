package de.mq.merchandise.order.support;

import java.util.Collection;
import java.util.Currency;

interface Item {

	public abstract String itemId();

	public abstract ItemSet itemSet();

	public abstract String productId();

	public abstract Integer quantity();

	public abstract String quality();

	public abstract String unit();

	public abstract void assignPricePerUnit(Money pricePerUnit);

	public abstract void assignProductId(String productId);

	public abstract void assignQuantity(Integer quantity);

	public abstract void assignQuality(String quality);

	public abstract void assignUnit(String unit);

	public abstract Money pricePerUnit();

	public abstract Collection<Currency> currencies();

	public abstract Money amount();

}