package de.mq.merchandise.subject;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaadin.navigator.View;

import de.mq.merchandise.subject.support.UserModel;

@Component("languageFilter")
public class LanguageFilterImpl extends OncePerRequestFilter {

	private final ApplicationContext applicationContext;

	@Autowired
	public LanguageFilterImpl(ApplicationContext applicationContext) {

		this.applicationContext = applicationContext;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String languge = request.getParameter("language");
		final UserModel userModel = applicationContext.getBean(UserModel.class);
		// touched for the very first time
		applicationContext.getBeansOfType(View.class); 
		
		
		if (StringUtils.hasText(languge)) {
			final Locale locale = StringUtils.parseLocaleString(languge.toLowerCase());
			userModel.setLocale(locale);
		} else if( userModel.getLocale() == null) {
			userModel.setLocale(request.getLocale());
		}
		chain.doFilter(request, response);
	}

}
