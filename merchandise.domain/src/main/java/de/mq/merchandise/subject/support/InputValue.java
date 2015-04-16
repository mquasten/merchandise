package de.mq.merchandise.subject.support;

import java.util.Optional;

interface InputValue {

	<T> Optional<T> value();

}