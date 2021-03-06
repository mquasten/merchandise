package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;









import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.util.ComponentTestHelper;
import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.util.event.Observer;

public class SubjectViewTest {

	private static final long ID = 19680528L;

	private static final String VALUE_FIELD = "value";

	private static final String CONDITION_QUALITY = "Quality";

	private static final String CONDITION_TYPE_FIELD = SubjectViewImpl.I18N_CONDITION_FIELD_PREFIX + StringUtils.uncapitalize(ConditionCols.ConditionType.name());
	
	private static final String CONDITION_DATA_TYPE_FIELD = SubjectViewImpl.I18N_CONDITION_FIELD_PREFIX + StringUtils.uncapitalize(ConditionCols.DataType.name());
	@SuppressWarnings("unchecked")
	private final Converter<Item, Condition> itemToConditionConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Item, Subject> itemToSubjectConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Subject, Item> subjectToItemConverter = Mockito.mock(Converter.class);
	private final RefreshableContainer lazyQueryContainer = Mockito.mock(RefreshableContainer.class);
	private final Item subjectItem = Mockito.mock(Item.class);
	private final Item subjectEditItem = Mockito.mock(Item.class);
	private final SubjectModel subjectModel = Mockito.mock(SubjectModel.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	@SuppressWarnings("unchecked")
	private final Converter<Condition, Item> conditionToItemConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Collection<Condition>, Container> conditionToContainerConverter = Mockito.mock(Converter.class);
	
	private MainMenuBarView mainMenuBarView = Mockito.mock(MainMenuBarView.class);

	private ValidationUtil validationUtil = Mockito.mock(ValidationUtil.class);
	private final SubjectViewImpl subjectView = new SubjectViewImpl(itemToSubjectConverter, subjectToItemConverter, lazyQueryContainer, subjectItem, subjectModel, userModel, messageSource, conditionToContainerConverter, conditionToItemConverter, itemToConditionConverter, validationUtil, mainMenuBarView);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final ArgumentCaptor<Observer<UserModel.EventType>> localChangedObserverCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Observer.class);

	private final ArgumentCaptor<UserModel.EventType> localChangedTypeCapture = ArgumentCaptor.forClass(UserModel.EventType.class);

	private final Map<EventType, Observer<EventType>> observers = new HashMap<>();

	private final Map<String, Component> components = new HashMap<>();

	private Map<ConditionCols, Collection<?>> conditionValues = new HashMap<>();

	private final Condition condition = Mockito.mock(Condition.class);
	private final Item conditionItem = Mockito.mock(Item.class);
	
