package de.mq.merchandise.subject.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;
import de.mq.merchandise.util.support.RefreshableContainer;

@Configuration
class SubjectModels {
	
	
	
	@Autowired
	private LazyQueryContainerFactory lazyQueryContainerFactory;
	
	@Autowired
	private ItemContainerFactory itemContainerFactory;

	@Autowired
	private CustomerService customerService;

	@Bean
	@Scope( proxyMode=ScopedProxyMode.TARGET_CLASS  ,  value="session")
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectModel)
	SubjectModel subjectModel() {
		return new SubjectModelImpl();

	}

	@Bean
	@Scope( proxyMode=ScopedProxyMode.TARGET_CLASS  ,  value="session")
	UserModel userModel() {
		return new UserModelImpl(customerService.customer(Optional.of(1L)));
	}

	@Bean
	@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToSubjectConverter)
	Converter<Item, Subject> itemToSubjectConverter() {
		return new ItemToDomainConverterImpl<>(SubjectImpl.class, SubjectCols.class);
	} 
	
	@Bean()
	@SubjectModelQualifier(SubjectModelQualifier.Type.LazyQueryContainer)
	@Scope(  proxyMode=ScopedProxyMode.TARGET_CLASS ,  value="session")
	RefreshableContainer  subjectLazyQueryContainer() {
		return lazyQueryContainerFactory.create(SubjectCols.Id, SubjectConverterImpl.class, SubjectModelControllerImpl.class);
		
	}
	
	@Bean()
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectSearchItem)
	@Scope(  proxyMode=ScopedProxyMode.TARGET_CLASS ,  value="session")
	Item subjectItemContainer() {
		return itemContainerFactory.create(SubjectCols.class);
		
	}
	
	
	

}
