package de.mq.merchandise.customer;

import java.util.Date;


public interface NativityBuilder {

	NativityBuilder withBirthPlace(String birthPlace);

	NativityBuilder withBirthDate(Date birthDate);

	Nativity build();

}