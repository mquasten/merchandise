package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

public class CouchDBUrlBuilderTest {
	private static final int PORT = 4711;
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
		Assert.assertEquals(VIEW_URL.replace("localhost", HOST ).replace("5984", "" + PORT),  new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withHost(HOST).withPort(PORT).build());
	}
	
	@Test
	public final void withParams() {
		final String result = VIEW_URL+String.format("?%s={%s}", "quality", "quality");
		Assert.assertEquals(result, new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withParams("quality").build());
	}
	
	@Test
	public final void withParamsCollection() {
		final String result = VIEW_URL+String.format("?%s={%s}", "quality", "quality");
		final Collection<String> params = new ArrayList<>();
		params.add("quality");
		Assert.assertEquals(result, new SimpleChouchDBUrlBuilder().withView("qualityByArtist").withDatabase("petstore").withParams("quality").withParams(params).build());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void wrongPort() {
		new SimpleChouchDBUrlBuilder().withPort(-1);
	}

}
