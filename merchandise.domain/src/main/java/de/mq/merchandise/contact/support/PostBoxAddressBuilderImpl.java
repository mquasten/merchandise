package de.mq.merchandise.contact.support;

import java.util.Locale;

import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.PostBoxAddressBuilder;

class PostBoxAddressBuilderImpl implements  PostBoxAddressBuilder {

	private String zipCode;
	
	private String city;
	
	private String postBox;
	
	private Locale country=Locale.GERMANY;
	
	@Override
	public final PostBoxAddressBuilder withCountry(final Locale country) {
		this.country=country;
		return this;
	}
	
	@Override
	public final PostBoxAddressBuilder withCity(final String city) {
		this.city=city;
		return this;
	}
	
	
	@Override
	public final PostBoxAddressBuilder withZipCode(final String zipCode) {
		this.zipCode=zipCode;
		return this;
	}
	
	
	@Override
	public final PostBoxAddressBuilder withBox(final String postBox) {
		this.postBox=postBox;
		return this;
	}
	
	
	@Override
	public PostBox build() {
		zipCodeExistsGuard();
		cityExistsGuard();
		boxExistsGuard();
		return new PostBoxImpl(country, zipCode, city, postBox);
	}


	private void boxExistsGuard() {
		if(postBox==null){
			throw new IllegalArgumentException("PostBox is missing");
		}
	}


	private void cityExistsGuard() {
		if(city==null){
			throw new IllegalArgumentException("City is missing");
		}
	}


	private void zipCodeExistsGuard() {
		if( zipCode==null){
			throw new IllegalArgumentException("ZipCode is missing");
		}
	}

	

	

}
