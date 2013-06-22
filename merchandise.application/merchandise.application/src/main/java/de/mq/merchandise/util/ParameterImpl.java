package de.mq.merchandise.util;

public class ParameterImpl<T>  implements Parameter<T> {

	private final String name;
	
	public ParameterImpl(String name, T value) {
		this.name = name;
		this.value = value;
	}

	private final T value;
	
	@Override
	public String name() {
		
		return name;
	}

	@Override
	public T value() {
		return value;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Parameter<?>)) {
			return false;
		}
		return name.equals(((Parameter<?>) obj).name());
		
	}

	@Override
	public String toString() {
		return name+"=["+ value.toString()+"]";
	}
	
	
	

}
