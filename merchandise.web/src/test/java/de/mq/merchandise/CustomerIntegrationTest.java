package de.mq.merchandise;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.CustomerService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/repositories.xml"})
@ActiveProfiles("db")
public class CustomerIntegrationTest {

	@Autowired
	CustomerService customerService;
	
	@Test
	@Transactional
	public final void test() {
		System.out.println(customerService.customer(4030L));
	}
	
}
