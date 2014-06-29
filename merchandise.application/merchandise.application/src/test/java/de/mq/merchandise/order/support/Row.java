package de.mq.merchandise.order.support;

import org.codehaus.jackson.map.annotate.JsonDeserialize;


	class Row<K,V> {
		@JsonDeserialize(as=PetPriceKey.class)
		K key;
		
		V value;
		
		String id;
	}
