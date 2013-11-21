package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.Address;

public class Opportunity2PointConverterImpl implements Converter<Opportunity, Collection<String>>{

	@Override
	public Collection<String> convert(final Opportunity source) {
		final Collection<String> results = new ArrayList<>();
		for(Address address: source.addresses()){
			results.add(String.format("POINT(%s %s)", address.coordinates().longitude(), address.coordinates().latitude()));
		}
		return results;
	}

}
