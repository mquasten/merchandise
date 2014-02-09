package de.mq.merchandise.rule.support;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.controller.SerialisationControllerImpl;
import de.mq.merchandise.opportunity.support.PagingAO;
import de.mq.merchandise.rule.RuleService;

public class RuleProxyFactoryTest {
	
	
	private final RuleService ruleService = Mockito.mock(RuleService.class);
	
	private AOProxyFactory aoProxyFactory = Mockito.mock(AOProxyFactory.class);
	
	private BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	
	private SourceFactoryImpl sourceFactory = Mockito.mock(SourceFactoryImpl.class);
	
	private RuleProxyFactoryImpl ruleProxyFactory = new RuleProxyFactoryImpl(ruleService, sourceFactory, aoProxyFactory, beanResolver);
	
	@Test
	public final void init() {
		Assert.assertNull(ReflectionTestUtils.getField(ruleProxyFactory, "ruleController"));
		ruleProxyFactory.init();
		Assert.assertEquals(RuleControllerImpl.class, ReflectionTestUtils.getField(ruleProxyFactory, "ruleController").getClass());
		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void rulesModel() {
		ruleProxyFactory.init();
		final RuleModelAO ruleModelAO = Mockito.mock(RuleModelAO.class);
		final PagingAO pagingAO = Mockito.mock(PagingAO.class);
		final RuleAO ruleAO = Mockito.mock(RuleAO.class);
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		
		Mockito.when(aoProxyFactory.createProxy(classCaptor.capture(), modelRepositoryCaptor.capture())).thenAnswer(new Answer() {

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Class<?>  clazz = (Class<?>) invocation.getArguments()[0];
				
				if( clazz.equals(RuleModelAO.class)){
					return ruleModelAO;
				}
				
				if( clazz.equals(PagingAO.class) ){
					return pagingAO;
				}
				
				if( clazz.equals(RuleAO.class) ){
					return ruleAO;
				}
				Assert.fail("Invalid Model Class:" + clazz);
				return null;
			}
		});
		
		Assert.assertEquals(ruleModelAO, ruleProxyFactory.rulesModel());
		
		Assert.assertEquals(RuleModelAO.class, classCaptor.getValue());
		final Map<?,?> items =(Map<?, ?>) ReflectionTestUtils.getField(modelRepositoryCaptor.getValue(), "modelItems");
		Assert.assertEquals(4, items.size());
		final boolean[] found = new boolean[] {false, false, false, false };
		for(final Entry<?, ?> item : items.entrySet()) {
			if( keyName(item).equals("paging")) {
				Assert.assertEquals(pagingAO, item.getValue());
				Assert.assertEquals("Map", keyType(item));
				found[0]=true;
				continue;
			}
			if( keyName(item).equals("selected")){ 
				Assert.assertEquals("Map", keyType(item));
				Assert.assertEquals(ruleAO, item.getValue());
				found[1]=true;
				continue;
			}
			if( keyName(item).equals(RuleControllerImpl.class.getName())){ 
				Assert.assertEquals("Domain", keyType(item));
				Assert.assertEquals(ReflectionTestUtils.getField(ruleProxyFactory, "ruleController"), item.getValue());
				found[2]=true;
				continue;
			}
			
			if( keyName(item).equals(SerialisationControllerImpl.class.getName())){ 
				Assert.assertEquals("Domain", keyType(item));
				Assert.assertEquals(SerialisationControllerImpl.class, item.getValue().getClass());
				found[3]=true;
				continue;
			}
			
			Assert.fail("Wrong Object in ModelRepository: " + item);
			
		}
		for(final boolean procced:found ){
			Assert.assertTrue(procced);
		}
	}


	@SuppressWarnings("unchecked")
	@Test
	public final void rule() {
		ruleProxyFactory.init();
		final RuleAO ruleAO = Mockito.mock(RuleAO.class);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(aoProxyFactory.createProxy(classCaptor.capture(), modelRepositoryCaptor.capture())).thenReturn(ruleAO);
		
		Assert.assertEquals(ruleAO, ruleProxyFactory.rule());
		
		Assert.assertEquals(RuleAO.class, classCaptor.getValue());
		final Map<?,?> items =(Map<?, ?>) ReflectionTestUtils.getField(modelRepositoryCaptor.getValue(), "modelItems");
		final boolean[] found = new boolean[] {false, false};
		Assert.assertEquals(2, items.size());
		for(final Entry<?,?> entry : items.entrySet()){
			if( keyName(entry).equals(RuleImpl.class.getName())){
				Assert.assertEquals("Domain", keyType(entry));
				Assert.assertEquals(RuleImpl.class, entry.getValue().getClass());
				found[0]=true;
				continue;
			}
			if( keyName(entry).equals(RuleControllerImpl.class.getName())){
				Assert.assertEquals("Domain", keyType(entry)); 
				Assert.assertEquals(ReflectionTestUtils.getField(ruleProxyFactory, "ruleController"), entry.getValue());
				found[1]=true;
				continue;
			}
			Assert.fail("Wrong Object in ModelRepository: " + entry);
		}
		
		for(final boolean procced:found ){
			Assert.assertTrue(procced);
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void rulecontroller() {
		ruleProxyFactory.init();
		final RuleController ruleController = Mockito.mock(RuleController.class);
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(aoProxyFactory.createProxy(classCaptor.capture(), modelRepositoryCaptor.capture())).thenReturn(ruleController);
		Assert.assertEquals(ruleController, ruleProxyFactory.ruleController());
		
		Assert.assertEquals(RuleController.class, classCaptor.getValue());
		
		final Map<?,?> items =(Map<?, ?>) ReflectionTestUtils.getField(modelRepositoryCaptor.getValue(), "modelItems");
		Assert.assertEquals(1, items.size());
		
		Assert.assertEquals(RuleControllerImpl.class.getName(), keyName(items.entrySet().iterator().next()));
		Assert.assertEquals("Domain", keyType(items.entrySet().iterator().next()));
		Assert.assertEquals(ReflectionTestUtils.getField(ruleProxyFactory, "ruleController"), items.entrySet().iterator().next().getValue());
		
	
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new RuleProxyFactoryImpl());
	}
	

	private String keyType(Entry<?, ?> x) {
		return  ReflectionTestUtils.getField(x.getKey(), "keyType").toString();
	}



	private String keyName(Entry<?, ?> x) {
		return (String) ReflectionTestUtils.getField(x.getKey(), "name");
	}

}
