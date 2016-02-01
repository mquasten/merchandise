package de.mq.merchandise.subject.support;

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
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

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

	private static final String CONDITION_VALUE = "HotScore";

	private static final long CONDITION_ID = 19680528L;

	private static final String COMMERCIAL_SUBJECT_NAME = "PetsStore";

	private final CommercialSubjectModel commercialSubjectModel = Mockito.mock(CommercialSubjectModel.class);

	private final UserModel userModel = Mockito.mock(UserModel.class);

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final ViewNav viewNav = Mockito.mock(ViewNav.class);

	private final MainMenuBarView mainMenuBarView = Mockito.mock(MainMenuBarView.class);

	private final RefreshableContainer lazyQueryContainer = Mockito.mock(RefreshableContainer.class);
	private Subject subject = Mockito.mock(Subject.class);
	
	private Condition condition = Mockito.mock(Condition.class);

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
	final Item itemToCommercialSubjectDatasource = Mockito.mock(Item.class);

	final Item itemFieldsDatasource = Mockito.mock(Item.class);

	private final ArgumentCaptor<CommercialSubject> commercialSubjectCaptor = ArgumentCaptor.forClass(CommercialSubject.class);
	private final ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);
	private final ArgumentCaptor<FieldGroup> fieldGroupCaptor = ArgumentCaptor.forClass(FieldGroup.class);

	private final ArgumentCaptor<CommercialSubjectItem> commercialSubjectItemCaptor = ArgumentCaptor.forClass(CommercialSubjectItem.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final ArgumentCaptor<Observer<UserModel.EventType>> localChangedObserverCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Observer.class);

	private final ArgumentCaptor<UserModel.EventType> localChangedTypeCapture = ArgumentCaptor.forClass(UserModel.EventType.class);

	private final ClickEvent clickEvent = Mockito.mock(ClickEvent.class);
	private final ValueChangeEvent valueChangeEvent = Mockito.mock(ValueChangeEvent.class);
	private final Map<String, Component> components = new HashMap<>();
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

		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_TRUE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_TRUE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_FALSE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_MANDATORY_BOX_FALSE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME);
		// Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX,
		// new Object[] {},
		// Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONVERSION_ERROR, new Object[] {ConditionDataType.IntegralNumber.name()}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONVERSION_ERROR);

		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX + CommercialSubjectItemCols.Name.name().toLowerCase(), new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectItemCols.Name.name());
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX + CommercialSubjectItemCols.Mandatory.name().toLowerCase(), new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectItemCols.Mandatory.name());
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX + CommercialSubjectItemCols.Subject.name().toLowerCase(), new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectItemCols.Subject.name());

		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE);
		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_DELETE, new Object[] {}, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_DELETE);

		Mockito.when(messageSource.getMessage(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE, new Object[] { ConditionDataType.String.name() }, Locale.GERMAN)).thenReturn(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);

		Mockito.doAnswer(i -> {
			observers.put((EventType) i.getArguments()[1], (Observer<EventType>) i.getArguments()[0]);
			return null;
		}).when(commercialSubjectModel).register(Mockito.any(Observer.class), Mockito.any(CommercialSubjectModel.EventType.class));

		final Property commercialSubjectNameProperty = Mockito.mock(Property.class);

		Mockito.when(commercialSubjectNameProperty.getValue()).thenReturn(COMMERCIAL_SUBJECT_NAME);
		Mockito.when(commercialSubjectNameProperty.getType()).thenReturn(String.class);
		Mockito.when(itemToCommercialSubjectDatasource.getItemProperty(CommercialSubjectCols.Name)).thenReturn(commercialSubjectNameProperty);

		Mockito.when(commercialSubjectToItemConverter.convert(Mockito.any())).thenReturn(itemToCommercialSubjectDatasource);

		final Property itemFieldsNameProperty = Mockito.mock(Property.class);
		final Property itemFieldsMandatoryProperty = Mockito.mock(Property.class);
		final Property itemFieldsSubjectProperty = Mockito.mock(Property.class);

		Mockito.when(itemFieldsDatasource.getItemProperty(CommercialSubjectItemCols.Name)).thenReturn(itemFieldsNameProperty);
		Mockito.when(itemFieldsDatasource.getItemProperty(CommercialSubjectItemCols.Mandatory)).thenReturn(itemFieldsMandatoryProperty);
		Mockito.when(itemFieldsDatasource.getItemProperty(CommercialSubjectItemCols.Subject)).thenReturn(itemFieldsSubjectProperty);

		Mockito.when(commercialSubjectItemConverter.convert(commercialSubjectItem)).thenReturn(itemFieldsDatasource);

		view.init();

		Mockito.verify(userModel).register(localChangedObserverCapture.capture(), localChangedTypeCapture.capture());

		localChangedObserverCapture.getValue().process(localChangedTypeCapture.getValue());

		ComponentTestHelper.components(view, components);
	}

	@Test
	public final void init() {

		final TextField searchName = (TextField) components.get(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME);
		Assert.assertEquals(CommercialSubjectViewImpl.I18_COMMERCIAL_SUBJECT_SEARCH_NAME, searchName.getCaption());
		Assert.assertTrue(searchName.isVisible());

		final TextField searchItem = (TextField) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME, searchItem.getCaption());
		Assert.assertTrue(visible(searchItem));

		final Button searchButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SEARCH, searchButton.getCaption());
		Assert.assertTrue(visible(searchButton));
		Assert.assertTrue(searchButton.isEnabled());

		final Table subjectList = (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_TABLE_NAME, subjectList.getCaption());
		Assert.assertTrue(subjectList.isVisible());

		final TextField editorNameField = (TextField) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME, editorNameField.getCaption());
		Assert.assertTrue(editorNameField.isVisible());

		final Button saveButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE, saveButton.getCaption());
		Assert.assertTrue(visible(saveButton));
		Assert.assertTrue(saveButton.isEnabled());
		Assert.assertEquals(view.newIcon, saveButton.getIcon());

		final Button newButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NEW, newButton.getCaption());
		Assert.assertTrue(visible(newButton));
		Assert.assertFalse(newButton.isEnabled());

		final Button deleteButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE, deleteButton.getCaption());

		Assert.assertTrue(visible(deleteButton));
		Assert.assertFalse(deleteButton.isEnabled());

		final TextField subjectItemNameField = (TextField) components.get(CommercialSubjectItemCols.Name.name());
		Assert.assertEquals(CommercialSubjectItemCols.Name.name(), subjectItemNameField.getCaption());
		Assert.assertFalse(visible(subjectItemNameField));

		final ComboBox subjectItemMandatoryField = (ComboBox) components.get(CommercialSubjectItemCols.Mandatory.name());
		Assert.assertEquals(CommercialSubjectItemCols.Mandatory.name(), subjectItemMandatoryField.getCaption());
		Assert.assertFalse(visible(subjectItemMandatoryField));

		final ComboBox subjectItemSubjectField = (ComboBox) components.get(CommercialSubjectItemCols.Subject.name());
		Assert.assertEquals(CommercialSubjectItemCols.Subject.name(), subjectItemSubjectField.getCaption());
		Assert.assertFalse(visible(subjectItemSubjectField));

		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON, saveItemButton.getCaption());
		Assert.assertFalse(visible(saveItemButton));
		Assert.assertTrue(saveItemButton.isEnabled());
		Assert.assertEquals(view.newIcon, saveItemButton.getIcon());

		final Button newItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_NEW_ITEM_BUTTON, newItemButton.getCaption());
		Assert.assertFalse(visible(newItemButton));
		Assert.assertFalse(newItemButton.isEnabled());

		final Button deleteItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON, deleteItemButton.getCaption());
		Assert.assertFalse(visible(deleteItemButton));
		Assert.assertFalse(deleteItemButton.isEnabled());

		final Table itemTable = (Table) components.get(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION, itemTable.getCaption());
		Assert.assertFalse(visible(itemTable));

		final ComboBox conditionBox = (ComboBox) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION, conditionBox.getCaption());
		Assert.assertFalse(visible(conditionBox));

		final TextField conditionValueField = (TextField) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE, conditionValueField.getCaption());
		Assert.assertFalse(visible(conditionValueField));

		final Button saveValueButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE, saveValueButton.getCaption());
		Assert.assertFalse(visible(saveValueButton));
		Assert.assertFalse(saveValueButton.isEnabled());
		Assert.assertEquals(view.newIcon, saveValueButton.getIcon());

		final Button deleteValueButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_DELETE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_DELETE, deleteValueButton.getCaption());
		Assert.assertFalse(visible(deleteValueButton));
		Assert.assertFalse(deleteValueButton.isEnabled());

		final Table valueTable = (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE, valueTable.getCaption());
		Assert.assertFalse(visible(valueTable));
	}

	public final boolean visible(Component parent) {
		if (!parent.isVisible()) {
			return false;
		}

		if (parent.getParent() == null) {
			return parent.isVisible();
		}

		return visible(parent.getParent());

	}

	@Test
	public final void saveButton() {

		Mockito.when(itemToCommercialSubjectConverter.convert(itemToCommercialSubjectDatasource)).thenReturn(commercialSubject);
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);

		final Button saveButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);

		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());
		listener.get().buttonClick(clickEvent);

		Mockito.verify(validationUtil).validate(Mockito.any(), Mockito.any(), Mockito.any());

		Assert.assertEquals(commercialSubject, commercialSubjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		Mockito.verify(commercialSubjectModel).save(commercialSubject);
		Mockito.verify(lazyQueryContainer).refresh();

	}

	@Test
	public final void saveButtonInvalid() {

		Mockito.when(itemToCommercialSubjectConverter.convert(itemToCommercialSubjectDatasource)).thenReturn(commercialSubject);
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(false);

		final Button saveButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);

		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());
		listener.get().buttonClick(clickEvent);

		Mockito.verify(validationUtil).validate(Mockito.any(), Mockito.any(), Mockito.any());

		Assert.assertEquals(commercialSubject, commercialSubjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		Mockito.verify(commercialSubjectModel, Mockito.never()).save(commercialSubject);
		Mockito.verify(lazyQueryContainer, Mockito.never()).refresh();

	}

	@Test(expected = IllegalStateException.class)
	public final void saveButtonCommitSucks() {

		final TextField editorNameField = (TextField) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_NAME);
		editorNameField.setValue(null);
		editorNameField.setRequired(true);

		final Button saveButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_SAVE);

		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());
		listener.get().buttonClick(clickEvent);

	}

	@Test
	public final void deleteButton() {
		Mockito.when(itemToCommercialSubjectConverter.convert(itemToCommercialSubjectDatasource)).thenReturn(commercialSubject);
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);

		final Button deleteButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_DELETE);
		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) deleteButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());
		listener.get().buttonClick(clickEvent);

		Mockito.verify(commercialSubjectModel).delete(commercialSubject);
		Mockito.verify(lazyQueryContainer).refresh();
	}

	@Test
	public final void saveItemButton() {
		final Table itemTable = prepareSaveItem();
		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON);

		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());

		Assert.assertEquals(0, itemTable.getVisibleColumns().length);
		listener.get().buttonClick(clickEvent);

		final TextField nameField = (TextField) components.get(CommercialSubjectItemCols.Name.name());

		Assert.assertEquals(commercialSubjectItem, commercialSubjectItemCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		Assert.assertEquals(nameField, fieldGroupCaptor.getValue().getField(CommercialSubjectItemCols.Name));
		Assert.assertEquals(nameField.getCaption(), fieldGroupCaptor.getValue().getField(CommercialSubjectItemCols.Name).getCaption());

		Mockito.verify(commercialSubjectModel).save(commercialSubjectItem);
		Assert.assertEquals(Arrays.asList(CommercialSubjectItemCols.Name, CommercialSubjectItemCols.Mandatory), Arrays.asList(itemTable.getVisibleColumns()));

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Table prepareSaveItem() {
		final Container container = Mockito.mock(Container.class);
		Mockito.when(container.getContainerPropertyIds()).thenReturn((Collection) Arrays.asList(CommercialSubjectItemCols.values()));
		Mockito.when(commercialSubjectItemToContainerConverter.convert(Mockito.any())).thenReturn(container);
		final Table itemTable = (Table) components.get(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);

		Mockito.when(itemToCommercialSubjectItemConverter.convert(itemFieldsDatasource)).thenReturn(commercialSubjectItem);
		Mockito.when(subject.id()).thenReturn(Optional.of(19680528L));
		Mockito.when(commercialSubjectItem.subject()).thenReturn(subject);
		Mockito.when(validationUtil.validate(commercialSubjectItemCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);
		return itemTable;
	}

	@Test
	public final void saveItemButtonSubjectNull() {
		{
			final Table itemTable = prepareSaveItem();

			final CommercialSubjectItem commercialSubjectItem = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class);
			Mockito.when(subject.id()).thenReturn(Optional.empty());

			ReflectionUtils.doWithFields(commercialSubjectItem.getClass(), field -> ReflectionTestUtils.setField(commercialSubjectItem, field.getName(), subject), field -> field.getType().equals(Subject.class));

			Mockito.when(validationUtil.validate(commercialSubjectItemCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(false);

			Mockito.when(itemToCommercialSubjectItemConverter.convert(itemFieldsDatasource)).thenReturn(commercialSubjectItem);

			final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_SAVE_ITEM_BUTTON);

			@SuppressWarnings("unchecked")
			final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
			Assert.assertTrue(listener.isPresent());

			Assert.assertEquals(subject, commercialSubjectItem.subject());

			listener.get().buttonClick(clickEvent);

			Assert.assertNull(commercialSubjectItem.subject());

			Mockito.verify(commercialSubjectModel, Mockito.never()).save(commercialSubjectItem);

			Assert.assertEquals(0, itemTable.getVisibleColumns().length);

		}

	}
	
	
	@Test
	public final void deleteItemButton() {
		final Table itemTable = prepareSaveItem();
		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_DELETE_ITEM_BUTTON);
		
		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
		Assert.assertTrue(listener.isPresent());
		Assert.assertEquals(0, itemTable.getVisibleColumns().length);
		listener.get().buttonClick(clickEvent);
		
		
		Mockito.verify(commercialSubjectModel).delete(commercialSubjectItem);
		
		Assert.assertEquals(Arrays.asList(CommercialSubjectItemCols.Name, CommercialSubjectItemCols.Mandatory), Arrays.asList(itemTable.getVisibleColumns()));
		
		
	}
	
	
	
	@Test
	public final void saveValueButton() throws CommitException {
		
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);
		Mockito.when(commercialSubjectModel.getInputValue()).thenReturn(String.valueOf(CONDITION_ID));
		Mockito.when(commercialSubjectModel.canConvertConditionValue(String.valueOf(CONDITION_ID),CONDITION_ID)).thenReturn(true);
		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE);
		
		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
		
		
		final ComboBox conditionBox =  (ComboBox) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION);
	
		conditionBox.addItem(CONDITION_ID);

		conditionBox.setValue(CONDITION_ID);
	
		
		Assert.assertTrue(listener.isPresent());
		
		listener.get().buttonClick(clickEvent);
		
		Assert.assertEquals(commercialSubjectModel, commercialSubjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		
		Mockito.verify(validationUtil).reset(fieldGroupCaptor.getValue());
		
		Assert.assertEquals(conditionValueItem, fieldGroupCaptor.getValue().getItemDataSource());
		
		Mockito.verify(itemIntoCommercialSubjectModel, Mockito.times(1)).mapInto(conditionValueItem, commercialSubjectModel);
	
		Mockito.verify(commercialSubjectModel).addInputValue(CONDITION_ID);
		
	}
	
	
	
	@Test
	public final void saveValueButtonValidationSucks() throws CommitException {
		
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(false);
	
		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE);
		
		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
		
		Assert.assertTrue(listener.isPresent());
		
		listener.get().buttonClick(clickEvent);
		
		Assert.assertEquals(commercialSubjectModel, commercialSubjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		
		Mockito.verify(validationUtil).reset(fieldGroupCaptor.getValue());
		
		Assert.assertEquals(conditionValueItem, fieldGroupCaptor.getValue().getItemDataSource());
		
		Mockito.verify(itemIntoCommercialSubjectModel, Mockito.times(1)).mapInto(conditionValueItem, commercialSubjectModel);
	
		Mockito.verify(commercialSubjectModel, Mockito.never()).addInputValue(CONDITION_ID);
		
	}
	
	
	@Test
	public final void saveValueButtonConversionSucks() throws CommitException {
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.IntegralNumber);
		Mockito.when(validationUtil.validate(commercialSubjectCaptor.capture(), fieldGroupCaptor.capture(), localeCaptor.capture())).thenReturn(true);
		Mockito.when(commercialSubjectModel.getInputValue()).thenReturn(String.valueOf(CONDITION_ID));
		Mockito.when(commercialSubjectModel.canConvertConditionValue(String.valueOf(CONDITION_ID),CONDITION_ID)).thenReturn(false);
		
		Mockito.when(commercialSubjectModel.getCondition(CONDITION_ID)).thenReturn(condition);
		
		final Button saveItemButton = (Button) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE_SAVE);
		
		@SuppressWarnings("unchecked")
		final Optional<ClickListener> listener = (Optional<ClickListener>) saveItemButton.getListeners(ClickEvent.class).stream().findAny();
		
		
		final ComboBox conditionBox =  (ComboBox) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION);
	
		conditionBox.addItem(CONDITION_ID);

		conditionBox.setValue(CONDITION_ID);
		final TextField valueInputField =  (TextField) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONDITION_VALUE);
		Assert.assertNull(valueInputField.getErrorMessage());
		
		Assert.assertTrue(listener.isPresent());
		
		listener.get().buttonClick(clickEvent);
		
		Assert.assertEquals(commercialSubjectModel, commercialSubjectCaptor.getValue());
		Assert.assertEquals(userModel.getLocale(), localeCaptor.getValue());
		
		Mockito.verify(validationUtil).reset(fieldGroupCaptor.getValue());
		
		Assert.assertEquals(conditionValueItem, fieldGroupCaptor.getValue().getItemDataSource());
		
		Mockito.verify(itemIntoCommercialSubjectModel, Mockito.times(1)).mapInto(conditionValueItem, commercialSubjectModel);
	
		Assert.assertEquals(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_CONVERSION_ERROR, ((CompositeErrorMessage)valueInputField.getErrorMessage()).iterator().next().toString());
		Assert.assertEquals(ErrorLevel.ERROR, ((CompositeErrorMessage)valueInputField.getErrorMessage()).iterator().next().getErrorLevel());
		
		
	}
	
	
	@Test
	public final void valueTableValueChangeListener() {
		
		final Table valueTable =  (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		prepareValueTable(valueTable, ConditionValueCols.InputValue, CONDITION_VALUE);
		
		@SuppressWarnings("unchecked")
		final Optional<ValueChangeListener> valueChangeListener =  (Optional<ValueChangeListener>) valueTable.getListeners(ValueChangeEvent.class).stream().findAny();
		Assert.assertTrue(valueChangeListener.isPresent());
	
		
		valueChangeListener.get().valueChange(valueChangeEvent);
		
		Mockito.verify(commercialSubjectModel).setCurrentInputValue(CONDITION_VALUE);
		
		
	
	}

	private void  prepareValueTable(final Table valueTable, final Object col, final Object value) {
		valueTable.addContainerProperty(col, Object.class, "");
			
		final Object index = valueTable.addItem();
	
		final Item item = valueTable.getItem(index);
		@SuppressWarnings("unchecked")
		final Property<Object> property = item.getItemProperty(col);
		property.setValue(value);
		Mockito.when(valueChangeEvent.getProperty()).thenReturn(new ObjectProperty<>(index));
		
	}
	
	@Test
	public final void valueTableValueChangeListenerNullValue() {
		
		final Table valueTable =  (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		prepareValueTable(valueTable, ConditionValueCols.InputValue, CONDITION_VALUE);
		@SuppressWarnings("rawtypes")
		final ObjectProperty mock = Mockito.mock(ObjectProperty.class);
		Mockito.when(valueChangeEvent.getProperty()).thenReturn(mock);
		@SuppressWarnings("unchecked")
		final Optional<ValueChangeListener> valueChangeListener =  (Optional<ValueChangeListener>) valueTable.getListeners(ValueChangeEvent.class).stream().findAny();
		Assert.assertTrue(valueChangeListener.isPresent());
	
		
		valueChangeListener.get().valueChange(valueChangeEvent);
		
		Mockito.verify(commercialSubjectModel).setCurrentInputValue(null);
		
		
		
	}
	
	@Test
	public final void itemTableValueChangeListener() {
		final Table itemTable = (Table) components.get(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);
		final Table valueTable =  (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		prepareValueTable(itemTable,CommercialSubjectItemCols.Id,  CONDITION_ID);
		@SuppressWarnings("unchecked")
		final Optional<ValueChangeListener> valueChangeListener =  (Optional<ValueChangeListener>) itemTable.getListeners(ValueChangeEvent.class).stream().findAny();
		Assert.assertTrue(valueChangeListener.isPresent());
		Assert.assertFalse(valueTable.getParent().isVisible());
		valueChangeListener.get().valueChange(valueChangeEvent);
		
		Mockito.verify(commercialSubjectModel).setCommercialSubjectItemId(CONDITION_ID);
		Assert.assertTrue(valueTable.getParent().isVisible());
		
	}
	
	@Test
	public final void itemTableValueChangeListenerNull() {
		final Table itemTable = (Table) components.get(CommercialSubjectViewImpl.I18N_ITEM_TABLE_CAPTION);
		final Table valueTable =  (Table) components.get(CommercialSubjectViewImpl.I18N_COMMERCIAL_SUBJECT_VALUE_TABLE);
		prepareValueTable(itemTable,CommercialSubjectItemCols.Id,  CONDITION_ID);
		
		@SuppressWarnings("rawtypes")
		final ObjectProperty mock = Mockito.mock(ObjectProperty.class);
		Mockito.when(valueChangeEvent.getProperty()).thenReturn(mock);
		@SuppressWarnings("unchecked")
		final Optional<ValueChangeListener> valueChangeListener =  (Optional<ValueChangeListener>) itemTable.getListeners(ValueChangeEvent.class).stream().findAny();
		Assert.assertTrue(valueChangeListener.isPresent());
		Assert.assertFalse(valueTable.getParent().isVisible());
		valueChangeListener.get().valueChange(valueChangeEvent);
		
		Mockito.verify(commercialSubjectModel).setCommercialSubjectItemId(null);
		Assert.assertFalse(valueTable.getParent().isVisible());
		
	}


}
