package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import de.mq.merchandise.util.Event;
import de.mq.merchandise.util.EventBuilder;
import de.mq.merchandise.util.LazyQueryContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

@Component
class SimpleReadOnlyLazyQueryContainerFactoryImpl implements LazyQueryContainerFactory   {
	
	private static final String VALUES_METHOD = "values";
	

	private 	final ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	SimpleReadOnlyLazyQueryContainerFactoryImpl(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}
	
	
	private <T> QueryFactory  createQueryFactory(final Enum<? extends TableContainerColumns> idPropertyId, final Converter<?,Item> converter, final T countEvent, final T pageEvent) {
		
		
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
				
				
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final 	Event<T,Collection<Object>> event =  EventBuilder.of(pageEvent, (Class) Collection.class).withParameter(resultNavigation).build();
				applicationEventPublisher.publishEvent(event);
				
				Assert.isTrue( event.result().isPresent(), "ListMethod should return a value");
				final List<Item> items = new ArrayList<>();
	
				event.result().get().forEach(result -> items.add( convert(converter, result)) );
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
			final 	Event<T,Number> event = EventBuilder.of(countEvent, Number.class).build();
			applicationEventPublisher.publishEvent(event);
			Assert.isTrue( event.result().isPresent(), "CountMethod should return a value");
			return event.result().get().intValue();
	
				
			} }; 
	}
			
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.OnlyLazyQueryContainerFactory#create(java.lang.Enum, java.lang.Number, java.lang.Class, java.lang.Class, java.lang.Object)
	 */
	@Override
	public final <T>  RefreshableContainer   create(final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Converter<?,Item> converter, final T countEvent, final T pageEvent) {
		
		
		final Method method = ReflectionUtils.findMethod(idPropertyId.getClass(), VALUES_METHOD);
		
		method.setAccessible(true);
		final TableContainerColumns[] cols = 	 (TableContainerColumns[]) ReflectionUtils.invokeMethod(method, null);
		
		return new MyLazyQueryContainer(createQueryFactory(idPropertyId, converter, countEvent, pageEvent), idPropertyId, batchSize.intValue(), cols);
		
	}


	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.support.LazyQueryContainerFactory#create(java.lang.Enum, java.lang.Class, java.lang.Class, java.lang.Object[])
	 */
	@Override
	public final <T> RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, Converter<?, Item> converter, T countEvent,  T pageEvent) {
		return  create(idPropertyId, 50, converter, countEvent, pageEvent);
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


