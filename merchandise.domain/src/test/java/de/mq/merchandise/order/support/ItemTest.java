package de.mq.merchandise.order.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;
import de.mq.merchandise.order.Money;
import de.mq.merchandise.util.EntityUtil;

public class ItemTest {
	private static final long ID = 19680528L;
	private static final String EXTERN_ID = "externId";
	private static final String PRICE_PER_UNIT_AS_STRING = "1000 USD";
	private static final Currency CURRENCY = Currency.getInstance("USD");
	private static final String DETAIL = "Details zum Date";
	private static final String UNIT = "private date";
	private static final Double QUANTITY = 42d;
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
	
	@Test
	public final void assign() {
		final Item item = newItem();
		final Collection<Condition> conditions = new ArrayList<>();
		
		final Condition productCondition = Mockito.mock(Condition.class);
		Mockito.when(productCondition.conditionType()).thenReturn(Condition.ConditionType.Product);
	
		Mockito.when(productCondition.input()).thenReturn(PRODUCT_ID);
		
		final Condition qualityCondition = Mockito.mock(Condition.class);
		Mockito.when(qualityCondition.conditionType()).thenReturn(Condition.ConditionType.Quality);
		
		Mockito.when(qualityCondition.input()).thenReturn(QUALITY);
		
		final Condition unitCondition = Mockito.mock(Condition.class);
		Mockito.when(unitCondition.conditionType()).thenReturn(Condition.ConditionType.Unit);
		
		Mockito.when(unitCondition.input()).thenReturn(UNIT);
		
		final Condition detailCondition = Mockito.mock(Condition.class);
		Mockito.when(detailCondition.conditionType()).thenReturn(Condition.ConditionType.Detail);
		
		Mockito.when(detailCondition.input()).thenReturn(DETAIL);
		
		final Condition quantityCondition = Mockito.mock(Condition.class);
		Mockito.when(quantityCondition.conditionType()).thenReturn(Condition.ConditionType.Quantity);
		
		Mockito.when(quantityCondition.input()).thenReturn(String.valueOf(QUANTITY));
		
		
		final Condition pricePerUnitCondition = Mockito.mock(Condition.class);
		Mockito.when(pricePerUnitCondition.conditionType()).thenReturn(Condition.ConditionType.PricePerUnit);
		
		Mockito.when(pricePerUnitCondition.input()).thenReturn(PRICE_PER_UNIT_AS_STRING);
		
		conditions.add(productCondition);
		conditions.add(qualityCondition);
		conditions.add(unitCondition);
		conditions.add(detailCondition);
		conditions.add(quantityCondition);
		conditions.add(pricePerUnitCondition);
		
		item.assign(conditions);
		
		Assert.assertEquals(PRODUCT_ID, item.productId());
		Assert.assertEquals(QUALITY, item.quality());
		Assert.assertEquals(UNIT, item.unit());
		Assert.assertEquals(DETAIL, item.detail());
		Assert.assertEquals(QUANTITY, item.quantity());
		Assert.assertEquals(new String2MoneyConverter().convert(PRICE_PER_UNIT_AS_STRING), item.pricePerUnit());
		
	}
	
	@Test
	public final void assignItemId() {
		final Item item = newItem();
		item.assign(EXTERN_ID);
		Assert.assertEquals(EXTERN_ID, item.itemId());
	}
	
	@Test
	public final void assignWithOutValue() {
		final Item item = newItem();
		final Collection<Condition> conditions = new ArrayList<>();
		
		final Condition productCondition = Mockito.mock(Condition.class);
		Mockito.when(productCondition.conditionType()).thenReturn(Condition.ConditionType.Product);
		
		
		conditions.add(productCondition);
		
		
		ReflectionTestUtils.setField(item, "productId", PRODUCT_ID);
		
		Assert.assertEquals(PRODUCT_ID, item.productId());
		
		item.assign(conditions);
		Assert.assertNull(item.productId());
	}
	
	@Test
	public final void hasId() {
		final Item item = newItem();
		Assert.assertFalse(item.hasId());
		EntityUtil.setId(item, ID);
		Assert.assertTrue(item.hasId());
	}
	
	@Test
	public final void id() {
		final Item item = newItem();
		EntityUtil.setId(item, ID);
		Assert.assertEquals(ID, item.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotSet() {
		newItem().id();
	}
	
}
