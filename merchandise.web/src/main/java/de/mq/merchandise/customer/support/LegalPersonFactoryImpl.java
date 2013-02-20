package de.mq.merchandise.customer.support;



import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ReflectionUtils;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.TradeRegisterImpl;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.EntityUtil;

@Configuration
public class LegalPersonFactoryImpl {
	
	
	@Autowired
	private final WebProxyFactory  webProxyFactory;
	
	LegalPersonFactoryImpl() {
		webProxyFactory=null;
	}
	
	LegalPersonFactoryImpl(final WebProxyFactory webProxyFactory) {
		this.webProxyFactory=webProxyFactory;
	}
	
   
	
	
	
	@Bean(name="legalPerson")
	@Scope("view")
	public LegalPersonAO legalPerson() {
		final LegalPerson legalPerson = EntityUtil.create(LegalPersonImpl.class);
		final Field field = ReflectionUtils.findField(LegalPersonImpl.class, "tradeRegister");
		field.setAccessible(true);
		ReflectionUtils.setField(field, legalPerson, EntityUtil.create(TradeRegisterImpl.class));
		return webProxyFactory.webModell(LegalPersonAO.class, legalPerson);
				
	}
	

}
