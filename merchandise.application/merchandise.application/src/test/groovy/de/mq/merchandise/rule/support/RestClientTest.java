package de.mq.merchandise.rule.support;

import java.util.Map;

import org.junit.Test;

import de.mq.merchandise.rule.Converter;

public class RestClientTest {
	
	@Test
	public final void restClient() {
		final Converter<OrderItem, Map<String, Object>> restClient = new SimpleOrderItemRestClient();
		
		restClient.convert(null);
		
	
	}

}
