package de.mq.merchandise.subject;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;

import de.mq.merchandise.subject.support.SubjectViewImpl;
import de.mq.merchandise.subject.support.UserModel;
import de.mq.merchandise.util.support.AbstractUIBeanInjector;
import de.mq.merchandise.util.support.VaadinOperations;
import de.mq.merchandise.util.support.ViewNav;

@Theme( "valo")
public class StartViewImpl extends AbstractUIBeanInjector {

	
	private static final long serialVersionUID = 1L;

	@Autowired
	private SubjectViewImpl subjectView;
	
	@Autowired
	private Collection<View> views ; 
	@Autowired
	private ViewNav viewNav;
	@Autowired
	private VaadinOperations vaadinOperations;
	
	@Autowired
	private UserModel  userModel;
	
	@Override
	protected void init() {
		//setContent(subjectView);
		
		viewNav.create(subjectView, views, vaadinOperations);
		userModel.setLocale(locale());
		
	}

}
