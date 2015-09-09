package de.mq.merchandise.subject.support;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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

import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private final RefreshableContainer lazyQueryContainer;

	private UserModel userModel;

	private final MainMenuBarView mainMenuBarView;

	private final Item commercialSubjectSearchItem;

	private final CommercialSubjectModel commercialSubjectModel;

	private final Converter<Item, CommercialSubject> itemToCommercialSubjectConverter;

	private final ValidationUtil validationUtil;

	private final Converter<CommercialSubject, Item> commercialSubjectToItemConverter;

	final ThemeResource editIcon = new ThemeResource("edit-icon.png");
	final ThemeResource newIcon = new ThemeResource("new-icon.png");

	@Autowired
	CommercialSubjectViewImpl(final CommercialSubjectModel commercialSubjectModel, final UserModel userModel, final MessageSource messageSource, ViewNav viewNav, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar) final MainMenuBarView mainMenuBarView, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectSearchItem) final Item commercialSubjectSearchItem, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemToCommercialSubjectConverter) Converter<Item, CommercialSubject> itemToCommercialSubjectConverter, final ValidationUtil validationUtil, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter) final Converter<CommercialSubject, Item> commercialSubjectToItemConverter) {
		this.mainMenuBarView = mainMenuBarView;
		this.lazyQueryContainer = lazyQueryContainer;
		this.commercialSubjectSearchItem = commercialSubjectSearchItem;
		this.itemToCommercialSubjectConverter = itemToCommercialSubjectConverter;
		this.commercialSubjectModel = commercialSubjectModel;
		this.userModel = userModel;
		this.validationUtil = validationUtil;
		this.commercialSubjectToItemConverter = commercialSubjectToItemConverter;
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
		TextField nameField = new TextField("Name");
		col1Layout.addComponent(nameField);

		col1Layout.setMargin(new MarginInfo(true, false, false, true));

		searchLayout.addComponent(col1Layout);

		final FormLayout col2Layout = new FormLayout();
		TextField itemNameField = new TextField("ItemName");
		col2Layout.addComponent(itemNameField);

		col2Layout.setMargin(new MarginInfo(true, false, false, true));
		searchLayout.addComponent(col2Layout);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		final Button searchButton = new Button("search");

		searchButton.addClickListener(e -> searchCriteria2Model(fieldGroup));

		buttonLayout.addComponent(searchButton);

		buttonLayout.setWidth("100%");
		searchBox.addComponent(buttonLayout, 0, 1);
		fieldGroup.bind(nameField, CommercialSubjectCols.Name);
		fieldGroup.bind(itemNameField, CommercialSubjectCols.ItemName);

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

		final TextField field = new TextField("xxxx");
		field.setNullRepresentation("");
		field.setSizeFull();

		editorFields.bind(field, CommercialSubjectCols.Name);
		col1.addComponent(field);

		editorFields.setItemDataSource(commercialSubjectToItemConverter.convert(commercialSubjectModel.getCommercialSubject().get()));

		final Button saveButton = new Button();
		final Button newButton = new Button();
		final Button deleteButton = new Button();
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setIcon(newIcon);

		editorLayout.addComponent(col1);
		editorLayout.setWidth("100%");

		final HorizontalLayout saveButtonLayout = new HorizontalLayout();
		saveButtonLayout.setSpacing(true);
		editor.addComponent(saveButtonLayout);

		saveButtonLayout.addComponent(saveButton);

		saveButtonLayout.addComponent(newButton);
		saveButtonLayout.addComponent(deleteButton);

		newButton.addClickListener(event -> subjectList.setValue(null));

		splitPanel.addComponent(editor);

		commercialSubjectModel.register(e -> {
			validationUtil.reset(editorFields);

			editorFields.setItemDataSource(null);
			newButton.setEnabled(false);

			deleteButton.setEnabled(false);
			editorFields.setItemDataSource(commercialSubjectToItemConverter.convert(commercialSubjectModel.getCommercialSubject().get()));

			if (commercialSubjectModel.getCommercialSubject().get().id().isPresent()) {
				saveButton.setIcon(editIcon);
				newButton.setEnabled(true);
				deleteButton.setEnabled(true);

				return;
			}

			saveButton.setIcon(newIcon);

		}, CommercialSubjectModel.EventType.CommericalSubjectChanged);

	}

	private void searchCriteria2Model(final FieldGroup fieldGroup) {
		try {

			fieldGroup.commit();
			commercialSubjectModel.setSearch(itemToCommercialSubjectConverter.convert(commercialSubjectSearchItem));
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
