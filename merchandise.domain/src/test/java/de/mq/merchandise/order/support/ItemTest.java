package de.mq.merchandise.order.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;

public class ItemTest {
	
	@Test
	public final void createItem() {
		final ItemSet itemSet = Mockito.mock(ItemSet.class);
		final Item item = new ItemImpl(itemSet);
		Assert.assertNotNull( item.itemId());
		Assert.assertEquals(ReflectionTestUtils.getField(item, "itemId"), item.itemId().toString());
		Assert.assertEquals(itemSet, item.itemSet());
	}

}
