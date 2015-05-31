package de.mq.merchandise.subject.support;




import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Item;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.util.EventFascadeProxyFactory;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.support.ItemToDomainConverterImpl;
import de.mq.merchandise.util.support.RefreshableContainer;

public class SubjectModelsTest {
	
	private static final String SUBJECT_EVENT_FASCADE_FIELD = "subjectEventFascade";
	private final SubjectModels subjectModels = new SubjectModels();
	private final CustomerService customerService = Mockito.mock(CustomerService.class);
	private final Customer customer = Mockito.mock(Customer.class);
	private final LazyQueryContainerFactory lazyQueryContainerFactory = Mockito.mock(LazyQueryContainerFactory.class);
	private final RefreshableContainer refreshableContainer = Mockito.mock(RefreshableContainer.class);
	private final ItemContainerFactory itemContainerFactory = Mockito.mock(ItemContainerFactory.class);
	private final Item item = Mockito.mock(Item.class);
	
	
	@SuppressWarnings("unchecked")
	private final  Converter<Subject, Item> converter = Mockito.mock(Converter.class);
	
	private final EventFascadeProxyFactory eventFascadeProxyFactory = Mockito.mock(EventFascadeProxyFactory.class);
	private final  SubjectEventFascade subjectEventFascade = Mockito.mock(SubjectEventFascade.class);
	
	@SuppressWarnings("unchecked")
	@Before	
	public final void setup() {
		Mockito.when(eventFascadeProxyFactory.createProxy(SubjectEventFascade.class)).thenReturn(subjectEventFascade);
		Mockito.when(customerService.customer(Mockito.any(Optional.class))).thenReturn(customer);
		Mockito.when(lazyQueryContainerFactory.create(SubjectCols.Id, converter, EventType.CountPaging, EventType.ListPaging)).thenReturn(refreshableContainer);
		Mockito.when(itemContainerFactory.create(SubjectCols.class)).thenReturn(item);
		ReflectionTestUtils.setField(subjectModels, "customerService", customerService);
		ReflectionTestUtils.setField(subjectModels, "lazyQueryContainerFactory", lazyQueryContainerFactory);
		ReflectionTestUtils.setField(subjectModels, "itemContainerFactory", itemContainerFactory);
		ReflectionTestUtils.setField(subjectModels, "subjectToItemConverter", converter);
		ReflectionTestUtils.setField(subjectModels, "eventFascadeProxyFactory", eventFascadeProxyFactory);
	}
	
	@Test
	public final void subjectModel() {
		
		
		Assert.assertTrue(subjectModels.subjectModel() instanceof SubjectModelImpl);
		
		Assert.assertEquals(subjectEventFascade, ReflectionTestUtils.getField(subjectModels.subjectModel(), SUBJECT_EVENT_FASCADE_FIELD));
	}
	
	@Test
	public final void  userModel() {
		final UserModel userModel = subjectModels.userModel();
		Assert.assertEquals(customer, userModel.getCustomer());
	}
	
	@Test
	public final void  itemToSubjectConverter() {
		final Converter<Item, Subject> result = subjectModels.itemToSubjectConverter();
		Assert.assertTrue(result instanceof ItemToDomainConverterImpl);
		Assert.assertEquals(SubjectImpl.class, ReflectionTestUtils.getField(result, "clazz"));
		Assert.assertArrayEquals(SubjectCols.values(), (Object[]) ReflectionTestUtils.getField(result, "cols"));
	}
	
	@Test
	public final void  subjectLazyQueryContainer() {
		Assert.assertEquals(refreshableContainer, subjectModels.subjectLazyQueryContainer());
	}
	
	@Test
	public final void subjectItemContainer() {
		Assert.assertEquals(item, subjectModels.subjectItemContainer());
	}

}
