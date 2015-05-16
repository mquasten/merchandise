package de.mq.merchandise.subject;



import java.util.ArrayList;
import java.util.Collection;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.navigator.View;

import de.mq.merchandise.subject.StartViewImpl;
import de.mq.merchandise.subject.support.SubjectViewImpl;
import de.mq.merchandise.util.support.AbstractUIBeanInjector;
import de.mq.merchandise.util.support.VaadinOperations;
import de.mq.merchandise.util.support.VaadinTemplate;
import de.mq.merchandise.util.support.ViewNav;

public class StartViewTest {
	
	private final AbstractUIBeanInjector startView = new StartViewImpl();
	private final SubjectViewImpl subjectView = Mockito.mock(SubjectViewImpl.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final Collection<View> views = new ArrayList<>();
	private final View view = Mockito.mock(View.class);
	
	@Before
	public final void setup() {
		views.add(subjectView);
		views.add(view);
		ReflectionTestUtils.setField(startView, "subjectView", subjectView);
		ReflectionTestUtils.setField(startView, "viewNav", viewNav);
		ReflectionTestUtils.setField(startView, "views", views);
	}
	
	@Test
	public final void init() {
		ReflectionTestUtils.invokeMethod(startView, "init");
		ArgumentCaptor<SubjectViewImpl> rootViewCaptor = ArgumentCaptor.forClass(SubjectViewImpl.class);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArgumentCaptor<Collection<View>> viewsCapture = (ArgumentCaptor) ArgumentCaptor.forClass(Collection.class);
	
		ArgumentCaptor<VaadinOperations> vaadinOperationsCapture = ArgumentCaptor.forClass(VaadinOperations.class);
		Mockito.verify(viewNav).create(rootViewCaptor.capture(), viewsCapture.capture(), vaadinOperationsCapture.capture());
		Assert.assertEquals(subjectView, rootViewCaptor.getValue());
		Assert.assertEquals(views, viewsCapture.getValue());
		Assert.assertTrue(vaadinOperationsCapture.getValue() instanceof VaadinTemplate);
	}

}
