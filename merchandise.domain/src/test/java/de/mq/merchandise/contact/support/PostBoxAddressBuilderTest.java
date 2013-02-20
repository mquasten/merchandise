package de.mq.merchandise.contact.support;

import java.util.Locale;

import org.junit.Test;

import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.PostBoxAddressBuilder;
import de.mq.merchandise.contact.support.PostBoxAddressBuilderImpl;

import junit.framework.Assert;

public class PostBoxAddressBuilderTest {
	
	private static final String BOX = "12345";
	private static final String CITY = "Wegberg";
	private static final String ZIP_CODE = "41844";

	@Test
	public final void zipCode() {
		final PostBoxAddressBuilder builder = new PostBoxAddressBuilderImpl();
		Assert.assertEquals(builder, builder.withZipCode(ZIP_CODE));
	}
	
	@Test
	public final void city() {
		final PostBoxAddressBuilder builder = new PostBoxAddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCity(CITY));
	}
	
	@Test
	public final void box() {
		final PostBoxAddressBuilder builder = new PostBoxAddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCity(BOX));
	}
	
	@Test
	public final void country(){
		final PostBoxAddressBuilder builder = new PostBoxAddressBuilderImpl();
		Assert.assertEquals(builder, builder.withCountry(Locale.GERMANY));
	}
	
	@Test
	public final void build() {
		final PostBox postBox = new PostBoxAddressBuilderImpl().withBox(BOX).withCity(CITY).withZipCode(ZIP_CODE).withCountry(Locale.US).build();
		Assert.assertEquals(BOX, postBox.box());
		Assert.assertEquals(CITY, postBox.city());
		Assert.assertEquals(ZIP_CODE, postBox.zipCode());
		Assert.assertEquals(Locale.US, postBox.country());
	}
	
	@Test
	public final void buildDefaultLocale() {
		final PostBox postBox = new PostBoxAddressBuilderImpl().withBox(BOX).withCity(CITY).withZipCode(ZIP_CODE).build();
		
		Assert.assertEquals(Locale.GERMANY, postBox.country());
	}
	
	
	
	@Test(expected=IllegalArgumentException.class)
	public final void boxMissing() {
		new PostBoxAddressBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void cityMissing() {
		new PostBoxAddressBuilderImpl().withBox(BOX).withZipCode(ZIP_CODE).build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void zipCodeMissing() {
		new PostBoxAddressBuilderImpl().withBox(BOX).withCity(CITY).build();
	}
	
	

}
