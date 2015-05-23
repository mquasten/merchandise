package de.mq.merchandise.util;

import org.springframework.core.convert.converter.Converter;

public interface Mapper<Source,Target> extends Converter<Source,Target> {
	Target mapInto(final Source source, final Target target);
}
