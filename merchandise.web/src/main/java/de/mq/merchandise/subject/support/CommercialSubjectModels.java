package de.mq.merchandise.subject.support;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.DomainToItemConverterImpl;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;
import de.mq.util.application.et.ExceptionTranslatorOperations;
import de.mq.util.event.EventFascadeProxyFactory;

@Configuration
class CommercialSubjectModels {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LazyQueryContainerFactory lazyQueryContainerFactory;

	@Autowired
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter)
	private Converter<CommercialSubject, Item> commercialSubjectToItemConverter;

	private CommercialSubjectEventFascade commercialSubjectEventFascade = null;

	@Autowired
	@EventFascadeProxyFactory.EventFascadeProxyFactoryQualifier(EventFascadeProxyFactory.FactoryType.CGLib)
	private EventFascadeProxyFactory commercialSubjecteventFascadeProxyFactory;

	@Autowired
	private ItemContainerFactory itemContainerFactory;

	@Autowired
	@MapperQualifier(MapperType.Customer2Subject)
	private Mapper<Customer, CommercialSubject> customerIntoSubjectMapper;
	
	@Autowired
	private ExceptionTranslatorOperations exceptionTranslatorOperations;

	@PostConstruct
	void init() {
		commercialSubjectEventFascade = commercialSubjecteventFascadeProxyFactory.createProxy(CommercialSubjectEventFascade.class);
	}

	@Bean()
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	MainMenuBarView mainMenuBaCommercialSubject(final UserModel userModel, final ViewNav viewNav) {
		return new MainMenuBarView(userModel, messageSource, viewNav);
	}

	@Bean()
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.LazyQueryContainer)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	RefreshableContainer commercialsubjectLazyQueryContainer() {
		return lazyQueryContainerFactory.create(CommercialSubjectCols.Id, commercialSubjectToItemConverter, commercialSubjectEventFascade, EventType.CountPaging, EventType.ListPaging);

	}

	@Bean()
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectSearchItem)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	Item commercialSubjectSearchItem() {
		return itemContainerFactory.create(CommercialSubjectCols.class);

	}

	@Bean
	@Scope(value = "session")
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectModel)
	public CommercialSubjectModel commercialSubjectModel() {

		return new CommercialSubjectModelImpl(newCommercialSubject(), newCommercialSubject(), commercialSubjectEventFascade, customerIntoSubjectMapper,exceptionTranslatorOperations);

	}

	private CommercialSubject newCommercialSubject() {
		return BeanUtils.instantiateClass(CommercialSubjectImpl.class);
	}

	@Bean()
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ConditionValueItem)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	Item conditionValueItem() {
		return itemContainerFactory.create(ConditionValueCols.class);

	}

	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemToCommercialSubjectItemConverter)
	Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter() {
		return new ItemToDomainConverterImpl<CommercialSubjectItem>(CommercialSubjectItemImpl.class, CommercialSubjectItemCols.class).withChild(CommercialSubjectItemCols.Subject, SubjectImpl.class);
	}

	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter)
	Converter<CommercialSubject, Item> commercialSubjectConverter() {
		return new DomainToItemConverterImpl<>(CommercialSubjectCols.class);
	}

	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemIntoCommercialSubjectModel)
	Mapper<Item, CommercialSubjectModel> itemIntoCommercialSubjectModel() {
		return new ItemToDomainConverterImpl<>(CommercialSubjectModelImpl.class, new Enum[] { ConditionValueCols.InputValue });

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToItemConverter)
	Converter<CommercialSubjectItem, Item> commercialSubjectItemConverter() {
		return new DomainToItemConverterImpl(CommercialSubjectItemCols.class).withChild(CommercialSubjectItemCols.Subject);
	}

	@SuppressWarnings("unchecked")
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.InputValueToContainerConverter)
	@Bean
	Converter<Collection<?>, Container> inputValueConverter() {
		
		return inputValues -> {
			final IndexedContainer container = new IndexedContainer();
			container.addContainerProperty(ConditionValueCols.InputValue, String.class, "");
			inputValues.stream().map(v -> (v instanceof String) ? v : String.valueOf(v)).forEach(v -> container.getItem(container.addItem()).getItemProperty(ConditionValueCols.InputValue).setValue(v));
			return container;
		};

		
		
		
	}
}
