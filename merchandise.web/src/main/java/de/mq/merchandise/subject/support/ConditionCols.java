package de.mq.merchandise.subject.support;

import org.springframework.util.StringUtils;

import de.mq.merchandise.util.TableContainerColumns;

enum ConditionCols implements TableContainerColumns {
	Id(false,Long.class, -1L),
	ConditionType(true, String.class, ""),
	DataType(true, ConditionDataType.class, ConditionDataType.String);
	
	private final boolean visible ;
	private final Class<?> type;
	private final Object defaultValue;
	
	ConditionCols(final boolean visible, final Class<?> type, final Object defaultValue ) {
		this.visible=visible;
		this.type=type;
		this.defaultValue=defaultValue;
	}

	@Override
	public boolean visible() {
		
		return this.visible;
	}

	@Override
	public Class<?> target() {
		return type;
	}

	@Override
	public boolean sortable() {
		return true;
	}

	@Override
	public String orderBy() {
		return StringUtils.uncapitalize(name());
	}

	@Override
	public Object nvl() {
		return defaultValue;
	}
	
}
