package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.model.support.WebProxyFactory;

public class PostBoxProxyFactoryTest {
	
	@Test
	public final void postBox() {
		
		final WebProxyFactory webProxyFactory = Mockito.mock(WebProxyFactory.class);
	    final PostBoxProxyFactoryImpl postBoxProxyFactory = new PostBoxProxyFactoryImpl(); 
	    ReflectionTestUtils.setField(postBoxProxyFactory, "webProxyFactory", webProxyFactory);
	    final PostBoxAO postBoxAO = Mockito.mock(PostBoxAO.class);
	    Mockito.when(webProxyFactory.webModell(PostBoxAO.class, PostBoxImpl.class)).thenReturn(postBoxAO);
	    
	    Assert.assertEquals(postBoxAO, postBoxProxyFactory.postBox());
	}

}
