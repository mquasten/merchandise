package de.mq.merchandise.util.support;

import java.util.Collection;




import org.junit.Assert;
import org.junit.Test;

import com.vaadin.data.Item;

import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

public class ItemContainerFactoryTest {
	
	private final ItemContainerFactory itemContainerFactory  = new SimpleItemContainerFactoryImpl();
	
	
	@Test
	public final void create() {
		
		Collection<TableContainerColumns> results = TestConstants.subjectColumns();
		final Item item = itemContainerFactory.create(TestConstants.SUBJECT_COLS_CLASS);
		
		Assert.assertEquals(results.size(), item.getItemPropertyIds().size());
		item.getItemPropertyIds().forEach(id -> Assert.assertTrue(results.contains(id)));
		results.forEach(col -> Assert.assertEquals(col.nvl(), item.getItemProperty(col).getValue()));
	
	}

}
