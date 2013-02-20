package de.mq.merchandise.contact.support;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;

public class AddressSelector implements Converter<CityAddress, Class<?>> {

	@Override
	public final Class<?> convert(final CityAddress source) {
		if (source instanceof Address) {
			return AddressAO.class;
			
		}
		return PostBoxAO.class;
	}

	

}
