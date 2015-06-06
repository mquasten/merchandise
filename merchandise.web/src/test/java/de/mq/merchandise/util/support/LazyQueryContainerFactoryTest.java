package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ReflectionUtils;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Table;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.LazyQueryContainerFactory;

public class LazyQueryContainerFactoryTest {

	public static final String SUBJECT_DESCRIPTION = "Pets for you";
	public static final String SUBJECT_NAME = "PetStore";
	public static final Subject SUBJECT = Mockito.mock(Subject.class);
	public static final Long ID = 19680528L;
	
	public static final String LIST_PAGING_EVENT = "listPaging";
	public static final String COUNT_PAGING_EVENT = "countPaging";

	@SuppressWarnings("unchecked")
	private final Converter<Subject, Item> converter = Mockito.mock(Converter.class);

	private final EventAnnotationOperations eventAnnotationOperations = Mockito.mock(EventAnnotationOperations.class);
	
	private final BeanContainerOperations beanContainerOperations = Mockito.mock(BeanContainerOperations.class);
	
	private final LazyQueryContainerFactory lazyQueryContainerFactory = new SimpleReadOnlyLazyQueryContainerFactoryImpl(eventAnnotationOperations, beanContainerOperations);
	
	private final EventFascadeTest eventFascade = Mockito.mock(EventFascadeTest.class) ; 


	private final ArgumentCaptor<ResultNavigation> resultNavigationCaptor = ArgumentCaptor.forClass(ResultNavigation.class);


	Collection<Subject> subjects = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {

		subjects.add(SUBJECT);

		final Item item = new PropertysetItem();
		item.addItemProperty(TestConstants.SUBJECT_COLS_ID, new ObjectProperty<>(ID));
		item.addItemProperty(TestConstants.SUBJECT_COLS_NAME, new ObjectProperty<>(SUBJECT_NAME));
		item.addItemProperty(TestConstants.SUBJECT_COLS_DESC, new ObjectProperty<>(SUBJECT_DESCRIPTION));

		Mockito.when(converter.convert(SUBJECT)).thenReturn(item);
		
		final Method countMethod = ReflectionUtils.findMethod(eventFascade.getClass(), "countSubjects");
		final Method listMethod = ReflectionUtils.findMethod(eventFascade.getClass(), "subjects", ResultNavigation.class);
		
		
		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(countMethod)).thenReturn(true);
		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(listMethod)).thenReturn(true);
		
		Mockito.when(eventAnnotationOperations.valueFromAnnotation(countMethod)).thenReturn( COUNT_PAGING_EVENT);
		Mockito.when(eventAnnotationOperations.valueFromAnnotation(listMethod)).thenReturn(LIST_PAGING_EVENT);

		Mockito.when(eventFascade.countSubjects()).thenReturn(1L);
		Mockito.when(eventFascade.subjects(Mockito.any(ResultNavigation.class))).thenReturn(subjects);
		
		Mockito.doAnswer(i -> { 
			final List<Object> results = new ArrayList<>();
			final Map<?,?>  fuck =  (Map<?,?>) i.getArguments()[0];
			
			Arrays.asList((Class[])i.getArguments()[1]).stream().filter(c -> fuck.containsKey(c)).forEach(c -> results.add(fuck.get(c)));
		
			return results.toArray(new Object[results.size()]);
		}).when(beanContainerOperations).resolveMandatoryBeansFromDefaultOrContainer(Mockito.anyMap(), Mockito.any(Class[].class));
		

	}

	

	@Test
	public final void create() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter,  eventFascade, COUNT_PAGING_EVENT, LIST_PAGING_EVENT);
		Assert.assertEquals(1, container.getItemIds().size());

		Assert.assertEquals(ID, container.getItemIds().stream().findFirst().get());
		final Item item = container.getItem(ID);
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(SUBJECT_DESCRIPTION, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());

		
		
		Mockito.verify(eventFascade).subjects(resultNavigationCaptor.capture());
		
		Mockito.verify(eventFascade).countSubjects();
		
		
		Assert.assertEquals(0, resultNavigationCaptor.getValue().firstRow());
		Assert.assertEquals(1, resultNavigationCaptor.getValue().pageSize());
		Assert.assertEquals(1, resultNavigationCaptor.getValue().orders().size());
		Assert.assertEquals(Direction.ASC, resultNavigationCaptor.getValue().orders().stream().findFirst().get().getDirection());
		Assert.assertEquals(TestConstants.SUBJECT_COLS_ID.name().toLowerCase(), resultNavigationCaptor.getValue().orders().stream().findFirst().get().getProperty());

	}

	@Test
	public final void sort() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, eventFascade, COUNT_PAGING_EVENT, LIST_PAGING_EVENT);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setSortContainerPropertyId(TestConstants.SUBJECT_COLS_NAME);
		table.setSortAscending(false);
		
		Mockito.verify(eventFascade,Mockito.times(2) ).subjects(resultNavigationCaptor.capture());
		
		Mockito.verify(eventFascade, Mockito.times(2)).countSubjects();

		Assert.assertEquals(2, resultNavigationCaptor.getValue().orders().size());

		Assert.assertEquals(new Order(Direction.DESC, String.format("COALESCE(%s, '')", TestConstants.SUBJECT_COLS_NAME.name().toLowerCase())), resultNavigationCaptor.getValue().orders().get(0));

		Assert.assertEquals(new Order(Direction.ASC, TestConstants.SUBJECT_COLS_ID.name().toLowerCase()), resultNavigationCaptor.getValue().orders().get(1));

		table.setSortAscending(true);

		Assert.assertEquals(2, resultNavigationCaptor.getValue().orders().size());

		Assert.assertEquals(new Order(Direction.ASC, String.format("COALESCE(%s, '')", TestConstants.SUBJECT_COLS_NAME.name().toLowerCase())), resultNavigationCaptor.getValue().orders().get(0));

		Assert.assertEquals(new Order(Direction.ASC, TestConstants.SUBJECT_COLS_ID.name().toLowerCase()), resultNavigationCaptor.getValue().orders().get(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void remove() {

		final LazyQueryContainer container = (LazyQueryContainer) lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, eventFascade, COUNT_PAGING_EVENT, LIST_PAGING_EVENT);
		container.removeAllItems();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void add() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, eventFascade, COUNT_PAGING_EVENT, LIST_PAGING_EVENT);

		Assert.assertNotNull(container.addItem());

		container.commit();
	}
	
}
	interface   EventFascadeTest {
		
		abstract Number countSubjects();

		abstract Collection<Subject> subjects(final ResultNavigation paging);

}
