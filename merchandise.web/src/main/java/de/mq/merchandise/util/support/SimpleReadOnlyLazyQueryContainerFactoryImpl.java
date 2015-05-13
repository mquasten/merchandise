package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Item;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

@Component
class SimpleReadOnlyLazyQueryContainerFactoryImpl implements LazyQueryContainerFactory   {
	
	private static final String VALUES_METHOD = "values";
	private  final BeanResolver beanResolver; 
	
	@Autowired
	SimpleReadOnlyLazyQueryContainerFactoryImpl(final    BeanResolver beanResolver) {
		this.beanResolver=beanResolver;
	}
	
	
	private  QueryFactory  createQueryFactory(final Enum<? extends TableContainerColumns> idPropertyId, final Class<? extends Converter<?,Item>> converterClass, final Map<PagingMethods, Method> methods) {
		
		
		return queryDefinition -> new Query() {

			@Override
			public Item constructItem() {
				
			 return null;
				
			}

			@Override
			public final boolean deleteAllItems() {
				throw new UnsupportedOperationException();
			}

			@Override
			public final List<Item> loadItems(final int startIndex, final int pageSize) {
				
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
				
						@SuppressWarnings("unchecked")
						final Optional<Boolean> sortAsc  =  CollectionUtils.arrayToList(queryDefinition.getSortPropertyAscendingStates()).stream().findFirst();
						
						final List<Order> orders = order2(Arrays.asList(queryDefinition.getSortPropertyIds()).stream().findFirst(), sortAsc);
						orders.add(new Order(((TableContainerColumns)idPropertyId).orderBy()));
						return orders;
					}

					
				};
				
				
				final Method method = methods.get(PagingMethods.Read);
				Assert.notNull(method , "ReadMethod for Paging not found");
				method.setAccessible(true);
				final Map<Class<?>, Object> beans = new HashMap<>();
				beans.put(ResultNavigation.class, resultNavigation);
			
				@SuppressWarnings("unchecked")
				final Collection<Object> results = (Collection<Object>) ReflectionUtils.invokeMethod(method, beanResolver.resolve(method.getDeclaringClass()), Arrays.asList(method.getParameterTypes()).stream().map(t -> beanResolver.resolve(beans, t)).collect(Collectors.toList()).toArray());
				final List<Item> items = new ArrayList<>();
				@SuppressWarnings("unchecked")
				final Converter<Object, Item> converter=  (Converter<Object, Item>) beanResolver.resolve(converterClass);
			
				results.forEach(result -> items.add( converter.convert(result)) );
				return items;
			}
			
			
			private List<Order> order2(final Optional<Object> column, final Optional<Boolean> asc) {
				final List<Order> orders = new ArrayList<>();
				if((!column.isPresent()) || ( ! asc.isPresent())){
					return orders;
				}
				Direction dir = Direction.ASC;
				if (!asc.get() ) {
					dir=Direction.DESC;
				}
				
				orders.add(new Order(dir, ((TableContainerColumns)  column.get()).orderBy()));
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
				return ((Number) ReflectionUtils.invokeMethod(method, beanResolver.resolve(method.getDeclaringClass()), Arrays.asList(method.getParameterTypes()).stream().map(t -> beanResolver.resolve(t)).collect(Collectors.toList()).toArray())).intValue();
				
				
			} }; 
	}
			
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.OnlyLazyQueryContainerFactory#create(java.lang.Enum, java.lang.Number, java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public final  RefreshableContainer   create(final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Class<? extends Converter<?,Item>> converterClass, final Class<?>controllerTarget) {
		
		
		final Method method = ReflectionUtils.findMethod(idPropertyId.getClass(), VALUES_METHOD);
		
		method.setAccessible(true);
		final TableContainerColumns[] cols = 	 (TableContainerColumns[]) ReflectionUtils.invokeMethod(method, null);
		
		final Object controller = beanResolver.resolve(controllerTarget);
		final Map<PagingMethods, Method> methods = new HashMap<>();
		ReflectionUtils.doWithMethods(controller.getClass(), m -> methods.put(m.getAnnotation(PagingMethod.class).value(), m) , m -> m.isAnnotationPresent(PagingMethod.class));
		
		return new MyLazyQueryContainer(createQueryFactory(idPropertyId, converterClass, methods), idPropertyId, batchSize.intValue(), cols);
		
	}


	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.support.LazyQueryContainerFactory#create(java.lang.Enum, java.lang.Class, java.lang.Class, java.lang.Object[])
	 */
	@Override
	public final RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, Class<? extends Converter<?, Item>> converterClass, Class<?> controllerTarget) {
		return  create(idPropertyId, 50, converterClass, controllerTarget);
	}



	/**
	 * Ueberschreibt LazyQueryContainer, damit man es gebrauchen kann.
	 * Implentieren gegen interfaces, Grundlagen Programmierung usw.
	 * @author Admin
	 *
	 */
	class MyLazyQueryContainer  extends LazyQueryContainer implements RefreshableContainer  {
		
		MyLazyQueryContainer(QueryFactory queryFactory, Object idPropertyId, int batchSize, final TableContainerColumns[] cols ){
			super(queryFactory,idPropertyId,batchSize, false );
			Arrays.asList(cols).stream().forEach(col -> addContainerProperty(col, col.target(), null, col.sortable(), true));
		}
	
		private static final long serialVersionUID = 1L;

		
		
	}
	
	
}


