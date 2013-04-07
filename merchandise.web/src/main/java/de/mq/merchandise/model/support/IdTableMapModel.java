package de.mq.merchandise.model.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import de.mq.merchandise.customer.support.CustomerAO;

class IdTableMapModel  extends ListDataModel<CustomerAO> implements SelectableDataModel<CustomerAO> {
	
	private Map<String,CustomerAO> model = new HashMap<>();
	
	
	IdTableMapModel(final List<CustomerAO> customers) {
		super(customers);
		for(final CustomerAO customer  : customers){
			model.put(customer.getId(), customer);	
		}
		
	}

	@Override
	public final Object getRowKey(final CustomerAO customer) {
		return customer.getId();
	}

	@Override
	public final CustomerAO getRowData(final String rowKey) {
		return model.get(rowKey);
	}

}
