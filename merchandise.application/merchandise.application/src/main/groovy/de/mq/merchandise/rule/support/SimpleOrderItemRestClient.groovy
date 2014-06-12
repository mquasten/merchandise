package de.mq.merchandise.rule.support

 
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.dao.DataAccessException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


import groovy.lang.MetaClass;
import groovy.util.slurpersupport.NodeChild
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient;

class SimpleOrderItemRestClient implements  de.mq.merchandise.rule.Converter<OrderItem, Map<String,Object>>  {

	def RESTClient rest = new RESTClient('http://localhost:5984/petstore/_design/',   groovyx.net.http.ContentType.JSON);
	// http://localhost:5984/petstore/_design/pricePerUnit/_list/quantityFilter/PricePerUnit?key={%22quality%22:%22platinium%22,%22unit%22:%22date%22}&quantity=11
	@Override
	public Map<String,Object> convert(final OrderItem source) {
	
		final Map<String,String> queryMap = new HashMap<>();
		queryMap.put("key" , String.format("\"%s\"" , "nicole"));
		
	    final HttpResponseDecorator results  = rest.get(  'path' :'qualityByArtist/_view/QualityByArtist', query : queryMap );
	
		final  String quality = DataAccessUtils.requiredSingleResult(results.responseData.get("rows")).get("value");
		if( quality == null){
			throw new InvalidDataAccessApiUsageException("Result is invalid");
		}
		
		
		queryMap.clear();
		
		queryMap.put("key" , String.format("{\"quality\":\"%s\",\"unit\":\"date\"  }", quality));
		
		final HttpResponseDecorator prices  = rest.get(  'path' :'pricePerUnit/_view/PricePerUnit', query : queryMap );
		
	    int quantity =7;
		if( CollectionUtils.isEmpty(prices.responseData.get("rows")) ){
			throw new InvalidDataAccessApiUsageException("Result is invalid");
		}
	
		for( final Map<String,Object> price : prices.responseData.get("rows")) {
			
			if( CollectionUtils.isEmpty(price.value)){
				throw new InvalidDataAccessApiUsageException("Result is invalid");
			}
			Integer min  =  price.value.min ;
			if( min == null){
				min = 0 ;  
			}
			Integer max = price.value.max ;
			if( max == null) {
				max=Integer.MAX_VALUE;
			}
			
			if( quantity <  min.intValue()) {
			   continue;
			}
				
		    if (quantity >  max.intValue()  ) {
			   continue;
			}
			
			if( price.value.pricePerUnit == null) {
				throw new InvalidDataAccessApiUsageException("Result is invalid");
			}
			println "***" + price.value.pricePerUnit + "***"
			return;
			
		}
		throw new InvalidDataAccessApiUsageException("PricePerUnit not found for Parameters");
		
	   
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
