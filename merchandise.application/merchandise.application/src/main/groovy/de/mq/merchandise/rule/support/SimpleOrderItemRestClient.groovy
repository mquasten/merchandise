package de.mq.merchandise.rule.support

 
import groovy.lang.MetaClass;
import groovy.util.slurpersupport.NodeChild
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient;

class SimpleOrderItemRestClient implements  de.mq.merchandise.rule.Converter<OrderItem, Map<String,Object>>  {

	def RESTClient restClient = new RESTClient('http://echo.jsontest.com',  groovyx.net.http.ContentType.JSON);
	
	@Override
	public Map<String,Object> convert(final OrderItem source) {
	
	    def HttpResponseDecorator results  = restClient.get('path' :  '/quantity/2/quality/platinum/product/nicole/unit/date/priceperunit/999.00/currency/EUR');
	
		return results.responseData
	   
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
