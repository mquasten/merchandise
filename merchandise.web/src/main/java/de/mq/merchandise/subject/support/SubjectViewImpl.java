package de.mq.merchandise.subject.support;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.LazyQueryContainerFactory;



@Title("Subjects")
// "valo", "reindeer", "runo", "chameleon" , "liferay"
@Theme( "valo")
public class SubjectViewImpl extends AbstractUIBeanInjector {
	

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private LazyQueryContainerFactory lazyQueryContainerFactory;
	
	@Autowired
	private ItemContainerFactory itemContainerFactory;
	
	@Autowired
	private SubjectModel subjectModel;
	@Autowired
	private UserModel userModel;
	
	

	private void initLayout() {

		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		setContent(splitPanel);

	
		final VerticalLayout leftLayout = new VerticalLayout();
		
		final TextField searchName = new TextField();
		final TextField searchDescription = new TextField();
		final FieldGroup fieldGroup = new FieldGroup();
		
		itemContainerFactory.assign(fieldGroup, SubjectCols.class);
		
		final HorizontalLayout searchLayout = new HorizontalLayout();
	
		searchLayout.setSpacing(true);
		final FormLayout col1Layout  = new FormLayout();
		
		searchLayout.addComponent(col1Layout);
		col1Layout.addComponent(searchName);
		
		final FormLayout col2Layout  = new FormLayout();
		searchLayout.addComponent(col2Layout);
		col2Layout.addComponent(searchDescription);
		
		searchDescription.setCaption("Beschreibung");
		
	
		final FormLayout col3Layout  = new FormLayout();
		searchLayout.addComponent(col3Layout);
	
		final Button searchButton = new Button("suchen");
		col3Layout.addComponent(searchButton);
		
		fieldGroup.setBuffered(true);
		fieldGroup.bind(searchName, SubjectCols.Name);
		fieldGroup.bind(searchDescription, SubjectCols.Description);
		
		
		searchButton.addClickListener(e ->  searchCriteria2Model(fieldGroup)) ;
		
		leftLayout.addComponent(searchLayout);
		
		final Table contactList = new Table();
		

		final FormLayout editorLayout = new FormLayout();

		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(contactList);
	
		searchName.setCaption("Name");

		
		leftLayout.setSizeFull();

		
		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		
		editorLayout.setMargin(true);
		editorLayout.setVisible(false);
	
		contactList.setSortContainerPropertyId(SubjectCols.Name);
		contactList.setSelectable(true);
		
		contactList.setBuffered(true);
		
		lazyQueryContainerFactory.assign(contactList, SubjectCols.Id, SubjectConverterImpl.class, SubjectModelControllerImpl.class);
	
	
		subjectModel.register(event ->  ((LazyQueryContainer) contactList.getContainerDataSource()).refresh(), SubjectModel.EventType.SearchCriteriaChanged);
		
		
	}

	private void searchCriteria2Model(final FieldGroup fieldGroup) {
		try {
			 
			fieldGroup.commit();
			subjectModel.setSerachCriteria(conversionService.convert(fieldGroup.getItemDataSource(), Subject.class));
			
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	

	




	

	

	@Override
	protected void init() {
	
		subjectModel.setCustomer(userModel.getCustomer());
	
		initLayout();
	
		
		
	}
	
	

}
