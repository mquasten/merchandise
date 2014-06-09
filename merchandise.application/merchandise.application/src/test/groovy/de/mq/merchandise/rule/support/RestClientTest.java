package de.mq.merchandise.rule.support;

import java.util.HashSet;
import java.util.Map;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.rule.Converter;

public class RestClientTest {
	
	@Test
	public final void restClient() {
		final Converter<OrderItem, Map<String, Object>> restClient = new SimpleOrderItemRestClient();
		
		final Map<String,Object> results = restClient.convert(null);
		final Set<String> values= new HashSet<>();
		for(final Condition.ConditionType conditionType : Condition.ConditionType.values()){
			values.add(conditionType.name().toLowerCase());
		}
		Assert.assertEquals(Condition.ConditionType.values().length, values.size());
		Assert.assertEquals(Condition.ConditionType.values().length, results.size());
		for(String key : results.keySet()){
			values.remove(key);
			if(key.equalsIgnoreCase(Condition.ConditionType.Product.name())){
				Assert.assertEquals("nicole", results.get(key));
				continue;
			}
			if(key.equalsIgnoreCase(Condition.ConditionType.Unit.name())){
				Assert.assertEquals("date", results.get(key));
				continue;
			}
			if(key.equalsIgnoreCase(Condition.ConditionType.Quality.name())){
				Assert.assertEquals("platinum", results.get(key));
				continue;
			}
			if(key.equalsIgnoreCase(Condition.ConditionType.Quantity.name())){
				Assert.assertEquals("2", results.get(key));
				continue;
			}
			if(key.equalsIgnoreCase(Condition.ConditionType.PricePerUnit.name())){
				Assert.assertEquals("999.00", results.get(key));
				continue;
			}

			if(key.equalsIgnoreCase(Condition.ConditionType.Currency.name())){
				Assert.assertEquals("EUR", results.get(key));
				continue;
			}

			Assert.fail("Invalid key" + key);
			
		}
		Assert.assertTrue(values.isEmpty());
	}

}
