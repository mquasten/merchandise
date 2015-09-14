package de.mq.merchandise.subject.support;

import org.springframework.util.StringUtils;

import de.mq.merchandise.util.TableContainerColumns;

enum CommercialSubjectItemCols implements TableContainerColumns {
	Id(false,Long.class, ""),
	Name(true, String.class, ""),
	Mandatory(true, Boolean.class, Boolean.FALSE);
	
	private final boolean visible ;
	private final Class<?> type;
	private final Object defaultValue;
	
	CommercialSubjectItemCols(final boolean visible, final Class<?> type, final Object defaultValue ) {
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
