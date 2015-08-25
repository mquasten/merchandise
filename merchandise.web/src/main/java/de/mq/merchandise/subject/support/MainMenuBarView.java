package de.mq.merchandise.subject.support;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.mq.merchandise.util.support.ViewNav;





@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class MainMenuBarView extends CustomComponent {



	private static final long serialVersionUID = 1L;



	
	private final MessageSource messageSource;
	private final UserModel userModel;
	
	private final ViewNav viewNav;

	
	@Autowired
	MainMenuBarView(final UserModel userModel, final MessageSource messageSource, final ViewNav viewNav) {
		this.userModel=userModel;
		this.messageSource = messageSource;
		this.viewNav=viewNav;
	}

	@PostConstruct
	void init() {

		setLocale(userModel.getLocale());
		final MenuBar menubar = new MenuBar();
	
		menubar.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		setCompositionRoot(menubar);
		

		
		

		final VerticalLayout content = new VerticalLayout();
		final FormLayout formLayout = new FormLayout();
		content.addComponent(formLayout);
		

	
		

		content.setMargin(true);
	
		

	
		userModel.register(event -> {

		 menubar.removeItems();
			
			final MenuItem settings = menubar.addItem(getString("menu_product"), null);
		
			settings.addItem(getString("menu_product_template"), item -> viewNav.navigateTo(SubjectViewImpl.class));
			settings.addItem(getString("menu_product_definition"), item -> viewNav.navigateTo(CommercialSubjectViewImpl.class));
		
			

			

		}, UserModel.EventType.LocaleChanged);
	}

	private String getString(final String key) {
		return messageSource.getMessage(key, null, getLocale());
	}

}
