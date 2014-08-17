package de.mq.merchandise.rule.support

 
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import de.mq.merchandise.util.chouchdb.support.CouchDBOperations;
import groovy.lang.MetaClass;
import groovy.util.slurpersupport.NodeChild

import org.springframework.util.Assert

class SimpleOrderItemRestClient implements  de.mq.merchandise.rule.Converter<Item,Item>  {


	
	def ConditionOperations conditionOperations = new ConditionReflectionTemplate();
	
	def CouchDBOperations couchDBTemplate 
	
	public SimpleOrderItemRestClient(CouchDBOperations couchDBTemplate) {
		this.couchDBTemplate = couchDBTemplate;
	}

	@Override
	def Item convert(final Item item) {
		
		EntityUtil.mandatoryGuard item.productId(),  "ProductId"
		EntityUtil.mandatoryGuard item.unit(), "Unit"
		EntityUtil.notNullGuard item.quantity(), "Quantity" 
		
		def Collection<String> qualities =  couchDBTemplate.forKey "qualityByArtist", item.productId().trim().toLowerCase() , String.class
		
		conditionOperations.copy ConditionType.Quality, DataAccessUtils.requiredUniqueResult(qualities) , item 
	
		
		def Map<String,String> key =  new LinkedHashMap<>()
		key.put "quality", item.quality()
		key.put "unit", item.unit().trim().toLowerCase();
		def Map<String,String> params = new HashMap<>();
		params.put "quantity", item.quantity();
		
		final List<Map<String,String>> prices = couchDBTemplate.forKey "pricePerUnit", "quantityFilter", key, params ,Map.class
		conditionOperations.copy ConditionType.PricePerUnit, requiredResult(DataAccessUtils.requiredUniqueResult(prices), "pricePerUnit") + " EUR", item
		
	
		return item 
		
	}

	private requiredResult(final Map<String,?> map, final String key) {
		def value = map.get(key);
		Assert.notNull value, key + " not found in result"
		return value;
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
