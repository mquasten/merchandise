package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;
import de.mq.util.event.EventFascadeProxyFactory;

public class CommercialSubjectModelsTest {
	
	
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	
	private final LazyQueryContainerFactory lazyQueryContainerFactory = Mockito.mock(LazyQueryContainerFactory.class);

	@SuppressWarnings("unchecked")
	private final Converter<CommercialSubject, Item> commercialSubjectToItemConverter =  Mockito.mock(Converter.class);

	

	
	private final EventFascadeProxyFactory commercialSubjecteventFascadeProxyFactory = Mockito.mock(EventFascadeProxyFactory.class);

	
	private final ItemContainerFactory itemContainerFactory=Mockito.mock(ItemContainerFactory.class);

	
	@SuppressWarnings("unchecked")
	private final Mapper<Customer, CommercialSubject> customerIntoSubjectMapper=Mockito.mock(Mapper.class);
	
	private CommercialSubjectEventFascade commercialSubjectEventFascade = Mockito.mock(CommercialSubjectEventFascade.class);
	
	
	private final CommercialSubjectModels  commercialSubjectModels = new CommercialSubjectModels();
	private final Map<Class<?>,Object> dependencies = new HashMap<>();
	@Before
	public final void setup() {
		
		
		dependencies.put(MessageSource.class, messageSource);
		dependencies.put(LazyQueryContainerFactory.class, lazyQueryContainerFactory);
		dependencies.put(Converter.class, commercialSubjectToItemConverter);
		dependencies.put(EventFascadeProxyFactory.class, commercialSubjecteventFascadeProxyFactory);
		dependencies.put(ItemContainerFactory.class, itemContainerFactory);
		dependencies.put(Mapper.class, customerIntoSubjectMapper);
		dependencies.put(CommercialSubjectEventFascade.class, commercialSubjectEventFascade);
		
		ReflectionUtils.doWithFields(commercialSubjectModels.getClass(), field ->ReflectionTestUtils.setField(commercialSubjectModels, field.getName(), dependencies.get(field.getType())), field -> dependencies.containsKey(field.getType()));
	}
	
	@Test
	public final void dependencies() {
		ReflectionUtils.doWithFields(commercialSubjectModels.getClass(),field -> Assert.assertEquals(dependencies.get(field.getType()), ReflectionTestUtils.getField(commercialSubjectModels, field.getName())), field -> dependencies.containsKey(field.getType()));
	}
	
	@Test
	public final void init() {
		
		final Optional<Field> fascadeField = findField(commercialSubjectModels.getClass(), CommercialSubjectEventFascade.class);
		Assert.assertTrue(fascadeField.isPresent());
		ReflectionTestUtils.setField(commercialSubjectModels, fascadeField.get().getName(), null);
		Assert.assertNull(ReflectionTestUtils.getField(commercialSubjectModels, fascadeField.get().getName()));
		Mockito.when(commercialSubjecteventFascadeProxyFactory.createProxy(CommercialSubjectEventFascade.class)).thenReturn(commercialSubjectEventFascade);
		commercialSubjectModels.init();
		Assert.assertEquals(commercialSubjectEventFascade, ReflectionTestUtils.getField(commercialSubjectModels, fascadeField.get().getName()));
	}

	private Optional<Field> findField(Class<?> parent, Class<?> dependency ) {
		final Optional<Field> fascadeField = Arrays.asList(parent.getDeclaredFields()).stream().filter(field -> field.getType().equals(dependency)).findAny();
		return fascadeField;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T value(Object parent ,Class<T> dependency ) {
		final Optional<Field> field = findField(parent.getClass(), dependency) ;
		Assert.assertTrue(field.isPresent());
		return (T) ReflectionTestUtils.getField(parent, field.get().getName());
	}
	
	@Test
	public final void mainMenuBaCommercialSubject() {
		final UserModel userModel = Mockito.mock(UserModel.class);
		final ViewNav viewNav = Mockito.mock(ViewNav.class);
		final MainMenuBarView mainMenuBarView = commercialSubjectModels.mainMenuBaCommercialSubject(userModel, viewNav);
		
		Assert.assertEquals(userModel, value(mainMenuBarView, UserModel.class));
		Assert.assertEquals(messageSource, value(mainMenuBarView, MessageSource.class));
		Assert.assertEquals(viewNav, value(mainMenuBarView, ViewNav.class));
		
	}
	
	@Test
	public final void commercialsubjectLazyQueryContainer() {
		final RefreshableContainer container = Mockito.mock(RefreshableContainer.class);
		Mockito.when(lazyQueryContainerFactory.create(CommercialSubjectCols.Id, commercialSubjectToItemConverter, commercialSubjectEventFascade, EventType.CountPaging, EventType.ListPaging)).thenReturn(container);
		Assert.assertEquals(container, commercialSubjectModels.commercialsubjectLazyQueryContainer());
	}
	
	@Test
	public final void commercialSubjectSearchItem() {
		final Item item = Mockito.mock(Item.class);
		Mockito.when(itemContainerFactory.create(CommercialSubjectCols.class)).thenReturn(item);
		Assert.assertEquals(item, commercialSubjectModels.commercialSubjectSearchItem());
	}
	
	@Test
	public final void commercialSubjectModel() {
		final CommercialSubjectModel model = commercialSubjectModels.commercialSubjectModel();
		Assert.assertNotNull(model.getSearch());
		
		Assert.assertTrue(model.getCommercialSubject().isPresent());
		Assert.assertEquals(commercialSubjectEventFascade, value(model, CommercialSubjectEventFascade.class));
		Assert.assertEquals(customerIntoSubjectMapper, value(model, Mapper.class));
	}
	
	@Test
	public final void  conditionValueItem() {
		final Item item = Mockito.mock(Item.class);
		Mockito.when(itemContainerFactory.create(ConditionValueCols.class)).thenReturn(item);
		Assert.assertEquals(item, commercialSubjectModels.conditionValueItem());
	}

}
