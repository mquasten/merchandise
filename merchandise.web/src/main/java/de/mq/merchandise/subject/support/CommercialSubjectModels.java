package de.mq.merchandise.subject.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import de.mq.merchandise.util.support.ViewNav;

@Configuration
class CommercialSubjectModels {
	
	@Autowired
	private MessageSource messageSource;
	
	
	@Bean()
	@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.MenuBar)
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	MainMenuBarView mainMenuBaCommercialSubject(final UserModel userModel, final ViewNav viewNav) {
		return new MainMenuBarView(userModel, messageSource, viewNav);
	}

}
