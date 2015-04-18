package de.mq.merchandise.support;

import de.mq.merchandise.Paging;



public class SimpleResultSetPagingImpl  implements Paging {

	private final Number pageSize;

	private final Number maxPages;

	private Number currentPage;

	SimpleResultSetPagingImpl(final Number pageSize, final Number counter) {
		this.pageSize = pageSize;
		maxPages = Double.valueOf(Math.ceil( counter.doubleValue() / pageSize.doubleValue())).longValue();
		currentPage = 1L;
	}
	
	public SimpleResultSetPagingImpl() {
		this(0,0);
	}

	
	public final boolean hasNextPage() {
		return currentPage.longValue() < maxPages.longValue();
	}

	
	public final boolean hasPreviousPage() {
		return currentPage.longValue() > 1L;
	}

	
	public final boolean  inc() {
		if (!hasNextPage()) {
			return false;
		}
		currentPage = new Long(currentPage.longValue() + 1L);
		return true;
	}


	public final boolean  dec() {
		if (!hasPreviousPage()) {
			return false;
		}
		currentPage = new Long(currentPage.longValue() - 1L);
		return true;
	}

	
	public final boolean  first() {
		if( isBegin()){
			return false;
		}
		currentPage = 1L;
		return true;
	}

	
	public final boolean  last() {
		if( isEnd()) {
			return false;
		}
		currentPage = maxPages;
		return true;
		
	}

	
	public final Number firstRow() {
		return (currentPage.longValue() - 1L) * pageSize.longValue();
	}

	
	public final Number pageSize() {
		return pageSize;
	}
	
	public final Number maxPages() {
		 return maxPages;
	}

	
	public final Number currentPage() {
		return currentPage;
	}

	public boolean isEnd() {
		return currentPage.longValue() >= maxPages.longValue();
	}

	public boolean isBegin() {
		return currentPage.longValue() <= 1L;
	}
}
