package de.mq.merchandise.subject.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.support.Mapper;

public class CommercialSubjectIntoCommercialSubjectMapperTest {
	
	private static final String NAME = "Kylie";
	private final  Mapper<CommercialSubject,CommercialSubject> mapper = new  CommercialSubjectIntoCommercialSubjectMapperImpl(); 
	
	@Test
	public final void mapInto() {
		final Customer customer = Mockito.mock(Customer.class);
		final CommercialSubject source = new CommercialSubjectImpl(NAME, customer);
		final CommercialSubject target = BeanUtils.instantiateClass(CommercialSubjectImpl.class);
		mapper.mapInto(source, target);
		Assert.assertEquals(NAME, target.name());
	}

}
