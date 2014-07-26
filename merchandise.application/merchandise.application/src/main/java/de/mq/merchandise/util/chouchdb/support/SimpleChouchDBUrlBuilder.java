package de.mq.merchandise.util.chouchdb.support;



import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

class SimpleChouchDBUrlBuilder {
	
	
	private String host = "localhost";
	
	private int port= 5984; 
	
	private String view; 
	
	private String viewFunction;
	
	private String database; 
	
	private String listFunktion;
	
	private final Set<String> params = new HashSet<>();
	
	public final SimpleChouchDBUrlBuilder withView(final String view) {
		Assert.notNull(view);
		this.view=view;
		return this;
	}
	
	public final SimpleChouchDBUrlBuilder withParams(final Collection<String> params) {
		this.params.addAll(params);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public final SimpleChouchDBUrlBuilder withParams(final String ... params) {
		this.params.addAll(new HashSet<String>(CollectionUtils.arrayToList(params)));
		return this;
	}
	 
	
	public final SimpleChouchDBUrlBuilder withViewFunction(final String viewFunction) {
		Assert.notNull(viewFunction);
		this.viewFunction=viewFunction;
		return this;
	}
	
	
	
	public final SimpleChouchDBUrlBuilder withDatabase(final String database) {
		this.database=database;
		return this;
	}
	
	public final SimpleChouchDBUrlBuilder withListFunction(final String listFunction) {
		Assert.notNull(listFunction, "Listfunction is Mandatory");
		this.listFunktion=listFunction;
		return this;
	}
	
	public final SimpleChouchDBUrlBuilder withHost(final String host) {
		Assert.notNull(host, "Host is mandatory");
		this.host=host;
		return this;
	}
	
	public final SimpleChouchDBUrlBuilder withPort(final int port) {
		Assert.isTrue(port > 0, "Port must be > 0");
		this.port=port;
		return this;
	}

	
	public String build() {
		if( viewFunction==null){
			viewFunction=view;
		}
		Assert.notNull(view, "View must be defined");
		Assert.notNull(viewFunction, "ViewFunction must be defined");
		Assert.notNull(database, "Database must be defined");
		
		String list="_view";
		if(listFunktion != null){
			list="_list/quantityFilter";
		}
		final StringBuilder stringBuilder = new StringBuilder();
		String prefix= "?";	
		for(final String key : params){	
			stringBuilder.append(String.format("%s%s={%s}", prefix, key, key));
			prefix="&";
		}
		
		return "http://" + String.format( "%s:%s/%s/_design/%s/%s/%s", host, port, database, view,list, viewFunction).replaceAll("[/]+", "/")+stringBuilder.toString();
	}
}
