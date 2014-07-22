package de.mq.merchandise.util.chouchdb.support;

import junit.framework.Assert;

import org.junit.Test;

public class CouchDBUrlBuilder {
	private static final String PORT = "4711";
	private static final String HOST = "www.kylie.com";
	final static String VIEW_URL = "http://localhost:5984/petstore/_design/qualityByArtist/_view/qualityByArtist";
	final static String VIEW_FUNCTION_URL = "http://localhost:5984/petstore/_design/qualityByArtist/_view/QualityByArtist";
	
	final static String VIEW_URL_WITH_LIST=  "http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/pricePerUnit";
	@Test
	public final void urlForView() {
		//System.out.println(new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").build());
		Assert.assertEquals(VIEW_URL, new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").build());
	}
	@Test
	public final void withListFunction() {
		//System.out.println( new SimpleChouchDBUrlBuilder().withView("pricePerUnit").withDatabase("petstore").withListFunction("quantityFilter").build());
		Assert.assertEquals(VIEW_URL_WITH_LIST, new SimpleChouchDBUrlBuilder().withView("pricePerUnit").withDatabase("petstore").withListFunction("quantityFilter").build());
	}
	
	@Test
	public final void withViewFunction() {
		//System.out.println(new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withViewFunction("QualityByArtist").build());
		Assert.assertEquals(VIEW_FUNCTION_URL, new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withViewFunction("QualityByArtist").build());
	}
	@Test
	public final void withHost() {
		Assert.assertEquals(VIEW_URL.replace("localhost", HOST ).replace("5984", PORT),  new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withHost(HOST).withPort(PORT).build());
	}

}
