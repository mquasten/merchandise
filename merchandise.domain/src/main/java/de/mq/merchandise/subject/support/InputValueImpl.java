package de.mq.merchandise.subject.support;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable

class InputValueImpl  {

	
	
	@Column(length = 50, name = "string_value", nullable=false)
	private String stringValue;

	@Column(name = "long_value", nullable=false)
	private Long longValue;

   @Column(name = "double_value", nullable=false)
	private Double doubleValue;
	
	@SuppressWarnings("unused")
	private InputValueImpl() {
		// touched for the very first time ...
	}
	

	InputValueImpl(final Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	InputValueImpl(final Long longValue) {
		this.longValue = longValue;
	}

	InputValueImpl(final String stringValue) {
		this.stringValue = stringValue;
	}

	
	@SuppressWarnings("unchecked")
	<T> Optional<T> value() {

		if (doubleValue != null) {
			return (Optional<T>) Optional.of(doubleValue);
		}

		if (longValue != null) {
			return (Optional<T>) Optional.of(longValue);
		}

		if (stringValue != null) {
			return (Optional<T>) Optional.of(stringValue);
		}
		return Optional.empty();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final Optional<?> value = value();
		if (! value.isPresent()) {
			return super.hashCode();
		}
		return value.get().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final Optional<?> value = value();
		if (!value.isPresent()) {
			return false;
		}

		if (!(obj instanceof InputValueImpl)) {

			return false;
		}

		final Optional<?> other = ((InputValueImpl) obj).value();
		if (!other.isPresent()) {
			return false;
		}
		return value.get().equals(other.get());
	}

}
