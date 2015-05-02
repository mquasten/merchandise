package de.mq.merchandise.util.support;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;



public interface RefreshableContainer extends   Container.Indexed, Container.Sortable, Container.ItemSetChangeNotifier, Container.PropertySetChangeNotifier, Buffered   {
	
	void refresh(); 

}
