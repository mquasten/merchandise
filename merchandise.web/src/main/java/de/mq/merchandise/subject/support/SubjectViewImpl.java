package de.mq.merchandise.subject.support;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.RefreshableContainer;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class SubjectViewImpl extends CustomComponent implements View {

	private  final ThemeResource editIcon = new ThemeResource("edit-icon.png");
	private  final ThemeResource newIcon = new ThemeResource("new-icon.png");

	private static final long serialVersionUID = 1L;
	
	static final String I18N_SUBJECT_TABLE_HEADLINE = "subject_table_headline";
	static final String I18N_SUBJECT_SEARCH_HEADLINE = "subject_search_headline";
	static final String I18N_SUBJECT_SEARCH_BUTTON = "subject_search_button";
	static final String I18N_SUBJECT_SEARCH_NAME = "subject_search_name";
	static final String I18N_SUBJECT_SEARCH_DESCRIPTION = "subject_search_description";
	static final String I18N_SUBJECT_TABLE_PREFIX = "subject_table_";
	
	private final Converter<Item, Subject> itemToSubjectMapper;
	
	private final Converter<Subject, Item> subjectToItemConverter;
	
	private final RefreshableContainer lazyQueryContainer;
  private final Item subjectSearchItem;

	private final SubjectModel subjectModel;
	private final UserModel userModel;
	private final MessageSource messageSource;
	
	private final Converter<Collection<Condition>, Container> conditionToContainerConverter;
	

	@Autowired
	public SubjectViewImpl(@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToSubjectConverter) final Converter<Item, Subject> itemToSubjectConverter,@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectToItemConverter) final Converter<Subject, Item>  subjectToItemConverter, @SubjectModelQualifier(SubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectSearchItem) final Item subjectSearchItem,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectModel) final SubjectModel subjectModel, final UserModel userModel, final MessageSource messageSource, @SubjectModelQualifier(SubjectModelQualifier.Type.ConditionToContainerConverter) final Converter<Collection<Condition>, Container> conditionToContainerConverter) {

		this.itemToSubjectMapper = itemToSubjectConverter;
		this.subjectToItemConverter = subjectToItemConverter;
		this.lazyQueryContainer = lazyQueryContainer;
		this.subjectSearchItem = subjectSearchItem;
	
		this.subjectModel = subjectModel;
		this.userModel = userModel;
		this.messageSource=messageSource;
		this.conditionToContainerConverter=conditionToContainerConverter;
	}

	private void initLayout() {

		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		setCompositionRoot(splitPanel);

		final VerticalLayout leftLayout = new VerticalLayout();
		
		
		final TextField searchName = new TextField();
		final TextField searchDescription = new TextField();
		final FieldGroup fieldGroup = new FieldGroup();

		
		fieldGroup.setItemDataSource(subjectSearchItem);

		final Panel searchPanel = new Panel();
		
		final GridLayout searchBox = new GridLayout(1,2);
		final HorizontalLayout searchLayout = new HorizontalLayout();
		
		searchBox.addComponent(searchLayout, 0,0);
		searchPanel.setContent(searchBox);
	
		final FormLayout col1Layout = new FormLayout();
		
		col1Layout.setMargin(new MarginInfo(true, false, false, true));
		
		searchLayout.addComponent(col1Layout);
		col1Layout.addComponent(searchName);
		
	
		final FormLayout col2Layout = new FormLayout();
		
		col2Layout.setMargin(new MarginInfo(true, false, false, true));
		searchLayout.addComponent(col2Layout);
		col2Layout.addComponent(searchDescription);
		

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		final Button searchButton = new Button();
		buttonLayout.addComponent(searchButton);
		
		buttonLayout.setWidth("100%");
		searchBox.addComponent(buttonLayout, 0,1);
		
		fieldGroup.setBuffered(true);
		fieldGroup.bind(searchName, SubjectCols.Name);
		fieldGroup.bind(searchDescription, SubjectCols.Description);

		searchButton.addClickListener(e -> searchCriteria2Model(fieldGroup));

		leftLayout.addComponent(searchPanel);

		final Table subjectList = new Table();

		final VerticalLayout editor = new VerticalLayout();
		editor.setMargin(true);
		editor.setSizeUndefined();
		final HorizontalLayout editorLayout = new HorizontalLayout();
		editor.addComponent(editorLayout);
		
		
		final FormLayout col1 = new FormLayout();
		
		
		
		final FieldGroup editorFields = new FieldGroup();
	
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()&& !col.equals(SubjectCols.DateCreated)).forEach(col -> {
			final TextField field = new TextField(col.name());
			field.setNullRepresentation("");
			editorFields.bind( field, col);
			col1.addComponent(field);
			
		});
		
		editorFields.setItemDataSource(subjectToItemConverter.convert(subjectModel.getSubject().get()));
		final Button saveButton = new Button("speichern");
		final Button newButton = new Button("neu");
		final Button deleteButton = new Button("delete");
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setIcon(newIcon);
	   subjectModel.register(e -> { 
			editorFields.setItemDataSource(null);
			newButton.setEnabled(false);
			deleteButton.setEnabled(false);
			editorFields.setItemDataSource(subjectToItemConverter.convert(subjectModel.getSubject().get()));
			if( subjectModel.getSubject().get().id().isPresent()) {
				saveButton.setIcon(editIcon);
				newButton.setEnabled(true);
				deleteButton.setEnabled(true);
				return;
			} 
			
			saveButton.setIcon(newIcon);
			
		}, SubjectModel.EventType.SubjectChanged);  
		
	   
	   
	   saveButton.addClickListener(e -> { 
	   	
	   	try {
				editorFields.commit();
			} catch (Exception e1) {
				return;
			}
	   	final Subject subject = itemToSubjectMapper.convert(editorFields.getItemDataSource());
	   	
	   	subjectModel.save(subject);
	   	lazyQueryContainer.refresh();
	   	subjectList.setValue(null);
	   	
	   });
	   
		
	   deleteButton.addClickListener(e -> {
	   	final Subject subject = itemToSubjectMapper.convert(editorFields.getItemDataSource());
	   	subjectModel.delete(subject);
	   	lazyQueryContainer.refresh();
	   	subjectList.setValue(null);
	   });
	   
		editorLayout.addComponent(col1);	
		
	
		final HorizontalLayout saveButtonLayout = new HorizontalLayout();
		saveButtonLayout.setSpacing(true);
		editor.addComponent(saveButtonLayout);
		
	
		
		saveButtonLayout.addComponent(saveButton);
		
		saveButtonLayout.addComponent(newButton);
		saveButtonLayout.addComponent(deleteButton);
		
		
		newButton.addClickListener(event -> { 
			subjectList.setValue(null);
		//	subjectModel.setSubjectId(null);
		});
		
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editor);
		
		
		
		leftLayout.addComponent(subjectList);

		leftLayout.setSizeFull();

		leftLayout.setExpandRatio(subjectList, 1);
		subjectList.setSizeFull();

		
	

		subjectList.setSelectable(true);

		subjectList.setContainerDataSource(lazyQueryContainer);
		subjectList.setVisibleColumns(Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).toArray());
		subjectList.setSortContainerPropertyId(SubjectCols.Name);
		
		subjectList.addValueChangeListener(e -> { 
			
		
			subjectModel.setSubjectId((Long) e.getProperty().getValue());
			
			
		});
		

		subjectModel.register(event -> lazyQueryContainer.refresh(), SubjectModel.EventType.SearchCriteriaChanged);

	    
	    userModel.register(o -> {
	    	searchDescription.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_DESCRIPTION, null, userModel.getLocale()));
	         searchName.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_NAME, null, userModel.getLocale() ));
	         searchButton.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_BUTTON, null, userModel.getLocale() ));
	         searchPanel.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_HEADLINE, null, userModel.getLocale()));
	         subjectList.setCaption(messageSource.getMessage(I18N_SUBJECT_TABLE_HEADLINE, null, userModel.getLocale()));
	         Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).forEach(col ->  subjectList.setColumnHeader(col, messageSource.getMessage(I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name()), null, userModel.getLocale())));
	         
	    }, UserModel.EventType.LocaleChanged);
	    
	    
	    final VerticalLayout conditionTableLayout = new VerticalLayout();
	    final Table conditionTable = new Table();
	    conditionTable.setCaption("Konditionen");
	    conditionTableLayout.addComponent(conditionTable);
	    editor.addComponent(conditionTableLayout);
	    conditionTable.setSelectable(true);
	    
	    final Collection<Condition> conditions = new ArrayList<>();
	    conditions.add(new ConditionImpl(BeanUtils.instantiateClass(SubjectImpl.class), "Unit", ConditionDataType.String));
	    conditionTable.setContainerDataSource(conditionToContainerConverter.convert(conditions));
	    
	}

	
	private void searchCriteria2Model(final FieldGroup fieldGroup) {
		try {

			fieldGroup.commit();
			subjectModel.setSerachCriteria(itemToSubjectMapper.convert(fieldGroup.getItemDataSource()));

		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	@PostConstruct
	void init() {
		subjectModel.setCustomer(userModel.getCustomer());

		initLayout();

	}

	@Override
	public void enter(final ViewChangeEvent event) {
	
		// TODO Auto-generated method stub

	}

}
