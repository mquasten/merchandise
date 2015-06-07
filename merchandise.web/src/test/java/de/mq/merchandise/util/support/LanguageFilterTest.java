package de.mq.merchandise.util.support;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import com.vaadin.navigator.View;

import de.mq.merchandise.subject.support.UserModel;
import de.mq.merchandise.util.support.BeanContainerOperations.BeanFilter;


public class LanguageFilterTest {
	
	private BeanContainerOperations beanResolver = Mockito.mock(BeanContainerOperations.class);
	
	private final Filter filter = new LanguageFilterImpl(beanResolver);
	
	private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	private final HttpServletResponse response  = Mockito.mock(HttpServletResponse.class);
	private final FilterChain filterChain = Mockito.mock(FilterChain.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	
	@SuppressWarnings("unchecked")
	ArgumentCaptor<BeanFilter<?>> beanFilterCaptor = (ArgumentCaptor<BeanFilter<?>>) ArgumentCaptor.forClass((Class<?>) BeanFilter.class);
	
	final ApplicationContext ctx = Mockito.mock(ApplicationContext.class);
	
	final View view = Mockito.mock(View.class);
	
	
	
	@Before
	public final void setup() {
		
		Mockito.when(beanResolver.requiredSingelBean(UserModel.class)).thenReturn(userModel);
	
		Mockito.when(request.getLocale()).thenReturn(Locale.ENGLISH);
		
		final Map<String,View> beans = new HashMap<>();
		beans.put(view.getClass().getSimpleName(), view);
		Mockito.when(ctx.getBeansOfType(View.class)).thenReturn(beans);
	}
	
	@Test
	public final void doFilterInternalLanguageFromParameter() throws ServletException, IOException {
		
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.when(request.getParameter(LanguageFilterImpl.PARAM_LANGUAGE)).thenReturn(Locale.GERMAN.getLanguage());
		
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel).setLocale(Locale.GERMAN);
		Mockito.verify(filterChain).doFilter(request, response);
		
		Mockito.verify(beanResolver).beansForFilter(beanFilterCaptor.capture());
		
		@SuppressWarnings("unchecked")
		final Collection<View> results = (Collection<View>) beanFilterCaptor.getValue().filter(ctx);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(view, results.stream().findFirst().get());
		
	}
	
	@Test
	public final void doFilterInternalLanguageFromRequest() throws ServletException, IOException {
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel).setLocale(Locale.ENGLISH);
		Mockito.verify(filterChain).doFilter(request, response);
	
		
		Mockito.verify(beanResolver).beansForFilter(beanFilterCaptor.capture());
		
		@SuppressWarnings("unchecked")
		final Collection<View> results = (Collection<View>) beanFilterCaptor.getValue().filter(ctx);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(view, results.stream().findFirst().get());
		
	}
	
	@Test
	public final void doFilterInternalLanguageFromRequestLanguageAware() throws IOException, ServletException{
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		filter.doFilter(request, response, filterChain);
		
		Mockito.verify(userModel, Mockito.times(0)).setLocale(Mockito.any(Locale.class));
		Mockito.verify(filterChain).doFilter(request, response);
		
		Mockito.verify(beanResolver).beansForFilter(beanFilterCaptor.capture());
		
		@SuppressWarnings("unchecked")
		final Collection<View> results = (Collection<View>) beanFilterCaptor.getValue().filter(ctx);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(view, results.stream().findFirst().get());
		
		
	}

}
