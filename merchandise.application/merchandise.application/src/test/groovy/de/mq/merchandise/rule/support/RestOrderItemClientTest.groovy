package de.mq.merchandise.rule.support

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.support.ItemTestFactory;
import de.mq.merchandise.order.support.MoneyImpl
import de.mq.merchandise.rule.Converter;
import  org.junit.Assert;

class RestOrderItemClientTest {
	
	def static final PRODUCT = 'Nicole';
	def static final QUANTITY = '3';
	def static final UNIT = 'Date';
	def static final PRICE = 949.99;
	def static final QUALITY = 'platinium'
	
	@Test
	public final void restClient() {
		def final Converter<Item, Item> restClient = new SimpleOrderItemRestClient() 
		def final Item item = ItemTestFactory.createItem();
		def  final Map<ConditionType,String> values = new HashMap<>();
		values.put ConditionType.Product, PRODUCT 
		values.put ConditionType.Quantity, QUANTITY 
		values.put ConditionType.Unit, UNIT 
		
		ItemTestFactory.assin values, item 
		
		def Item result = restClient.convert(item);
		
		Assert.assertEquals Double.valueOf(QUANTITY), result.quantity , 0
		Assert.assertEquals UNIT, result.unit
		Assert.assertEquals PRODUCT, result.productId
		Assert.assertEquals QUALITY, result.quality
		Assert.assertEquals new MoneyImpl(PRICE), result.pricePerUnit
		
		Assert.assertEquals Double.valueOf(QUANTITY), item.quantity , 0
		Assert.assertEquals UNIT, item.unit
		Assert.assertEquals PRODUCT, item.productId
		Assert.assertEquals QUALITY, item.quality
		Assert.assertEquals new MoneyImpl(PRICE), item.pricePerUnit
		
	}
}
