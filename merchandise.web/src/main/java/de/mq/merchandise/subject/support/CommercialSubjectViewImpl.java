package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl extends CustomComponent implements View {

	private static final String I18N_COMMERCIAL_SUBJECT_DELETE = "commercial_subject_delete";

	private static final String I18N_COMMERCIAL_SUBJECT_NEW = "commercial_subject_new";

	private static final String I18N_COMMERCIAL_SUBJECT_SAVE = "commercial_subject_save";

	private static final String I18N_COMMERCIAL_SUBJECT_TABLE_NAME = "commercial_subject_table_name";

	private static final String I18N_COMMERCIAL_SUBJECT_SEARCH = "commercial_subject_search";

	private static final String I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME = "commercial_subject_search_item_name";

	private static final String I18_COMMERCIAL_SUBJECT_SEARCH_NAME = "commercial_subject_search_name";

	private static final String I18N_COMMERCIAL_SUBJECT_NAME = "commercial_subject_name";

	private static final long serialVersionUID = 1L;

	private final RefreshableContainer lazyQueryContainer;

	private UserModel userModel;

	private final MainMenuBarView mainMenuBarView;

	private final Item commercialSubjectSearchItem;

	private final CommercialSubjectModel commercialSubjectModel;

	private final Converter<Item, CommercialSubject> itemToCommercialSubjectConverter;

	private final ValidationUtil validationUtil;

	private final Converter<CommercialSubject, Item> commercialSubjectToItemConverter;
	
	
	private  final Converter<Collection<Subject>, Container> entriesToConatainerConverter;
	 
	 private  final Converter<CommercialSubjectItem, Item> commercialSubjectItemConverter;
	 private final Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter; 
	
	private final MessageSource messageSource; 

	final ThemeResource editIcon = new ThemeResource("edit-icon.png");
	final ThemeResource newIcon = new ThemeResource("new-icon.png");

	@Autowired
	CommercialSubjectViewImpl(final CommercialSubjectModel commercialSubjectModel, 
			final UserModel userModel, 
			final MessageSource messageSource, 
			ViewNav viewNav, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar) final MainMenuBarView mainMenuBarView,
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectSearchItem) final Item commercialSubjectSearchItem, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemToCommercialSubjectConverter) Converter<Item, CommercialSubject> itemToCommercialSubjectConverter, 
			final ValidationUtil validationUtil, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter) final Converter<CommercialSubject, Item> commercialSubjectToItemConverter, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.EntriesToConatainerConverter) Converter<Collection<Subject>, Container> entriesToConatainerConverter, 
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToItemConverter) final Converter<CommercialSubjectItem, Item> commercialSubjectItemConverter, 
			final Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter ) {
		this.mainMenuBarView = mainMenuBarView;
		this.lazyQueryContainer = lazyQueryContainer;
		this.commercialSubjectSearchItem = commercialSubjectSearchItem;
		this.itemToCommercialSubjectConverter = itemToCommercialSubjectConverter;
		this.commercialSubjectModel = commercialSubjectModel;
		this.userModel = userModel;
		this.validationUtil = validationUtil;
		this.messageSource=messageSource;
		this.commercialSubjectToItemConverter = commercialSubjectToItemConverter;
		
		this.entriesToConatainerConverter=entriesToConatainerConverter;
		this.commercialSubjectItemConverter=commercialSubjectItemConverter;
		this.itemToCommercialSubjectItemConverter=itemToCommercialSubjectItemConverter;
	}

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout() {

		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		splitPanel.setSplitPosition(64, Unit.PERCENTAGE);

		setCompositionRoot(splitPanel);

		final Panel searchPanel = new Panel();

		final GridLayout searchBox = new GridLayout(1, 2);
		final HorizontalLayout searchLayout = new HorizontalLayout();

		searchBox.addComponent(searchLayout, 0, 0);
		searchPanel.setContent(searchBox);

		final FieldGroup fieldGroup = new FieldGroup();
		fieldGroup.setItemDataSource(commercialSubjectSearchItem);
		fieldGroup.setBuffered(true);
		final FormLayout col1Layout = new FormLayout();
		final TextField searchNameField = new TextField();
		col1Layout.addComponent(searchNameField);

		col1Layout.setMargin(new MarginInfo(true, false, false, true));

		searchLayout.addComponent(col1Layout);

		final FormLayout col2Layout = new FormLayout();
		final TextField searchItemNameField = new TextField();
		col2Layout.addComponent(searchItemNameField);

		col2Layout.setMargin(new MarginInfo(true, false, false, true));
		searchLayout.addComponent(col2Layout);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		final Button searchButton = new Button();

		searchButton.addClickListener(e -> commitSearch(fieldGroup));

		buttonLayout.addComponent(searchButton);

		buttonLayout.setWidth("100%");
		searchBox.addComponent(buttonLayout, 0, 1);
		fieldGroup.bind(searchNameField, CommercialSubjectCols.Name);
		fieldGroup.bind(searchItemNameField, CommercialSubjectCols.ItemName);

		commercialSubjectModel.register(event -> lazyQueryContainer.refresh(), CommercialSubjectModel.EventType.SearchCriteriaChanged);

		final VerticalLayout leftLayout = new VerticalLayout();

		leftLayout.addComponent(mainMenuBarView);

		splitPanel.addComponent(leftLayout);
		leftLayout.setSizeFull();

		leftLayout.addComponent(searchPanel);
		final Table subjectList = new Table();

		leftLayout.addComponent(subjectList);

		subjectList.setSizeFull();

		subjectList.setSelectable(true);

		subjectList.setContainerDataSource(lazyQueryContainer);
		subjectList.setVisibleColumns(Arrays.asList(CommercialSubjectCols.values()).stream().filter(col -> col.visible()).toArray());
		subjectList.setSortContainerPropertyId(CommercialSubjectCols.Name);
		subjectList.addValueChangeListener(e -> commercialSubjectModel.setCommercialSubjectId((Long) e.getProperty().getValue()));

		final VerticalLayout editor = new VerticalLayout();
		editor.setSizeUndefined();

		editor.setMargin(new MarginInfo(true, false, false, true));

		final HorizontalLayout editorLayout = new HorizontalLayout();
		editorLayout.setSizeFull();

		editor.addComponent(editorLayout);

		final FormLayout col1 = new FormLayout();

		col1.setWidth("100%");
		final FieldGroup editorFields = new FieldGroup();

		final TextField editorNameField = new TextField();
		editorNameField.setNullRepresentation("");
		editorNameField.setSizeFull();

		editorFields.bind(editorNameField, CommercialSubjectCols.Name);
		col1.addComponent(editorNameField);

		editorFields.setItemDataSource(commercialSubjectToItemConverter.convert(commercialSubjectModel.getCommercialSubject().get()));

		final Button saveButton = new Button();
		
		
		saveButton.addClickListener(e -> {

			commit(editorFields);
			final CommercialSubject commercialSubject = itemToCommercialSubjectConverter.convert(editorFields.getItemDataSource());
		

			if (!validationUtil.validate(commercialSubject, editorFields, userModel.getLocale())) {
				return;
			}
			commercialSubjectModel.save(commercialSubject);
			lazyQueryContainer.refresh();
			subjectList.setValue(null);

		});
		
		final Button newButton = new Button();
		final Button deleteButton = new Button();
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setIcon(newIcon);

		deleteButton.addClickListener(e -> {			
			commercialSubjectModel.delete(itemToCommercialSubjectConverter.convert(editorFields.getItemDataSource()));
			lazyQueryContainer.refresh();
			subjectList.setValue(null);
			
		});
		
		
		editorLayout.addComponent(col1);
		editorLayout.setWidth("100%");

		final HorizontalLayout saveButtonLayout = new HorizontalLayout();
		saveButtonLayout.setSpacing(true);
		editor.addComponent(saveButtonLayout);

		saveButtonLayout.addComponent(saveButton);

		saveButtonLayout.addComponent(newButton);
		saveButtonLayout.addComponent(deleteButton);

		newButton.addClickListener(event -> subjectList.setValue(null));

		
		
		
		
		
		final VerticalLayout itemTableLayout = new VerticalLayout();

		itemTableLayout.setVisible(false);
		itemTableLayout.setMargin(new MarginInfo(true, false, false, false));

		final HorizontalLayout buttonItemLayout = new HorizontalLayout();

		buttonItemLayout.setMargin(new MarginInfo(false, false, true, false));
		buttonItemLayout.setSpacing(true);

		final HorizontalLayout itemEditorLayout = new HorizontalLayout();
		itemEditorLayout.setWidth("100%");
		final FormLayout itemCols = new FormLayout();
		itemCols.setWidth("100%");
		itemEditorLayout.addComponent(itemCols);
		itemTableLayout.addComponent(itemEditorLayout);

		final FieldGroup itemFields = new FieldGroup();
		itemFields.setItemDataSource(commercialSubjectItemConverter.convert(commercialSubjectModel.getCommercialSubjectItem().get()));
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col -> col.visible()).forEach(col -> {

			final Field<?> field = col.newField();

			itemFields.bind(field, col);
			itemCols.addComponent(field);

		});
		
		

		
		final ComboBox box = (ComboBox) itemFields.getField(CommercialSubjectItemCols.Subject);
		box.setItemCaptionPropertyId(SubjectCols.Name);

		

		
		
		box.setContainerDataSource(entriesToConatainerConverter.convert(commercialSubjectModel.getSubjects()));
		
		
		final Button saveItemButton = new Button("speichern");
		
		
		saveItemButton.addClickListener(event -> {
			//System.out.println(">>>>" + itemFields.getItemDataSource().getItemProperty(CommercialSubjectItemCols.Subject).getValue());
			
			commit(itemFields);
			CommercialSubjectItem item = itemToCommercialSubjectItemConverter.convert(itemFields.getItemDataSource());
			
			System.out.println("subjectId=" +item.subject().id());
			System.out.println("subjectName=" +item.name());
			System.out.println("mandatory=" +item.mandatory());
			
			
		});
		saveItemButton.setIcon(newIcon);
		final Button newItemButton = new Button("neu");
		newItemButton.setEnabled(false);

		final Button deleteItemButton = new Button("löschen");
		deleteItemButton.setEnabled(false);
		buttonItemLayout.addComponent(saveItemButton);
		buttonItemLayout.addComponent(newItemButton);
		buttonItemLayout.addComponent(deleteItemButton);
		itemTableLayout.addComponent(buttonItemLayout);

		final Table itemTable = new Table();
		itemTable.setCaption("Positionen");
	

		itemTable.setWidth("100%");
		itemTable.setPageLength(5);

		itemTableLayout.addComponent(itemTable);
		editor.addComponent(itemTableLayout);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		splitPanel.addComponent(editor);

		commercialSubjectModel.register(e -> {
			validationUtil.reset(editorFields);

			editorFields.setItemDataSource(null);
			newButton.setEnabled(false);

			deleteButton.setEnabled(false);
			editorFields.setItemDataSource(commercialSubjectToItemConverter.convert(commercialSubjectModel.getCommercialSubject().get()));
			itemTableLayout.setVisible(false);
			if (commercialSubjectModel.getCommercialSubject().get().id().isPresent()) {
				itemTableLayout.setVisible(true);
				saveButton.setIcon(editIcon);
				newButton.setEnabled(true);
				deleteButton.setEnabled(true);
				
				return;
			}

			saveButton.setIcon(newIcon);

		}, CommercialSubjectModel.EventType.CommericalSubjectChanged);
		
		
		userModel.register(e-> {
			editorNameField.setCaption(message(I18N_COMMERCIAL_SUBJECT_NAME));
			searchNameField.setCaption(message(I18_COMMERCIAL_SUBJECT_SEARCH_NAME));
			searchItemNameField.setCaption(message(I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME));
			searchButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_SEARCH));
			subjectList.setColumnHeader(CommercialSubjectCols.Name, message(I18N_COMMERCIAL_SUBJECT_TABLE_NAME));
			saveButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_SAVE));
			newButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_NEW));
			deleteButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_DELETE));
		}, UserModel.EventType.LocaleChanged);

	}

	private void commitSearch(final FieldGroup fieldGroup) {
		commit(fieldGroup); 
		commercialSubjectModel.setSearch(itemToCommercialSubjectConverter.convert(commercialSubjectSearchItem));
	}
	private String message(final String key ) {
	 return messageSource.getMessage(key, null, userModel.getLocale());
	}

	private void commit(final FieldGroup fieldGroup) {
		try {

			fieldGroup.commit();
			
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	@PostConstruct
	void init() {
		commercialSubjectModel.setCustomer(userModel.getCustomer());
		initLayout();

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
