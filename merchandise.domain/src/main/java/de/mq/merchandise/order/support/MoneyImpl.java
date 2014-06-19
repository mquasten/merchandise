package de.mq.merchandise.order.support;

import java.util.Currency;

public class MoneyImpl implements Comparable<Money>, Money {

	private static final String DEFAULT_CURRENCY = "EUR";

	private long amount;

	private String currencyCode;

	private int fractionDigits;

	final static int CENT_FRACTION_DIGITS = 3;

	MoneyImpl() {
		amount = 0;
		currencyCode = DEFAULT_CURRENCY;
	}

	public MoneyImpl(final double amount) {
		this(amount, Currency.getInstance(DEFAULT_CURRENCY));
	}

	public MoneyImpl(final double amount, final Currency currency) {
		fractionDigits = currency.getDefaultFractionDigits();
		this.amount = (long) (amount * Math.pow(10, effectivFractionDigits()));
		this.currencyCode = currency.getCurrencyCode();
	}

	private int effectivFractionDigits() {
		return CENT_FRACTION_DIGITS + fractionDigits;
	}

	@Override
	public final double amountRounded() {
		return Math.round(Math.pow(10, fractionDigits) * amount()) / Math.pow(10, fractionDigits);
	}

	@Override
	public double amount() {
		return (double) amount / Math.pow(10, effectivFractionDigits());
	}

	@Override
	public final Currency currency() {
		return Currency.getInstance(currencyCode);
	}

	@Override
	public final boolean sameCurrency(final Money money) {
		return currencyCode.equals(money.currency().getCurrencyCode());
	}

	@Override
	public final Money add(final Money money) {
		sameCurrencyGuard(money);
		return new MoneyImpl(this.amount() + money.amount(), currency());
	}

	private void sameCurrencyGuard(final Money money) {
		if (!sameCurrency(money)) {
			throw new IllegalArgumentException("Currenies are different: " + currencyCode + " <--> " + money.currency().getCurrencyCode());
		}
	}

	@Override
	public final Money subtract(final Money money) {
		sameCurrencyGuard(money);
		return new MoneyImpl(this.amount() - money.amount(), currency());
	}

	@Override
	public final Money multiply(final double scalar) {
		return new MoneyImpl(amount() * scalar, currency());
	}

	@Override
	public final Money divide(final double scalar) {
		return new MoneyImpl(amount() / scalar, currency());
	}

	@Override
	public int hashCode() {
		return Double.valueOf(amountRounded()).hashCode() + currencyCode.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Money)) {
			return false;
		}
		final Money money = (Money) other;
		return amountRounded() == money.amountRounded() && currencyCode.equals(money.currency().getCurrencyCode());
	}

	@Override
	public String toString() {
		return amountRounded() + " " + currencyCode;
	}

	@Override
	public int compareTo(final Money money) {
		sameCurrencyGuard(money);
		return (int) Math.signum(amountRounded() - money.amountRounded());
	}

	@Override
	public boolean greaterThan(final Money money) {
		return compareTo(money) > 0;
	}

	@Override
	public boolean greaterOrEqualsThan(final Money money) {
		return compareTo(money) >= 0;
	}

	@Override
	public boolean lessThan(final Money money) {
		return compareTo(money) < 0;
	}

	@Override
	public boolean lessOrEqualsThan(final Money money) {
		return compareTo(money) <= 0;
	}

}
