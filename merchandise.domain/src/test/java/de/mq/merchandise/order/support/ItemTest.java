package de.mq.merchandise.order.support;

import java.util.Currency;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;
import de.mq.merchandise.order.Money;

public class ItemTest {
	private static final Currency CURRENCY = Currency.getInstance("USD");
	private static final String DETAIL = "Details zum Date";
	private static final String UNIT = "private date";
	private static final Integer QUANTITY = 42;
	private static final String QUALITY = "Platinium";
	private static final String PRODUCT_ID = "Nicole";
	private final ItemSet itemSet = Mockito.mock(ItemSet.class);
	private final CommercialSubject subject = Mockito.mock(CommercialSubject.class);
	
	@Test
	public final void createItem() {
		
		final Item item = newItem();
		Assert.assertNotNull( item.itemId());
		Assert.assertEquals(ReflectionTestUtils.getField(item, "itemId"), item.itemId().toString());
		Assert.assertEquals(itemSet, item.itemSet());
		Assert.assertEquals(subject, item.subject());
	}
	
	

	private Item  newItem() {
		return  new ItemImpl(itemSet, subject);
	}
	
	@Test
	public final void productId() {
		final Item item = newItem();
		Assert.assertNull(item.productId());
		ReflectionTestUtils.setField(item, "productId", PRODUCT_ID);
		Assert.assertEquals(PRODUCT_ID, item.productId());
		
	}
	
	@Test
	public final void quality() {
		final Item item = newItem();
		Assert.assertNull(item.quality());
		ReflectionTestUtils.setField(item, "quality", QUALITY);
		Assert.assertEquals(QUALITY, item.quality());
	}
	
	@Test
	public final void quantity() {
		final Item item = newItem();
		Assert.assertNull(item.quantity());
		ReflectionTestUtils.setField(item, "quantity", QUANTITY);
		Assert.assertEquals(QUANTITY, item.quantity());
		
	}
	
	@Test
	public final void unit() {
		final Item item = newItem();
		Assert.assertNull(item.unit());
		ReflectionTestUtils.setField(item, "unit", UNIT);
		Assert.assertEquals(UNIT, item.unit());
	}
	
	@Test
	public final void pricePerUnit() {
		final Item item = newItem();
		Assert.assertNull(item.pricePerUnit());
		final Money pricePerUnit = Mockito.mock(Money.class);
		ReflectionTestUtils.setField(item, "pricePerUnit", pricePerUnit);
		Assert.assertEquals(pricePerUnit, item.pricePerUnit());
	}
	
	@Test
	public final void detail() {
		final Item item = newItem();
		Assert.assertNull(item.detail());
		ReflectionTestUtils.setField(item, "detail", DETAIL);
		Assert.assertEquals(DETAIL, item.detail());
	}
	@Test
	public final void hash() {
		final Item item = newItem();
		final int hashCode = item.itemSet().hashCode()  + item.itemId().toString().hashCode();
		Assert.assertEquals(hashCode, item.hashCode());
	}
	
	@Test
	public final void equals() {
		final Item item1 = newItem();
		final Item item2 = newItem();
		Assert.assertFalse(item1.equals(item2));
		ReflectionTestUtils.setField(item2, "itemId", item1.itemId().toString());
		Assert.assertTrue(item1.equals(item2));
	}
	
	@Test
	public final void amount() {
		final Item item = newItem();
		final Money pricePerUnit = Mockito.mock(Money.class);
		final Money result = Mockito.mock(Money.class);
		Mockito.when(pricePerUnit.multiply(QUANTITY)).thenReturn(result);
		ReflectionTestUtils.setField(item, "quantity", QUANTITY);
		ReflectionTestUtils.setField(item, "pricePerUnit", pricePerUnit);
		Assert.assertEquals(result, item.amount());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void amountWithautQuantity() {
		final Money pricePerUnit = Mockito.mock(Money.class);
		final Item item = newItem();
		ReflectionTestUtils.setField(item, "pricePerUnit", pricePerUnit);
		item.amount();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void amountWithoutPricePerunit() {
		final Item item = newItem();
		ReflectionTestUtils.setField(item, "quantity", QUANTITY);
		item.amount();
	}
	
	@Test
	public final void currency() {
		final Item item = newItem();
		final Money pricePerUnit = Mockito.mock(Money.class);
		Mockito.when(pricePerUnit.currency()).thenReturn(CURRENCY);
		ReflectionTestUtils.setField(item, "pricePerUnit", pricePerUnit);
		Assert.assertEquals(CURRENCY, item.currency());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void currencyWithoutPricePerUnit() {
		newItem().currency();
	}

}
