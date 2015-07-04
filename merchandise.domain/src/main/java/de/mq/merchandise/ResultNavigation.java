package de.mq.merchandise;

import java.util.List;

import org.springframework.data.domain.Sort.Order;

public interface ResultNavigation {

	Number firstRow();

	Number pageSize();
	
	List<Order> orders();
	

}