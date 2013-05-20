package de.mq.merchandise.opportunity.support;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.SimplePagingImpl;

public abstract  class PagingAO {
	
	    @Getter(clazz=SimplePagingImpl.class, value = "currentPage")
		public abstract int getCurrentPage(); 
	    
	    @Setter(clazz=SimplePagingImpl.class , value="currentPage")
	    public abstract void setCurrentPage();
	    
	    @Getter(clazz=SimplePagingImpl.class, value = "maxPages")
	    public abstract int getMaxPages(); 
	    
	    @GetterDomain(clazz=SimplePagingImpl.class)
		public abstract Paging getPaging(); 
	    
	    @Getter(clazz=SimplePagingImpl.class, value = "pageSize")
	    public abstract int getPageSize(); 
	     

}
