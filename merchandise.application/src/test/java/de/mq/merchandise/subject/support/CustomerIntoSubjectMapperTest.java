package de.mq.merchandise.subject.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;

public class CustomerIntoSubjectMapperTest {
	
	 final Mapper<Customer,Subject> mapper = new CustomerIntoSubjectMapperImpl();
	 
	 @Test
	 public final void mapInto() {
		 final Customer customer = Mockito.mock(Customer.class);
		 final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		 Assert.assertNull(subject.customer());
		 mapper.mapInto(customer, subject);
		 
		 Assert.assertEquals(customer, subject.customer());
	 }
	 

}
