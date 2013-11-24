package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.Address;

public class Opportunity2PointConverterImpl implements Converter<Opportunity, Map<Address,String>>{

	@Override
	public Map<Address,String> convert(final Opportunity source) {
		final Map<Address,String> results = new HashMap<>();
		for(Address address: source.addresses()){
			results.put(address, String.format("POINT(%s %s)", address.coordinates().longitude(), address.coordinates().latitude()));
		}
		return results;
	}

}
