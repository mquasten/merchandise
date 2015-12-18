package de.mq.merchandise.subject.support;

import java.util.Arrays;







import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class ConditionValueColsTest {
	
	@Test
	public final void visual() {
		Arrays.asList( ConditionValueCols.values()).stream().filter(col -> col != ConditionValueCols.Id).forEach(col -> Assert.assertTrue(col.visible()));
		Assert.assertFalse(ConditionValueCols.Id.visible());
	}
	
	@Test
	public final void target() {
		Arrays.asList( ConditionValueCols.values()).stream().filter(col -> col != ConditionValueCols.InputValue).forEach(col -> Assert.assertEquals(Long.class, col.target()));
	   Assert.assertEquals(String.class, ConditionValueCols.InputValue.target());
	}

	@Test
	public final void sortable() {
		Arrays.asList( ConditionValueCols.values()).forEach(col -> Assert.assertTrue(col.sortable()));
	}
	
	@Test
	public final void orderBy() {
		Arrays.asList( ConditionValueCols.values()).forEach(col -> Assert.assertEquals( StringUtils.uncapitalize(col.name()), col.orderBy()));
	}
	
	@Test
	public final void nvl() {
		Arrays.asList( ConditionValueCols.values()).stream().filter(col -> col != ConditionValueCols.InputValue).forEach(col ->Assert.assertEquals(-1L, col.nvl()));
		Assert.assertEquals("", ConditionValueCols.InputValue.nvl());
	}
	
	@Test
	public final void  newField() {
		Arrays.asList( ConditionValueCols.values()).stream().filter(col -> col != ConditionValueCols.Condition).forEach(col ->Assert.assertEquals(TextField.class, col.newField().getClass()));
		Assert.assertEquals(ComboBox.class, ConditionValueCols.Condition.newField().getClass());
	}
	
	@Test
	public final void  create() {
		Arrays.asList( ConditionValueCols.values()).forEach(col -> Assert.assertEquals(col, ConditionValueCols.valueOf(col.name())));
	}
}
