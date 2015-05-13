package de.mq.merchandise.util.support;



import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.data.Item;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.support.SubjectModel;
import de.mq.merchandise.subject.support.TestConstants;
import de.mq.merchandise.util.LazyQueryContainerFactory;

public class LazyQueryContainerFactoryTest {
	
	private final    BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
	private final LazyQueryContainerFactory    lazyQueryContainerFactory = new SimpleReadOnlyLazyQueryContainerFactoryImpl(beanResolver);

	private final SubjectModel subjectModel = Mockito.mock(SubjectModel.class);
	
	private final ResultNavigation resultNavigation = Mockito.mock(ResultNavigation.class); 
	
	
	
	
	@Test
	public final void create() {
		Object controller = TestConstants.controller();
		Mockito.when(beanResolver.resolve((Class<Object>)TestConstants.CONTROLLER_TARGET)).thenReturn(controller);
		Mockito.when(beanResolver.resolve(Mockito.anyMap(), (Class) Mockito.any())).thenReturn(subjectModel, resultNavigation);
		Mockito.when(beanResolver.resolve( (Class) TestConstants.SUBJECT_COMVERTER_CLASS)).thenReturn(TestConstants.CONVERTER);
		
		
		final RefreshableContainer result = lazyQueryContainerFactory.create(TestConstants.SUBJECT_COLS_ID, TestConstants.SUBJECT_COMVERTER_CLASS, TestConstants.CONTROLLER_TARGET);
		System.out.println(result.getItemIds());
		Item x = result.getItem(4711L);
		System.out.println(x.getItemProperty(TestConstants.SUBJECT_COLS_NAME));
		
		
	}


}
