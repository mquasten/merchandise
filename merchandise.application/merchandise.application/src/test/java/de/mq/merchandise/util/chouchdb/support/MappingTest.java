package de.mq.merchandise.util.chouchdb.support;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.util.chouchdb.MapBasedResultRow;

public class MappingTest {
	
	private static final String PATH_VALUE_NEXT = "path2";
	private static final String PATH_VALUE_FIRST = "path1";
	private static final String PATHS_FIELD_NAME = "paths";
	private static final String FIELD_FIELD_NAME = "field";
	private static final String KEY_FIELD_NAME = "key";
	private static final String FIELD_VALUE = "field-value";
	private static final String KEY_VALUE = "key-value";

	@Test
	public final void constructor() {
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>(KEY_VALUE , FIELD_VALUE , PATH_VALUE_FIRST , PATH_VALUE_NEXT);
		
		Assert.assertEquals(KEY_VALUE, ReflectionTestUtils.getField(mapping, KEY_FIELD_NAME));
		Assert.assertEquals(FIELD_VALUE, ReflectionTestUtils.getField(mapping,FIELD_FIELD_NAME));
		final List<String> paths = pathsField(mapping);
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals(PATH_VALUE_FIRST, paths.get(0));
		Assert.assertEquals(PATH_VALUE_NEXT, paths.get(1));
	}

	@SuppressWarnings("unchecked")
	private List<String> pathsField(final Mapping<MapBasedResultRow> mapping) {
		return (List<String>) ReflectionTestUtils.getField(mapping,PATHS_FIELD_NAME);
	}
	
	@Test
	public final void constructorChild() {
		final Mapping<MapBasedResultRow> parent = new Mapping<>(KEY_VALUE, null);
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>(parent,  FIELD_VALUE , PATH_VALUE_FIRST , PATH_VALUE_NEXT);
		Assert.assertNull(ReflectionTestUtils.getField(mapping, KEY_FIELD_NAME));
		Assert.assertEquals(FIELD_VALUE, ReflectionTestUtils.getField(mapping,FIELD_FIELD_NAME));
		final List<String> paths = pathsField(mapping);
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals(PATH_VALUE_FIRST, paths.get(0));
		Assert.assertEquals(PATH_VALUE_NEXT, paths.get(1));
		
		 final Collection<Mapping<MapBasedResultRow>>  parentChilds = childs(parent);
		 Assert.assertEquals(1, parentChilds.size());
		 Assert.assertEquals(mapping, parentChilds.iterator().next());
	}

	@SuppressWarnings("unchecked")
	private Collection<Mapping<MapBasedResultRow>>  childs(final Mapping<MapBasedResultRow> parent) {
		return (Collection<Mapping<MapBasedResultRow>>) ReflectionTestUtils.getField(parent,"childs");
	}	

}
