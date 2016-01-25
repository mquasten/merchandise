package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.ComponentTestHelper;
import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;
import de.mq.util.event.Observer;

public class CommercialSubjectViewTest {

	private final CommercialSubjectModel commercialSubjectModel = Mockito.mock(CommercialSubjectModel.class);

	private final UserModel userModel = Mockito.mock(UserModel.class);

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final ViewNav viewNav = Mockito.mock(ViewNav.class);

	private final MainMenuBarView mainMenuBarView = Mockito.mock(MainMenuBarView.class);

	private final RefreshableContainer lazyQueryContainer = Mockito.mock(RefreshableContainer.class);

	private final Item commercialSubjectSearchItem = Mockito.mock(Item.class);
	private final ValidationUtil validationUtil = Mockito.mock(ValidationUtil.class);

	@SuppressWarnings("unchecked")
	private final Converter<Item, CommercialSubject> itemToCommercialSubjectConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<CommercialSubject, Item> commercialSubjectToItemConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<Collection<Subject>, Container> entriesToConatainerConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<CommercialSubjectItem, Item> commercialSubjectItemConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Collection<CommercialSubjectItem>, Container> commercialSubjectItemToContainerConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Collection<Condition>, Container> conditionToContainerConverter = Mockito.mock(Converter.class);
	final Item conditionValueItem = Mockito.mock(Item.class);
	@SuppressWarnings("unchecked")
	private final Mapper<Item, CommercialSubjectModel> itemIntoCommercialSubjectModel = Mockito.mock(Mapper.class);

	@SuppressWarnings("unchecked")
	private final Converter<Collection<?>, Container> inputValuesConverter = Mockito.mock(Converter.class);

	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);

	private final CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);
	
	private final Map<EventType, Observer<EventType>> observers = new HashMap<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final ArgumentCaptor<Observer<UserModel.EventType>> localChangedObserverCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Observer.class);

	private final ArgumentCaptor<UserModel.EventType> localChangedTypeCapture = ArgumentCaptor.forClass(UserModel.EventType.class);
	
	final Map<String, Component> components = new HashMap<>();
	private final CommercialSubjectViewImpl view = new CommercialSubjectViewImpl(commercialSubjectModel, userModel, messageSource, viewNav, mainMenuBarView, lazyQueryContainer, commercialSubjectSearchItem, itemToCommercialSubjectConverter, validationUtil, commercialSubjectToItemConverter, entriesToConatainerConverter, commercialSubjectItemConverter, itemToCommercialSubjectItemConverter, commercialSubjectItemToContainerConverter,

	conditionToContainerConverter, conditionValueItem,

	itemIntoCommercialSubjectModel,

	inputValuesConverter);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before
	public final void setup() {

		final Property<CommercialSubjectCols> commercialSubjectSearchItemPropertyName = Mockito.mock(Property.class);
		Mockito.when(commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.Name)).thenReturn(commercialSubjectSearchItemPropertyName);

		final Property<CommercialSubjectCols> commercialSubjectSearchItemPropertyItemName = Mockito.mock(Property.class);
		Mockito.when(commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.ItemName)).thenReturn(commercialSubjectSearchItemPropertyItemName);

		Mockito.when(lazyQueryContainer.getContainerPropertyIds()).thenReturn((Collection) (Arrays.asList(CommercialSubjectCols.Name)));

		Mockito.when(commercialSubjectModel.getCommercialSubject()).thenReturn(Optional.of(commercialSubject));

		Mockito.when(commercialSubjectModel.getCommercialSubjectItem()).thenReturn(Optional.of(commercialSubjectItem));

		final Property<ConditionValueCols> valuePropertyInputValue = Mockito.mock(Property.class);
		Mockito.when(conditionValueItem.getItemProperty(ConditionValueCols.InputValue)).thenReturn(valuePropertyInputValue);
		
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION,new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_TRUE, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_TRUE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_FALSE, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_FALSE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONVERSION_ERROR, new Object[] {},  Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONVERSION_ERROR);
		
		
		
		
		Mockito.doAnswer(i -> {
			observers.put((EventType) i.getArguments()[1], (Observer<EventType>) i.getArguments()[0]);
			return null;
		}).when(commercialSubjectModel).register(Mockito.any(Observer.class), Mockito.any(CommercialSubjectModel.EventType.class));

		
		view.init();
	
		 
		Mockito.verify(userModel).register(localChangedObserverCapture.capture(), localChangedTypeCapture.capture());
		
		localChangedObserverCapture.getValue().process(localChangedTypeCapture.getValue());
		
	}

	@Test
	public final void init() {
	
		
	
		ComponentTestHelper.components(view, components);
		
	}

}
