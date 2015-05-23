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
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.ComponentTestHelper;
import de.mq.merchandise.util.Mapper;
import de.mq.merchandise.util.Observer;
import de.mq.merchandise.util.support.RefreshableContainer;

public class SubjectViewTest {
	
	@SuppressWarnings("unchecked")
	private final Mapper<Item, Subject> itemToSubjectConverter = Mockito.mock(Mapper.class);
	private final RefreshableContainer lazyQueryContainer = Mockito.mock(RefreshableContainer.class);
	private final Item subjectItem = Mockito.mock(Item.class);
	private final Item subjectEditItem = Mockito.mock(Item.class);
	private final SubjectModel subjectModel = Mockito.mock(SubjectModel.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	
	private final SubjectViewImpl subjectView= new SubjectViewImpl(itemToSubjectConverter,lazyQueryContainer, subjectItem, subjectEditItem, subjectModel, userModel, messageSource); 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final ArgumentCaptor<Observer<UserModel.EventType>> localChangedObserverCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Observer.class);

	private final ArgumentCaptor<UserModel.EventType> localChangedTypeCapture = ArgumentCaptor.forClass(UserModel.EventType.class);
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final ArgumentCaptor<Observer<SubjectModel.EventType>> searchChangedObserverCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Observer.class);
	
	private final ArgumentCaptor<SubjectModel.EventType> searchChangedTypeCapture = ArgumentCaptor.forClass(SubjectModel.EventType.class);
	
	private final Map<String, Component> components = new HashMap<>();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before()
	public final void setup() {
		
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
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON, null, Locale.GERMAN )).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE, null,Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE);
		Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE, null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).forEach(col -> Mockito.when(messageSource.getMessage(SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name()), null, Locale.GERMAN)).thenReturn(SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name())));
		
	
		subjectView.init();
		
		
		
		Mockito.verify(userModel).register(localChangedObserverCapture.capture(), localChangedTypeCapture.capture() );
	
		Assert.assertNotNull(localChangedObserverCapture.getValue());
		Assert.assertEquals(UserModel.EventType.LocaleChanged, localChangedTypeCapture.getValue());
		localChangedObserverCapture.getValue().process(localChangedTypeCapture.getValue());
		
		Mockito.verify(subjectModel).register(searchChangedObserverCapture.capture(), searchChangedTypeCapture.capture());
		
		Assert.assertNotNull(searchChangedObserverCapture.getValue());
		Assert.assertEquals(SubjectModel.EventType.SearchCriteriaChanged, searchChangedTypeCapture.getValue());
		
		
		components.clear();
		ComponentTestHelper.components(subjectView, components);
	}
	
	
	
	
	@Test
	public final void init() {
		
		
		final Table table = (Table) components.get(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE);
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_TABLE_HEADLINE, table.getCaption());
		Arrays.asList(SubjectCols.values()).stream().filter(value -> value.visible()).forEach(value -> Assert.assertTrue(Arrays.asList(table.getColumnHeaders()).contains((SubjectViewImpl.I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(value.name())))));
		
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME,  components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_NAME) instanceof TextField) ;
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION, components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_DESCRIPTION) instanceof TextField) ;
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE,  components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_HEADLINE) instanceof Panel) ;
		Assert.assertEquals(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON,  components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON).getCaption());
		Assert.assertTrue(components.get(SubjectViewImpl.I18N_SUBJECT_SEARCH_BUTTON) instanceof Button) ;
		
	}
	
	@Test
	public final void refreshSubjectContainer() {
		searchChangedObserverCapture.getValue().process(searchChangedTypeCapture.getValue());
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
	
	@Test(expected=IllegalStateException.class)
	public final void searchSucks() {
		Mockito.when(itemToSubjectConverter.convert(subjectItem)).thenThrow(new RuntimeException("Don' t worry only for test"));
		executeListener();
	}
	
	@Test
	public final void enter() {
		final ViewChangeEvent event = Mockito.mock(ViewChangeEvent.class);
		subjectView.enter(event);
	}
	
}
