package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

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

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl extends CustomComponent implements View {

	private static final String I18N_ITEM_TABLE_CAPTION = "commercial_subject_item_table_name";
	private static final String I18N_DELETE_ITEM_BUTTON = "commercial_subject_delete_item_button";
	private static final String I18N_NEW_ITEM_BUTTON = "commercial_subject_new_item_button";
	private static final String I18N_SAVE_ITEM_BUTTON = "commercial_subject_save_item_button";
	private static final String I18N_MANDATORY_BOX_TRUE = "commercial_subject_mandatoryBox_true";
	private static final String I18N_MANDATORY_BOX_FALSE = "commercial_subject_mandatoryBox_false";

	private static final String I18N_COMMERCIAL_SUBJECT_DELETE = "commercial_subject_delete";

	private static final String I18N_COMMERCIAL_SUBJECT_NEW = "commercial_subject_new";

	private static final String I18N_COMMERCIAL_SUBJECT_SAVE = "commercial_subject_save";

	private static final String I18N_COMMERCIAL_SUBJECT_TABLE_NAME = "commercial_subject_table_name";

	private static final String I18N_COMMERCIAL_SUBJECT_SEARCH = "commercial_subject_search";

	private static final String I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME = "commercial_subject_search_item_name";

	private static final String I18_COMMERCIAL_SUBJECT_SEARCH_NAME = "commercial_subject_search_name";

	private static final String I18N_COMMERCIAL_SUBJECT_NAME = "commercial_subject_name";
	
	private static final String I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX  = "commercial_subject_item_";

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
	 private final Converter<Collection<CommercialSubjectItem>, Container> commercialSubjectItemToContainerConverter;
	 private final Converter<CommercialSubjectItemConditionImpl, Item>  commercialSubjectItemCondition;
	 
	private  final   Converter<Collection<Condition>, Container> conditionToContainerConverter;
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
			final Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter,
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToContainerConverter) final Converter<Collection<CommercialSubjectItem>, Container> commercialSubjectItemToContainerConverter,
			
			@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemConditionToContainerConverter)  final Converter<CommercialSubjectItemConditionImpl, Item>  commercialSubjectItemCondition,
			
			@SubjectModelQualifier(SubjectModelQualifier.Type.ConditionToContainerConverter)final   Converter<Collection<Condition>, Container> conditionToContainerConverter
			
			) {
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
		this.commercialSubjectItemToContainerConverter=commercialSubjectItemToContainerConverter;
		this.commercialSubjectItemCondition=commercialSubjectItemCondition;
		this.conditionToContainerConverter=conditionToContainerConverter;
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

		editor.setMargin(new MarginInfo(false, false, false, true));

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
		itemTableLayout.setMargin(new MarginInfo(false, false, false, false));

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
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col -> col.visible()||CommercialSubjectItemCols.Subject == col ).forEach(col -> {

			final com.vaadin.ui.Field<?> field = col.newField();

			itemFields.bind(field, col);
			itemCols.addComponent(field);

		});
		
		

		
		final ComboBox subjectBox = (ComboBox) itemFields.getField(CommercialSubjectItemCols.Subject);
		subjectBox.setItemCaptionPropertyId(SubjectCols.Name);

		subjectBox.setContainerDataSource(entriesToConatainerConverter.convert(commercialSubjectModel.getSubjects()));
		
		final ComboBox mandatoryBox = (ComboBox) itemFields.getField(CommercialSubjectItemCols.Mandatory);
		mandatoryBox.addItems(Boolean.TRUE, Boolean.FALSE);
	
		
		
		mandatoryBox.setNullSelectionAllowed(false);
		
		final Button saveItemButton = new Button();
		final Table itemTable = new Table();
		
		saveItemButton.addClickListener(event -> {
			
			commit(itemFields);
			final CommercialSubjectItem item = itemToCommercialSubjectItemConverter.convert(itemFields.getItemDataSource());
			
			if( item.subject().id().orElse(-1L) <=0 ) {
				ReflectionUtils.doWithFields(item.getClass(), f -> f.set(item, null), f -> f.getType().equals(Subject.class));
				
			}
			
			
			if( ! validationUtil.validate(item, itemFields, userModel.getLocale()) ) {
				return;
			}
			commercialSubjectModel.save(item);
			
			refresh(itemTable);
			
		});
		saveItemButton.setIcon(newIcon);
		final Button newItemButton = new Button();
		
		newItemButton.addClickListener(e -> itemTable.setValue(null));
		newItemButton.setEnabled(false);

		final Button deleteItemButton = new Button();
		deleteItemButton.setEnabled(false);
		buttonItemLayout.addComponent(saveItemButton);
		buttonItemLayout.addComponent(newItemButton);
		buttonItemLayout.addComponent(deleteItemButton);
		itemTableLayout.addComponent(buttonItemLayout);

		deleteItemButton.addClickListener(e -> {
			commercialSubjectModel.delete(itemToCommercialSubjectItemConverter.convert(itemFields.getItemDataSource()));
			refresh(itemTable);
		});

		
		
		itemTable.setSelectable(true);
		
	

		itemTable.setWidth("100%");
		itemTable.setPageLength(2);

		itemTableLayout.addComponent(itemTable);
		editor.addComponent(itemTableLayout);
		
		
		final HorizontalLayout buttonValueLayout = new HorizontalLayout();

		buttonValueLayout.setMargin(new MarginInfo(false, false, true, false));
		buttonValueLayout.setSpacing(true);
		
		final VerticalLayout valueTableLayout = new VerticalLayout();

		valueTableLayout.setVisible(false);
		valueTableLayout.setMargin(new MarginInfo(false, false, false, false));
		
		final HorizontalLayout valueEditorLayout = new HorizontalLayout();
		valueEditorLayout.setWidth("100%");
		final FormLayout valueCols = new FormLayout();
		valueCols.setWidth("100%");
		valueEditorLayout.addComponent(valueCols);
		valueTableLayout.addComponent(valueEditorLayout);

		final FieldGroup valueFields = new FieldGroup();
		valueFields.setItemDataSource( commercialSubjectItemCondition.convert(BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class)));
		
		Arrays.asList(ConditionValueCols.values()).stream().filter( v -> v.visible()).forEach(v -> {
			final Field<?> field = v.newField();
			field.setCaption(v.name());
			valueCols.addComponent(field);
			valueFields.bind(field, v);
		});
		
	
		getConditionBox(valueFields).setItemCaptionPropertyId(ConditionCols.ConditionType);
		
		
		
		
		//conditionBox.setContainerDataSource(entriesToConatainerConverter.convert(commercialSubjectModel.getSubjects()));
		
		final Button saveValueButton = new Button("speichern");
		final Table valueTable = new Table();
		valueTable.setCaption("Werteauswahl");
		
		saveValueButton.setIcon(newIcon);
		
		saveValueButton.addClickListener(e -> {
			System.out.println("*******");
			System.out.println(getConditionBox(valueFields).getValue());
			commit(valueFields);
		});
		
		final Button newValueButton = new Button("neu");
		
		newValueButton.setEnabled(false);

		buttonValueLayout.addComponent(saveValueButton);
		buttonValueLayout.addComponent(newValueButton);
		
		valueTableLayout.addComponent(buttonValueLayout);

		valueTable.setSelectable(true);

		valueTable.setWidth("100%");
		valueTable.setPageLength(2);

		valueTableLayout.addComponent(valueTable);
		
		
		editor.addComponent(valueTableLayout);
		
		
		splitPanel.addComponent(editor);

		commercialSubjectModel.register(e -> {
			validationUtil.reset(editorFields);

			editorFields.setItemDataSource(null);
			newButton.setEnabled(false);

			deleteButton.setEnabled(false);
			editorFields.setItemDataSource(commercialSubjectToItemConverter.convert(commercialSubjectModel.getCommercialSubject().get()));
			itemTableLayout.setVisible(false);
			refresh(itemTable);
			if (commercialSubjectModel.getCommercialSubject().get().id().isPresent()) {
				itemTableLayout.setVisible(true);
				saveButton.setIcon(editIcon);
				newButton.setEnabled(true);
				deleteButton.setEnabled(true);
				
				return;
			}

			
			
			saveButton.setIcon(newIcon);

			
			
		}, CommercialSubjectModel.EventType.CommericalSubjectChanged);
		
		
		
		itemTable.addValueChangeListener(e ->{ 
			
			commercialSubjectModel.setCommercialSubjectItemId(e.getProperty().getValue() != null ? (Long) itemTable.getItem(e.getProperty().getValue()).getItemProperty(CommercialSubjectItemCols.Id).getValue() : null);
			
			valueTableLayout.setVisible(e.getProperty().getValue() != null);		
			
			
		}
		);
		
		commercialSubjectModel.register(e -> {
			validationUtil.reset(itemFields);
		
			itemFields.setItemDataSource(null);
			newItemButton.setEnabled(false);
			deleteItemButton.setEnabled(false);

			itemFields.setItemDataSource(commercialSubjectItemConverter.convert(commercialSubjectModel.getCommercialSubjectItem().get()));
			
			getConditionBox(valueFields).setContainerDataSource(null);
			if (commercialSubjectModel.getCommercialSubjectItem().get().id().isPresent()) {
				saveItemButton.setIcon(editIcon);
				newItemButton.setEnabled(true);
				deleteItemButton.setEnabled(true);
				
				

				getConditionBox(valueFields).setContainerDataSource(conditionToContainerConverter.convert(commercialSubjectModel.getConditions()));
				
				return;
			}
			
			saveItemButton.setIcon(newIcon);

		}, CommercialSubjectModel.EventType.CommericalSubjectItemChanged);
		
		
		userModel.register(e-> {
			editorNameField.setCaption(message(I18N_COMMERCIAL_SUBJECT_NAME));
			searchNameField.setCaption(message(I18_COMMERCIAL_SUBJECT_SEARCH_NAME));
			searchItemNameField.setCaption(message(I18N_COMMERCIAL_SUBJECT_SEARCH_ITEM_NAME));
			searchButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_SEARCH));
			subjectList.setColumnHeader(CommercialSubjectCols.Name, message(I18N_COMMERCIAL_SUBJECT_TABLE_NAME));
			saveButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_SAVE));
			newButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_NEW));
			deleteButton.setCaption(message(I18N_COMMERCIAL_SUBJECT_DELETE));
			saveItemButton.setCaption(message(I18N_SAVE_ITEM_BUTTON));
			mandatoryBox.setItemCaption(Boolean.TRUE, message(I18N_MANDATORY_BOX_TRUE));
			mandatoryBox.setItemCaption(Boolean.FALSE, message(I18N_MANDATORY_BOX_FALSE));
			
			
			newItemButton.setCaption(message(I18N_NEW_ITEM_BUTTON));
			
			deleteItemButton.setCaption(message(I18N_DELETE_ITEM_BUTTON));
			
			itemTable.setCaption(message(I18N_ITEM_TABLE_CAPTION));
			
			
			Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col -> col.visible()||CommercialSubjectItemCols.Subject == col ).forEach(col -> {
				itemFields.getField(col).setCaption(message(I18N_COMMERCIAL_SUBJECT_ITEM_PREFIX + col.name().toLowerCase()));
			});
			
			
		}, UserModel.EventType.LocaleChanged);

	}

	private ComboBox getConditionBox(final FieldGroup valueFields) {
		return (ComboBox) valueFields.getField(ConditionValueCols.Condition);
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
	
	private void refresh(final Table itemTable) {
		itemTable.setContainerDataSource(commercialSubjectItemToContainerConverter.convert(commercialSubjectModel.getCommercialSubject().get().commercialSubjectItems()));
		itemTable.setVisibleColumns(Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col -> col.visible()).collect(Collectors.toList()).toArray());
		itemTable.setValue(null);
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
