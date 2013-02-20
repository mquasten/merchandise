package de.mq.merchandise.contact;

import java.util.Locale;


public interface PostBoxAddressBuilder  {

	PostBoxAddressBuilder withCity(String city);

	PostBoxAddressBuilder withZipCode(String zipCode);

	PostBoxAddressBuilder withBox(String postBox);

	/**
	 * The country of the address
	 * @param country the country
	 * @return the Builder itselves
	 */
	PostBoxAddressBuilder withCountry(Locale country);
	
	PostBox build();
	

}