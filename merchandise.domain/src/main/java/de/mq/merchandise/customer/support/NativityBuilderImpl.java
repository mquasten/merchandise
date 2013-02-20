package de.mq.merchandise.customer.support;

import java.util.Date;

import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NativityBuilder;

class NativityBuilderImpl implements NativityBuilder {
	
	private String birthPlace;
	
	private Date birthDate;
	
	
	
	@Override
	public final NativityBuilder withBirthPlace(final String birthPlace) {
		this.birthPlace=birthPlace;
		return this;
	}
	
	
	
	@Override
	public final NativityBuilder withBirthDate(final Date birthDate) {
		this.birthDate=birthDate;
		return this;
	}
	
	
	@Override
	public final Nativity build() {
		return new NativityImpl(birthPlace, birthDate);
	}

}
