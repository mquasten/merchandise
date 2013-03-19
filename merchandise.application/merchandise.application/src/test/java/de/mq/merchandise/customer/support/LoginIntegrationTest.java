package de.mq.merchandise.customer.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/repositories.xml"})
@ActiveProfiles("db")
public class LoginIntegrationTest {
	
	@Autowired()
	private CustomerRepository customerRepository; 
	
	@Test
	@Transactional
	public final void testLogin() {


	System.out.println(customerRepository.forLogin("4924346113"));
	}
	

}
