package de.mq.merchandise.order.support;

import java.util.Currency;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.order.Money;

public class MoneyTest {

	@Test
	public final void constructor() {
		final Money money = new MoneyImpl(47.11, Currency.getInstance("EUR"));
		Assert.assertEquals(4711000L, amount(money));
		Assert.assertEquals("EUR", currencyCode(money));

	}

	@Test
	public final void constructorAmount() {
		final Money money = new MoneyImpl(8.15);
		Assert.assertEquals(815000, amount(money));
		Assert.assertEquals("EUR", currencyCode(money));

	}

	@Test
	public final void defaultConstructor() {
		final Money money = new MoneyImpl();
		Assert.assertEquals(0, amount(money));
		Assert.assertEquals("EUR", currencyCode(money));
	}

	@Test
	public final void amount() {
		Assert.assertEquals(47.114d, new MoneyImpl(47.114, Currency.getInstance("EUR")).amount());
		Assert.assertEquals(47.115d, new MoneyImpl(47.115, Currency.getInstance("EUR")).amount());
	}

	@Test
	public final void amountRounded() {
		Assert.assertEquals(47.11d, new MoneyImpl(47.114, Currency.getInstance("EUR")).amountRounded());
		Assert.assertEquals(47.12d, new MoneyImpl(47.115, Currency.getInstance("EUR")).amountRounded());
	}

	@Test
	public final void currency() {
		Assert.assertEquals(Currency.getInstance("USD"), new MoneyImpl(47.11, Currency.getInstance("USD")).currency());
	}

	@Test
	public final void sameCurrency() {
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).sameCurrency(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).sameCurrency(new MoneyImpl(08.15, Currency.getInstance("USD"))));
	}

	@Test
	public final void add() {
		Money result = new MoneyImpl(47.11, Currency.getInstance("EUR")).add(new MoneyImpl(08.15, Currency.getInstance("EUR")));
		Assert.assertEquals(4711000 + 815000, amount(result));
		Assert.assertEquals("EUR", currencyCode(result));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void addDifferentCurencies() {
		new MoneyImpl(47.11, Currency.getInstance("EUR")).add(new MoneyImpl(47.11, Currency.getInstance("USD")));

	}

	@Test
	public final void subtract() {
		final Money result = new MoneyImpl(47.11, Currency.getInstance("EUR")).subtract(new MoneyImpl(08.15, Currency.getInstance("EUR")));
		Assert.assertEquals(4711000 - 815000, amount(result));
		Assert.assertEquals("EUR", currencyCode(result));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void subtractDifferentCurencies() {
		new MoneyImpl(47.11, Currency.getInstance("EUR")).subtract(new MoneyImpl(47.11, Currency.getInstance("USD")));
	}

	@Test
	public final void multiply() {
		final Money result = new MoneyImpl(47.11, Currency.getInstance("EUR")).multiply(08.15);
		Assert.assertEquals(38394650, amount(result));
		Assert.assertEquals("EUR", currencyCode(result));
	}

	@Test
	public final void divide() {
		final Money result = new MoneyImpl(47.11, Currency.getInstance("EUR")).divide(08.15);
		Assert.assertEquals(578036, amount(result));
		Assert.assertEquals("EUR", currencyCode(result));
	}

	@Test
	public final void hash() {
		final Money result = new MoneyImpl(47.11, Currency.getInstance("EUR"));
		Assert.assertEquals(Double.valueOf(result.amountRounded()).hashCode() + currencyCode(result).hashCode(), result.hashCode());
	}

	@Test
	public final void equals() {
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).equals(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).equals(new MoneyImpl(47.11, Currency.getInstance("USD"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).equals(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).equals("dontLetMeGetMe"));
	}

	@Test
	public final void string() {
		Assert.assertEquals("47.11 EUR", new MoneyImpl(47.112, Currency.getInstance("EUR")).toString());
	}

	@Test
	public final void compareTo() {
		Assert.assertEquals(1, new MoneyImpl(47.11, Currency.getInstance("EUR")).compareTo(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertEquals(-1, new MoneyImpl(08.15, Currency.getInstance("EUR")).compareTo(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertEquals(0, new MoneyImpl(47.11, Currency.getInstance("EUR")).compareTo(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
	}

	@Test
	public final void greaterThan() {
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).greaterThan(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).greaterThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(08.15, Currency.getInstance("EUR")).greaterThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
	}

	@Test
	public final void greaterOrEqualsThan() {
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).greaterOrEqualsThan(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).greaterOrEqualsThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(08.15, Currency.getInstance("EUR")).greaterOrEqualsThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
	}

	@Test
	public final void lessThan() {
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).lessThan(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).lessThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertTrue(new MoneyImpl(08.15, Currency.getInstance("EUR")).lessThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
	}

	@Test
	public final void lessOrEqualsThan() {
		Assert.assertFalse(new MoneyImpl(47.11, Currency.getInstance("EUR")).lessOrEqualsThan(new MoneyImpl(08.15, Currency.getInstance("EUR"))));
		Assert.assertTrue(new MoneyImpl(47.11, Currency.getInstance("EUR")).lessOrEqualsThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
		Assert.assertTrue(new MoneyImpl(08.15, Currency.getInstance("EUR")).lessOrEqualsThan(new MoneyImpl(47.11, Currency.getInstance("EUR"))));
	}

	private long amount(final Money money) {
		return (Long) ReflectionTestUtils.getField(money, "amount");
	}

	private String currencyCode(final Money money) {
		return (String) ReflectionTestUtils.getField(money, "currencyCode");
	}

}
