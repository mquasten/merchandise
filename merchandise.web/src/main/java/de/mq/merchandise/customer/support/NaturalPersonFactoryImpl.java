package de.mq.merchandise.customer.support;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.support.NativityImpl;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.model.support.WebProxyFactory;
import de.mq.merchandise.util.EntityUtil;

@Configuration
public class NaturalPersonFactoryImpl {
	
	@Autowired
	private final WebProxyFactory webProxyFactory;
	
	NaturalPersonFactoryImpl(){
		this.webProxyFactory=null;
	}
	
	NaturalPersonFactoryImpl(final WebProxyFactory webProxyFactory) {
		this.webProxyFactory=webProxyFactory;
	}
	
	@Bean(name="naturalPerson")
	@Scope("view")
	
	public NaturalPersonAO naturalPerson() {
		final NaturalPerson naturalPerson = EntityUtil.create(NaturalPersonImpl.class);
		final Field field = ReflectionUtils.findField(NaturalPersonImpl.class, "nativity");
		field.setAccessible(true);
		ReflectionUtils.setField(field, naturalPerson, EntityUtil.create(NativityImpl.class));
		return webProxyFactory.webModell(NaturalPersonAO.class, naturalPerson);
	}

}
