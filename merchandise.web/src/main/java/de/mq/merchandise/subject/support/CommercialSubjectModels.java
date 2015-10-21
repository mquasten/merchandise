package de.mq.merchandise.subject.support;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.EventFascadeProxyFactory;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.DomainToItemConverterImpl;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

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
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectModel)
	CommercialSubjectModel commercialSubjectModel() {

		return new CommercialSubjectModelImpl( newCommercialSubject(), newCommercialSubject(), commercialSubjectEventFascade, customerIntoSubjectMapper);

	}


	private CommercialSubject newCommercialSubject() {
		return BeanUtils.instantiateClass(CommercialSubjectImpl.class);
	}
	
	

	
	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemToCommercialSubjectItemConverter)
	Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter() {
		return new ItemToDomainConverterImpl<CommercialSubjectItem>(CommercialSubjectItemImpl.class, CommercialSubjectItemCols.class).withChild(CommercialSubjectItemCols.Subject, SubjectImpl.class);
	}
	
	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter)
	 Converter<CommercialSubject, Item>   commercialSubjectConverter()  {
		return new DomainToItemConverterImpl<>(CommercialSubjectCols.class);
	}
	
	
	
	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemConditionToContainerConverter)
	public Converter<CommercialSubjectItemConditionImpl, Item> commercialSubjectItemConditionToContainerConverter () {
		return new DomainToItemConverterImpl<CommercialSubjectItemConditionImpl>(new ConditionValueCols[]{ConditionValueCols.Condition, ConditionValueCols.InputValue}).withChild(ConditionValueCols.Condition);
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToItemConverter)
	 Converter<CommercialSubjectItem, Item>  commercialSubjectItemConverter()  {
		return new DomainToItemConverterImpl(CommercialSubjectItemCols.class).withChild(CommercialSubjectItemCols.Subject);
	}

}
