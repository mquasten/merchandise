package de.mq.merchandise.subject.support;


import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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

import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;



@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl  extends CustomComponent implements View {
	

	private static final long serialVersionUID = 1L;
	


	private final RefreshableContainer lazyQueryContainer;
	

	private final MainMenuBarView mainMenuBarView;

	private final Item commercialSubjectSearchItem;
	
	@Autowired
	CommercialSubjectViewImpl(final MessageSource messageSource, ViewNav viewNav, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar) final MainMenuBarView mainMenuBarView, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectSearchItem) final Item commercialSubjectSearchItem ) {
		this.mainMenuBarView = mainMenuBarView;
		this.lazyQueryContainer=lazyQueryContainer;
		this.commercialSubjectSearchItem=commercialSubjectSearchItem;
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
		
		
		
		
		
		

		final VerticalLayout leftLayout = new VerticalLayout();
	
		leftLayout.addComponent(mainMenuBarView);
		
		splitPanel.addComponent(leftLayout);
		leftLayout.setSizeFull();

		leftLayout.addComponent(searchPanel);
		final Table subjectList = new Table();
		
		leftLayout.addComponent(subjectList);
		
	// leftLayout.setExpandRatio(subjectList, 1);
			subjectList.setSizeFull();

			subjectList.setSelectable(true);

			subjectList.setContainerDataSource(lazyQueryContainer);
			subjectList.setVisibleColumns(Arrays.asList(CommercialSubjectCols.values()).stream().filter(col -> col.visible()).toArray());
			subjectList.setSortContainerPropertyId(CommercialSubjectCols.Name);
		
			//subjectList.addValueChangeListener(e -> subjectModel.setSubjectId((Long) e.getProperty().getValue()));

			//subjectModel.register(event -> lazyQueryContainer.refresh(), SubjectModel.EventType.SearchCriteriaChanged);
		
		
		
	}

	


	private void searchCriteria2Model(final FieldGroup fieldGroup) {
		try {

			fieldGroup.commit();
			System.out.println(">>>" + commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.Name).getValue());
			System.out.println(">>>" + commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.ItemName).getValue());
			//subjectModel.setSerachCriteria(itemToSubjectMapper.convert(fieldGroup.getItemDataSource()));

		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
	


	@PostConstruct
	void init() {	
		initLayout();
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	

}
