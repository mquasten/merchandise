package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

@Component
class SimpleReadOnlyLazyQueryContainerFactoryImpl implements LazyQueryContainerFactory   {
	
	private static final String VALUES_METHOD = "values";
	

	private final EventAnnotationOperations eventAnnotationOperations;
	
	@Autowired
	SimpleReadOnlyLazyQueryContainerFactoryImpl(final EventAnnotationOperations eventAnnotationOperations) {
		this.eventAnnotationOperations=eventAnnotationOperations;
	}
	
	
	private <T> QueryFactory  createQueryFactory(final Enum<? extends TableContainerColumns> idPropertyId, final Converter<?,Item> converter, final T fascade,  final Method countMethod, final Method pageMethod) {
		
		
		return queryDefinition -> new Query() {

			@Override
			public Item constructItem() {
			
			 final Item  item =  new PropertysetItem();
			
			 item.addItemProperty(idPropertyId,  new ObjectProperty<>( Long.valueOf((long) (Math.random()*1e12) ))) ;
			 return item;	
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
				
				@SuppressWarnings("unchecked")
				final Collection<Object> results = (Collection<Object>) ReflectionUtils.invokeMethod(pageMethod, fascade ,resultNavigation );
				Assert.notNull(results, "ListMethod should return a value");
				final List<Item> items = new ArrayList<>();
	
				results.forEach(result -> items.add( convert(converter, result)) );
				return items;
			}

			@SuppressWarnings("unchecked")
			private Item convert(final Converter<?, Item> converter, Object result) {
				return ((Converter<Object, Item>) converter).convert(result);
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
			
			final Number result = (Number) ReflectionUtils.invokeMethod(countMethod, fascade);
			Assert.notNull(result, "CountMethod should return a value");
			return result.intValue();
				
			} }; 
	}
			
	
	
	
	
	
	
/*
 * (non-Javadoc)
 * @see de.mq.merchandise.util.LazyQueryContainerFactory#create(java.lang.Enum, java.lang.Number, org.springframework.core.convert.converter.Converter, java.lang.Object, java.lang.Object, java.lang.Object)
 */
	@Override
	public final <E,T>  RefreshableContainer   create(final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Converter<?,Item> converter, final T fascade, final E countEvent, final E pageEvent) {
		final Method method = ReflectionUtils.findMethod(idPropertyId.getClass(), VALUES_METHOD);
		method.setAccessible(true);
		return new MyLazyQueryContainer(createQueryFactory(idPropertyId, converter, fascade, method(fascade, countEvent), method(fascade, pageEvent)), idPropertyId, batchSize.intValue(), (TableContainerColumns[]) ReflectionUtils.invokeMethod(method, null));
		
	}


	private <T, E>Method method(final T fascade, final E eventId) {
		final Optional<Method> result =  Arrays.asList(ReflectionUtils.getAllDeclaredMethods(fascade.getClass())).stream().filter(m ->  (eventAnnotationOperations.isAnnotaionPresent(m) && eventAnnotationOperations.valueFromAnnotation(m).equals(eventId))).findFirst();
		Assert.isTrue(result.isPresent(), String.format("Method not found for %s" , eventId));
		return result.get();
	}


	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.LazyQueryContainerFactory#create(java.lang.Enum, org.springframework.core.convert.converter.Converter, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public final <E,T> RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, Converter<?, Item> converter, T fascade,  E countEvent,  E pageEvent) {
		return  create(idPropertyId, 50, converter, fascade, countEvent, pageEvent);
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
			
			Arrays.asList(cols).stream().forEach(col -> addContainerProperty(col, col.target(), null , false, col.sortable()));
		}
	
		private static final long serialVersionUID = 1L;

		
		
	}
	
	
}


