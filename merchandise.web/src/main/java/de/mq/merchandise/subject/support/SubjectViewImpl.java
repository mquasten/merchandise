package de.mq.merchandise.subject.support;


import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.RefreshableContainer;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class SubjectViewImpl extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;
	
	static final String I18N_SUBJECT_TABLE_HEADLINE = "subject_table_headline";
	static final String I18N_SUBJECT_SEARCH_HEADLINE = "subject_search_headline";
	static final String I18N_SUBJECT_SEARCH_BUTTON = "subject_search_button";
	static final String I18N_SUBJECT_SEARCH_NAME = "subject_search_name";
	static final String I18N_SUBJECT_SEARCH_DESCRIPTION = "subject_search_description";
	static final String I18N_SUBJECT_TABLE_PREFIX = "subject_table_";
	
	private final Converter<Item, Subject> itemToSubjectConverter;
	private final RefreshableContainer lazyQueryContainer;
	private final Item subjectItem;
	private final SubjectModel subjectModel;
	private final UserModel userModel;
	private final MessageSource messageSource;
	
	

	@Autowired
	public SubjectViewImpl(@SubjectModelQualifier(SubjectModelQualifier.Type.ItemToSubjectConverter) final Converter<Item, Subject> itemToSubjectConverter, @SubjectModelQualifier(SubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectSearchItem) final Item subjectItem,
			@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectModel) final SubjectModel subjectModel, final UserModel userModel, final MessageSource messageSource) {

		this.itemToSubjectConverter = itemToSubjectConverter;
		this.lazyQueryContainer = lazyQueryContainer;
		this.subjectItem = subjectItem;
		this.subjectModel = subjectModel;
		this.userModel = userModel;
		this.messageSource=messageSource;
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

		final Panel searchPanel = new Panel();
		
		//searchPanel.setCaption("Produkt-Template suchen");
		
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
		
		//searchDescription.setCaption("Beschreibungxx");

		

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		final Button searchButton = new Button();
		buttonLayout.addComponent(searchButton);
		
		//buttonLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_CENTER);
		buttonLayout.setWidth("100%");
		searchBox.addComponent(buttonLayout, 0,1);
		
		
	
		//searchLayout.addComponent(searchButton);
		//searchLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_LEFT);

	  
		
		fieldGroup.setBuffered(true);
		fieldGroup.bind(searchName, SubjectCols.Name);
		fieldGroup.bind(searchDescription, SubjectCols.Description);

		searchButton.addClickListener(e -> searchCriteria2Model(fieldGroup));

		leftLayout.addComponent(searchPanel);

		final Table subjectList = new Table();

		final FormLayout editorLayout = new FormLayout();

		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(subjectList);

		leftLayout.setSizeFull();

		leftLayout.setExpandRatio(subjectList, 1);
		subjectList.setSizeFull();

		editorLayout.setMargin(true);
		editorLayout.setVisible(false);

		subjectList.setSelectable(true);

		subjectList.setContainerDataSource(lazyQueryContainer);
		subjectList.setVisibleColumns(Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).toArray());
		subjectList.setSortContainerPropertyId(SubjectCols.Name);

		subjectModel.register(event -> lazyQueryContainer.refresh(), SubjectModel.EventType.SearchCriteriaChanged);

	    //subjectList.setCaption("Produkt-Templates");
	    
	    userModel.register(o -> {
	    	searchDescription.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_DESCRIPTION, null, userModel.getLocale()));
	         searchName.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_NAME, null, userModel.getLocale() ));
	         searchButton.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_BUTTON, null, userModel.getLocale() ));
	         searchPanel.setCaption(messageSource.getMessage(I18N_SUBJECT_SEARCH_HEADLINE, null, userModel.getLocale()));
	         subjectList.setCaption(messageSource.getMessage(I18N_SUBJECT_TABLE_HEADLINE, null, userModel.getLocale()));
	         Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible()).forEach(col ->  subjectList.setColumnHeader(col, messageSource.getMessage(I18N_SUBJECT_TABLE_PREFIX + StringUtils.uncapitalize(col.name()), null, userModel.getLocale())));
	         
	    }, UserModel.EventType.LocaleChanged);
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
