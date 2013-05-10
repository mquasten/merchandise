package de.mq.merchandise.util;

import java.io.Serializable;

/**
 * Paging for long resultsets from database or other resources.
 *The resultSet will be splitted into Pages, with a defined size
 * @author mquasten
 *
 */
public interface Paging extends Serializable{
	/**
	 * The number of rows, that will be readed on block for every page
	 * @return the number of rows for a page
	 */
	int pageSize();
	
	/**
	 * The current page of the resultSet, that rows will be reded from the dataSource (next time, next request)
	 * @return the current page of the resultSet
	 */
	int currentPage();
	
	/**
	 * The number of pages that is needed to read the complete result
	 * @return the number of all pages, that belongs to the resultSEt
	 */
	int maxPages();
	
	/**
	 * Increment currentPage, if possible. If higher than last, do nothing
	 */
	void next();
	
	/**
	 * Decrement current page, if possible.  If lower than 1, do nothing
	 */
	void previous();
	
	/**
	 * Set current page to the first one.
	 */
	void first();
	
	
	/**
	 * Set current page to the last one
	 */
	void last();
	
	/**
	 * Set current Page to the given one, if possible.
	 * if the given page > last, set it to the last page
	 * if the given page < first set it to the first one 
	  * @param currentPage the number of currentPage
	 */
	void assignCurrentPage(int currentPage);
	/**
	 * Calculates the maxPages, and checks if the currentPage  <= lastPage. If not set it to last page.
	 * @param numberOfRows the total number of rows, of the complete resultSet
	 */
	void assignRowCounter(long numberOfRows) ;

}
