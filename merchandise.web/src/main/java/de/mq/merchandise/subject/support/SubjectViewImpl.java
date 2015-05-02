package de.mq.merchandise.subject.support;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.RefreshableContainer;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class SubjectViewImpl extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private final Converter<Item, Subject> itemToSubjectConverter;

	private final RefreshableContainer lazyQueryContainer;

	private final Item subjectItem;

	private final SubjectModel subjectModel;

	private final UserModel userModel;

	@Autowired
	public SubjectViewImpl(@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToSubjectConverter) final Converter<Item, Subject> itemToSubjectConverter, @SubjectModelQualifier(SubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectSearchItem) final Item subjectItem,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectModel) final SubjectModel subjectModel, final UserModel userModel) {

		this.itemToSubjectConverter = itemToSubjectConverter;
		this.lazyQueryContainer = lazyQueryContainer;
		this.subjectItem = subjectItem;
		this.subjectModel = subjectModel;
		this.userModel = userModel;
	}

	private void initLayout() {

		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		setCompositionRoot(splitPanel);

		final VerticalLayout leftLayout = new VerticalLayout();

		final TextField searchName = new TextField();
		final TextField searchDescription = new TextField();
		final FieldGroup fieldGroup = new FieldGroup();

		// itemContainerFactory.assign(fieldGroup, SubjectCols.class);
		fieldGroup.setItemDataSource(subjectItem);

		final HorizontalLayout searchLayout = new HorizontalLayout();

		searchLayout.setSpacing(true);
		final FormLayout col1Layout = new FormLayout();

		searchLayout.addComponent(col1Layout);
		col1Layout.addComponent(searchName);

		final FormLayout col2Layout = new FormLayout();
		searchLayout.addComponent(col2Layout);
		col2Layout.addComponent(searchDescription);

		searchDescription.setCaption("Beschreibung");

		final FormLayout col3Layout = new FormLayout();
		searchLayout.addComponent(col3Layout);

		final Button searchButton = new Button("suchen");
		col3Layout.addComponent(searchButton);

		fieldGroup.setBuffered(true);
		fieldGroup.bind(searchName, SubjectCols.Name);
		fieldGroup.bind(searchDescription, SubjectCols.Description);

		searchButton.addClickListener(e -> searchCriteria2Model(fieldGroup));

		leftLayout.addComponent(searchLayout);

		final Table contactList = new Table();

		final FormLayout editorLayout = new FormLayout();

		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(contactList);

		leftLayout.setSizeFull();

		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		editorLayout.setMargin(true);
		editorLayout.setVisible(false);

		contactList.setSelectable(true);

		contactList.setContainerDataSource(lazyQueryContainer);
		contactList.setVisibleColumns(Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).toArray());
		contactList.setSortContainerPropertyId(SubjectCols.Name);

		subjectModel.register(event -> lazyQueryContainer.refresh(), SubjectModel.EventType.SearchCriteriaChanged);

	}

	private void searchCriteria2Model(final FieldGroup fieldGroup) {
		try {

			fieldGroup.commit();
			subjectModel.setSerachCriteria(itemToSubjectConverter.convert(fieldGroup.getItemDataSource()));

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
