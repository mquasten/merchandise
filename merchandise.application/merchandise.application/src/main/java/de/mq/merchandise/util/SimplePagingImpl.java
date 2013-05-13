package de.mq.merchandise.util;

public class SimplePagingImpl implements Paging{
	
	private static final long serialVersionUID = 1L;
	
	private final int pageSize;
	
	private int currentPage;
	
	private int maxPages;
	
	private final String orderBy; 
	
    public SimplePagingImpl(final int pageSize, final String orderBy){
		this.pageSize=pageSize;
		this.currentPage=1;
		this.maxPages=1;
		this.orderBy=orderBy;
	}

	@Override
	public final  int pageSize() {
		return this.pageSize;
	}

	@Override
	public final int currentPage() {
		return currentPage;
	}

	@Override
	public final int maxPages() {
		return maxPages;
	}

	@Override
	public final  void next() {
		if( currentPage < maxPages) {
			currentPage++;
		}
		
	}

	@Override
	public final  void previous() {
		if( currentPage > 1){
			currentPage--;
		}
		
	}

	@Override
	public final  void first() {
		currentPage=1;
		
	}

	@Override
	public void last() {
		currentPage=maxPages;
		
	}

	@Override
	public final void assignCurrentPage(final int currentPage) {
		if( currentPage < 1){
			this.currentPage=1;
			return;
		}
		if( currentPage >= maxPages){
			this.currentPage=maxPages;
			return;
		}
		this.currentPage=currentPage;
	}

	@Override
	public final  void assignRowCounter(final long numberOfRows) {
		maxPages = (int) Math.ceil( (double) numberOfRows / pageSize);
		if ( currentPage > maxPages){
			currentPage=maxPages;
			return;
		} 
		if( currentPage <= 0){
			currentPage=1;
		}
		
		
	}
	
	@Override
	public final int firstRow() {
		return (currentPage-1)*pageSize;
	}

	@Override
	public final String sortHint() {
		return orderBy;
	}

}
