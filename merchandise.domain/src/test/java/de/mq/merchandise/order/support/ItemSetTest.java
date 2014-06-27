package de.mq.merchandise.order.support;

import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;
import de.mq.merchandise.util.EntityUtil;

public class ItemSetTest {
	
	private static final long ID = 19680528L;
	private static final String ITEM_ID = "externalId";
	private static final MoneyImpl AMOUNT2 = new MoneyImpl(20, Currency.getInstance("EUR"));
	private static final MoneyImpl AMOUNT1 = new MoneyImpl(10, Currency.getInstance("EUR"));
	private static final Currency CURRENCY = Currency.getInstance("USD");
	private static final Date SUBMIT_DATE = new GregorianCalendar(1968, 4, 28).getTime();
	private final Customer tradingPartner = Mockito.mock(Customer.class);
	private final Opportunity opportunity = Mockito.mock(Opportunity.class);
	
	@Test
	public final void create() {
		final ItemSet itemSet = newItemSet();
		Assert.assertEquals(tradingPartner, itemSet.tradingPartner());
		Assert.assertEquals(opportunity, itemSet.opportunity());
		Assert.assertTrue(Math.abs(itemSet.created().getTime() - new Date().getTime())< 50);
	}

	private ItemSetImpl newItemSet() {
		return new ItemSetImpl(tradingPartner, opportunity, CURRENCY);
	}
	
	
	@Test
	public final  void submitted() {
		final ItemSet itemSet = newItemSet();
		ReflectionTestUtils.setField(itemSet, "submitted", SUBMIT_DATE);
		Assert.assertEquals(SUBMIT_DATE, itemSet.submitted());
	}
	
	@Test
	public final void isSubmitted() {
		final ItemSet itemSet = newItemSet();
		Assert.assertFalse(itemSet.isSubmitted());
		ReflectionTestUtils.setField(itemSet, "submitted", SUBMIT_DATE);
		Assert.assertTrue(itemSet.isSubmitted());
	}
	
	@Test
	public final void currency() {
		final ItemSet itemSet = newItemSet();
		Assert.assertEquals(CURRENCY, itemSet.currency());
	}
	
	@Test
	public final void amount() {
		final ItemSet itemSet = new ItemSetImpl(tradingPartner, opportunity, AMOUNT1.currency());
	
		final Item item1 = Mockito.mock(Item.class);
		Mockito.when(item1.amount()).thenReturn(AMOUNT1);
		Mockito.when(item1.currency()).thenReturn(AMOUNT1.currency());
		
		final Item item2 = Mockito.mock(Item.class);
		Mockito.when(item2.amount()).thenReturn(AMOUNT2);
		Mockito.when(item2.currency()).thenReturn(AMOUNT2.currency());
		
	   final Collection<Item> items = items(itemSet);
	   items.add(item1);
	   items.add(item2);
		
		Assert.assertEquals(AMOUNT1.add(AMOUNT2), itemSet.amount());
		
	}

	@SuppressWarnings("unchecked")
	private Collection<Item> items(final ItemSet itemSet) {
		return (Collection<Item>) ReflectionTestUtils.getField(itemSet, "items");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void assignWrongCurrency() {
		final ItemSet itemSet = newItemSet();
		final Item item = Mockito.mock(Item.class);
		Mockito.when(item.amount()).thenReturn(AMOUNT1);
		Mockito.when(item.currency()).thenReturn(AMOUNT1.currency());
		itemSet.assign(item);
		
	}
	@Test
	public final void assign() {
		final ItemSet itemSet = new ItemSetImpl(tradingPartner, opportunity);
		final Item item1 = Mockito.mock(Item.class);
		Mockito.when(item1.amount()).thenReturn(AMOUNT1);
		Mockito.when(item1.currency()).thenReturn(AMOUNT1.currency());
		
		final Item item2 = Mockito.mock(Item.class);
		Mockito.when(item2.amount()).thenReturn(AMOUNT2);
		Mockito.when(item2.currency()).thenReturn(AMOUNT2.currency());
		
		itemSet.assign(item1);
		itemSet.assign(item2);
		final Collection<Item> items = items(itemSet);
		Assert.assertEquals(2, items.size());
		
		Assert.assertTrue(items.contains(item1));
		Assert.assertTrue(items.contains(item2));
		
	}
	
	@Test
	public final void items() {
		final ItemSet itemSet = new ItemSetImpl(tradingPartner, opportunity);
		final Item item = Mockito.mock(Item.class);
		Assert.assertTrue(itemSet.items().isEmpty());
		items(itemSet).add(item);
		Assert.assertEquals(1, itemSet.items().size());
		Assert.assertEquals(item, itemSet.items().iterator().next());
	}
	@Test
	public final void hash() {
		final ItemSet itemSet = new ItemSetImpl(tradingPartner, opportunity);
		final int result = itemSet.created().hashCode() + tradingPartner.hashCode() + opportunity.hashCode();
		Assert.assertEquals(result, itemSet.hashCode());
	}
	
	@Test
	public final void equals() throws InterruptedException {
		final ItemSet itemSet1 = new ItemSetImpl(tradingPartner, opportunity);
		Thread.sleep(10);
		final ItemSet itemSet2 = new ItemSetImpl(tradingPartner, opportunity);
		Assert.assertFalse(itemSet1.equals(itemSet2));
		
		ReflectionTestUtils.setField(itemSet2, "created", itemSet1.created());
		Assert.assertTrue(itemSet1.equals(itemSet2));
	}
	
	@Test
	public final void item() {
		final ItemSet itemSet = newItemSet();
		final Item item = Mockito.mock(Item.class);
		
		Mockito.when(item.itemId()).thenReturn(ITEM_ID);
		
		items(itemSet).add(item);
		Assert.assertEquals(item, itemSet.item(ITEM_ID));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void itemNotFound() {
		final Item item = Mockito.mock(Item.class);
		Mockito.when(item.itemId()).thenReturn("dontLetMeGetMe");
		final ItemSet itemSet= newItemSet();
		items(itemSet).add(item);
		
		itemSet.item(ITEM_ID);
		
	}
	
	@Test
	public final void remove() {
		final ItemSet itemSet = newItemSet();
		final Item item = Mockito.mock(Item.class);
		
		Mockito.when(item.itemId()).thenReturn(ITEM_ID);
		items(itemSet).add(item);
		
		Assert.assertFalse(items(itemSet).isEmpty());
		itemSet.remove(ITEM_ID);
		
		Assert.assertTrue(items(itemSet).isEmpty());
		
	}
	
	@Test
	public final void removeNotFound() {
		 newItemSet().remove(ITEM_ID);
	}
	
	@Test
	public final void hasId() {
		final ItemSet itemSet = newItemSet();
		Assert.assertFalse(itemSet.hasId());
		EntityUtil.setId(itemSet, ID);
		Assert.assertTrue(itemSet.hasId());
		
	}
	
	@Test
	public final void id() {
		final ItemSet itemSet = newItemSet();
		EntityUtil.setId(itemSet, ID);
		Assert.assertEquals(ID, itemSet.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNotSet() {
		newItemSet().id();
	}
	
	
	

}
