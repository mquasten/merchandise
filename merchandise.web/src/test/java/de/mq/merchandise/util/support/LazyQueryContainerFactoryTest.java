package de.mq.merchandise.util.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Table;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.Event;
import de.mq.merchandise.util.LazyQueryContainerFactory;

public class LazyQueryContainerFactoryTest {

	public static final String SUBJECT_DESCRIPTION = "Pets for you";
	public static final String SUBJECT_NAME = "PetStore";
	public static final Subject SUBJECT = Mockito.mock(Subject.class);
	public static final Long ID = 19680528L;

	@SuppressWarnings("unchecked")
	private final Converter<Subject, Item> converter = Mockito.mock(Converter.class);
	private final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
	private final LazyQueryContainerFactory lazyQueryContainerFactory = new SimpleReadOnlyLazyQueryContainerFactoryImpl(applicationEventPublisher);

	@SuppressWarnings("unused")
	private final SubjectModel subjectModel = Mockito.mock(SubjectModel.class);

	private ResultNavigation resultNavigation;

	Collection<Subject> subjects = new ArrayList<>();

	@Before
	public void setup() {

		subjects.add(SUBJECT);

		final Item item = new PropertysetItem();
		item.addItemProperty(TestConstants.SUBJECT_COLS_ID, new ObjectProperty<>(ID));
		item.addItemProperty(TestConstants.SUBJECT_COLS_NAME, new ObjectProperty<>(SUBJECT_NAME));
		item.addItemProperty(TestConstants.SUBJECT_COLS_DESC, new ObjectProperty<>(SUBJECT_DESCRIPTION));

		Mockito.when(converter.convert(SUBJECT)).thenReturn(item);

		Mockito.doAnswer(i -> {

			final Event<EventType, Object> event = event(i);
			if (event.id().equals(SubjectModel.EventType.CountPaging)) {
				event.assign(1L);
			} else if (event.id().equals(SubjectModel.EventType.ListPaging)) {
				event.assign(subjects);

				final Map<Class<?>, Object> params = event.parameter();
				Assert.assertTrue(params.containsKey(ResultNavigation.class));
				resultNavigation = (ResultNavigation) params.get(ResultNavigation.class);
			}
			return event;
		}).when(applicationEventPublisher).publishEvent(Mockito.any(Event.class));

	}

	@SuppressWarnings("unchecked")
	private Event<EventType, Object> event(InvocationOnMock i) {
		return (Event<EventType, Object>) i.getArguments()[0];
	}

	@Test
	public final void create() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, EventType.CountPaging, EventType.ListPaging);
		Assert.assertEquals(1, container.getItemIds().size());

		Assert.assertEquals(ID, container.getItemIds().stream().findFirst().get());
		final Item item = container.getItem(ID);
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(SUBJECT_DESCRIPTION, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());

		Assert.assertEquals(0, resultNavigation.firstRow());
		Assert.assertEquals(1, resultNavigation.pageSize());
		Assert.assertEquals(1, resultNavigation.orders().size());
		Assert.assertEquals(Direction.ASC, resultNavigation.orders().stream().findFirst().get().getDirection());
		Assert.assertEquals(TestConstants.SUBJECT_COLS_ID.name().toLowerCase(), resultNavigation.orders().stream().findFirst().get().getProperty());

	}

	@Test
	public final void sort() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, EventType.CountPaging, EventType.ListPaging);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setSortContainerPropertyId(TestConstants.SUBJECT_COLS_NAME);
		table.setSortAscending(false);

		Assert.assertEquals(2, resultNavigation.orders().size());

		Assert.assertEquals(new Order(Direction.DESC, String.format("COALESCE(%s, '')", TestConstants.SUBJECT_COLS_NAME.name().toLowerCase())), resultNavigation.orders().get(0));

		Assert.assertEquals(new Order(Direction.ASC, TestConstants.SUBJECT_COLS_ID.name().toLowerCase()), resultNavigation.orders().get(1));

		table.setSortAscending(true);

		Assert.assertEquals(2, resultNavigation.orders().size());

		Assert.assertEquals(new Order(Direction.ASC, String.format("COALESCE(%s, '')", TestConstants.SUBJECT_COLS_NAME.name().toLowerCase())), resultNavigation.orders().get(0));

		Assert.assertEquals(new Order(Direction.ASC, TestConstants.SUBJECT_COLS_ID.name().toLowerCase()), resultNavigation.orders().get(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void remove() {

		final LazyQueryContainer container = (LazyQueryContainer) lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, EventType.CountPaging, EventType.ListPaging);
		container.removeAllItems();
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void add() {

		final RefreshableContainer container = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, converter, EventType.CountPaging, EventType.ListPaging);

		Assert.assertNotNull(container.addItem());

		container.commit();
	}

}
