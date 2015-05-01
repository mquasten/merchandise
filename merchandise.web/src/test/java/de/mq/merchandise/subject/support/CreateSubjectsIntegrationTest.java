package de.mq.merchandise.subject.support;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:merchandise.xml" })
@Ignore
public class CreateSubjectsIntegrationTest {
	
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CustomerService customerService;
	
	@Test
	public final void create() {
		final Customer customer = customerService.customer(Optional.of(1L));
		
		IntStream.range(1, 10000).forEach(i  -> subjectService.save(new SubjectImpl(customer, "Observable+" + Math.round(Math.random()*1000),"Description+" + Math.round(Math.random()*1000) )));
	}

}
