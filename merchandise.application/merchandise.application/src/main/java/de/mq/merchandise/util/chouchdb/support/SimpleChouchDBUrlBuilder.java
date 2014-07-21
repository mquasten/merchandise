package de.mq.merchandise.util.chouchdb.support;

import org.springframework.util.Assert;

public class SimpleChouchDBUrlBuilder {
	
	
	private String host = "localhost";
	
	private String port= "5984"; 
	
	private String view; 
	
	private String viewFunction;
	
	private String database; 
	
	private String listFunktion;
	
	public final SimpleChouchDBUrlBuilder withView(final String view) {
		Assert.notNull(view);
		this.view=view;
		this.viewFunction=view;
		return this;
	}
	
	public final SimpleChouchDBUrlBuilder withListFunction(final String listFunction) {
		Assert.notNull(listFunction);
		this.listFunktion=listFunction;
		return this;
	}

	
	public String build() {
		Assert.notNull(view);
		Assert.notNull(viewFunction);
		Assert.notNull(database);
		
		String list="";
		if(listFunktion != null){
			list="_list/quantityFilter";
		}
		
		return String.format( "http://%s:%s/%s/_design/%s/%s/%s", host, port, database, view,list, viewFunction);
	}
}
