package de.mq.merchandise.util.support;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.navigator.View;

import de.mq.merchandise.subject.support.UserModel;


public class LanguageFilterTest {
	
	private BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	private final Filter filter = new LanguageFilterImpl(beanResolver);
	
	private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	private final HttpServletResponse response  = Mockito.mock(HttpServletResponse.class);
	private final FilterChain filterChain = Mockito.mock(FilterChain.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	
	@Before
	public final void setup() {
		
		Mockito.when(beanResolver.resolve(UserModel.class)).thenReturn(userModel);
	
		Mockito.when(request.getLocale()).thenReturn(Locale.ENGLISH);
	}
	
	@Test
	public final void doFilterInternalLanguageFromParameter() throws ServletException, IOException {
		
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.when(request.getParameter(LanguageFilterImpl.PARAM_LANGUAGE)).thenReturn(Locale.GERMAN.getLanguage());
		
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel).setLocale(Locale.GERMAN);
		Mockito.verify(filterChain).doFilter(request, response);
		Mockito.verify(beanResolver).resolveAll(View.class);
		
	}
	
	@Test
	public final void doFilterInternalLanguageFromRequest() throws ServletException, IOException {
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel).setLocale(Locale.ENGLISH);
		Mockito.verify(filterChain).doFilter(request, response);
		Mockito.verify(beanResolver).resolveAll(View.class);
		
	}
	
	@Test
	public final void doFilterInternalLanguageFromRequestLanguageAware() throws IOException, ServletException{
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel, Mockito.times(0)).setLocale(Mockito.any(Locale.class));
		Mockito.verify(filterChain).doFilter(request, response);
		Mockito.verify(beanResolver).resolveAll(View.class);
	}

}
