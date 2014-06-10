package de.mq.merchandise.rule.support

 
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.util.CollectionUtils;


import groovy.lang.MetaClass;
import groovy.util.slurpersupport.NodeChild
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient;

class SimpleOrderItemRestClient implements  de.mq.merchandise.rule.Converter<OrderItem, Map<String,Object>>  {

	def RESTClient restClient = new RESTClient('http://localhost:5984/petstore/_design/qualityByArtist/_view/QualityByArtist',   groovyx.net.http.ContentType.JSON);
	
	@Override
	public Map<String,Object> convert(final OrderItem source) {
	
		final Map<String,String> map = new HashMap<>();
		map.put("key" , String.format("\"%s\"" , "nicole"));
	
	    final HttpResponseDecorator results  = restClient.get( 'query' :  map );
	

		final  String quality = DataAccessUtils.requiredSingleResult(results.responseData.get("rows")).get("value");
		
		println quality
		
		return null;
	   
	}

	

	@Override
	public String[] parameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] ok() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] bad() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	

	
}
