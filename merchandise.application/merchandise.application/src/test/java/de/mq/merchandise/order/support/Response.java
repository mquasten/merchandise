package de.mq.merchandise.order.support;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class Response<K,V> {
	
	
	Long total_rows;
	Long offset;
	
	
	@JsonDeserialize(contentAs=Row.class)
	List<Row<K,V>> rows;
}