package de.mq.merchandise.util.support;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaadin.navigator.View;

import de.mq.merchandise.subject.support.UserModel;

@Component("languageFilter")
public class LanguageFilterImpl  extends OncePerRequestFilter {

	static final String PARAM_LANGUAGE = "language";
	private final BeanContainerOperations beanContainerOperations;

	@Autowired
	public LanguageFilterImpl(final BeanContainerOperations beanContainerOperations) {

		this.beanContainerOperations = beanContainerOperations;
	}

	 @Override
	 protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String languge = request.getParameter(PARAM_LANGUAGE);
		final UserModel userModel = beanContainerOperations.requiredSingelBean(UserModel.class);
	
		beanContainerOperations.beansForFilter(ctx -> ctx.getBeansOfType(View.class).values());
	
		
		if (StringUtils.hasText(languge)) {
			final Locale locale = StringUtils.parseLocaleString(languge.toLowerCase());
			userModel.setLocale(locale);
		} else if( userModel.getLocale() == null) {
			userModel.setLocale(request.getLocale());
		}
		chain.doFilter(request, response);
	}

}
