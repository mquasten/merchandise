package de.mq.merchandise.util;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class PagingTest {
	
	
	
	final Paging paging = new SimplePagingImpl(PAGE_SIZE, ORDER_BY);
	private static final String ORDER_BY = "id";
	private static final int PAGE_SIZE = 50;

	@Test
	public final void create() {
		Assert.assertEquals(PAGE_SIZE, paging.pageSize());
		Assert.assertEquals(ORDER_BY, paging.sortHint());
		Assert.assertEquals(1, paging.currentPage());
		Assert.assertEquals(1, paging.maxPages());
	}
	
	@Test
	public final void next() {
		Assert.assertEquals(1, paging.currentPage());
		ReflectionTestUtils.setField(paging, "maxPages", 2);
		paging.next();
		Assert.assertEquals(2, paging.currentPage());
		for(int i=0; i < 10 ; i++){
			paging.next();
			Assert.assertEquals(2, paging.currentPage());
		}
	}
	
	@Test
	public final void privious() {
		Assert.assertEquals(1, paging.currentPage());
		ReflectionTestUtils.setField(paging, "maxPages", 2);
		ReflectionTestUtils.setField(paging, "currentPage", 2);
		Assert.assertEquals(2, paging.currentPage());
		paging.previous();
		Assert.assertEquals(1, paging.currentPage());
		
		for(int i=0; i < 10 ; i++){
			paging.previous();
			Assert.assertEquals(1, paging.currentPage());
		}
	}
	
	@Test
	public final void first() {
		
		ReflectionTestUtils.setField(paging, "currentPage", 2);
		ReflectionTestUtils.setField(paging, "maxPages", 2);
		
		Assert.assertEquals(2, paging.currentPage());
		paging.first();
		Assert.assertEquals(1, paging.currentPage());
	}
	
	@Test
	public final void last() {
		ReflectionTestUtils.setField(paging, "currentPage", 2);
		ReflectionTestUtils.setField(paging, "maxPages", 42);
			
		Assert.assertEquals(2, paging.currentPage());
		paging.last();
		Assert.assertEquals(42, paging.currentPage());
	
	}
	
	@Test
	public final void assignCurrentPage() {
		ReflectionTestUtils.setField(paging, "maxPages", 42);
		Assert.assertEquals(1, paging.currentPage());
		
		paging.assignCurrentPage(21);
		Assert.assertEquals(21, paging.currentPage());
		
		paging.assignCurrentPage(100);
		Assert.assertEquals(42, paging.currentPage());
		
		paging.assignCurrentPage(-1);
		Assert.assertEquals(1, paging.currentPage());
		
	}
	
	@Test
	public final void assignRowCounter() {
        paging.assignRowCounter(250);
        Assert.assertEquals(5, paging.maxPages());
        Assert.assertEquals(1, paging.currentPage());
       
       paging.assignRowCounter(249);
       Assert.assertEquals(5, paging.maxPages());
       Assert.assertEquals(1, paging.currentPage());
       
       paging.assignRowCounter(251);
       Assert.assertEquals(6, paging.maxPages());
       Assert.assertEquals(1, paging.currentPage());
	}
	
	
	@Test
	public final void assignRowCounterCurrentPageToLarge() {
		ReflectionTestUtils.setField(paging, "currentPage", 10);
		paging.assignRowCounter(251);
		Assert.assertEquals(6, paging.maxPages());
		Assert.assertEquals(6, paging.currentPage());
	}
	
	@Test
	public final void assignRowCounterCurrentPageToLess() {
		ReflectionTestUtils.setField(paging, "currentPage", 0);
		paging.assignRowCounter(251);
		Assert.assertEquals(6, paging.maxPages());
		Assert.assertEquals(1, paging.currentPage());
	}
	
	@Test
	public final void firstRow() {
		ReflectionTestUtils.setField(paging, "currentPage", 1);
		Assert.assertEquals(0, paging.firstRow());
		ReflectionTestUtils.setField(paging, "currentPage", 5);
		Assert.assertEquals(200, paging.firstRow());
	}


}
