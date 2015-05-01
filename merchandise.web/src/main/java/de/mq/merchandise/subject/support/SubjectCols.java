package de.mq.merchandise.subject.support;

import java.util.Date;

import de.mq.merchandise.util.TableContainerColumns;

enum SubjectCols implements TableContainerColumns {
	
	
	Id(false, Long.class , "id", -1L),
	Name(true, String.class , "COALESCE(name, '')" , ""),
	Description(true, String.class,  "COALESCE(description , '')" , ""),
	DateCreated(true, Date.class, "date_created"  , new Date(0));
	
	private final boolean visible ;
	private final Class<?> type;
	
	private final String orderBy;
	private final Object nvl;
	
	SubjectCols(final boolean visible, final Class<?> type, final String orderBy, final Object nvl) {
		this.visible=visible;
		this.type=type;
		this.orderBy=orderBy;
		this.nvl=nvl;
	}
	@Override
	public boolean visible() {
		return visible;
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
		return orderBy;
	}
	@Override
	public Object nvl() {
		return nvl;
	}
	
	
}
