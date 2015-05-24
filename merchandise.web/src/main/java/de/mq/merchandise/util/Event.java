package de.mq.merchandise.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface Event<T> {
	
	 T id();
	 
	default  Map<Class<?>, Object> parameter() {
		return new HashMap<>();
	}
	
	default <R> Optional<R>  result() {
		return Optional.empty();
	}
	
	default<R> void  assign(R result) {
		throw new UnsupportedOperationException("Results not supported for this event");
	}

}
