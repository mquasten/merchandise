package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.customer.CustomerService;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;


/* 
 * UI class is the starting point for your app. You may deploy it with VaadinServlet
 * or VaadinPortlet by giving your UI class name a parameter. When you browse to your
 * app a web page showing your UI is automatically generated. Or you may choose to 
 * embed your UI to an existing web page. 
 */
@Title("Addressbook")
// "valo", "reindeer", "runo", "chameleon" , "liferay"
@Theme( "valo")
public class AddressbookUI extends AbstractUIBeanInjector {
	

	private static final long serialVersionUID = 1L;
	
	
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private LazyQueryContainerFactory lazyQueryContainerFactory;
	
	
	 enum SubjectCols implements TableContainerColumns {
		
		Id(false, Long.class , "id"),
		Name(true, String.class , "COALESCE(name, '')" ),
		Description(true, String.class,  "COALESCE(description , '')"),
		DateCreated(true, Date.class, "date_created");
		
		private final boolean visible ;
		private final Class<?> type;
		
		private final String orderBy;
		
		SubjectCols(final boolean visible, final Class<?> type, final String orderBy) {
			this.visible=visible;
			this.type=type;
			this.orderBy=orderBy;
		}
		@Override
		public boolean visible() {
			return visible;
		}
		@Override
		public Class<?> target() {
			return type;
		}
		@Override
		public boolean sortable() {
			return true;
		}
		@Override
		public String orderBy() {
			return orderBy;
		}
		
		
	}
	
	 
	/* User interface components are stored in session. */
	private Table contactList = new Table();
	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button removeContactButton = new Button("Remove this contact");
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();

	private static final String FNAME = "First Name";
	private static final String LNAME = "Last Name";
	private static final String COMPANY = "Company";
	private static final String[] fieldNames = new String[] { FNAME, LNAME,
			COMPANY, "Mobile Phone", "Work Phone", "Home Phone", "Work Email",
			"Home Email", "Street", "City", "Zip", "State", "Country" , "Distance"};

	/*
	 * Any component can be bound to an external data source. This example uses
	 * just a dummy in-memory list, but there are many more practical
	 * implementations.
	 */
	

	

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout() {

		/* Root of the user interface component tree is set */
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		setContent(splitPanel);

		/* Build the component tree */
		VerticalLayout leftLayout = new VerticalLayout();
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(contactList);
		final HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(addNewContactButton);

		/* Set the contents in the left of the split panel to use all the space */
		leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the contactList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
		editorLayout.setMargin(true);
		editorLayout.setVisible(false);
	
		
		
	}

	private void initEditor() {

		editorLayout.addComponent(removeContactButton);
		
      Arrays.asList(fieldNames).stream().filter( name -> ! "Distance".equalsIgnoreCase(name)).forEach( name ->{
			TextField field = new TextField(name);
			editorLayout.addComponent(field);
			field.setWidth("100%");

			
			editorFields.bind(field, name);
			
			
		});

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		editorFields.setBuffered(false);
	}

	private void initSearch() {

		
		searchField.setInputPrompt("Search contacts");

		
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		removeContactButton.addClickListener(new ClickListener() {
		
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Object contactId = contactList.getValue();
				contactList.removeItem(contactId);
			}
		});
	}

	private void initContactList() {
	
		
		
	
		
		
		
		
		contactList.setSortContainerPropertyId(SubjectCols.Name);
		contactList.setSelectable(true);
		
		contactList.setBuffered(true);
		final Subject subject = new SubjectImpl(customerService.customer(Optional.of(1L)), "XXX");
		final Field field = ReflectionUtils.findField(subject.getClass(),"name");
		field.setAccessible(true);
		ReflectionUtils.setField(field, subject, null);
		lazyQueryContainerFactory.assign(contactList, SubjectCols.Id, SubjectConverterImpl.class, SubjectControllerImpl.class, subject);
	
		
		

	}



	

	@Override
	protected void init() {
	
		
	
		initLayout();
		initContactList();
		initEditor();
		initSearch();
		
		
	}
	
	

}
