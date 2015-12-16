package de.mq.merchandise.subject.support;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class CommercialSubjectColsTest {
	
	private static final String SUBJECT_DESCRIPTION_ORDER = "s.description";
	private static final String SUBJECT_NAME_ORDER = "s.name";
	private static final String ITEM_NAME_ORDER = "i.name";

	@Test
	public final void visible() {
		Arrays.asList(CommercialSubjectCols.values()).stream().filter(col -> col != CommercialSubjectCols.Name ).forEach(col -> Assert.assertFalse(col.visible()));
		Assert.assertTrue(CommercialSubjectCols.Name.visible());
	}
	
	@Test
	public final void target() {
		Arrays.asList(CommercialSubjectCols.values()).stream().filter(col -> col != CommercialSubjectCols.Id ).forEach(col -> Assert.assertEquals(String.class, col.target()));
		 Assert.assertEquals(Long.class, CommercialSubjectCols.Id.target());
	}
	
	@Test
	public final void sortable() {
		Arrays.asList(CommercialSubjectCols.values()).forEach(col -> Assert.assertTrue(col.sortable()));
	}
	
	@Test
	public final void nvl() {
		Arrays.asList(CommercialSubjectCols.values()).stream().filter(col -> col != CommercialSubjectCols.Id ).forEach(col -> Assert.assertEquals("", col.nvl()));
		Assert.assertEquals(-1L, CommercialSubjectCols.Id.nvl());
	}
	
	@Test
	public final void orderBy() {	
		Arrays.asList(CommercialSubjectCols.Id, CommercialSubjectCols.Name).forEach(col -> Assert.assertEquals(String.format("cs.%s",col.name().toLowerCase()), col.orderBy()));
		Assert.assertEquals(ITEM_NAME_ORDER, CommercialSubjectCols.ItemName.orderBy());
		Assert.assertEquals(CommercialSubjectCols.SubjectName.orderBy(), SUBJECT_NAME_ORDER );
		Assert.assertEquals(CommercialSubjectCols.SubjectDesc.orderBy(), SUBJECT_DESCRIPTION_ORDER );

	}
	
	@Test
	public final void create(){
		Arrays.asList(CommercialSubjectCols.values()).forEach(col -> Assert.assertEquals(col , CommercialSubjectCols.valueOf(col.name())));
	}

}
