package de.mq.merchandise.util.chouchdb.support;



import org.springframework.util.Assert;

class SimpleChouchDBUrlBuilder {
	
	
	private String host = "localhost";
	
	private String port= "5984"; 
	
	private String view; 
	
	private String viewFunction;
	
	private String database; 
	
	private String listFunktion;
	
	public final SimpleChouchDBUrlBuilder withView(final String view) {
		Assert.notNull(view);
		this.view=view;
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
	
	public final SimpleChouchDBUrlBuilder withPort(final String port) {
		Assert.notNull(port, "Port is mandatory");
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
		
		return "http://" + String.format( "%s:%s/%s/_design/%s/%s/%s", host, port, database, view,list, viewFunction).replaceAll("[/]+", "/");
	}
}
