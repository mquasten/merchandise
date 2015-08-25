package de.mq.merchandise.subject.support;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.merchandise.subject.support.UserModel.EventType;
import de.mq.merchandise.util.support.ViewNav;



@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CommercialSubjectViewImpl  extends CustomComponent implements View {
	

	private static final long serialVersionUID = 1L;
	



	

	MainMenuBarView mainMenuBarView;
	final UserModel userModel;
	
	
	@Autowired
	CommercialSubjectViewImpl( final UserModel userModel, MessageSource messageSource, ViewNav viewNav) {
		this.mainMenuBarView = new MainMenuBarView(userModel, messageSource, viewNav);
		mainMenuBarView.init();
		this.userModel=userModel;
	}

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout() {

		
		System.out.println("fuck");
		

		final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

		splitPanel.setSplitPosition(64, Unit.PERCENTAGE);

		setCompositionRoot(splitPanel);

		final VerticalLayout leftLayout = new VerticalLayout();
		userModel.notifyObservers(EventType.LocaleChanged);
		leftLayout.addComponent(mainMenuBarView);
		
		splitPanel.addComponent(leftLayout);
		leftLayout.setSizeFull();

	leftLayout.addComponent(new TextField());;
		
		
		
	}

	


	


	@PostConstruct
	void init() {
	System.out.println("***************************************");
		userModel.notifyObservers(EventType.LocaleChanged);
	
		initLayout();
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	

}
