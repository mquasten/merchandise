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








import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.util.EventFascadeProxyFactory;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
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

		return new CommercialSubjectModelImpl( newCommercialSubject(), newCommercialSubject(), commercialSubjectEventFascade);

	}


	private CommercialSubject newCommercialSubject() {
		return BeanUtils.instantiateClass(CommercialSubjectImpl.class);
	}

}