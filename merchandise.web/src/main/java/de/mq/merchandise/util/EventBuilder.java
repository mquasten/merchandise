package de.mq.merchandise.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.Assert;

public final class  EventBuilder<T> {
	
	private final T type;
	
	private final Map<Class<?>,Object> parameters = new HashMap<>();
	
	private EventBuilder(final T type ) {
		this.type=type;
	}
	
	
	public  final static <T>  EventBuilder<T> of(final T type) {
		
		return new EventBuilder<T>(type); 
	}
	
	
	public final EventBuilder<T> withParameter(final Object parameter) {
		parameters.put(parameter.getClass(), parameter);
		Arrays.asList(parameter.getClass().getInterfaces()).forEach(clazz -> parameters.put(clazz, parameter));
		return this;
	}
	
	public final EventBuilder<T> withParameter(final Class<?> clazz, final Object parameter) {
		parameters.put(clazz, parameter);
		return this;
	}
	
	public final Event<T> build() {
		Assert.notNull(type , "Type is mandatory");
		return new Event<T>() {

			private Object result ;
			
			@Override
			public T id() {
				return  type;
			}

			@Override
			public Map<Class<?>,Object> parameter() {
				return parameters;
			}

			@SuppressWarnings("unchecked")
			@Override
			public <R> Optional<R> result() {
				return (Optional<R>) Optional.ofNullable(result);
			}
			
			
			@Override
			public <R> void  assign(final R result) {
				this.result=(R) result;
			}
			
			
			@Override
			public int hashCode() {
				return type.hashCode();
			}
			
			
			

			@Override
			public boolean equals(final Object obj) {
				if (!(obj instanceof Event)) {
					return equals(obj);
				}
				return type.equals(((Event<?>) obj).id());
			}

			@Override
			public String toString() {
				return   id().toString();
			}
			
		};
		
	}

}
