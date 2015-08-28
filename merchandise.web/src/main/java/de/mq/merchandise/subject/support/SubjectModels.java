package de.mq.merchandise.subject.support;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.EventFascadeProxyFactory;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

@Configuration
class SubjectModels {

	@Autowired
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectToItemConverter)
	private Converter<Subject, Item> subjectToItemConverter;

	@Autowired
	@EventFascadeProxyFactory.EventFascadeProxyFactoryQualifier(EventFascadeProxyFactory.FactoryType.CGLib)
	private EventFascadeProxyFactory eventFascadeProxyFactory;

	@Autowired
	private LazyQueryContainerFactory lazyQueryContainerFactory;

	@Autowired
	@SubjectMapper(SubjectMapperType.Customer2Subject)
	private Mapper<Customer, Subject> customerIntoSubjectMapper;

	@Autowired
	private ItemContainerFactory itemContainerFactory;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private  MessageSource messageSource;
	private SubjectEventFascade subjectEventFascade = null;

	@PostConstruct
	void init() {
		subjectEventFascade = eventFascadeProxyFactory.createProxy(SubjectEventFascade.class);
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectModel)
	SubjectModel subjectModel() {

		return new SubjectModelImpl(subjectEventFascade, customerIntoSubjectMapper);

	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	UserModel userModel() {
		return new UserModelImpl(customerService.customer(Optional.of(1L)));
	}

	@Bean
	@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToSubjectConverter)
	Converter<Item, Subject> itemToSubjectConverter() {
		return new ItemToDomainConverterImpl<>(SubjectImpl.class, SubjectCols.class);
	}
	
	@Bean
	@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToConditionConverter)
	Converter<Item, Condition> itemToConditionConverter() {
		return new ItemToDomainConverterImpl<>(ConditionImpl.class, ConditionCols.class);
	}

	@Bean()
	@SubjectModelQualifier(SubjectModelQualifier.Type.LazyQueryContainer)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	RefreshableContainer subjectLazyQueryContainer() {

		return lazyQueryContainerFactory.create(SubjectCols.Id, subjectToItemConverter, subjectEventFascade, EventType.CountPaging, EventType.ListPaging);

	}
	
	
	@Bean()
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectSearchItem)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	Item subjectItemContainer() {
		return itemContainerFactory.create(SubjectCols.class);

	}
	
	@Bean()
	@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectMenuBar)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	MainMenuBarView mainMenuBarViewSubject(final UserModel userModel, final ViewNav viewNav) {
		return new MainMenuBarView(userModel, messageSource, viewNav);
	}

}
