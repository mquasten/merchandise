package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

@Component
public  class SimpleReadOnlyLazyQueryContainerFactoryImpl implements LazyQueryContainerFactory   {
	
	private static final String VALUES_METHOD = "values";
	private  final ApplicationContext ctx; 
	
	@Autowired
	SimpleReadOnlyLazyQueryContainerFactoryImpl(final   ApplicationContext ctx) {
		this.ctx=ctx;
	}
	
	
	private  QueryFactory  createQueryFactory(final Table parent,  final Enum<? extends TableContainerColumns> idPropertyId, final Class<? extends Converter<?,Item>> converterClass, final Map<PagingMethods, Method> methods, final Object ... values ) {
		return queryDefinition -> new Query() {

			@Override
			public Item constructItem() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public final boolean deleteAllItems() {
				throw new UnsupportedOperationException();
			}

			@Override
			public final List<Item> loadItems(final int startIndex, final int pageSize) {
				System.out.println(startIndex + ":" +  pageSize);
				
				
				final ResultNavigation  resultNavigation  = new ResultNavigation() {	
					@Override
					public Number pageSize() {
						return pageSize;
					}
					
					@Override
					public Number firstRow() {
						return startIndex;
					}

					@Override
					public List<Order> orders() {
						final List<Order> orders = order(parent);
						orders.add(new Order(((TableContainerColumns)idPropertyId).orderBy()));
						return orders;
					}

					
				};
				final Method method = methods.get(PagingMethods.Read);
				Assert.notNull(method , "ReadMethod for Paging not found");
				method.setAccessible(true);
				final List<Object> params = new ArrayList<>();
				params.addAll(Arrays.asList(values));
				params.add(resultNavigation);
				
				@SuppressWarnings("unchecked")
				final Collection<Object> results = (Collection<Object>) ReflectionUtils.invokeMethod(method, ctx.getBean(method.getDeclaringClass()), params.toArray());
				final List<Item> items = new ArrayList<>();
				@SuppressWarnings("unchecked")
				final Converter<Object, Item> converter=  (Converter<Object, Item>) ctx.getBean(converterClass);
				results.forEach(result -> items.add( converter.convert(result)) );
				parent.setValue(null);
				return items;
			}
			
			private List<Order> order(final Table parent) {
				final List<Order> orders = new ArrayList<>();
				if(parent.getSortContainerPropertyId() == null){
					return orders;
				}
				Direction dir = Direction.ASC;
				if (!parent.isSortAscending() ) {
					dir=Direction.DESC;
				}
				final TableContainerColumns  col = (TableContainerColumns)   parent.getSortContainerPropertyId();
				final Order order = new Order(dir, col.orderBy()); 
				
				orders.add(order);
				return orders;
			}
			
			

			@Override
			public final void saveItems(final List<Item> add, final List<Item> merge, final List<Item> remove) {
				throw new UnsupportedOperationException();
				
			}

			@Override
			public final int size() {
				final Method method = methods.get(PagingMethods.Count);
				Assert.notNull(method , "CountMethod for Paging not found");
				method.setAccessible(true);
				return ((Number) ReflectionUtils.invokeMethod(method, ctx.getBean(method.getDeclaringClass()), values)).intValue();
				
				
			} }; 
	}
			
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.OnlyLazyQueryContainerFactory#create(java.lang.Enum, java.lang.Number, java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public final  void   assign(final Table parent, final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Class<? extends Converter<?,Item>> converterClass, final Class<?>controllerTarget, final Object ... values) {
		
		final Method method = ReflectionUtils.findMethod(idPropertyId.getClass(), VALUES_METHOD);
		
		method.setAccessible(true);
		final TableContainerColumns[] cols = 	 (TableContainerColumns[]) ReflectionUtils.invokeMethod(method, null);
		
		final Object controller = ctx.getBean(controllerTarget);
		final Map<PagingMethods, Method> methods = new HashMap<>();
		ReflectionUtils.doWithMethods(controller.getClass(), m -> methods.put(m.getAnnotation(PagingMethod.class).value(), m) , m -> m.isAnnotationPresent(PagingMethod.class));
		
		final LazyQueryContainer result =  new LazyQueryContainer(createQueryFactory(parent,idPropertyId, converterClass, methods, values), idPropertyId, batchSize.intValue(), false);
		Arrays.asList(cols).stream().forEach(col -> result.addContainerProperty(col, col.target(), null, col.sortable(), true));
		
		parent.setContainerDataSource(result);
		parent.setVisibleColumns(Arrays.asList(cols).stream().filter(col -> col.visible() ).collect(Collectors.toList()).toArray());
		
		
	}


	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.support.LazyQueryContainerFactory#create(java.lang.Enum, java.lang.Class, java.lang.Class, java.lang.Object[])
	 */
	@Override
	public void assign(final Table parent, final Enum<? extends TableContainerColumns> idPropertyId, Class<? extends Converter<?, Item>> converterClass, Class<?> controllerTarget, Object... values) {
		 assign(parent, idPropertyId, 50, converterClass, controllerTarget, values);
	}





	
}