	final Subject subject = Mockito.mock(Subject.class);
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before()
	public final void setup() {

		conditionValues.put(ConditionCols.ConditionType, new ArrayList<>());
		conditionValues.put(ConditionCols.DataType, new ArrayList<>());

		Mockito.when(subjectModel.getConditionValues()).thenReturn(conditionValues);

		Mockito.when(subjectModel.getCondition()).thenReturn(Optional.of(condition));
		Mockito.when(conditionToItemConverter.convert(Mockito.any(Condition.class))).thenReturn(conditionItem);

		final Property<?> conditionTypeProperty = Mockito.mock(Property.class);
		final Property<?> dataTypeProperty = Mockito.mock(Property.class);

		Mockito.when(conditionItem.getItemProperty(ConditionCols.ConditionType)).thenReturn(conditionTypeProperty);
		Mockito.when(conditionItem.getItemProperty(ConditionCols.DataType)).thenReturn(dataTypeProperty);
		// Mockito.when(conditionItem.getItemProperty(ConditionCols.Id)).thenReturn(conditionIdProperty);

	
		Mockito.when(subjectModel.getSubject()).thenReturn(Optional.of(subject));

		final Property<?> idProperty = Mockito.mock(Property.class);
		final Property<?> nameProperty = Mockito.mock(Property.class);
		final Property<?> descriptionProperty = Mockito.mock(Property.class);
		Mockito.when(subjectItem.getItemProperty(SubjectCols.Id)).thenReturn(idProperty);
		Mockito.when(subjectItem.getItemProperty(SubjectCols.Name)).thenReturn(nameProperty);
		Mockito.when(subjectItem.getItemProperty(SubjectCols.Description)).thenReturn(descriptionProperty);

		final Property<?> namePropertyEdit = Mockito.mock(Property.class);
		Mockito.when(subjectEditItem.getItemProperty(SubjectCols.Name)).thenReturn(namePropertyEdit);

		final Property<?> descriptionPropertyEdit = Mockito.mock(Property.class);
		Mockito.when(subjectEditItem.getItemProperty(SubjectCols.Description)).thenReturn(descriptionPropertyEdit);

		Mockito.when(lazyQueryContainer.getContainerPropertyIds()).thenReturn((Collection) Arrays.asList(SubjectCols.values()));

		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);

		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SAVE_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SAVE_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_DELETE_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_DELETE_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_CONDITION_DELETE_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_CONDITION_DELETE_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_NEW_CONDITION_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_NEW_CONDITION_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_NEW_BUTTON, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_NEW_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE);
		
		Mockito.when(messageSource.getMessage(CONDITION_DATA_TYPE_FIELD, null, Locale.GERMAN)).thenReturn(CONDITION_DATA_TYPE_FIELD);
		
		
		
		Mockito.when(messageSource.getMessage(CONDITION_TYPE_FIELD, null, Locale.GERMAN)).thenReturn(CONDITION_TYPE_FIELD);
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).forEach(col -> Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name()), null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name())));

		Mockito.doAnswer(i -> {
			observers.put((EventType) i.getArguments()[1], (Observer<EventType>) i.getArguments()[0]);
			return null;
		}).when(subjectModel).register(Mockito.any(Observer.class), Mockito.any(SubjectModel.EventType.class));

		 
		final Container conditionContainer = Mockito.mock(Container.class);
		Mockito.when(subjectToItemConverter.convert(Mockito.any(Subject.class))).thenReturn(subjectItem);
	
		Mockito.when(conditionContainer.getContainerPropertyIds()).thenReturn((Collection) Arrays.asList(ConditionCols.values()));
		Mockito.when(conditionToContainerConverter.convert(Mockito.anyCollection())).thenReturn(conditionContainer);

		Mockito.when(itemToConditionConverter.convert(conditionItem)).thenReturn(condition);
		subjectView.init();

		Mockito.verify(userModel).register(localChangedObserverCapture.capture(), localChangedTypeCapture.capture());

		Assert.assertNotNull(localChangedObserverCapture.getValue());
		Assert.assertEquals(UserModel.EventType.LocaleChanged, localChangedTypeCapture.getValue());
		localChangedObserverCapture.getValue().process(localChangedTypeCapture.getValue());

		Assert.assertEquals(3, observers.size());
		Assert.assertTrue(observers.containsKey(EventType.SearchCriteriaChanged));
		Assert.assertTrue(observers.containsKey(EventType.SubjectChanged));
		Mockito.verify(subjectModel).register(observers.get(EventType.SearchCriteriaChanged), EventType.SearchCriteriaChanged);
		Mockito.verify(subjectModel).register(observers.get(EventType.SubjectChanged), EventType.SubjectChanged);

		components.clear();
		ComponentTestHelper.components(subjectView, components);

	}

	@Test
	public final void init() {

		final Table table = (Table) components.get(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE, table.getCaption());
		Arrays.asList(SubjectCols.values()).stream().filter(value -> value.visible()).forEach(value -> Assert.assertTrue(Arrays.asList(table.getColumnHeaders()).contains((SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(value.name())))));

		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME, components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME) instanceof TextField);
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION, components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION) instanceof TextField);
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE, components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE) instanceof Panel);
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON, components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON) instanceof Button);

	}

	@Test
	public final void refreshSubjectContainer() {
		observers.get(EventType.SearchCriteriaChanged).process(EventType.SearchCriteriaChanged);
		// searchChangedObserverCapture.getValue().process(searchChangedTypeCapture.getValue());
		Mockito.verify(lazyQueryContainer, Mockito.times(1)).refresh();
	}

	@Test
	public final void search() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenReturn(subject);
		executeListener();
		Mockito.verify(subjectModel, Mockito.times(1)).setSerachCriteria(subject);
	}

	private void executeListener() {
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON);

		@SuppressWarnings({ "unchecked" })
		final Optional<ClickListener> listener = (Optional<ClickListener>) button.getListeners(ClickEvent.class).stream().findFirst();
		Assert.assertTrue(listener.isPresent());
		final ClickEvent event = Mockito.mock(ClickEvent.class);
		listener.get().buttonClick(event);
	}

	@Test(expected = IllegalStateException.class)
	public final void searchSucks() {
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenThrow(new RuntimeException("Don' t worry only for test"));
		executeListener();
	}

	@Test
	public final void enter() {
		final ViewChangeEvent event = Mockito.mock(ViewChangeEvent.class);
		subjectView.enter(event);
	}

	@Test
	public final void saveSubjectButton() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenReturn(subject);
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_SAVE_BUTTON);
		Assert.assertNotNull(button);
		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		final ArgumentCaptor<Subject> subjectCaptor = ArgumentCaptor.forClass(Subject.class);
		final ArgumentCaptor<FieldGroup> fieldGroupCaptor = ArgumentCaptor.forClass(FieldGroup.class);
		final ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);
		Mockito.when(validationUtil.validate(subjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));

		Assert.assertEquals(subject, subjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		Assert.assertEquals(subjectItem, fieldGroupCaptor.getValue().getItemDataSource());

		Mockito.verify(subjectModel).save(subject);
		Mockito.verify(lazyQueryContainer).refresh();
		Mockito.verify(validationUtil).validate(subject, fieldGroupCaptor.getValue(), userModel.getLocale());
	}

	@Test
	public final void saveSubjectButtonValidationSucks() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenReturn(subject);
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_SAVE_BUTTON);
		Assert.assertNotNull(button);
		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));

		Mockito.verify(validationUtil).validate(Mockito.any(Subject.class), Mockito.any(FieldGroup.class), Mockito.any(Locale.class));
		Mockito.verify(subjectModel, Mockito.times(0)).save(subject);
		Mockito.verify(lazyQueryContainer, Mockito.times(0)).refresh();

	}

	@Test
	public final void commitSubjectField() throws CommitException {
		final FieldGroup fieldGroup = Mockito.mock(FieldGroup.class);
		Assert.assertTrue(subjectView.commitFields(fieldGroup));
		Mockito.doThrow(new CommitException("Don't worry only for test")).when(fieldGroup).commit();
		Assert.assertFalse(subjectView.commitFields(fieldGroup));
	}

	@Test
	public final void deleteSubjectButton() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenReturn(subject);
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_DELETE_BUTTON);
		Assert.assertNotNull(button);

		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));

		Mockito.verify(subjectModel).delete(subject);
		Mockito.verify(lazyQueryContainer).refresh();
	}

	@Test
	public final void deleteConditionButton() {
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_CONDITION_DELETE_BUTTON);
		Assert.assertNotNull(button);

		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));
		Mockito.verify(subjectModel).delete(condition);
	}

	@Test
	public final void saveConditionButton() {
		final ArgumentCaptor<Condition> conditionCaptor = ArgumentCaptor.forClass(Condition.class);
		final ArgumentCaptor<FieldGroup> fieldGroupCaptor = ArgumentCaptor.forClass(FieldGroup.class);
		final ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);

		Mockito.when(validationUtil.validate(conditionCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);

		final Button button = (Button) components.get(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON);
		Assert.assertNotNull(button);

		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));

		Assert.assertEquals(condition, conditionCaptor.getValue());
		Assert.assertEquals(conditionItem, fieldGroupCaptor.getValue().getItemDataSource());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());

		Mockito.verify(subjectModel).save(condition);
	}

	@Test
	public final void saveConditionButtonValidationSucks() {

		final Button button = (Button) components.get(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON);
		Assert.assertNotNull(button);

		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));

		Mockito.verify(validationUtil).validate(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.verify(subjectModel, Mockito.times(0)).save(condition);
	}
	
	@Test
	public final void saveConditionButtonValidationConditionValitation() {
		Mockito.when(validationUtil.validate(Mockito.any(Condition.class), Mockito.any(FieldGroup.class), Mockito.any(Locale.class))).thenReturn(true);
		String errorMessage = "condition already exists";
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_CONDITION_EXISTS, new String[] {condition.conditionType()}, userModel.getLocale())).thenReturn(errorMessage);
		AbstractComponent box = (AbstractComponent) components.get(CONDITION_TYPE_FIELD);
		Assert.assertNull(box.getComponentError());
	
		Mockito.when(condition.id()).thenReturn(Optional.empty());
		final Button button = (Button) components.get(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON);
		Assert.assertNotNull(button);

		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());

		Mockito.when(subjectModel.hasCondition(condition)).thenReturn(true);
		
		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));
	   
		Assert.assertEquals(errorMessage, box.getComponentError().toString());
		
		Mockito.verify(subjectModel,Mockito.times(0)).save(condition);
		
		Mockito.when(condition.id()).thenReturn(Optional.of(19680529L));
		box.setComponentError(null);
		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(Mockito.mock(ClickEvent.class));
		
		Assert.assertNull(box.getComponentError());
		Mockito.verify(subjectModel,Mockito.times(1)).save(condition);
	}
	
	@Test
	public final void subjectChangedObserver() {
		Mockito.when(subject.id()).thenReturn(Optional.of(19680528L));
	
		
		final Button newButton = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_NEW_BUTTON);
		final Button deleteButton = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_DELETE_BUTTON);
		final Layout conditionTableLayout = (Layout) components.get(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE).getParent();
		final Button saveButton = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_SAVE_BUTTON);
		
		Assert.assertFalse(conditionTableLayout.isVisible());
		Assert.assertFalse(newButton.isEnabled());
		Assert.assertFalse(deleteButton.isEnabled());
		Assert.assertEquals(subjectView.newIcon, saveButton.getIcon());
		
		observers.get(EventType.SubjectChanged).process(EventType.SubjectChanged);
		
	
		Assert.assertTrue(newButton.isEnabled());
		Assert.assertTrue(deleteButton.isEnabled());
		Assert.assertTrue(conditionTableLayout.isVisible());
		Assert.assertEquals(subjectView.editIcon, saveButton.getIcon());
		
		Mockito.when(subject.id()).thenReturn(Optional.empty());
		
		observers.get(EventType.SubjectChanged).process(EventType.SubjectChanged);
		
		Assert.assertFalse(conditionTableLayout.isVisible());
		Assert.assertFalse(newButton.isEnabled());
		Assert.assertFalse(deleteButton.isEnabled());
		Assert.assertEquals(subjectView.newIcon, saveButton.getIcon());
		
	}
	
	@Test
	public final void conditionChangedObserver() {
		Mockito.when(condition.id()).thenReturn(Optional.of(19680528L));
		final Map<ConditionCols, Collection<?>> values = new HashMap<>();
		values.put(ConditionCols.DataType, Arrays.asList(ConditionDataType.values()));
		values.put(ConditionCols.ConditionType, Arrays.asList(CONDITION_QUALITY));
		Mockito.when(subjectModel.getConditionValues()).thenReturn(values);
		
		
		
		final Button newConditionButton = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_NEW_CONDITION_BUTTON);
		final Button deleteConditionButton = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_CONDITION_DELETE_BUTTON);
		final Button saveConditionButton = (Button) components.get(SubjectViewImpl.I18N_CONDITION_SAVE_BUTTON);
		final ComboBox conditionDataTypes = (ComboBox) components.get(CONDITION_DATA_TYPE_FIELD);
		final ComboBox conditionTypes = (ComboBox) components.get(CONDITION_TYPE_FIELD);
		
		Assert.assertTrue(conditionDataTypes.getItemIds().isEmpty());
		Assert.assertTrue(conditionTypes.getItemIds().isEmpty());
		
		Assert.assertFalse(newConditionButton.isEnabled());
		Assert.assertFalse(deleteConditionButton.isEnabled());
		Assert.assertEquals(subjectView.newIcon, saveConditionButton.getIcon());
		Assert.assertTrue(conditionTypes.isEnabled());
		
		observers.get(EventType.ConditionChanged).process(EventType.ConditionChanged);
		
		Assert.assertEquals(Arrays.asList(ConditionDataType.values()), conditionDataTypes.getItemIds());
		Assert.assertTrue(conditionTypes.getItemIds().stream().findFirst().isPresent());
		Assert.assertEquals(CONDITION_QUALITY, conditionTypes.getItemIds().stream().findFirst().get());
		
		Assert.assertFalse(conditionTypes.isEnabled());
		Assert.assertTrue(newConditionButton.isEnabled());
		Assert.assertTrue(deleteConditionButton.isEnabled());
		Assert.assertEquals(subjectView.editIcon, saveConditionButton.getIcon());
		
		Mockito.when(condition.id()).thenReturn(Optional.empty());
		observers.get(EventType.ConditionChanged).process(EventType.ConditionChanged);
		
		Assert.assertTrue(conditionTypes.isEnabled());
		Assert.assertFalse(newConditionButton.isEnabled());
		Assert.assertFalse(deleteConditionButton.isEnabled());
		Assert.assertEquals(subjectView.newIcon, saveConditionButton.getIcon());
		
	}
	
	@Test
	public final void newSubjectClickListener() {
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_NEW_BUTTON);
		final Table subjectList = (Table) components.get(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		ReflectionTestUtils.setField(subjectList, VALUE_FIELD, ID);
		
		Assert.assertEquals(ID, subjectList.getValue());
		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());
		
		final ClickEvent event = Mockito.mock(ClickEvent.class);
		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(event);
		
		Assert.assertNull(subjectList.getValue());
	}
	
	@Test
	public final void subjectChangedListener() {
		final Table subjectList = (Table) components.get(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		Assert.assertTrue(subjectList.getListeners(ValueChangeEvent.class).stream().findFirst().isPresent());
		final ValueChangeEvent event = Mockito.mock(ValueChangeEvent.class);
		@SuppressWarnings("unchecked")
		final Property<Long> property = Mockito.mock(Property.class);
		Mockito.when(property.getValue()).thenReturn(ID);
		Mockito.when(event.getProperty()).thenReturn(property);
		((ValueChangeListener) subjectList.getListeners(ValueChangeEvent.class).stream().findFirst().get()).valueChange(event);
	   
		Mockito.verify( subjectModel).setSubjectId(ID);
	}
	
	@Test
	public final void newConditionButtonClickListener() {
		final Button button = (Button) components.get(SubjectViewImpl.I18N_SUBJECT_NEW_CONDITION_BUTTON);
		final Table conditionTable = (Table) components.get(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE);
		
		ReflectionTestUtils.setField(conditionTable, VALUE_FIELD, ID);
		
		Assert.assertEquals(ID, conditionTable.getValue());
		Assert.assertTrue(button.getListeners(ClickEvent.class).stream().findFirst().isPresent());
		
		final ClickEvent event = Mockito.mock(ClickEvent.class);
		((ClickListener) button.getListeners(ClickEvent.class).stream().findFirst().get()).buttonClick(event);
		Assert.assertNull(conditionTable.getValue());
		
		
	}
	
	@Test
	public final void conditionChangedListener() {
		
		
		final Table conditionTable = (Table) components.get(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE);
		
		final IndexedContainer indexedContainer = Mockito.mock(IndexedContainer.class);
		final Item item = Mockito.mock(Item.class);
		@SuppressWarnings("unchecked")
		final Property<Long> idProperty = Mockito.mock(Property.class) ;
		Mockito.when(idProperty.getValue()).thenReturn(ID);
		Mockito.when(item.getItemProperty(ConditionCols.Id)).thenReturn(idProperty);
		Mockito.when(indexedContainer.getItem(4711)).thenReturn(item);
		conditionTable.setContainerDataSource(indexedContainer);
		
		Assert.assertTrue(conditionTable.getListeners(ValueChangeEvent.class).stream().findFirst().isPresent());
		final ValueChangeEvent event = Mockito.mock(ValueChangeEvent.class);
		@SuppressWarnings("unchecked")
		final Property<Integer> property = Mockito.mock(Property.class);
		Mockito.when(property.getValue()).thenReturn(4711);
		
		Mockito.when(event.getProperty()).thenReturn(property);
		((ValueChangeListener) conditionTable.getListeners(ValueChangeEvent.class).stream().findFirst().get()).valueChange(event);
	   
		Mockito.verify(subjectModel).setConditionId(ID);
	}
	
	@Test
	public final void conditionChangedListenerNull() {
		final Table conditionTable = (Table) components.get(SubjectViewImpl.I18N_CONDITION_TABLE_HEADLINE);
		Assert.assertTrue(conditionTable.getListeners(ValueChangeEvent.class).stream().findFirst().isPresent());
		final ValueChangeEvent event = Mockito.mock(ValueChangeEvent.class);
		@SuppressWarnings("unchecked")
		final Property<Integer> property = Mockito.mock(Property.class);
		Mockito.when(event.getProperty()).thenReturn(property);
		
		((ValueChangeListener) conditionTable.getListeners(ValueChangeEvent.class).stream().findFirst().get()).valueChange(event);
		
		Mockito.verify(subjectModel).setConditionId(null);
		
	}

}
