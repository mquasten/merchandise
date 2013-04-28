package de.mq.merchandise.util.support;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Id;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.Equals;
import de.mq.merchandise.util.EqualsBuilder;
import de.mq.merchandise.util.support.SimpleReflectionEqualsBuilderImpl;

public class EqualsBuilderTest {
	
	
	
	private static final Long ID = 19680528L;
	private static final String LASTNAME = "Minogue";
	private static final String FIRSTNAME = "Kylie";




	@Test
	public final void withSource() {
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		final Artist source = new Artist(FIRSTNAME, LASTNAME);
		Assert.assertEquals(equalsBuilder, equalsBuilder.withSource(source));
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "sourceFields"));
		Assert.assertEquals(4, results.size());
	    Assert.assertTrue(results.containsKey(uuid(source.getClass(), "name")));
	    Assert.assertTrue(results.containsKey(uuid(source.getClass(), "firstName")));
	    Assert.assertTrue(results.containsKey(SimpleReflectionEqualsBuilderImpl.OBJECT_UUID));
		
	    Assert.assertEquals(LASTNAME, results.get((uuid(source.getClass(), "name"))));
	    Assert.assertEquals(FIRSTNAME, results.get((uuid(source.getClass(), "firstName"))));
	    Assert.assertEquals(source, results.get(SimpleReflectionEqualsBuilderImpl.OBJECT_UUID));
	    
	    equalsBuilder.withSource(new Date());
	   
		Assert.assertEquals(2,results.size());
	}
	
	
	@Test
	public final void withSourceNulls() {
		final Artist artist = new Artist((Long)null, null);
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl().withSource(artist);
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "sourceFields"));
		Assert.assertEquals(2, results.size());
		Assert.assertEquals(artist, results.get(SimpleReflectionEqualsBuilderImpl.OBJECT_UUID));
		Assert.assertEquals(0, results.get(SimpleReflectionEqualsBuilderImpl.ID_UUID));
		
	}


	@Test
	public final void withTarget(){
		final Artist artist = new Artist(FIRSTNAME, LASTNAME);
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		Assert.assertEquals(equalsBuilder, equalsBuilder.withTarget(artist));
		@SuppressWarnings("unchecked")
		final Map<UUID, Object> results = ((Map<UUID, Object>) ReflectionTestUtils.getField(equalsBuilder, "targetFields"));
		Assert.assertEquals(4, results.size());
		equalsBuilder.withTarget(new Date());
		Assert.assertEquals(2,results.size());
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
		
		
		final Artist artist = new Artist((Long) null, "Madonna");
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
	   
	   final Artist artist = new Artist((Long) null, "Madonna");
	   Assert.assertEquals(artist.hashCode(),  new SimpleReflectionEqualsBuilderImpl().withSource(artist).buildHashCode());
	      
	}
	
	@Test
	public final void forInstance() {
		final EqualsBuilder equalsBuilder = new SimpleReflectionEqualsBuilderImpl();
		Assert.assertEquals(Object.class, ReflectionTestUtils.getField(equalsBuilder, "clazz"));
		Assert.assertEquals(equalsBuilder, equalsBuilder.forInstance(Artist.class));
		Assert.assertEquals(Artist.class, ReflectionTestUtils.getField(equalsBuilder, "clazz"));
	}
	

	@Test
	public final void idsHash() {
		Assert.assertTrue(ID.hashCode() + FIRSTNAME.hashCode() == new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, FIRSTNAME)).buildHashCode());
		final Artist artist = new Artist(ID, null);
		Assert.assertTrue(System.identityHashCode(artist) == new SimpleReflectionEqualsBuilderImpl().withSource(artist).buildHashCode());
	}
	
	@Test
	public final void idsEquals() {
		Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, FIRSTNAME)).withTarget(new Artist(ID, FIRSTNAME)).isEquals());
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, FIRSTNAME)).withTarget(new Artist(ID, null)).isEquals());
		Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, null)).withTarget(new Artist(ID, null)).isEquals());
	    final Artist artist = new Artist(ID, null);
	    Assert.assertTrue(new SimpleReflectionEqualsBuilderImpl().withSource(artist).withTarget(artist).isEquals());
	    Assert.assertFalse(new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, FIRSTNAME)).withTarget(new Artist(FIRSTNAME, LASTNAME)).isEquals());
		Assert.assertFalse(builderWithOutIdNullSet(new Artist(FIRSTNAME, LASTNAME), new Artist(ID, FIRSTNAME)).isEquals());
		Assert.assertFalse(builderWithOutIdNullSet(new Artist(ID, FIRSTNAME), new Artist(FIRSTNAME, LASTNAME)).isEquals());
	}
	
	
	@Test(expected=IllegalStateException.class)
	public final void idHashMissing() {
		final EqualsBuilder builder = new SimpleReflectionEqualsBuilderImpl().withSource(new Artist(ID, FIRSTNAME));
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = (Map<UUID, Object>) ReflectionTestUtils.getField(builder, "sourceFields");
		map.remove(SimpleReflectionEqualsBuilderImpl.ID_UUID);
		builder.buildHashCode();
	}


	private EqualsBuilder builderWithOutIdNullSet(final Artist source, final Artist target) {
		EqualsBuilder builder =  new SimpleReflectionEqualsBuilderImpl().withSource(source).withTarget(target);
		ReflectionTestUtils.setField(builder, "idNulls", false);
		return builder;
	}
	
	
	
	private UUID uuid(final Class<?> clazz, final String field) {
		return new UUID(clazz.hashCode(),ReflectionUtils.findField(clazz, field).hashCode());
	}
	
	
	
	
	class Artist {
		
		Artist(final String firstName, final String name) {
			this.name=name;
			this.firstName=firstName;
		}
		
		Artist(Long id, String artistName){
			this.id=id;
			this.artistName=artistName;
		}
				
			
		
		@Id
		private Long id;
		
		@Id
		private String artistName;
		
		@Equals()
		private String name;
		
		@Equals
		private String firstName;
		
	}
	
	

}
