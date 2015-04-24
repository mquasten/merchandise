package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.Query;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
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
	
	
	
	
	enum SubjectCols {
		
		Id(false, Long.class, -1L),
		Name(true, String.class, ""),
		Description(true, String.class, ""),
		DateCreated(true, Date.class, null);
		
		private final boolean visible ;
		
		SubjectCols(boolean visible, Class<?> type, Object defaultValue) {
			this.visible=visible;
		}
		boolean visible() {
			return visible;
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
	
		
		final LazyQueryContainer container = newContainer();
	
		
		
		
		
		
		contactList.setContainerDataSource(container);
		
		contactList.setVisibleColumns(Arrays.asList(SubjectCols.values()).stream().filter(col -> col.visible() ).collect(Collectors.toList()).toArray());
	
		contactList.setSelectable(true);
		
		contactList.setBuffered(true);
		contactList.setSortContainerPropertyId(SubjectCols.Name);

	}

	private LazyQueryContainer newContainer() {
		
		final LazyQueryContainer result =  new LazyQueryContainer(queryDefinition -> new Query() {
		
				
				@Override
				public Item constructItem() {
				    // Diese Methode ist Sinnlos!!!
					return null;
				}
		
				@Override
				public boolean deleteAllItems() {
					throw new UnsupportedOperationException();
					
				}
		
				@Override
				public List<Item> loadItems(final int startIndex, int count) {
					final Item item1 = new PropertysetItem();
				
					System.out.println("loadItems");
					System.out.println(contactList.getSortContainerPropertyId() + ":"+  contactList.isSortAscending());
					
					item1.addItemProperty(SubjectCols.Id, new  ObjectProperty<>(19680528L));
					item1.addItemProperty(SubjectCols.Name, new  ObjectProperty<>("Pets4You"));
					item1.addItemProperty(SubjectCols.Description, new  ObjectProperty<>("Pussycat Escort-Service"));
					
					item1.addItemProperty(SubjectCols.DateCreated, new  ObjectProperty<>(new Date()));
					
					final Item item2 = new PropertysetItem();
				
				
					
					item2.addItemProperty(SubjectCols.Id, new  ObjectProperty<>(4711));
					item2.addItemProperty(SubjectCols.Name, new  ObjectProperty<>("Crime Bank"));
					item2.addItemProperty(SubjectCols.Description, new  ObjectProperty<>("Don Colerone's Banking Service"));
					
					item2.addItemProperty(SubjectCols.DateCreated, new  ObjectProperty<>(new Date()));
					
					
					final List<Item>  results = new ArrayList<>();
					results.add(item1);
					results.add(item2);
					return results;
				}
		
				@Override
				public void saveItems(final List<Item> added, final List<Item> modified, final List<Item> removed) {
						throw new UnsupportedOperationException();
					
				}
		
				@Override
				public int size() {
					
					System.out.println("size");
					// Backend
					return 2;
				}} ,SubjectCols.Id, 50, true);
		
		     Arrays.asList(SubjectCols.values()).forEach(col -> result.addContainerProperty(col, String.class , "", true,true)); 
		
			
		return result; 
	}
	

	

	@Override
	protected void init() {
	
		System.out.println("********************");
		System.out.println(customerService);
		System.out.println("********************");
	
		initLayout();
		initContactList();
		initEditor();
		initSearch();
		
		
	}
	
	

}
