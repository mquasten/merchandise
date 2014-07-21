package de.mq.merchandise.rule.support

 
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.order.Item
import de.mq.merchandise.order.Money
import de.mq.merchandise.order.support.ConditionOperations;
import de.mq.merchandise.order.support.ConditionReflectionTemplate;
import de.mq.merchandise.order.support.MoneyImpl
import de.mq.merchandise.util.EntityUtil;
import groovy.lang.MetaClass;
import groovy.util.slurpersupport.NodeChild
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient;

class SimpleOrderItemRestClient implements  de.mq.merchandise.rule.Converter<Item,Item>  {

	def ConditionOperations conditionOperations = new ConditionReflectionTemplate();
	def RESTClient rest = new RESTClient('http://localhost:5984/petstore/_design/',   groovyx.net.http.ContentType.JSON);
	@Override
	def Item convert(final Item item) {
		
		EntityUtil.mandatoryGuard item.productId(),  "ProductId"
		EntityUtil.mandatoryGuard item.unit(), "Unit"
		EntityUtil.notNullGuard item.quantity(), "Quantity" 
		
		def product=item.productId().toLowerCase();
		def unit = item.unit().toLowerCase();
		def quantity = item.quantity();
		
		
	    def HttpResponseDecorator results  = rest.get(  'path' :'qualityByArtist/_view/qualityByArtist', query : map( "key" ,  String.format("\"%s\"" , product )) );
	
		def  String quality = requiredResult(DataAccessUtils.requiredSingleResult(requiredResult(results.responseData, "rows")), "value");
		
		def HttpResponseDecorator prices  = rest.get(  'path' :'pricePerUnit/_list/quantityFilter/pricePerUnit', query : map("key" , String.format("{\"quality\":\"%s\",\"unit\":\"%s\"  }", quality, unit) , "quantity", quantity ) );
		def x =  requiredResult(requiredResult(DataAccessUtils.requiredSingleResult(requiredResult(prices.responseData, "rows")), "value"), "pricePerUnit");
	
		conditionOperations.copy ConditionType.PricePerUnit, x + " EUR", item
		conditionOperations.copy ConditionType.Quality, quality, item
		println quality
		return item
	   
	}

	private requiredResult(final Map<String,?> map, final String key) {
		def value = map.get(key);
		if( value== null ) {
		   throw new IllegalStateException("Incorrect result: Property: " + key + " not aware in  Response" );
	    }
		return value;
    }
			
	private Map<?,?> map(final Object ...  values) {
		def Map<?,?> results = new HashMap<>();
		def key =null 
		for(final Object value : values) {
			
			if( key==null) {
			   key=value;
			} else {
			   results.put(key,value);
			   key=null;
			}
			
		}
		return results;
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
