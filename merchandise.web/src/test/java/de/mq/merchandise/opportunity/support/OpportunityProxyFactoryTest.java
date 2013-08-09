package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.primefaces.model.TreeNode;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoModel;
import de.mq.merchandise.model.support.Conversation;
import de.mq.merchandise.util.SimplePagingImpl;

public class OpportunityProxyFactoryTest {
	
	final AOProxyFactory proxyFactory = Mockito.mock(AOProxyFactory.class);
	final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	final Conversation conversation = Mockito.mock(Conversation.class);
	
	final OpportunityProxyFactoryImpl opportunityProxyFactoryImpl = new OpportunityProxyFactoryImpl(proxyFactory, beanResolver, conversation);
	
	@Test
	public final void createDefaultConstructor() {
		Assert.assertNotNull(new OpportunityProxyFactoryImpl());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void opportunity() {
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryArgumentCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		final OpportunityAO opportunityAO = Mockito.mock(OpportunityAO.class);
		Mockito.when(proxyFactory.createProxy(clazzCaptor.capture(), modelRepositoryArgumentCaptor.capture())).thenReturn(opportunityAO);
		
		Assert.assertEquals(opportunityAO, opportunityProxyFactoryImpl.opportunity());
		
		Assert.assertEquals(OpportunityAO.class, clazzCaptor.getValue());
		
		final ModelRepository modelRepository = modelRepositoryArgumentCaptor.getValue();
		Assert.assertEquals(OpportunityImpl.class, modelRepository.get(OpportunityImpl.class).getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void opportunityModel() {
		final PagingAO pagingAO = Mockito.mock(PagingAO.class);
		
	
		final OpportunityModelAO opportunityModelAO = Mockito.mock(OpportunityModelAO.class);
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class),Mockito.any(ModelRepository.class))).thenAnswer(new Answer<Object>() {

			@SuppressWarnings("rawtypes")
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				
				if( invocation.getArguments()[0].equals(PagingAO.class)){
					Assert.assertEquals(SimplePagingImpl.class, ((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values().iterator().next().getClass());
					 return pagingAO;
				} else {
					Mockito.when(opportunityModelAO.getPaging()).thenReturn( (PagingAO) (((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values().iterator().next()));
					return opportunityModelAO;
				}
				
				
			}} );

		
		Assert.assertEquals(opportunityModelAO, opportunityProxyFactoryImpl.opportunityModel());
		Assert.assertEquals(pagingAO, opportunityModelAO.getPaging());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void activityClassifications() {
		final TreeNode treeNode = Mockito.mock(TreeNode.class);
		final ActivityClassificationTreeAO activityClassificationTreeAO = Mockito.mock(ActivityClassificationTreeAO.class);
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class),Mockito.any(ModelRepository.class))).thenAnswer(new Answer<Object>() {

			
		
			@SuppressWarnings("rawtypes")
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
			
				if( invocation.getArguments()[0].equals(TreeNode.class)){
					return treeNode;
				} 
					for(final Object value : ((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values()) {
						if (! (value instanceof TreeNode)) {
							Assert.assertTrue(value instanceof ClassificationTreeChangedObserveableControllerImpl);
							continue;
						}
						Mockito.when(activityClassificationTreeAO.getTreeNode()).thenReturn(treeNode);
					}
					
					return activityClassificationTreeAO;
				
			}});

			Assert.assertEquals(activityClassificationTreeAO, opportunityProxyFactoryImpl.activityClassifications());
			Assert.assertEquals(treeNode, activityClassificationTreeAO.getTreeNode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void productClassifications() {
		final TreeNode treeNode = Mockito.mock(TreeNode.class);
		final ProductClassificationTreeAO productClassificationTreeAO = Mockito.mock(ProductClassificationTreeAO.class);
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class),Mockito.any(ModelRepository.class))).thenAnswer(new Answer<Object>() {

			
		
			@SuppressWarnings("rawtypes")
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
			
				if( invocation.getArguments()[0].equals(TreeNode.class)){
					return treeNode;
				} 
					for(final Object value : ((Map) ReflectionTestUtils.getField(invocation.getArguments()[1], "modelItems")).values()) {
						if (! (value instanceof TreeNode)) {
							Assert.assertTrue(value instanceof ClassificationTreeChangedObserveableControllerImpl);
							continue;
						}
						Mockito.when(productClassificationTreeAO.getTreeNode()).thenReturn(treeNode);
					}
					
					return productClassificationTreeAO;
				
			}});

			Assert.assertEquals(productClassificationTreeAO, opportunityProxyFactoryImpl.productClassifications());
			Assert.assertEquals(treeNode, productClassificationTreeAO.getTreeNode());
	}

}
