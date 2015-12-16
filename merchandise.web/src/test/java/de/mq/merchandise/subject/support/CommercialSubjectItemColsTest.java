package de.mq.merchandise.subject.support;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;



public class CommercialSubjectItemColsTest {
	
	@Test
	public final void visible() {
		Arrays.asList(CommercialSubjectItemCols.values()).forEach(col -> Assert.assertEquals(!col.target().equals(Long.class), col.visible()));
	}
	
	@Test
	public final void sortable() {
		Arrays.asList(CommercialSubjectItemCols.values()).forEach(col -> Assert.assertTrue(col.sortable()));
	}
	
	@Test
	public final void orderBy() {
		Arrays.asList(CommercialSubjectItemCols.values()).forEach(col -> Assert.assertEquals(StringUtils.uncapitalize(col.name()), col.orderBy()));
	}
	
	@Test
	public final void nvl() {
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col-> col.target().equals(Long.class) ).forEach(col-> Assert.assertEquals(-1l, col.nvl()));
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col->  col.target().equals(String.class) ).forEach(col-> Assert.assertEquals("", col.nvl()));
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col->  col.target().equals(Boolean.class) ).forEach(col-> Assert.assertFalse((Boolean) col.nvl()));
	}
	
	@Test
	public final void newField()  {
		Arrays.asList(CommercialSubjectItemCols.Id, CommercialSubjectItemCols.Name).forEach(col -> Assert.assertTrue(col.newField() instanceof TextField));
		Arrays.asList(CommercialSubjectItemCols.Mandatory, CommercialSubjectItemCols.Subject).forEach(col -> Assert.assertTrue(col.newField() instanceof ComboBox));
	}
	
	@Test
	public final void create()  {
		Arrays.asList(CommercialSubjectItemCols.values()).forEach(col -> Assert.assertEquals(col, CommercialSubjectItemCols.valueOf(col.name())));
	}

}
