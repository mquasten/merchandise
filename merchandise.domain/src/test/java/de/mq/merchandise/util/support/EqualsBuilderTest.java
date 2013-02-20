package de.mq.merchandise.util.support;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.Equals;
import de.mq.merchandise.util.EqualsBuilder;
import de.mq.merchandise.util.support.SimpleReflectionEqualsBuilderImpl;

public class EqualsBuilderTest {
	
	
	
	private static final String LASTNAME = "Minogue";
	private static final String FIRSTNAME = "Kylie";




	@Test
	public final void withSource() {
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		final Artist source = new Artist(FIRSTNAME, LASTNAME);
		Assert.assertEquals(equalsBuilder, equalsBuilder.withSource(source));
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "sourceFields"));
		Assert.assertEquals(3, results.size());
	    Assert.assertTrue(results.containsKey(uuid(source.getClass(), "name")));
	    Assert.assertTrue(results.containsKey(uuid(source.getClass(), "firstName")));
	    Assert.assertTrue(results.containsKey(SimpleReflectionEqualsBuilderImpl.OBJECT_UUID));
		
	    Assert.assertEquals(LASTNAME, results.get((uuid(source.getClass(), "name"))));
	    Assert.assertEquals(FIRSTNAME, results.get((uuid(source.getClass(), "firstName"))));
	    Assert.assertEquals(source, results.get(SimpleReflectionEqualsBuilderImpl.OBJECT_UUID));
	    
	    equalsBuilder.withSource(new Date());
	   
		Assert.assertEquals(1,results.size());
	}
	
	
	@Test
	public final void withSourceNulls() {
		final Artist artist = new Artist(null, null);
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl().withSource(artist);
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "sourceFields"));
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(artist, results.values().iterator().next());
		
		
	}


	@Test
	public final void withTarget(){
		final Artist artist = new Artist(FIRSTNAME, LASTNAME);
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		Assert.assertEquals(equalsBuilder, equalsBuilder.withTarget(artist));
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "targetFields"));
		Assert.assertEquals(3, results.size());
		equalsBuilder.withTarget(new Date());
		Assert.assertEquals(1,results.size());
	}
	
	@Test
	public final void equals(){
		
		
		class HotArtist extends Artist {
			@Equals
			private Integer hotScore;
			HotArtist(String firstName, String name, int hotScore) {
				super(firstName, name);
				this.hotScore=hotScore;
			}
			
		}
		
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, LASTNAME)).withTarget(new Artist("Dannii" , LASTNAME)).isEquals());
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, LASTNAME)).withTarget(new Artist(FIRSTNAME , LASTNAME)).isEquals());
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new HotArtist(FIRSTNAME, LASTNAME, 10)).withTarget(new Artist(FIRSTNAME , LASTNAME)).isEquals());
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, LASTNAME)).withTarget(new HotArtist(FIRSTNAME, LASTNAME, 10)).isEquals());
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, LASTNAME)).withTarget(new HotArtist(FIRSTNAME, LASTNAME, 10)).isEquals());
		
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, null)).withTarget(new Artist(FIRSTNAME , null)).isEquals());
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Date()).withTarget(new Artist(FIRSTNAME , LASTNAME)).isEquals());
		
		
		final Artist artist = new Artist(null, "Madonna");
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(artist).withTarget(artist).isEquals());
		
		final Date date = new Date();
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(date).withTarget(date).isEquals());
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(date).withTarget(date).forInstance(java.sql.Date.class).isEquals());
		
		
	}
	
	@Test(expected=IllegalStateException.class)
	public final void sourceExistsGuard() {
		new SimpleReflectionEqualsBuilderImpl().withTarget(new Date()).isEquals();
	}
	
	@Test(expected=IllegalStateException.class)
	public final void targetExistsGuard()  {
		new SimpleReflectionEqualsBuilderImpl().withSource(new Date()).isEquals();
	}

	@Test
	public final void hash() {
	   Assert.assertEquals(FIRSTNAME.hashCode()+LASTNAME.hashCode(),  new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(FIRSTNAME, LASTNAME)).buildHashCode());
	
	   final Date date = new Date();
	   Assert.assertEquals(System.identityHashCode(date),  new SimpleReflectionEqualsBuilderImpl().withSource(date).buildHashCode());
	   
	   final Artist artist = new Artist(null, "Madonna");
	   Assert.assertEquals(artist.hashCode(),  new SimpleReflectionEqualsBuilderImpl().withSource(artist).buildHashCode());
	      
	}
	
	@Test
	public final void forInstance() {
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		Assert.assertEquals(Object.class, ReflectionTestUtils.getField(equalsBuilder, "clazz"));
		Assert.assertEquals(equalsBuilder, equalsBuilder.forInstance(Artist.class));
		Assert.assertEquals(Artist.class, ReflectionTestUtils.getField(equalsBuilder, "clazz"));
	}
	

	
	
	private UUID uuid(final Class<?> clazz, final String field) {
		return new UUID(clazz.hashCode(),ReflectionUtils.findField(clazz, field).hashCode());
	}
	
	
	
	
	class Artist {
		
		Artist(final String firstName, final String name) {
			this.name=name;
			this.firstName=firstName;
		}
		
		@Equals()
		private String name;
		
		@Equals
		private String firstName;
		
	}
	
	

}
