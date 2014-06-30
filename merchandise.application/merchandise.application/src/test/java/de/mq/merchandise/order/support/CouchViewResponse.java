package de.mq.merchandise.order.support;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class CouchViewResponse {
	
	
	
	private Long total_rows;
	private Long offset;
	
	
	@JsonDeserialize(contentAs=SimpleCouchViewRowImpl.class)
	private List<CouchViewResultRow>  rows;
	
	public final List<CouchViewResultRow> rows() {
		if( rows == null){
			rows=new ArrayList<>();
		}
		return rows; 
	}
	
	
	
}