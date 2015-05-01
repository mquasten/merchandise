package de.mq.merchandise.subject.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;

@Configuration
public class SubjectModels {

	@Autowired
	private CustomerService customerService;

	@Bean
	@Scope("session")
	public SubjectModel subjectModel() {
		return new SubjectModelImpl();

	}

	@Bean
	@Scope("session")
	public UserModel userModel() {
		return new UserModelImpl(customerService.customer(Optional.of(1L)));
	}

	@Bean
	public ConversionService conversionService() {
		final DefaultConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(Item.class, Subject.class, new ItemToDomainConverterImpl<>(SubjectImpl.class, SubjectCols.class));
		return conversionService;
	}

}
