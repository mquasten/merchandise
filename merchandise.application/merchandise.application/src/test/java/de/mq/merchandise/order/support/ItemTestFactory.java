package de.mq.merchandise.order.support;

import java.util.Map;

import org.mockito.Mockito;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;

public   class ItemTestFactory {
	
	public static  Item createItem() {
		final ItemSet itemSet = Mockito.mock(ItemSet.class);
		final CommercialSubject subject = Mockito.mock(CommercialSubject.class);
		return new ItemImpl(itemSet, subject);
	}
	
	public static void assin(final Map<Condition.ConditionType,String> values, final Item item) {
	   final ConditionOperations conditionOperations= new ConditionReflectionTemplate();
	   conditionOperations.copy(values, item);
		
	}

}
