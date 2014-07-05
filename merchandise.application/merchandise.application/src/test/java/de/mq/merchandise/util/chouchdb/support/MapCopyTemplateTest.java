package de.mq.merchandise.util.chouchdb.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.mapping.util.proxy.support.String2IntegerConverter;
import de.mq.merchandise.util.chouchdb.Field;

public class MapCopyTemplateTest {
	
	private static final String HOT_SCORE = "10";
	private static final String LAST_NAME = "Minogue";
	private static final String FIRST_NAME = "Kylie";
	private final MapCopyTemplate mapCopyTemplate = new MapCopyTemplate();
	
	@Test
	public final void copyMap() {
		
		final Map<String,String> artistMap = new HashMap<>();
		artistMap.put("name", LAST_NAME);
		artistMap.put("firstName", FIRST_NAME);
		artistMap.put("hotScore", HOT_SCORE);
		
		
		Artist artist = mapCopyTemplate.createShallowCopyFieldsFromMap(Artist.class, artistMap);
		
		Assert.assertEquals(FIRST_NAME, artist.getFirstName());
		Assert.assertEquals(LAST_NAME, artist.getLastName());
		Assert.assertEquals(Integer.valueOf(HOT_SCORE), artist.getHotScore());
		Assert.assertEquals(0, artist.getSongs().size() );
		
	}
	
	
	
	
	

}

class Artist {
	@Field("name")
	private String lastName;
	@Field
	private String firstName;
	@Field(converter=String2IntegerConverter.class)
	private Integer hotScore;
	
	@Field
	private Collection<String> songs = new ArrayList<>();;
	
	public Collection<String> getSongs() {
		return songs;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public Integer getHotScore() {
		return hotScore;
	}
	
	
}
