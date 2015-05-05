package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;





public class SubjectColsTest {
	@Test
	public final void create() {
		Arrays.asList(SubjectCols.values()).forEach( col -> Assert.assertEquals(col, SubjectCols.valueOf(col.name())));
	}
	
	@Test
	public final void visible() {
		
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.equals(SubjectCols.Id)).forEach(col ->Assert.assertFalse(col.visible()));
		Arrays.asList(SubjectCols.values()).stream().filter(col -> !col.equals(SubjectCols.Id)).forEach(col ->Assert.assertTrue(col.visible()));
	}
	
	@Test
	public final void target() {
		final Set<Class<?>> results = Arrays.asList(SubjectCols.values()).stream().map(col -> col.target()).collect(Collectors.toSet());
	    Assert.assertEquals(3, results.size());
	    Assert.assertTrue(results.contains(Long.class));
	    Assert.assertTrue(results.contains(String.class));
	    Assert.assertTrue(results.contains(Date.class));
	}
	@Test
	public final void sortable() {
		Arrays.asList(SubjectCols.values()).forEach(col -> Assert.assertTrue(col.sortable()));
	}
	@Test
	public final void orderBy() {
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.target().equals(String.class)).forEach( col -> Assert.assertTrue(col.orderBy().startsWith(String.format("COALESCE(%s", StringUtils.uncapitalize(col.name() ))))) ;
	
		Assert.assertEquals(StringUtils.uncapitalize(SubjectCols.Id.name()), (SubjectCols.Id.orderBy()));
		Assert.assertEquals("date_created" , (SubjectCols.DateCreated.orderBy()));
	}
	@Test
	public final void nvl() {
		Arrays.asList(SubjectCols.values()).stream().filter(col -> col.target().equals(String.class)).map(col -> col.nvl()).forEach(val -> Assert.assertEquals("", val));
	    Assert.assertEquals(-1L, SubjectCols.Id.nvl());
	    Assert.assertEquals(new Date(0), SubjectCols.DateCreated.nvl());
	}

}
