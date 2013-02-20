package de.mq.merchandise.model.support;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.core.convert.converter.Converter;

public class HibernateProxyConverter implements Converter<Object,Object> {

	@Override
	public Object convert(final Object source) {
		if (source instanceof HibernateProxy) {
			return ((HibernateProxy) source).getHibernateLazyInitializer().getImplementation();
		}
		return source;
	}

}
