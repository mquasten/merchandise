package de.mq.merchandise.util.support;


import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.UI;

public abstract class AbstractUIBeanInjector  extends UI {
	
	private static final long serialVersionUID = 1L;
	
	abstract protected void init(); 
	

	@Override
	protected final void init(final VaadinRequest request) {
		WebApplicationContextUtils.getRequiredWebApplicationContext(((WrappedHttpSession) request.getWrappedSession()).getHttpSession().getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		init();
	}
	
	
	
	
	

	



	

}
