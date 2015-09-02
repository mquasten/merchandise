package de.mq.merchandise.subject.support;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;



@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl  extends CustomComponent implements View {
	

	private static final long serialVersionUID = 1L;
	


	private final RefreshableContainer lazyQueryContainer;
	

	private final MainMenuBarView mainMenuBarView;

	
	
	@Autowired
	CommercialSubjectViewImpl(final MessageSource messageSource, ViewNav viewNav, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar) final MainMenuBarView mainMenuBarView, @CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.LazyQueryContainer) final RefreshableContainer lazyQueryContainer ) {
		this.mainMenuBarView = mainMenuBarView;
		this.lazyQueryContainer=lazyQueryContainer;
	}

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout() {

		
		
		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		splitPanel.setSplitPosition(64, Unit.PERCENTAGE);

		setCompositionRoot(splitPanel);

		final VerticalLayout leftLayout = new VerticalLayout();
	
		leftLayout.addComponent(mainMenuBarView);
		
		splitPanel.addComponent(leftLayout);
		leftLayout.setSizeFull();


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

	


	


	@PostConstruct
	void init() {	
		initLayout();
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	

}
