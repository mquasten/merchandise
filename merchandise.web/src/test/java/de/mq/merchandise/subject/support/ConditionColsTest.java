package de.mq.merchandise.subject.support;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class ConditionColsTest {

	@Test
	public final void values() {

		Arrays.asList(ConditionCols.values()).stream().forEach(col -> Assert.assertEquals(col, ConditionCols.valueOf(col.name())));

		Arrays.asList(ConditionCols.values()).stream().forEach(col -> {
			Assert.assertTrue(col.sortable());
			Assert.assertEquals(StringUtils.uncapitalize(col.name()), col.orderBy());
		});

	}

	@Test
	public final void id() {
		Assert.assertFalse(ConditionCols.Id.visible());
		Assert.assertEquals(Long.class, ConditionCols.Id.target());
		Assert.assertEquals("", ConditionCols.Id.nvl());
	}

	@Test
	public final void conditionType() {
		Assert.assertTrue(ConditionCols.ConditionType.visible());
		Assert.assertEquals(String.class, ConditionCols.ConditionType.target());
		Assert.assertEquals("", ConditionCols.ConditionType.nvl());
	}

	@Test
	public final void dataType() {
		Assert.assertTrue(ConditionCols.DataType.visible());
		Assert.assertEquals(ConditionDataType.class, ConditionCols.DataType.target());
		Assert.assertEquals(ConditionDataType.String, ConditionCols.DataType.nvl());
	}

}
