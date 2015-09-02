package de.mq.merchandise.subject.support;

import de.mq.merchandise.util.TableContainerColumns;

enum CommercialSubjectCols implements TableContainerColumns {
	
	
	Id(false, Long.class , "cs.id", -1L),
	Name(true, String.class ,"cs.name" , "");
	
	private final boolean visible ;
	private final Class<?> type;
	
	private final String orderBy;
	private final Object nvl;
	
	CommercialSubjectCols(final boolean visible, final Class<?> type, final String orderBy, final Object nvl) {
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
